/**
 * @author Martin Baláž
 * @author Josef Kuba
 */
package com.javaprojekt;

import com.seqComponent.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Controller, který se stará o chod aplikace, přesněji sekvenčního diagramu
 */
public class SequenceDiagramController{

    // Atributy
    @FXML
    public TabPane tabPane;
    private ViewModel viewModel ;
    private String arrowType;
    private List<String> ObjectNames = new LinkedList<>();
    private List<String> content = new LinkedList<>();
    private static int count = 0;
    Button firstBox;
    Line firstLine;
    static Double x1;
    Double y1;
    Messages arrow;

    // GETry
    public List<String> getObjectNames() {
        return ObjectNames;
    }
    public List<String> getContent() {
        return content;
    }

    /**
     * Po zavolání této metody se okno nastaví na sekvenční diagram
     * @param viewModel Okno, které se má zobrazit
     */
    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Tlačítko, které vás po kliknutí přesměruje na class diagram
     * @param event zmáčknutí tlačítka
     * @throws IOException
     */
    @FXML
    public void SwitchToClassDiagram(ActionEvent event) throws IOException {
        viewModel.setCurrentView(ViewModel.View.A);
    }

    /**
     * Vytvoří se event handler, pokud kliknu na pracovní plochu
     * @param event Stisknutí tlačítka Add Class
     */
    @FXML
    public void InsertObject(ActionEvent event){
        tabPane.getSelectionModel().getSelectedItem().getContent().setOnMousePressed(this::onTabPanePressed);
    }

    /**
     * Na místo kliknutí se vytvoří tlačítko, které představuje objekt, čárkovanou čáru, která představuje časovou osu a na ní aktivační box
     * @param mouseEvent Kliknutí na pracovní ploše
     */
    public void onTabPanePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().add(createObjectWithLine(mouseEvent));
            tabPane.getSelectionModel().getSelectedItem().getContent().setOnMousePressed(null);
        }
    }

    /**
     * Vytvoření tlačítka na místo kliknutí, které představuje objekt a zajištění jeho editaci
     * Vytvoření časové osy s posouvacím aktivačním boxem
     * @param mouseEvent Místo kliknutí, slouží ke získání souřadnicí místa kliknutí
     * @return Vrací objekt třídy ObjectWithLine
     */
    public ObjectWithLine createObjectWithLine(MouseEvent mouseEvent){
        ObjectWithLine box = new ObjectWithLine(mouseEvent.getX(), mouseEvent.getY());
        //ListofBoxes.add(box);
        //ListofBoxNames.add(box.getName());
        box.getClassButton().setOnMouseDragged(e -> onBoxDragged(e, box));
        box.setOnKeyPressed(e -> handleKeyboard(e, box));
        box.getTimeLineButton().setOnMouseDragged(e -> onCallBoxDragged(e, box));
        box.getTimeLineButton().setOnKeyPressed(e -> handleKeyboardTimeLine(e, box.getTimeLineButton()));
        box.setOnMouseEntered(e -> onObjectWithLineHover(e, box));
        return box;
    }

    /**
     * Pokud najedu na objekt třídy ObjectWithLine, mohu si vybrat na který komponent kliknu
     * @param evt Místo kliknutí
     * @param box Objekt s čárou a aktivačním boxem
     */
    public void onObjectWithLineHover(MouseEvent evt, ObjectWithLine box){
        box.getTimeLineButton().setOnMousePressed(e -> handleMouseArrow(e, box, true, false));
        box.getClassButton().setOnMousePressed(e -> handleMouseArrow(e, box, false, false));
        box.getLine().setOnMousePressed(e -> handleMouseArrow(e, box, false, true));
    }

    /**
     * Double-kliknutím na objektový box jej mohu editovat, dále kliknutím pravým tlačítkem na jednotlivé komponenty
     * třídy ObjectWithLine
     * @param evt Kliknutí na objekt z třídy ObjectWithLine
     * @param box Objekt, který může být objektový box, aktivační box, nebo časová osa
     * @param isTimeBox Jestli box je aktivační box
     * @param isTimeLine Jestli box je časová osa
     */
    public void handleMouseArrow(MouseEvent evt, ObjectWithLine box, Boolean isTimeBox, Boolean isTimeLine){
        // Kliknutí na objektový box
        if (evt.getButton().equals(MouseButton.PRIMARY)) {
            if(evt.getClickCount() == 2) {
                if (!isTimeBox && !isTimeLine) {
                    // Udržování aktuálních dat
                    content.addAll(EditObjectName.displayObject(box));
                    box.setNameObject(content.get(0));
                    box.setNameClass(content.get(1));
                    content.removeAll(content);
                    box.setNameAndObjectProperty(box.getNameObject() + ":" + box.getNameClass());

                    ClassDiagramController.d.addName("Main");
                    ObjectNames = ClassDiagramController.d.getListOfClassNames();
                    if(!ClassDiagramController.d.getListOfClassNames().contains(box.getNameClass())){
                        box.getClassButton().setStyle("-fx-border-color: red;");
                    }else{
                        box.getClassButton().setStyle("");
                    }
                }
            }
        }
        // Vytvoření zprávy mezi objekty, čárami a aktivačními boxy
        if(evt.getButton().equals(MouseButton.SECONDARY)){
            count++;
            if(count == 1){
                // První může být časová osa, nebo call-box
                if(!isTimeBox && !isTimeLine) count = 0;
                if(isTimeBox) {
                    // Přesné souřadnice
                    Bounds boundsInScene = box.getTimeLineButton().localToScene(box.getTimeLineButton().getBoundsInLocal());
                    // Odečítání z důvodu, že na scéně na levo a nahoře je menu a nepatří do kreslící plochy
                    x1 = (boundsInScene.getMinX() - 157 + evt.getX());
                    y1 = (boundsInScene.getMinY() - 28 + evt.getY());
                    firstBox = box.getTimeLineButton();
                    firstLine = null;
                // První je časová čára
                } else if(isTimeLine){
                    Bounds boundsInScene = box.getLine().localToScene(box.getLine().getBoundsInLocal());
                    x1 = (boundsInScene.getMinX() - 157);
                    firstLine = box.getLine();
                    firstBox = null;
                }
            }else if(count == 2){
                // Když první byl aktivační box, můžes spojit s čímkoli
                if(firstBox != box.getTimeLineButton() && firstBox != null) {
                    if(isTimeBox) {
                        Bounds boundsInScene = box.getTimeLineButton().localToScene(box.getTimeLineButton().getBoundsInLocal());
                        Double x2 = (boundsInScene.getMinX() - 157 + evt.getX());
                        arrow = createMessage(firstBox, box.getTimeLineButton(), x1, y1, x2);
                    } else if(isTimeLine) {
                        Bounds boundsInScene = box.getLine().localToScene(box.getLine().getBoundsInLocal());
                        Double x2 = (boundsInScene.getMinX() - 157);
                        arrow = createMessageToLine(firstBox, box.getLine(), x1, y1, x2);
                    }
                    else {
                        Bounds boundsInScene = box.getClassButton().localToScene(box.getClassButton().getBoundsInLocal());
                        Double x2 = (boundsInScene.getMinX() - 157 + evt.getX());
                        arrow = CreateMessageToObject(firstBox, box.getClassButton(), x1, y1, x2);
                    }

                // Z čáry na call-box
                } else if(firstLine != box.getLine() && firstLine != null){
                    if(isTimeBox) {
                        Bounds boundsInScene = box.getTimeLineButton().localToScene(box.getTimeLineButton().getBoundsInLocal());
                        Double x2 = (boundsInScene.getMinX() - 157 + evt.getX());
                        Double y2 = (boundsInScene.getMinY() - 28 + evt.getY());
                        arrow = createMessageWithLineFirst(firstLine, box.getTimeLineButton(), x1, y2, x2);
                    }
                }
                count = 0;
            }
        }
        // Zruší se objekt (objeví se na konci časové osy křížek)
        if(evt.getButton().equals(MouseButton.MIDDLE)){
            if(evt.getClickCount() == 1) box.setDestroyObject();
            else if(evt.getClickCount() == 2) box.deleteDestroyObject();
        }
    }

    /**
     * Pokud zmáčknem W, zavolá se metoda expand, pokud S, tak se zavolá reduce
     * @param e Stisknutá klávesa
     * @param callBox Aktivační box
     */
    private void handleKeyboardTimeLine(KeyEvent e, Button callBox){
        KeyCode k = e.getCode();
        switch (k) {
            case W:
                expand(callBox);
                break;
            case S:
                reduce(callBox);
                break;
        }
    }

    /**
     * Aktivační box se naplňuje prázdnými řádky, čímž se zvětšují
     * @param callBox zvětšované tlačítko na časové ose
     */
    private void expand(Button callBox){
        callBox.setText(callBox.getText() + "\n");
    }

    /**
     * Aktivační box se vyresetuje na původní velikost
     * @param callBox vyresetované tlačítko na časové ose
     */
    private void reduce(Button callBox){
        callBox.setText("\n");
    }

    /**
     * Při posouvání aktivačním boxem levým tlačítkem myši se ukládají jeho nové souřadnice
     * @param e Pousouvání aktivačním boxem levým tlačítkem myši
     * @param box Aktivační box, který posouváme
     */
    private void onCallBoxDragged(MouseEvent e, ObjectWithLine box){
        if(e.getButton().equals(MouseButton.PRIMARY)) {
            box.getTimeLineButton().setLayoutY(box.getTimeLineButton().getLayoutY() + e.getY() + box.getTimeLineButton().getTranslateY());
        }
    }

    /**
     * Při posouvání obsahem zprávy levým tlačítkem myši se ukládají jeho nové souřadnice
     * @param e Pousouvání obsahu zprávy levým tlačítkem myši
     * @param box Obsah zprávy, který posouváme
     */
    private void onMessageDragged(MouseEvent e, Button box){
        if(e.getButton().equals(MouseButton.PRIMARY)) {
            box.setLayoutX(box.getLayoutX() + e.getX() + box.getTranslateX());
        }
    }

    /**
     * Při posouvání objektem levým tlačítkem myši se ukládají jeho nové souřadnice
     * @param e Pousouvání objektem levým tlačítkem myši
     * @param box Objekt, který posouváme
     */
    private void onBoxDragged(MouseEvent e, ObjectWithLine box) {
        if(e.getButton().equals(MouseButton.PRIMARY)) {
            box.setLayoutX(box.getLayoutX() + e.getX() + box.getClassButton().getTranslateX());
            box.setLayoutY(box.getLayoutY() + e.getY() + box.getClassButton().getTranslateY());
            box.setX(box.getLayoutX() + e.getX() + box.getTranslateX());
            box.setY(box.getLayoutY() + e.getY() + box.getTranslateY());
        }
    }

    /**
     * Po double-kliknutí na tlačítko se zprávou vyskočí okno, kde můžeme změnit text
     * @param e Kliknutí na zprávu
     * @param arrow Návratová šipka na kterou je daný text zprávy nafixován
     */
    public void onReturnMessageBoxPressed(MouseEvent e, Messages arrow){
        if(e.getButton().equals(MouseButton.PRIMARY)){
            if(e.getClickCount() == 2) {
                String msg = EditMessages.displayReturn(arrow);
                arrow.setReturnMessage(msg);
                arrow.setReturnProperty(arrow.getReturnMessage());
            }
        }
    }

    /**
     * Po double-kliknutí na tlačítko se zprávou vyskočí okno, kde můžeme změnit text
     * @param e Kliknutí na zprávu
     * @param arrow Asynchronní, nebo synchronní šipka, na kterou je daný text zprávy nafixován
     */
    public void onAsynOrSynMessageBoxPressed(MouseEvent e, Messages arrow){
        if(e.getButton().equals(MouseButton.PRIMARY)){
            if(e.getClickCount() == 2) {
                String msg = EditMessages.displayAsynchOrSynch(arrow);
                arrow.setAsOrSynMessage(msg);
                arrow.setAsOrSynProperty(arrow.getAsOrSynMessage());
                arrow.CheckArrowMessage();
            }
        }
    }

    /**
     * Po double-kliknutí na tlačítko se zprávou vyskočí okno, kde můžeme změnit text
     * @param e Kliknutí na zprávu
     * @param arrow Vytvářecí šipka, na kterou je daný text zprávy nafixován
     */
    public void onCreateMessageBoxPressed(MouseEvent e, Messages arrow){
        if(e.getButton().equals(MouseButton.PRIMARY)){
            if(e.getClickCount() == 2) {
                String msg = EditMessages.displayCreate(arrow);
                arrow.setCreateMessage(msg);
                arrow.setCreateProperty(arrow.getCreateMessage());
            }
        }
    }

    /**
     * Vytváření zprávy (šipky) mezi call-boxy
     * @param b1 Počáteční call-box
     * @param b2 Koncový call-box
     * @param x1 Počáteční X souřadnice
     * @param y1 Počáteční a Koncová Y souřadnice
     * @param x2 Koncová X souřadnice
     * @return Objekt typu Messages
     */
    public Messages createMessage(Button b1, Button b2, Double x1, Double y1, Double x2){
        Messages arrow = new Messages(x1, y1, x2);
        arrow.setFrom(b1.getId());
        arrow.setTo(b2.getId());
        System.out.println(arrow.getFrom());
        System.out.println(arrow.getTo());
        arrow.setOnMousePressed(e -> handleMouseMessage(e, arrow));
        arrow.getReturnButton().setOnMouseDragged(e -> onMessageDragged(e, arrow.getReturnButton()));
        arrow.getAsynAndSynClassButton().setOnMouseDragged(e -> onMessageDragged(e, arrow.getAsynAndSynClassButton()));
        arrow.getReturnButton().setOnMousePressed(e -> onReturnMessageBoxPressed(e, arrow));
        arrow.getAsynAndSynClassButton().setOnMousePressed(e -> onAsynOrSynMessageBoxPressed(e, arrow));

        ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().addAll(arrow);
        return arrow;
    }

    /**
     * Vytváření zprávy (šipky) mezi od časové osy do call-boxu
     * @param l1 Počáteční časová osa
     * @param b2 Koncový call-box
     * @param x1 Počáteční X souřadnice
     * @param y1 Počáteční a Koncová Y souřadnice
     * @param x2 Koncová X souřadnice
     * @return Objekt typu Messages
     */
    public Messages createMessageWithLineFirst(Line l1, Button b2, Double x1, Double y1, Double x2){
        Messages arrow = new Messages(x1, y1, x2);
        arrow.setFrom(l1.getId());
        arrow.setTo(b2.getId());
        arrow.setOnMousePressed(e -> handleMouseMessage(e, arrow));
        arrow.getReturnButton().setOnMouseDragged((e -> onMessageDragged(e, arrow.getReturnButton())));
        arrow.getAsynAndSynClassButton().setOnMouseDragged((e -> onMessageDragged(e, arrow.getAsynAndSynClassButton())));
        arrow.getReturnButton().setOnMousePressed(e -> onReturnMessageBoxPressed(e, arrow));
        arrow.getAsynAndSynClassButton().setOnMousePressed(e -> onAsynOrSynMessageBoxPressed(e, arrow));

        ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().addAll(arrow);
        //rootPane.getChildren().addAll(arrow);
        return arrow;
    }

    /**
     * Vytváření vytvářecí zprávy (šipky) od call-boxu na objekt
     * @param b1 Počáteční call-box
     * @param b2 Koncový objekt
     * @param x1 Počáteční X souřadnice
     * @param y1 Počáteční a Koncová Y souřadnice
     * @param x2 Koncová X souřadnice
     * @return Objekt typu Messages
     */
    public Messages CreateMessageToObject(Button b1, Button b2, Double x1, Double y1, Double x2){
        Messages arrow = new Messages(x1, y1, x2);
        arrow.setFrom(b1.getId());
        arrow.setTo(b2.getId());
        arrow.setArrowType("Create");
        arrow.update();
        arrow.setOnMousePressed(e -> handleMouseMessageDeleteOnly(e, arrow));
        arrow.getCreateObjectButton().setOnMouseDragged((e -> onMessageDragged(e, arrow.getCreateObjectButton())));
        arrow.getCreateObjectButton().setOnMousePressed(e -> onCreateMessageBoxPressed(e, arrow));

        ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().addAll(arrow);
        return arrow;
    }

    /**
     * Vytváření zprávy (šipky) od call-boxu na časovou osu
     * @param b1 Počáteční objekt
     * @param l2 Koncová časová osa
     * @param x1 Počáteční X souřadnice
     * @param y1 Počáteční a Koncová Y souřadnice
     * @param x2 Koncová X souřadnice
     * @return Objekt typu Messages
     */
    public Messages createMessageToLine(Button b1, Line l2, Double x1, Double y1, Double x2){
        Messages arrow = new Messages(x1, y1, x2);
        arrow.setFrom(b1.getId());
        arrow.setTo(l2.getId());
        arrow.setOnMousePressed(e -> handleMouseMessage(e, arrow));
        arrow.getReturnButton().setOnMouseDragged((e -> onMessageDragged(e, arrow.getReturnButton())));
        arrow.getAsynAndSynClassButton().setOnMouseDragged((e -> onMessageDragged(e, arrow.getAsynAndSynClassButton())));
        arrow.getReturnButton().setOnMousePressed(e -> onReturnMessageBoxPressed(e, arrow));
        arrow.getAsynAndSynClassButton().setOnMousePressed(e -> onAsynOrSynMessageBoxPressed(e, arrow));

        ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().addAll(arrow);
        return arrow;
    }

    /**
     * Odstraní vytvářecí zprávu - šipku i obsah zprávy po kliknutí pravým tlačítkem myši
     * U vytvářecí čáry nelze měnit typ zprávy
     * @param e Kliknutí na objekt z třídy Messages
     * @param arrow Odstraňovaná zpráva
     */
    public void handleMouseMessageDeleteOnly(MouseEvent e, Messages arrow){
        if(e.getButton().equals(MouseButton.SECONDARY)) {
            //Arrow.ListOfArrows.remove(arrow);
            ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().remove(arrow);
        }
    }

    /**
     * Odstraní zprávu - šipku i obsah zprávy po kliknutí pravým tlačítkem myši.
     * Nebo po double-kliku se otevře okno, ve kterém si můžeme vybrat jaký typ zprávy chceme mít
     * @param e Kliknutí na objekt z třídy Messages
     * @param arrow Odstraňovaná zpráva, nebo Modifikovaná zpráva
     */
    public void handleMouseMessage(MouseEvent e, Messages arrow){
        // Odstranění šipky
        if(e.getButton().equals(MouseButton.SECONDARY)) {
            //Arrow.ListOfArrows.remove(arrow);
            ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().remove(arrow);
        }
        // Změna šipky (typu zprávy)
        if(e.getButton().equals(MouseButton.PRIMARY)) {
            if (e.getClickCount() == 2) {
                arrowType = EditMessageArrows.display(arrow);
                arrow.setArrowType(arrowType);
                arrow.update();
                arrow.CheckArrowMessage();
            }
        }
    }

    /**
     * Po vybrání objektu a stisknutí tlačítka Delete se zavolá metoda delete()
     * @param e Stisknutá klávesa na klávesnici
     * @param box Vybraný objekt v grafické části
     */
    public void handleKeyboard(KeyEvent e, ObjectWithLine box){
        if (e.getCode() == KeyCode.DELETE) delete(box);
    }

    /**
     * Po zavolání této metody se předaný objekt smaže
     * @param box Mazaná třída v grafické části
     */
    public void delete(ObjectWithLine box){
        ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().remove(box);
    }

    /**
     * Po stisknutí tlačítka se zavolá metoda, která bude načítat ze souboru
     * @param event Stisknutí tlačítka Save
     * @throws IOException
     */
    public void SaveJson(ActionEvent event) throws IOException{
        serializeObject();
    }

    /**
     * Po stisknutí tlačítka se zavolá metoda, která bude ukládat do souboru
     * @param event Stisknutí tlačítka Load
     * @throws InterruptedException
     */
    public void LoadJson(ActionEvent event) throws InterruptedException{
        deserializeObject();
    }

    /**
     * TODO
     */
    public void deserializeObject(){

    }

    /**
     * TODO
     */
    public void serializeObject(){

    }

    /**
     * Po stisknutí tlačítka se otevře okno s nápovědou
     * @param event Stisknutí tlačítka
     */
    @FXML
    public void Help(ActionEvent event){
        sqShowHelp.display();
    }

    /**
     * Po stlačení tlačítka Clear metoda odstraní všechny komponenty z obrazovky
     * @param e Stisknutí tlačítka Clear
     */
    @FXML
    public void Clear(ActionEvent e){
        ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().clear();
        getObjectNames().clear();
        getContent().clear();
        count = 0;
    }
}