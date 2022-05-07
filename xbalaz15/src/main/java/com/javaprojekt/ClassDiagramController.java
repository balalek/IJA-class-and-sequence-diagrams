/**
 * @author Martin Baláž
 * @author Josef Kuba
 */
package com.javaprojekt;

import com.component.*;
import com.component.ClassComponent;
import com.google.gson.*;
import com.seqComponent.ObjectWithLine;
import com.seqComponent.Messages;
import com.uml.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Controller, který se stará o chod aplikace, přesněji diagramu tříd
 */
public class ClassDiagramController{

    // Atributy
    @FXML
    private AnchorPane rootPane;

    private Stage stage;
    private ViewModel viewModel;
    private static List<ClassComponent> ListofBoxes = new LinkedList<>();
    private static List<String> ListofBoxNames = new LinkedList<>();
    private List<Object> content = new LinkedList<>();
    private List<String> duplicateAttr = new LinkedList<>();
    private List<String> listOfDuplicateAttr = new LinkedList<>();
    private List<UMLAttribute> listOfAttrForOper = new LinkedList<>();
    private List<UMLOperation> listOfDuplicateOper = new LinkedList<>();
    private List<String> listOfOldClassName = new LinkedList<>();
    private List<String> listOfNewClassName = new LinkedList<>();
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
    public static final ClassDiagram d = new ClassDiagram("Class Diagram");

    /**
     * Po zavolání této metody se okno nastaví na diagram tříd
     * @param viewModel Okno, které se má zobrazit
     */
    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel ;
    }

    /**
     * Vytvoří se event handler, pokud kliknu na pracovní plochu
     * @param event Stisknutí tlačítka InsertClass
     */
    @FXML
    public void InsertClass(ActionEvent event) {
        rootPane.setOnMousePressed(this::onGraphPressed);
    }

    /**
     * Na místo kliknutí se vytvoří tlačítko, které představuje třídu
     * @param mouseEvent Kliknutí na pracovní ploše
     */
    public void onGraphPressed(MouseEvent mouseEvent){
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            rootPane.getChildren().add(createClassBox(mouseEvent).getBox());
            objectStack.push(rootPane.getChildren().get(rootPane.getChildren().size() - 1));
            operationStack.push(operation.CREATE);
            rootPane.setOnMousePressed(null);
        }
    }

    /**
     * Vytvoření tlačítka na místo kliknutí, které představuje třídu a zajištění jeho editaci
     * @param mouseEvent Místo kliknutí, slouží ke získání souřadnicí místa kliknutí
     * @return Vrací objekt třídy Structure, ve které je uložena daná třída graficky a kódově
     */
    private Structure createClassBox(MouseEvent mouseEvent){
        ClassComponent box = new ClassComponent(mouseEvent.getX(), mouseEvent.getY());
        ClassComponent.getListofBoxes().add(box);
        ListofBoxNames.add(box.getName());
        UMLClass cls = d.createClass(box.getName());

        box.setOnDragDetected(e -> onBoxDragDetected(e, box));
        box.setOnMouseDragged(e -> onBoxDragged(e, box));
        box.setOnKeyPressed(e -> handleKeyboard(e, box, cls));
        box.setOnMousePressed(e -> handleMouse(e, box, cls));

        Structure structure = new Structure(box, cls);
        return structure;
    }

    /**
     * Jakmile se začne posouvat s tlačítkem, uloží se původní pozice pro operaci undo
     * @param e Kliknutí a posunutí třídou
     * @param box Třída, ktera je posouvána
     */
    private void onBoxDragDetected(MouseEvent e, ClassComponent box) {
        if(e.getButton().equals(MouseButton.PRIMARY)) {
            box.toFront();
            objectStack.push(box);
            coorX.push(box.getLayoutX());
            coorY.push(box.getLayoutY());
            operationStack.push(operation.DRAG);
        }
    }

    /**
     * Při posouvání třídou levým tlačítkem myši se ukládají její nové souřadnice
     * @param e Pousouvání třídou levým tlačítkem myši
     * @param box Třída, ktera je posouvána
     */
    private void onBoxDragged(MouseEvent e, ClassComponent box) {
        if(e.getButton().equals(MouseButton.PRIMARY)) {
            box.setLayoutX(box.getLayoutX() + e.getX() + box.getTranslateX());
            box.setLayoutY(box.getLayoutY() + e.getY() + box.getTranslateY());
            box.setX(box.getLayoutX() + e.getX() + box.getTranslateX());
            box.setY(box.getLayoutY() + e.getY() + box.getTranslateY());
        }
    }

    /**
     * Po double kliknutí levým tlačítkem myši se otevře okno, ve kterém se modifikuje obsah třídy (název, atributy, operace a typ třídy),
     * po stisknutí tlačítka confirm se obsah uloží do labelů uvnitř tlačítka a obsah se parsuje a kontroluje zda je zapsaný
     * správným formátem (syntaxí). Po zadání nepodporovaného textu, je třída červeně ohraničena a čeká na svou opravu.
     * Po stisknutí pravého tlačítka na dvě různé třídy se mezi nimi vytvoří defaultní vztah a to je asociace
     * @param evt Kliknutí na třídu
     * @param box Modifikovatelná třída
     * @param cls Třída uložená v diagramu
     */
    public void handleMouse(MouseEvent evt, ClassComponent box, UMLClass cls){
        d.addName("Main");
        //System.out.println(evt.getX() + "\n" + evt.getY());
        if(evt.getButton().equals(MouseButton.PRIMARY)){
            if(evt.getClickCount() == 2){
                String oldName = box.getName();
                d.addName(oldName);
                listOfOldClassName.add(oldName);

                // Pushování do stacku pro undo operace
                objectStack.push(box);
                operationStack.push(operation.RENAME);
                nameStack.push(box.getName());

                // Udržování aktuálních dat
                content.addAll(EditClassComponent.display(box));

                // Udržování aktuálního typu třídy
                box.setClassType((String) content.get(3));
                switch (box.getClassType()) {
                    case "":
                        box.setNormal(true);
                        box.setAbstractClass(false);
                        box.setInterface(false);
                        break;
                    case "<<Abstract>>":
                        box.setAbstractClass(true);
                        box.setInterface(false);
                        box.setNormal(false);
                        break;
                    case "<<Interface>>":
                        box.setInterface(true);
                        box.setAbstractClass(false);
                        box.setNormal(false);
                        break;
                }

                box.setName((String)content.get(0));

                // Parsování atributů
                duplicateAttr.clear();
                cls.removeAttributes();
                box.setAttributes((String)content.get(1));

                // Rozdělení atributů do 2D pole podle nových řádků a poté podle mezer
                String[] lines = ((String)content.get(1)).split("\\r?\\n");
                String[][] attrLine = new String[lines.length][];

                // Parsování metod
                cls.removeOperations();
                box.setOperations((String)content.get(2));
                listOfDuplicateAttr.clear();
                listOfAttrForOper.clear();

                // Rozdělení operací do 2D pole podle nových řádků a poté podle mezer
                String[] Lines = ((String)content.get(2)).split("\\r?\\n");
                String[][] opLine = new String[Lines.length][];

                content.removeAll(content);
                box.setClassTypeProperty(box.getClassType());
                box.setNameProperty(box.getName());

                Boolean emptyTextBox = false;
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
                            if(strings[1].matches("(List<\\w+>)")) d.classifierForName(strings[1]);
                            if(d.findClassifier(strings[1]) != null){
                                String storedClassifier = d.findClassifier(strings[1]).getName();
                                if((strings[1].matches("([i|I]nt|[s|S]tring|[B|b]oolean|[b|B]ool|[d|D]ouble|[f|F]loat|[L|l]ong|[s|S]hort|[b|B]yte|[c|C]har|" + storedClassifier + ")"))){
                                    box.getStyleClass().remove("redBox");
                                    duplicateAttr.add(strings[2]);
                                    UMLAttribute attr = new UMLAttribute(strings[2], d.classifierForName(strings[1]));
                                    cls.addAttribute(attr);
                                } else {
                                    emptyTextBox = true;
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
                                    emptyTextBox = true;
                                    duplicateAttr.clear();
                                    cls.removeAttributes();
                                    box.getStyleClass().add("redBox");
                                    break;
                                }
                            }
                            // Název atributu již existuje
                        } else {
                            emptyTextBox = true;
                            duplicateAttr.clear();
                            cls.removeAttributes();
                            box.getStyleClass().add("redBox");
                            break;
                        }
                        // Jestli okno atributů je prázdné, je to v pořádku
                    } else if (box.getAttributes().isEmpty()){
                        box.getStyleClass().remove("redBox");
                        break;
                        // Špatný počet prvků pro daný atribut
                    } else {
                        emptyTextBox = true;
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
                    if(box.getOperations().isEmpty()) break;
                    StringBuffer sb = new StringBuffer(Row);
                    // Každý řádek musí být zakončen uzavírací závorkou
                    if (sb.charAt(sb.length()-1) != ')'){
                        box.getStyleClass().add("redBox");
                        bracket = true;
                        break;
                    } else {
                        if(!emptyTextBox) {
                            box.getStyleClass().remove("redBox");
                            // Rozdělení argumentů od operace
                            opLine[i++] = Row.split("\\(");
                        }
                    }
                }
                // Procházení polem
                outerLoop:
                for (String[] Strings : opLine) {
                    if(bracket) break;
                    // Jestli okno metod je prázdné, je to v pořádku
                    else if (box.getOperations().isEmpty() && !emptyTextBox) {
                        System.out.println("jsi tu?");
                        box.getStyleClass().remove("redBox");
                        break;
                    }
                    // Špatný počet prvků pro danou metodu
                    else if(Strings != null) {
                        if (Strings[1].equals(")")) {
                            String[] beforeArg = Strings[0].split("\\s+");
                            if (beforeArg.length == 2) {
                                if (beforeArg[0].matches("[+|\\-|#|~]")) {
                                    // Konstruktor bez parametrů -> nemá navrátový typ
                                    if (beforeArg[1].equals(box.getName()) && !emptyTextBox) {
                                        box.getStyleClass().remove("redBox");
                                        UMLOperation op = UMLOperation.create(beforeArg[1], listOfAttrForOper);
                                        listOfDuplicateOper.add(op);
                                        cls.addOperation(op);

                                    } else {
                                        box.getStyleClass().add("redBox");
                                        listOfDuplicateOper.clear();
                                        cls.removeOperations();
                                    }
                                    break;
                                }
                                break;
                            }
                        }
                        // Řádek pro metodu musí obsahovat 3 prvky
                        else if (Strings.length == 2) {
                            // Rozparsování argumentů metody
                            String[] beforeArg = Strings[0].split("\\s+");
                            // Odstranění závorky na konci
                            String[] arg = Strings[1].split("\\)");
                            arg = arg[0].split("(,[\\s]*)");
                            String[][] argLine = new String[arg.length][10];
                            int m = 0;
                            listOfDuplicateAttr.clear();
                            listOfAttrForOper.clear();
                            for (String argument : arg) {
                                argLine[m] = argument.split("\\s+");
                                if (d.findClassifier(argLine[m][0]) != null) {
                                    String storedClassifier = d.findClassifier(argLine[m][0]).getName();
                                    if (argLine[m][0].matches("([void]|[i|I]nt|[s|S]tring|[B|b]oolean|[b|B]ool|[d|D]ouble|[f|F]loat|[L|l]ong|[s|S]hort|[b|B]yte|[c|C]har|" + storedClassifier + ")")) {
                                        UMLAttribute attr = new UMLAttribute(argLine[m][1], d.classifierForName(argLine[m][0]));
                                        listOfAttrForOper.add(attr);
                                        if (!listOfDuplicateAttr.contains(attr.getName()) && !emptyTextBox) {
                                            box.getStyleClass().remove("redBox");
                                            listOfDuplicateAttr.add(attr.getName());
                                        } else {
                                            box.getStyleClass().add("redBox");
                                            listOfDuplicateAttr.clear();
                                            listOfAttrForOper.clear();
                                            break outerLoop;
                                        }
                                        cls.addAttribute(attr);
                                    } else {
                                        box.getStyleClass().add("redBox");
                                        listOfDuplicateAttr.clear();
                                        listOfAttrForOper.clear();
                                        break outerLoop;
                                    }
                                } else {
                                    if (argLine[m][0].matches("([void]|[i|I]nt|[s|S]tring|[B|b]oolean|[b|B]ool|[d|D]ouble|[f|F]loat|[L|l]ong|[s|S]hort|[b|B]yte|[c|C]har)")) {
                                        UMLAttribute attr = new UMLAttribute(argLine[m][1], d.classifierForName(argLine[m][0]));
                                        listOfAttrForOper.add(attr);
                                        if (!listOfDuplicateAttr.contains(attr.getName()) && !emptyTextBox) {
                                            box.getStyleClass().remove("redBox");
                                            listOfDuplicateAttr.add(attr.getName());
                                        } else {
                                            box.getStyleClass().add("redBox");
                                            listOfDuplicateAttr.clear();
                                            listOfAttrForOper.clear();
                                            break outerLoop;
                                        }
                                    } else {
                                        box.getStyleClass().add("redBox");
                                        listOfDuplicateAttr.clear();
                                        listOfAttrForOper.clear();
                                        break outerLoop;
                                    }
                                }
                                m++;
                            }
                            listOfDuplicateAttr.clear();

                            // Žadné duplikátní názvy a regex pro modifikátoru přístupu
                            if (beforeArg[0].matches("[+|\\-|#|~]")) {
                                // Konstruktor -> nemá navrátový typ
                                if (beforeArg.length == 2) {
                                    if (beforeArg[1].equals(box.getName()) && !emptyTextBox) {
                                        box.getStyleClass().remove("redBox");
                                        UMLOperation op = UMLOperation.create(beforeArg[1], listOfAttrForOper);
                                        listOfDuplicateOper.add(op);
                                        cls.addOperation(op);
                                    } else {
                                        box.getStyleClass().add("redBox");
                                        listOfDuplicateOper.clear();
                                        cls.removeOperations();
                                    }
                                    break;
                                }
                                // Metoda může vrátit null a proto tato podmínka
                                if (d.findClassifier(beforeArg[1]) != null) {
                                    String storedClassifier = d.findClassifier(beforeArg[1]).getName();
                                    if ((beforeArg[1].matches("([void]|[i|I]nt|[s|S]tring|[B|b]oolean|[b|B]ool|[d|D]ouble|[f|F]loat|[L|l]ong|[s|S]hort|[b|B]yte|[c|C]har|" + storedClassifier + ")")) && !emptyTextBox) {
                                        box.getStyleClass().remove("redBox");
                                        UMLOperation op = UMLOperation.create(beforeArg[2], d.classifierForName(beforeArg[1]), listOfAttrForOper);
                                        if (!listOfDuplicateOper.contains(op)) {
                                            listOfDuplicateOper.add(op);
                                            System.out.println(listOfDuplicateOper.toString());
                                        } else System.out.println("broken");
                                        cls.addOperation(op);
                                    } else {
                                        cls.removeOperations();
                                        box.getStyleClass().add("redBox");
                                        break;
                                    }
                                } else {
                                    if (beforeArg[1].matches("([void]|[i|I]nt|[s|S]tring|[B|b]oolean|[b|B]ool|[d|D]ouble|[f|F]loat|[L|l]ong|[s|S]hort|[b|B]yte|[c|C]har)")) {
                                        box.getStyleClass().remove("redBox");
                                        UMLOperation op = UMLOperation.create(beforeArg[2], d.classifierForName(beforeArg[1]), listOfAttrForOper);
                                        if (!listOfDuplicateOper.contains(op)) {
                                            listOfDuplicateOper.add(op);
                                            System.out.println(listOfDuplicateOper.toString());
                                        } else System.out.println("broken");
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
                        } else {
                            cls.removeOperations();
                            box.getStyleClass().add("redBox");
                            break;
                        }
                    }
                    //listOfDuplicateOper.clear();
                }

                listOfAttrForOper.clear();
                listOfDuplicateAttr.clear();
                box.setOperationProperty(box.getOperations());

                // Přejmenování třídy v programové části
                listOfNewClassName.add(cls.getName());
                cls.rename(box.getName());
                listOfNewClassName.add(cls.getName());
                d.renameClass(oldName, cls.getName());
                d.renameName(oldName, cls.getName());
            }
        }
        // Vytvoření vztahu mezi dvěmi třídami
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

    /**
     * Vytvoření šipky mezi dvěmi třídami a automatické nastavování souřadnic na hranu třídy při pohybu tříd
     * @param b1 Třída od které šípka vychází
     * @param b2 Třída do které šipka směruje
     * @return Výsledná šipka
     */
    public Arrow createArrow(ClassComponent b1, ClassComponent b2){
        Arrow arrow = new Arrow(b1, b2);
        arrow.x1Property().bind(b1.layoutXProperty());
        arrow.y1Property().bind(b1.layoutYProperty());
        arrow.x2Property().bind(b2.layoutXProperty());
        arrow.y2Property().bind(b2.layoutYProperty());
        arrow.setFrom(b1.getName());
        arrow.setTo(b2.getName());
        arrow.setOnMousePressed(e -> handleMouseArrow(e, arrow));

        b1.edges.add(arrow);
        b2.edges.add(arrow);
        rootPane.getChildren().addAll(arrow);
        return arrow;
    }

    /**
     * Po kliknutí na šipku levým double klikem se objeví okno s nabídkou jiných typů vztahu (šipek).
     * Po stisknutí daného typu se šipka vizuálně změní. Pokud klikneme pravým tlačítkem tak se šipka odstraní.
     * @param e Kliknutí myší
     * @param arrow Vybraná šipka
     */
    private void handleMouseArrow(MouseEvent e, Arrow arrow) {
        if(e.getButton().equals(MouseButton.SECONDARY)) {
            Arrow.ListOfArrows.remove(arrow);
            objectStack.push(arrow);
            operationStack.push(operation.REMOVE);
            arrow.getFromBox().getOpLabel().setStyle("");
            rootPane.getChildren().remove(arrow);
        }
        if(e.getButton().equals(MouseButton.PRIMARY)) {
            if (e.getClickCount() == 2) {
                objectStack.push(arrow);
                operationStack.push(operation.CHANGE);
                nameStack.push(arrow.getArrowType());
                arrowType = EditArrowComponent.display(arrow);
                arrow.setArrowType(arrowType);
                Highlight(arrow);
                arrow.update();
            }
        }
    }

    /**
     * Tato funkce modře vyznačí třídu, ze které vychází generalizace a má společnou metodu s ze které dědí.
     * @param arrow Šipka, která propojuje třídy.
     */
    public void Highlight(Arrow arrow){
        if(!Objects.equals(arrow.getArrowType(), "generalization")) {
            arrow.getFromBox().getOpLabel().setStyle("");
            return;
        }
        ClassComponent from = arrow.getFromBox();
        ClassComponent to = arrow.getToBox();
        String[] FromMethodList = from.getOperations().split("\n");
        String[] ToMethodList = to.getOperations().split("\n");
        boolean Found = false;
        for(int i = 0; (i < FromMethodList.length) && !Found; i++){
            for(int j = 0; (j < ToMethodList.length) && !Found ; j++){
                if(Objects.equals(FromMethodList[i], ToMethodList[j])) Found = true;
            }
        }
        if(Found) from.getOpLabel().setStyle("-fx-border-style: solid;" + "-fx-border-width: 0 0 1 0;" + "-fx-border-color: blue;");
        else from.getOpLabel().setStyle("");
    }

    /**
     * Po stisknutí tlačítka Undo se ze zásobníku popne předchozí stav a aplikuje se
     */
    public void Undo(){
        if(!rootPane.getChildren().isEmpty()){
            if(!objectStack.isEmpty() && !operationStack.isEmpty()) {
                if(operationStack.peekFirst() == operation.CREATE) {
                    if (objectStack.peekFirst() instanceof ClassComponent) {
                        ClassComponent tmp = (ClassComponent) objectStack.peekFirst();
                        ClassComponent.getListofBoxes().remove(tmp);
                        ListofBoxNames.remove(tmp.getName());
                        d.deleteName(tmp.getName());
                    }
                    rootPane.getChildren().remove((Node) objectStack.pop());
                    operationStack.pop();
                }else if(operationStack.peekFirst() == operation.REMOVE){
                    if (objectStack.peekFirst() instanceof ClassComponent) {
                        ClassComponent tmp = (ClassComponent) objectStack.peekFirst();
                        d.addName(tmp.getName());
                    }
                    rootPane.getChildren().add((Node) objectStack.pop());
                    operationStack.pop();
                }else if(operationStack.peekFirst() == operation.RENAME){
                    //System.out.println("cuss");
                    //stackOfOldClassName.push(box.getName());
                    //ClassComponent tmp = (ClassComponent) objectStack.peekFirst();
                    /*System.out.println(listOfOldClassName);
                    System.out.println(tmp.getName());
                    if(listOfNewClassName.contains(tmp.getName())) {
                        int index = listOfNewClassName.indexOf(tmp.getName());
                        d.renameName(listOfOldClassName.get(index), tmp.getName());
                        System.out.println("cuss");
                    }*/
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
                    box.setX(box.getLayoutX());
                    box.setY(box.getLayoutY());
                    operationStack.pop();
                    // Refresh arrows
                    box.setLayoutY(box.getLayoutY()+10);
                    box.setLayoutY(box.getLayoutY()-10);
                }
            }
        } else {
            if(!objectStack.isEmpty() && !operationStack.isEmpty()) {
                if(operationStack.peekFirst() == operation.REMOVE){
                    if (objectStack.peekFirst() instanceof ClassComponent) {
                        ClassComponent tmp = (ClassComponent) objectStack.peekFirst();
                        d.addName(tmp.getName());
                    }
                    rootPane.getChildren().add((Node) objectStack.pop());
                    operationStack.pop();
                }
            }
        }
    }

    /**
     * Po vybrání třídy a stisknutí tlačítka Delete se zavolá metoda delete()
     * @param evt Stisknutá klávesa na klávesnici
     * @param box Vybraná třída v grafické části
     * @param cls Vybraná třída v programové části
     */
    public void handleKeyboard(KeyEvent evt, ClassComponent box, UMLClass cls){
        if (evt.getCode() == KeyCode.DELETE) delete(box, cls);
    }

    /**
     * Po zavolání této metody se předaná třída smaže
     * @param box Mazaná třída v grafické části
     * @param cls Mazaná třída v programové části
     */
    public void delete(ClassComponent box, UMLClass cls){
        ClassComponent.getListofBoxes().remove(box);
        ListofBoxNames.remove(box.getName());
        d.deleteName(box.getName());
        d.removeClass(box.getName(), cls);
        objectStack.push(box);
        operationStack.push(operation.REMOVE);
        rootPane.getChildren().remove(box);
        // Smažou se i čáry připojené na třídu
        for(Arrow arrow : box.edges) {
            Arrow.ListOfArrows.remove(arrow);
            objectStack.push(arrow);
            operationStack.push(operation.REMOVE);
            rootPane.getChildren().remove(arrow);
        }
    }

    /**
     * Po stisknutí tlačítka Sequence diagram se změní okno na sekvenční diagram
     * @param event Stisknutí tlačítka
     * @throws IOException
     */
    @FXML
    public void SwitchToSeqDiagram(ActionEvent event) throws IOException {
        viewModel.setCurrentView(ViewModel.View.B);
        for (ObjectWithLine tmp:
                ObjectWithLine.getListObjectWithLine()) {
            if(!d.getListOfClassNames().contains(tmp.getNameClass())){
                tmp.getClassButton().setStyle("-fx-border-color: red;");
                //System.out.println("Zacervenat");
            }else{
                tmp.getClassButton().setStyle("");
                //System.out.println("Zcervenani odstranit");
            }
        }
        for (Messages arrow:
             Messages.getListOfArrows()) {
             arrow.CheckArrowMessage();
        }
    }

    /**
     * Zobrazí nápovědu, jak ovládat daigram tříd
     * @param event Stisknutí tlačítka Help
     */
    @FXML
    public void showHelp(ActionEvent event){ShowHelp.display();}

    /**
     * Po stisknutí tlačítka se zavolá metoda, která bude načítat ze souboru
      * @param event Stisknutí tlačítka Save
     * @throws IOException
     */
    @FXML
    public void SaveJson(ActionEvent event) throws IOException {
        serializeObject();
    }

    /**
     * Pokud byl vybrán cílový soubor uloží Třídy a šipky do zvoleného json souboru
     * (Volá metody AddArrowsToJson a AddClassesToJson)
     * @throws IOException
     */
    public void serializeObject() throws IOException {
        JSONArray List = new JSONArray();

        Arrow.getListOfArrows().forEach(arrow -> AddArrowsToJson(List, (Arrow) arrow));
        ClassComponent.getListofBoxes().forEach(ClassBox -> AddClassesToJson(List, (ClassComponent) ClassBox));

        System.out.println(List);
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null){
            FileWriter writer = new FileWriter(selectedFile, false);
            writer.write(List.toJSONString());
            writer.close();
        }else System.out.println("Nebyla vybrana cesta");
    }

    /**
     * Tato Metoda uloží data třídy do json struktury, zabalí je a přidá do listu
     * @param List List do kterého se třídy přidávají
     * @param box Třída kterou budeme ukládat
     */
    public void AddClassesToJson(JSONArray List, ClassComponent box){
        JSONObject obj = new JSONObject();
        obj.put("x", box.x);
        obj.put("y", box.y);
        obj.put("Name", box.Name);
        obj.put("Attributes", box.Attributes);
        obj.put("Operations", box.getOperations());
        obj.put("ClassType", box.getClassType());
        JSONObject packaging = new JSONObject();
        packaging.put("class", obj);
        List.add(0, packaging);
    }

    /**
     * Tato Metoda uloží data šipky do json struktury, zabalí je a přidá do listu
     * @param List List do kterého se třídy přidávají
     * @param arrow Šipka kterou budeme ukládat
     */
    public void AddArrowsToJson(JSONArray List, Arrow arrow){
        JSONObject obj = new JSONObject();
        obj.put("from", arrow.getFrom());
        obj.put("to", arrow.getTo());
        obj.put("arrowType", arrow.getArrowType());
        JSONObject packaging = new JSONObject();
        packaging.put("messageArrow", obj);
        List.add(0, packaging);
    }

    /**
     * Po stisknutí tlačítka se zavolá metoda, která bude ukládat do souboru
     * @param event Stisknutí tlačítka Load
     * @throws InterruptedException
     */
    @FXML
    public void LoadJson(ActionEvent event) throws InterruptedException {
        deserializeObject();
    }

    /**
     * Metoda načte z vybraného souboru třídy a šipky
     */
    public void deserializeObject(){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null) {
            try {
                // Parse Json file
                JSONParser jsonP = new JSONParser();
                Reader reader = Files.newBufferedReader(Paths.get(selectedFile.getAbsolutePath()));
                Object obj = jsonP.parse(reader);
                JSONArray empList = (JSONArray) obj;
                // Iterate over emp array
                try{
                    empList.forEach(emp -> parseEmpObj((JSONObject) emp));
                }catch (IndexOutOfBoundsException exception)
                {
                    System.out.println("Index chyba");
                    ListofBoxNames.clear();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else System.out.println("Nebyla vybrana cesta");

    }

    /**
     * Parsování tříd a jejich zobrazení
     * @param emp JSONObject který se bude parsovat
     */
    private void parseEmpObj(JSONObject emp) {
        if(emp.get("class") != null){
            JSONObject empObj = (JSONObject) emp.get("class");
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            ClassComponent loadedBox = gson.fromJson(empObj.toString(), ClassComponent.class);
            rootPane.getChildren().addAll(loadClassBox(loadedBox).getBox());
        }else if(emp.get("messageArrow") != null)
        {
            JSONObject empObj = (JSONObject) emp.get("messageArrow");
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            HelpLoadArrow arrow = gson.fromJson(empObj.toString(), HelpLoadArrow.class);
            loadArrow(arrow);
        }
    }

    /**
     * Tlačítko refresh slouží k aktualizaci šipek po načtení diagramu tříd ze souboru
     * @param e Stisknutí tlačítka Refresh
     */
    @FXML
    public void refresh(ActionEvent e) {
        for (ClassComponent box : ClassComponent.getListofBoxes()) {
            box.setLayoutY(box.getLayoutY() + 10);
            box.setLayoutY(box.getLayoutY() - 10);
        }
        Arrow.getListOfArrows().forEach(arrow1 -> Highlight((Arrow) arrow1));
    }

    /**
     * Slouží ke správnému načtení šipek z JSON souboru, metoda převzatá a upravená z createArrow
     * @param arrow Šipka typu speciální třídy vytvořené kvůli správnému načítání šipek
     */
    public void loadArrow(HelpLoadArrow arrow){
        if(ListofBoxNames.contains(arrow.getFrom()) && ListofBoxNames.contains(arrow.getTo())) {
            int index = ListofBoxNames.indexOf(arrow.getFrom());
            ClassComponent b1 = ClassComponent.getListofBoxes().get(index);
            int index2 = ListofBoxNames.indexOf(arrow.getTo());
            ClassComponent b2 = ClassComponent.getListofBoxes().get(index2);
            Arrow finalArrow = new Arrow(b1, b2, arrow.getArrowType());
            finalArrow.x1Property().bind(b1.layoutXProperty());
            finalArrow.y1Property().bind(b1.layoutYProperty());
            finalArrow.x2Property().bind(b2.layoutXProperty());
            finalArrow.y2Property().bind(b2.layoutYProperty());
            finalArrow.setFrom(b1.getName());
            finalArrow.setTo(b2.getName());
            finalArrow.setOnMousePressed(e -> handleMouseArrow(e, finalArrow));

            b1.edges.add(finalArrow);
            b2.edges.add(finalArrow);
            rootPane.getChildren().addAll(finalArrow);
        }
    }

    /**
     * Slouží ke správnému načtení tříd z JSON souboru, metoda převzatá a upravená z createClassBox
     * @param loadedBox Načítaná třída
     * @return Struktura s grafickou a programovou třídou
     */
    private Structure loadClassBox(ClassComponent loadedBox){
        ClassComponent box = new ClassComponent(loadedBox.getX(), loadedBox.getY(), loadedBox.getName(), loadedBox.getAttributes(), loadedBox.getOperations(), loadedBox.getClassType());
        ClassComponent.getListofBoxes().add(box);
        ListofBoxNames.add(box.getName());
        UMLClass cls = d.createClass(box.getName());
        d.addName(box.getName());

        box.setOnDragDetected(e -> onBoxDragDetected(e, box));
        box.setOnMouseDragged(e -> onBoxDragged(e, box));
        box.setOnKeyPressed(e -> handleKeyboard(e, box, cls));
        box.setOnMousePressed(e -> handleMouse(e, box, cls));

        Structure structure = new Structure(box, cls);
        return structure;
    }

    /**
     * Po stlačení tlačítka Clear metoda odstraní všechny komponenty z obrazovky i z paměti
     * @param e Stisknutí tlačítka Clear
     */
    @FXML
    public void Clear(ActionEvent e){
        Arrow.ListOfArrows.clear();
        listOfNewClassName.clear();
        listOfOldClassName.clear();
        listOfDuplicateOper.clear();
        listOfAttrForOper.clear();
        listOfDuplicateAttr.clear();
        // Smažou se i čáry připojené na třídu
        for(ClassComponent box : ListofBoxes) {
            box.edges.clear();
        }
        ListofBoxes.clear();
        ListofBoxNames.clear();
        objectStack.clear();
        operationStack.clear();
        nameStack.clear();
        coorX.clear();
        coorY.clear();
        content.clear();
        d.getListOfClassNames().clear();
        d.getListOfClassif().clear();
        d.getListOfNames().clear();
        // Třídy budou pojmenovány opět od 1
        ClassComponent.setCount(1);
        rootPane.getChildren().clear();
    }
}