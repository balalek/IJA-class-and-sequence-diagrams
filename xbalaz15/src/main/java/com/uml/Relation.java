/**
 * @author Martin Baláž
 */
package com.uml;

public class Relation extends Element{

    // Atributy
    private UMLClass from;
    private UMLClass to;

    /**
     * Konstruktor pro vytvoření vztahů mezi třídami v diagramu tříd
     * @param name Název (Typ) vztahu
     * @param from Od koho se vztah odvýjí
     * @param to Ke komu se vztah vztahuje
     */
    public Relation(String name, UMLClass from, UMLClass to){
        super(name);
        this.from = from;
        this.to = to;
    }

    /**
     * Šipka agregace
     */
    public void Aggregation(){
        System.out.print("Agregace");
    }

    /**
     * Šipka asociace
     */
    public void Association(){
        System.out.print("Association");
    }

    /**
     * šipka kompozice
     */
    public void Composition(){
        System.out.print("Composition");
    }

    /**
     * šipka generalizace
     */
    public void Generalization(){
        System.out.print("Generalization");
    }

}