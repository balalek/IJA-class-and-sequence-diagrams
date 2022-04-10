package com.uml;

import java.util.*;

public class Diagram extends Element{
    protected List<String> listOfNames = new LinkedList<>();
    protected List<UMLClassifier> listOfClassif = new LinkedList<>();
    
    /**
     * Konstruktor pro vytvoření instance diagramu. Každý diagram má svůj název.
     * @param name Název diagramu.
     */
    public Diagram(String name){
       super(name);
    }

    /**
     * Vyhledá v diagramu klasifikátor podle názvu. Pokud neexistuje, vytvoří instanci třídy Classifier reprezentující klasifikátor,
     * který není v diagramu zachycen (viz UMLClassifier.forName(java.lang.String)); využito např. pro modelování typu proměnné, který v diagramu není.
     * Tato instance je zařazena do struktur diagramu, tzn. že při dalším pokusu o vyhledání se použije tato již vytvořená instance.
     * @param name Název klasifikátoru.
     * @return Nalezený, příp. vytvořený, klasifikátor.
     */
    public UMLClassifier classifierForName(String name){
        int index; 
        if((index = listOfNames.indexOf(name)) != -1){
            return listOfClassif.get(index);
        }
        else{
            UMLClassifier objOfClassif = UMLClassifier.forName(name);
            listOfClassif.add(objOfClassif);
            listOfNames.add(name);
            return objOfClassif;
        }
    }

    /**
     * Vyhledá v diagramu klasifikátor podle názvu.
     * @param name Název klasifikátoru.
     * @return Nalezený klasifikátor. Pokud v diagramu neexistuje klasifikátor daného jména, vrací null.
     */
    public UMLClassifier findClassifier(String name){
        int index; 
        if((index = listOfNames.indexOf(name)) != -1){
            return listOfClassif.get(index);
        }
        else{
            return null;
        }
    }
}
