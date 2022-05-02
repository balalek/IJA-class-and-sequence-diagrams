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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileStore;
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
    private static List<ClassComponent> ListofBoxes = new LinkedList<>();
    private static List<String> ListofBoxNames = new LinkedList<>();
    private List<String> content = new LinkedList<>();
    private List<String> duplicateAttr = new LinkedList<>();
    private List<UMLAttribute> listOfDuplicateAttr = new LinkedList<>();
    private List<UMLOperation> listOfDuplicateOper = new LinkedList<>();
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
        ListofBoxes.add(box);
        //ClassComponent box = new ClassComponent(250.0,250.0, "třída do potoka", "+ int železo", " bla bla bla\nbla bla bla");
        UMLClass cls = d.createClass(box.getName());

        box.setOnDragDetected(e -> onBoxDragDetected(e, box));
        box.setOnMouseDragged(e -> onBoxDragged(e, box));
        box.setOnKeyPressed(e -> handleKeyboard(e, box, cls));
        box.setOnMousePressed(e -> handleMouse(e, box, cls));

        rootPane.getChildren();

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


                // Rozdělení atributů do 2D pole podle nových řádků a poté podle mezer
                String[] lines = content.get(1).split("\\r?\\n");
                String[][] attrLine = new String[lines.length][];
                // Parsování metod
                cls.removeOperations();
                box.setOperations(content.get(2));
                // Rozdělení operací do 2D pole podle nových řádků a poté podle mezer
                String[] Lines = content.get(2).split("\\r?\\n");
                String[][] opLine = new String[Lines.length][];

                content.removeAll(content);
                box.setNameProperty(box.getName());
                /*---------------------------------------------------------------ATRIBUTY----------------------------------------------------------*/
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


                /*---------------------------------------------------------------OPERACE----------------------------------------------------------*/
                i = 0;
                Boolean bracket = false;
                for (String Row : Lines) {
                    StringBuffer sb = new StringBuffer(Row);
                    if (sb.charAt(sb.length()-1) != ')'){
                        box.getStyleClass().add("redBox");
                        bracket = true;
                        break;
                    } else {
                        box.getStyleClass().remove("redBox");
                        // Rozdělení argumentů od operace
                        opLine[i++] = Row.split("\\(");
                    }
                }
                // Procházení polem
                outerLoop:
                for (String[] Strings : opLine) {
                    if(bracket) break;
                    else if(Strings[1].equals(")")) break;
                    // Řádek pro metodu musí obsahovat 3 prvky
                    else if (Strings.length == 2) {
                        // Rozparsování argumentů metody
                        String[] beforeArg = Strings[0].split("\\s+");
                        String[] arg = Strings[1].split("(,[\\s]*)");
                        //System.out.println(arg.length);
                        String[][] argLine = new String[arg.length][10];
                        int m = 0;
                        for (String argument : arg) {
                            //argLine[m++] = argument.split("\\s+");
                            argLine[m] = argument.split("\\s+");
                            if(d.findClassifier(argLine[m][0]) != null) {
                                String storedClassifier = d.findClassifier(argLine[m][0]).getName();
                                if (argLine[m][0].matches("([void]|[i|I]nt|[s|S]tring|[B|b]oolean|[b|B]ool|[d|D]ouble|[f|F]loat|[L|l]ong|[s|S]hort|[b|B]yte|[c|C]har|" + storedClassifier + ")")) {
                                    box.getStyleClass().remove("redBox");
                                    UMLAttribute attr = new UMLAttribute(argLine[m][1], d.classifierForName(argLine[m][0]));
                                    listOfDuplicateAttr.add(attr);
                                    cls.addAttribute(attr);
                                } else {
                                    box.getStyleClass().add("redBox");
                                    listOfDuplicateAttr.clear();
                                    cls.removeAttributes();
                                    break outerLoop;
                                }
                            } else {
                                if (argLine[m][0].matches("([void]|[i|I]nt|[s|S]tring|[B|b]oolean|[b|B]ool|[d|D]ouble|[f|F]loat|[L|l]ong|[s|S]hort|[b|B]yte|[c|C]har)")) {
                                    box.getStyleClass().remove("redBox");
                                    UMLAttribute attr = new UMLAttribute(argLine[m][1], d.classifierForName(argLine[m][0]));
                                    listOfDuplicateAttr.add(attr);
                                    cls.addAttribute(attr);
                                } else {
                                    box.getStyleClass().add("redBox");
                                    listOfDuplicateAttr.clear();
                                    cls.removeAttributes();
                                    break outerLoop;
                                }
                            }
                            m++;
                        }

                        // Odstranění závorky na konci
                        StringBuffer sb = new StringBuffer(argLine[arg.length-1][1]);
                        argLine[arg.length-1][1] = sb.deleteCharAt(sb.length()-1).toString();

                        //System.out.println(argLine[2][0] + " " + argLine[2][1]);

                        // Žadné duplikátní názvy a regex pro modifikátoru přístupu
                        if (beforeArg[0].matches("[+|\\-|#|~]")){
                            // Konstruktor -> nemá navrátový typ
                            if(beforeArg[1].equals(box.getName())){
                                UMLOperation op = UMLOperation.create(beforeArg[2], listOfDuplicateAttr);
                                listOfDuplicateOper.add(op);
                                cls.addOperation(op);
                                break;
                            }
                            // Metoda může vrátit null a proto tato podmínka
                            if(d.findClassifier(beforeArg[1]) != null){
                                String storedClassifier = d.findClassifier(beforeArg[1]).getName();
                                if((beforeArg[1].matches("([void]|[i|I]nt|[s|S]tring|[B|b]oolean|[b|B]ool|[d|D]ouble|[f|F]loat|[L|l]ong|[s|S]hort|[b|B]yte|[c|C]har|" + storedClassifier + ")"))){
                                    //if(){};
                                    box.getStyleClass().remove("redBox");
                                    //UMLAttribute attr = new UMLAttribute(name[0], d.classifierForName(Strings[1]));
                                    UMLOperation op = UMLOperation.create(beforeArg[2], d.classifierForName(beforeArg[1]), listOfDuplicateAttr);
                                    /*if(!listOfDuplicateOper.contains(op)) {
                                        listOfDuplicateOper.add(op);
                                        System.out.println(listOfDuplicateOper.toString());
                                    }
                                    else System.out.println("broken");*/
                                    cls.addOperation(op);
                                } else {
                                    cls.removeOperations();
                                    box.getStyleClass().add("redBox");
                                    break;
                                }
                            }else{
                                if(beforeArg[1].matches("([void]|[i|I]nt|[s|S]tring|[B|b]oolean|[b|B]ool|[d|D]ouble|[f|F]loat|[L|l]ong|[s|S]hort|[b|B]yte|[c|C]har)")) {
                                    box.getStyleClass().remove("redBox");
                                    //UMLAttribute attr = new UMLAttribute(name[0], d.classifierForName(Strings[1]));
                                    UMLOperation op = UMLOperation.create(beforeArg[2], d.classifierForName(beforeArg[1]), listOfDuplicateAttr);
                                    /*if(!listOfDuplicateOper.contains(op)) {
                                        listOfDuplicateOper.add(op);
                                        System.out.println(listOfDuplicateOper.toString());
                                    }
                                    else System.out.println("broken");*/
                                    cls.addOperation(op);
                                } else {
                                    cls.removeOperations();
                                    box.getStyleClass().add("redBox");
                                    break;
                                }
                            }
                            // Název metody již existuje
                        } else {
                            cls.removeOperations();
                            box.getStyleClass().add("redBox");
                            break;
                        }
                        // Jestli okno metod je prázdné, je to v pořádku
                        //content.get(1)
                    } else if (box.getOperations().isEmpty()){
                        box.getStyleClass().remove("redBox");
                        break;
                        // Špatný počet prvků pro danou metodu
                    } else {
                        cls.removeOperations();
                        box.getStyleClass().add("redBox");
                        break;
                    }
                }
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
    public void SaveJson(ActionEvent event) throws IOException {
        serializeObject();
    }
    public void serializeObject() throws IOException {
        System.out.println("Serialize objfunct");
        JSONArray List = new JSONArray();

        //Serializace
        //ClassComponent box = ListofBoxes.get(0);
        //empList.forEach(emp -> parseEmpObj((JSONObject) emp));
        System.out.println("ListOfBoxes: " + ListofBoxes);
        ListofBoxes.forEach(ClassBox -> AddClassesToJson(List, (ClassComponent) ClassBox));


        System.out.println(List);
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);
        FileWriter writer = new FileWriter(selectedFile, false);
        //FileWriter writer = new FileWriter("D:\\Vskola\\4Sem\\IJA\\Projekt\\IJA---project\\xbalaz15\\newJson.json", false);
        writer.write(List.toJSONString());
        writer.close();
    }
    public void AddClassesToJson(JSONArray List, ClassComponent box)
    {
        JSONObject obj = new JSONObject();
        obj.put("x", box.x);
        obj.put("y", box.y);
        obj.put("Name", box.Name);
        obj.put("Attributes", box.Attributes);
        obj.put("Operations", box.getOperations());
        JSONObject packaging = new JSONObject();
        packaging.put("class", obj);
        List.add(0, packaging);
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
            Arrow arrow = gson.fromJson(reader, Arrow.class);
            reader.close();
            System.out.println(loadedBox.getName());
            reader.close();

            //System.out.println(loadedBox!=null);
            // close reader
            //createArrow();
            rootPane.getChildren().addAll(loadClassBox(loadedBox).getBox());
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

            HelpLoadArrow arrow = gson.fromJson(empObj.toString(), HelpLoadArrow.class);
            loadArrow(arrow);
        }
    }

    public void loadArrow(HelpLoadArrow arrow){
        if(ListofBoxNames.contains(arrow.getFrom()) && ListofBoxNames.contains(arrow.getTo())) {
            int index = ListofBoxNames.indexOf(arrow.getFrom());
            ClassComponent b1 = ListofBoxes.get(index);
            int index2 = ListofBoxNames.indexOf(arrow.getTo());
            ClassComponent b2 = ListofBoxes.get(index2);
            Arrow finalArrow = new Arrow(b1.getLayoutX(), b1.getLayoutY(), b2.getLayoutX(), b2.getLayoutY(), arrow.getArrowType());
            finalArrow.x1Property().bind(b1.layoutXProperty());
            finalArrow.y1Property().bind(b1.layoutYProperty());
            finalArrow.x2Property().bind(b2.layoutXProperty());
            finalArrow.y2Property().bind(b2.layoutYProperty());

            finalArrow.setOnMousePressed(e -> handleMouseArrow(e, finalArrow));

            b1.edges.add(finalArrow);
            b2.edges.add(finalArrow);
            rootPane.getChildren().addAll(finalArrow);
        }
    }

    private Structure loadClassBox(ClassComponent loadedBox){
        ClassComponent box = new ClassComponent(loadedBox.getX(), loadedBox.getY(), loadedBox.getName(), loadedBox.getAttributes(), loadedBox.getOperations());
        ListofBoxes.add(box);
        ListofBoxNames.add(box.getName());
        UMLClass cls = d.createClass(box.getName());

        box.setOnDragDetected(e -> onBoxDragDetected(e, box));
        box.setOnMouseDragged(e -> onBoxDragged(e, box));
        box.setOnKeyPressed(e -> handleKeyboard(e, box, cls));
        box.setOnMousePressed(e -> handleMouse(e, box, cls));

        Structure structure = new Structure(box, cls);
        return structure;
    }


}