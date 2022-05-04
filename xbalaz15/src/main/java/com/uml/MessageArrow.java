package com.uml;

/**
 * Třída reprezentuje komunikaci (šipku) v sekvenčním diagramu. Má své jméno, typ, zprávu, odesílatele a příjemce.
 * Je odvozena (rozšiřuje) od třídy Element. Příjemce a odesílatel je reprezentován třidou UMLClass. Typ komunikace
 * je definován jako enum.
 * Lze použít jako atribut UML třídy nebo argument operace.
 */

public class MessageArrow extends Element{

    private arrType arrowType;
    private String message;
    private UMLClass from;
    private UMLClass to;

    /**
     * Konstruktor pro vytvoření zprávy mezi objekty v sekvenčním diagramu
     * @param name Název zprávy
     * @param arrowType Typ zprávy
     * @param message Obsah zprávy
     * @param from Kdo zprávu poslal
     * @param to Komu zprávu poslal
     */

    public MessageArrow(String name, arrType arrowType, UMLClass from, UMLClass to, String message)
    {
        super(name);
        this.from = from;
        this.to = to;
        this.message = message;
        this.arrowType = arrowType;
    }



}
