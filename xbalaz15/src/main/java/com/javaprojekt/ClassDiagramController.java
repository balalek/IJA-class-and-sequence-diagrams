package com.javaprojekt;

import com.component.*;
import com.component.ClassComponent;
import com.google.gson.*;
import com.uml.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

enum operation{
    REMOVE,
    CREATE,
    RENAME,
    CHANGE,
    DRAG
}

public class ClassDiagramController{

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private AnchorPane rootPane;
    private List<String> content = new LinkedList<>();
    private List<String> duplicateAttr = new LinkedList<>();
    private Deque<Object> objectStack = new ArrayDeque<>();
    private Deque<String> nameStack = new ArrayDeque<>();
    private Deque<Enum<operation>> operationStack = new ArrayDeque<>();
    private Deque<Double> coorX = new ArrayDeque<>();
    private Deque<Double> coorY = new ArrayDeque<>();
    private String arrowType;
    private static int count = 0;
    private static int ID = 1;
    ClassComponent firstBox;
    Arrow arrow;
    public final ClassDiagram d = new ClassDiagram("Class Diagram");
    //public UMLClass cls;

    public void InsertClass(ActionEvent event) {
        rootPane.setOnMousePressed(this::onGraphPressed);
    }

    public void onGraphPressed(MouseEvent mouseEvent){
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            rootPane.getChildren().add(createClassBox(mouseEvent).getBox());
            objectStack.push(rootPane.getChildren().get(rootPane.getChildren().size() - 1));
            operationStack.push(operation.CREATE);
            rootPane.setOnMousePressed(null);
        }
    }

    private Structure createClassBox(MouseEvent mouseEvent){
        ClassComponent box = new ClassComponent(mouseEvent.getX(), mouseEvent.getY());
        //ClassComponent box = new ClassComponent(250.0,250.0, "třída do potoka", "+ int železo", " bla bla bla\nbla bla bla");
        UMLClass cls = d.createClass(box.getName());

        box.setOnDragDetected(e -> onBoxDragDetected(e, box));
        box.setOnMouseDragged(e -> onBoxDragged(e, box));
        box.setOnKeyPressed(e -> handleKeyboard(e, box, cls));
        box.setOnMousePressed(e -> handleMouse(e, box, cls));

        Structure structure = new Structure(box, cls);
        return structure;
    }

    private void onBoxDragDetected(MouseEvent e, ClassComponent box) {
        if(e.getButton().equals(MouseButton.PRIMARY)) {
            box.toFront();
            objectStack.push(box);
            coorX.push(box.getLayoutX());
            coorY.push(box.getLayoutY());
            operationStack.push(operation.DRAG);
        }
    }
    private void onBoxDragged(MouseEvent e, ClassComponent box) {
        if(e.getButton().equals(MouseButton.PRIMARY)) {
            box.setLayoutX(box.getLayoutX() + e.getX() + box.getTranslateX());
            box.setLayoutY(box.getLayoutY() + e.getY() + box.getTranslateY());
        }
    }

    // Update content of class
    public void handleMouse(MouseEvent evt, ClassComponent box, UMLClass cls){
        if(evt.getButton().equals(MouseButton.PRIMARY)){
            if(evt.getClickCount() == 2){
                String oldName = box.getName();

                // Pushování do stacku pro undo operace
                objectStack.push(box);
                operationStack.push(operation.RENAME);
                nameStack.push(box.getName());

                // Udržování aktuálních dat
                content.addAll(EditClassComponent.display(box));
                box.setName(content.get(0));

                // Parsování atributů
                duplicateAttr.clear();
                cls.removeAttributes();
                box.setAttributes(content.get(1));
                // Rozdělení atributů do pole podle nových řádků a poté podle mezer
                String[] lines = content.get(1).split("\\r?\\n");
                String[][] attrLine = new String[lines.length][];


                // Parse operations
                box.setOperations(content.get(2));
                /*UMLOperation op1 = UMLOperation.create("method1", d.classifierForName("void"),
                        new UMLAttribute("arg1", d.classifierForName("C1")),
                        new UMLAttribute("arg2", d.classifierForName("String")));*/
                content.removeAll(content);
                box.setNameProperty(box.getName());

                int i = 0;
                for (String row : lines) attrLine[i++] = row.split("\\s+");
                // Procházení polem
                for (String[] strings : attrLine) {
                    // Řádek pro atribut musí obsahovat 3 prvky
                    if (strings.length == 3) {
                        // Žadné duplikátní názvy a regex pro modifikátoru přístupu
                        if (!duplicateAttr.contains(strings[2]) && strings[0].matches("[+|\\-|#|~]")){
                            // Metoda může vrátit null a proto tato podmínka
                            if(d.findClassifier(strings[1]) != null){
                                String storedClassifier = d.findClassifier(strings[1]).getName();
                                if((strings[1].matches("([i|I]nt|[s|S]tring|[B|b]oolean|[b|B]ool|[d|D]ouble|[f|F]loat|[L|l]ong|[s|S]hort|[b|B]yte|[c|C]har|" + storedClassifier + ")"))){
                                    box.getStyleClass().remove("redBox");
                                    duplicateAttr.add(strings[2]);
                                    UMLAttribute attr = new UMLAttribute(strings[2], d.classifierForName(strings[1]));
                                    cls.addAttribute(attr);
                                } else {
                                    duplicateAttr.clear();
                                    cls.removeAttributes();
                                    box.getStyleClass().add("redBox");
                                    break;
                                }
                            }else{
                                if((strings[1].matches("([i|I]nt|[s|S]tring|[B|b]oolean|[b|B]ool|[d|D]ouble|[f|F]loat|[L|l]ong|[s|S]hort|[b|B]yte|[c|C]har)"))) {
                                    box.getStyleClass().remove("redBox");
                                    duplicateAttr.add(strings[2]);
                                    UMLAttribute attr = new UMLAttribute(strings[2], d.classifierForName(strings[1]));
                                    cls.addAttribute(attr);
                                } else {
                                    duplicateAttr.clear();
                                    cls.removeAttributes();
                                    box.getStyleClass().add("redBox");
                                    break;
                                }
                            }
                            // Název atributu již existuje
                        } else {
                            duplicateAttr.clear();
                            cls.removeAttributes();
                            box.getStyleClass().add("redBox");
                            break;
                        }
                        // Jestli okno atributů je prázdné, je to v pořádku
                        //content.get(1)
                    } else if (box.getAttributes().isEmpty()){
                        box.getStyleClass().remove("redBox");
                        break;
                        // Špatný počet prvků pro daný atribut
                    } else {
                        duplicateAttr.clear();
                        cls.removeAttributes();
                        box.getStyleClass().add("redBox");
                        break;
                    }
                }
                duplicateAttr.clear();
                box.setAttrProperty(box.getAttributes());


                box.setOperationProperty(box.getOperations());

                // Rename name in backend
                cls.rename(box.getName());
                d.renameClass(oldName, cls.getName());
                // TODO nahradit klasifikator novym v seznamu klasifikatorů
            }
        }
        // Create arrow between two classes
        if(evt.getButton().equals(MouseButton.SECONDARY)){
            count++;
            if(count == 1){
                firstBox = box;
            }else if(count == 2){
                if(firstBox != box) {
                    arrow = createArrow(firstBox, box);
                    objectStack.push(rootPane.getChildren().get(rootPane.getChildren().size() - 1));
                    operationStack.push(operation.CREATE);
                }
                count = 0;
            }
        }
    }

    public Arrow createArrow(ClassComponent b1, ClassComponent b2){
        Arrow arrow = new Arrow(b1.getLayoutX(), b1.getLayoutY(), b2.getLayoutX(), b2.getLayoutY());
        arrow.x1Property().bind(b1.layoutXProperty());
        arrow.y1Property().bind(b1.layoutYProperty());
        arrow.x2Property().bind(b2.layoutXProperty());
        arrow.y2Property().bind(b2.layoutYProperty());

        arrow.setOnMousePressed(e -> handleMouseArrow(e, arrow));

        b1.edges.add(arrow);
        b2.edges.add(arrow);
        rootPane.getChildren().addAll(arrow);
        return arrow;
    }

    private void handleMouseArrow(MouseEvent e, Arrow arrow) {
        if(e.getButton().equals(MouseButton.SECONDARY)) {
            objectStack.push(arrow);
            operationStack.push(operation.REMOVE);
            rootPane.getChildren().remove(arrow);
        }
        if(e.getButton().equals(MouseButton.PRIMARY)) {
            if (e.getClickCount() == 2) {
                objectStack.push(arrow);
                operationStack.push(operation.CHANGE);
                nameStack.push(arrow.getArrowType());
                arrowType = EditArrowComponent.display(arrow);
                arrow.setArrowType(arrowType);
                arrow.update();
            }
        }
    }

    public void Undo(){
        if(!rootPane.getChildren().isEmpty()){
            if(!objectStack.isEmpty() && !operationStack.isEmpty()) {
                if(operationStack.peekFirst() == operation.CREATE) {
                    rootPane.getChildren().remove(objectStack.pop());
                    operationStack.pop();
                } else if(operationStack.peekFirst() == operation.RENAME){
                    ClassComponent box = (ClassComponent) objectStack.pop();
                    box.setName(nameStack.pop());
                    box.setNameProperty(box.getName());
                    operationStack.pop();
                } else if(operationStack.peekFirst() == operation.CHANGE) {
                    Arrow arrow = (Arrow) objectStack.pop();
                    arrow.setArrowType(nameStack.pop());
                    arrow.update();
                    operationStack.pop();
                } else if(operationStack.peekFirst() == operation.DRAG){
                    ClassComponent box = (ClassComponent) objectStack.pop();
                    box.setLayoutX(coorX.pop());
                    box.setLayoutY(coorY.pop());
                    operationStack.pop();
                }
            }
        } else {
            if(!objectStack.isEmpty() && !operationStack.isEmpty()) {
                if(operationStack.peekFirst() == operation.REMOVE){
                rootPane.getChildren().add((Node) objectStack.pop());
                operationStack.pop();
                }
            }
        }
    }

    public void handleKeyboard(KeyEvent evt, ClassComponent box, UMLClass cls){
        KeyCode k = evt.getCode();
        switch (k) {
            case W -> moveUp(box);
            case S -> moveDown(box);
            case A -> moveLeft(box);
            case D -> moveRight(box);
            case DELETE -> delete(box, cls);
        }
    }
    public void moveUp(ClassComponent box){
        box.setLayoutY(box.getLayoutY()-10);
    }
    public void moveDown(ClassComponent box){
        box.setLayoutY(box.getLayoutY()+10);
    }
    public void moveLeft(ClassComponent box){
        box.setLayoutX(box.getLayoutX()-10);
    }
    public void moveRight(ClassComponent box){
        box.setLayoutX(box.getLayoutX()+10);
    }
    public void delete(ClassComponent box, UMLClass cls){
        //System.out.println(cls.getAttributes());
        //System.out.println("Klasifikator s nazvem " + cls.getName() + " je v diagramu " + d.findClassifier(cls.getName()));
        d.removeClass(box.getName(), cls);
        //System.out.println("Klasifikator s nazvem " + box.getName() + " neni v diagramu " + d.findClassifier(box.getName()));
        objectStack.push(box);
        operationStack.push(operation.REMOVE);
        rootPane.getChildren().remove(box);
        for(Arrow arrow : box.edges) {
            objectStack.push(arrow);
            operationStack.push(operation.REMOVE);
            rootPane.getChildren().remove(arrow);
        }
    }

    public void SwitchToSeqDiagram(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sequence-diagram.fxml"));
        root = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Sequence diagram");
        stage.setScene(scene);
        stage.show();

    }

    public void showHelp(ActionEvent event){
        ShowHelp.display();
    }
    public void SaveJson(ActionEvent event){

    }
    public void LoadJson(ActionEvent event){
         deserializeObject();
    }

    public void deserializeObject(){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);

        try {
            /*// create Gson instance
            //Gson gson = new Gson();
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get(selectedFile.getAbsolutePath()));
            //String reader = "{'x':255,'y':250}";
            // convert JSON string to User object
            System.out.println(reader);
            ClassComponent loadedBox = gson.fromJson(reader, ClassComponent.class);
            System.out.println(loadedBox.getName());
            reader.close();

            //System.out.println(loadedBox!=null);
            // close reader

            rootPane.getChildren().addAll(loadClassBox(loadedBox).getBox());*/

            //Můj pokus
            JSONParser jsonP = new JSONParser();
            Reader reader = Files.newBufferedReader(Paths.get(selectedFile.getAbsolutePath()));
            Object obj = jsonP.parse(reader);
            JSONArray empList = (JSONArray) obj;
            System.out.println(empList);
            //Iterate over emp array

            empList.forEach(emp -> parseEmpObj((JSONObject) emp));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    private void parseEmpObj(JSONObject emp){
        System.out.println("Vypis1: " + emp);
        if(emp.get("class") != null){
            JSONObject empObj = (JSONObject) emp.get("class");
            System.out.println("Vypis2: " + empObj);
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            ClassComponent loadedBox = gson.fromJson(empObj.toString(), ClassComponent.class);
            rootPane.getChildren().addAll(loadClassBox(loadedBox).getBox());
        }else if(emp.get("messageArrow") != null)
        {
            JSONObject empObj = (JSONObject) emp.get("messageArrow");
            System.out.println("Vypis2: " + empObj);
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();

            //ClassComponent.positionInArea();
            System.out.println(rootPane.getChildren().get(0));
            //createArrow(rootPane.get(empObj.get("from"), rootPane.get(empObj.get("to")));
            /*empObj.get("from")
            empObj.get("to")
            empObj.get("arrowType")*/

            //Arrow loadedBox = gson.fromJson(empObj.toString(), Arrow.class);
            //rootPane.getChildren().addAll(loadClassBox(loadedBox).getBox());
        }
        //get emp firstname, lastname, website
        /*String fname = (String) empObj.get("firstname");
        String lname = (String) empObj.get("lastname");
        String website = (String) empObj.get("website");
        System.out.println("First Name: " + fname);
        System.out.println("Last Name: " + lname);
        System.out.println("Website: " + website);*/

    }

    private Structure loadClassBox(ClassComponent loadedBox){
        ClassComponent box = new ClassComponent(loadedBox.getX(), loadedBox.getY(), loadedBox.getName(), loadedBox.getAttributes(), loadedBox.getOperations());
        //ClassComponent box = new ClassComponent(250.0,250.0, "třída do potoka", "+ int železo", " bla bla bla\nbla bla bla");
        UMLClass cls = d.createClass(box.getName());

        box.setOnDragDetected(e -> onBoxDragDetected(e, box));
        box.setOnMouseDragged(e -> onBoxDragged(e, box));
        box.setOnKeyPressed(e -> handleKeyboard(e, box, cls));
        box.setOnMousePressed(e -> handleMouse(e, box, cls));

        Structure structure = new Structure(box, cls);
        return structure;
    }


}