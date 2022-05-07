/**
 * @author Martin Baláž
 */
package com.uml;

import java.util.*;

/**
 * Třída je nadřazenou sekvenčnímu diagramu a diagramu tříd. Je odvozena od třídy Element (má název).
 * Obsahuje seznam tříd (instance třídy UMLClass), příp. klasifikátorů pro uživatelsky nedefinované typy (instance třídy UMLClassifier).
 * Obsahuje i dva seznamy názvů tříd pro jiné učely
 */
public class Diagram extends Element {

    // Atributy
    protected List<String> listOfNames = new LinkedList<>();
    protected List<UMLClassifier> listOfClassif = new LinkedList<>();
    protected List<String> listOfClassNames = new LinkedList<>();

    // GETry
    public List<String> getListOfClassNames() {
        return listOfClassNames;
    }
    public List<String> getListOfNames() {
        return listOfNames;
    }
    public List<UMLClassifier> getListOfClassif() {
        return listOfClassif;
    }

    /**
     * Konstruktor pro vytvoření instance diagramu. Každý diagram má svůj název.
     *
     * @param name Název diagramu.
     */
    public Diagram(String name) {
        super(name);
    }

    /**
     * Vyhledá v diagramu klasifikátor podle názvu. Pokud neexistuje, vytvoří instanci třídy Classifier reprezentující klasifikátor,
     * který není v diagramu zachycen (viz UMLClassifier.forName(java.lang.String)); využito např. pro modelování typu proměnné, který v diagramu není.
     * Tato instance je zařazena do struktur diagramu, tzn. že při dalším pokusu o vyhledání se použije tato již vytvořená instance.
     *
     * @param name Název klasifikátoru.
     * @return Nalezený, příp. vytvořený, klasifikátor.
     */
    public UMLClassifier classifierForName(String name) {
        int index;
        if ((index = listOfNames.indexOf(name)) != -1) {
            return listOfClassif.get(index);
        } else {
            UMLClassifier objOfClassif = UMLClassifier.forName(name);
            listOfClassif.add(objOfClassif);
            listOfNames.add(name);
            return objOfClassif;
        }
    }

    /**
     * Vyhledá v diagramu klasifikátor podle názvu.
     *
     * @param name Název klasifikátoru.
     * @return Nalezený klasifikátor. Pokud v diagramu neexistuje klasifikátor daného jména, vrací null.
     */
    public UMLClassifier findClassifier(String name) {
        int index;
        if ((index = listOfNames.indexOf(name)) != -1) {
            return listOfClassif.get(index);
        } else {
            return null;
        }
    }

    /**
     * Vyhledá v diagramu třídu podle názvu a pokud neexistuje, přidá jí.
     * Můžeme tak v sekvenčním diagramu kontrolovat, zda existuje třída, z který je vytvořen objekt
     *
     * @param name Název třídy.
     * @return True, pokud byl název třídy přidán do listu a False pokud ne (třeba již jej obsahuje)
     */
    public boolean addName(String name) {
        if (!listOfClassNames.contains(name)) listOfClassNames.add(name);
        else return false;
        return true;
    }

    /**
     * Vyhledá v diagramu třídu podle starého názvu a přejmenuje jej.
     * Můžeme tak v sekvenčním diagramu kontrolovat, zda existuje třída, z který je vytvořen objekt i po přejmenování
     *
     * @param oldName Starý název třídy.
     * @param newName Nový název třídy
     */
    public void renameName(String oldName, String newName) {
        if (listOfClassNames.contains(oldName)) {
            listOfClassNames.set(listOfClassNames.indexOf(oldName), newName);
        }
    }

    /**
     * Vyhledá v diagramu třídu podle názvu a odstraní jej.
     * Můžeme tak v sekvenčním diagramu kontrolovat, zda existuje třída, z který je vytvořen objekt i po odstranění
     *
     * @param name Název odstraňované třídy
     */
    public void deleteName(String name) {
        listOfClassNames.remove(name);
    }
}