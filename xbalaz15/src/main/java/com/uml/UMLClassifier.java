/**
 * @author Martin Baláž
 */
package com.uml;

/**
 * Třída reprezentuje klasifikátor v diagramu. Odvozené třídy reprezentují konkrétní podoby klasifikátoru (třída, rozhraní, atribut, apod.)
 */
public class UMLClassifier extends Element{

    // Atribut
    private boolean isUserDefined = true;

    /**
     * Vytvoří instanci třídy Classifier.
     * @param name Název klasifikátoru.
     * @param isUserDefined Uživatelsky definován (součástí diagramu).
     */
    public UMLClassifier(String name, boolean isUserDefined){
        super(name);
        this.isUserDefined = isUserDefined;
    }

    /**
     * Vytvoří instanci třídy Classifier. Instance je uživatelsky definována (je součástí diagramu).
     * @param name Název klasifikátoru.
     */
    public UMLClassifier(String name){
        super(name);
    }

    /**
     * Tovární metoda pro vytvoření instance třídy Classifier pro zadané jméno. Instance reprezentuje klasifikátor, který není v diagramu modelován.
     * @param name Název klasifikátoru.
     * @return Vytvořený klasifikátor.
     */
    public static UMLClassifier forName(String name){
        UMLClassifier objOfClassif = new UMLClassifier(name, false);
        return objOfClassif;
    }

    /**
     * Zjišťuje, zda objekt reprezentuje klasifikátor, který je modelován uživatelem v diagramu nebo ne.
     * @return Pokud je klasifikátor uživatelsky definován (je přímo součástí diagramu), vrací true. Jinak false.
     */
    public boolean isUserDefined(){
        return this.isUserDefined;
    }

    /**
     * Vrací řetězec reprezentující klasifikátor v podobě "nazev(userDefined)", kde userDefined je true nebo false.
     * @return Řetězec reprezentující klasifikátor.
     */
    public String toString(){
        return getName() + "(" + isUserDefined() + ")";
    }
}