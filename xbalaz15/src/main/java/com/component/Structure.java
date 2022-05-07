/**
 * @author Martin Baláž
 */
package com.component;

import com.uml.UMLClass;

/**
 * Třída slouží jako struktura, takže lze z metody vracet dva objekty ve struktuře, a dále s nimi pracovat odděleně
 */
public final class Structure {

    // Atributy
    private final ClassComponent box;
    private final UMLClass cls;

    // GETry a SETry
    public ClassComponent getBox() {
        return box;
    }
    public UMLClass getCls() {
        return cls;
    }

    /**
     * Konstruktor pro vytvoření struktury dvou objektů.
     * @param box Objekt GUI třídy
     * @param cls Objekt UML třídy
     */
    public Structure(ClassComponent box, UMLClass cls) {
        this.box = box;
        this.cls = cls;
    }

}

