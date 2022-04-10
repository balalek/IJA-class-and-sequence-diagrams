package com.uml;

/**
 * Třída reprezentuje diagram tříd. Je odvozen od třídy Element (má název). Obsahuje seznam tříd (instance třídy UMLClass) 
 * příp. klasifikátorů pro uživatelsky nedefinované typy (instance třídy UMLClassifier).
 */
public class ClassDiagram extends Diagram{
    
    /**
     * Konstruktor pro vytvoření instance diagramu. Každý diagram má svůj název.
     * @param name Název diagramu.
     */
    public ClassDiagram(String name){
       super(name);
    }

    /**
     * Vytvoří instanci UML třídy a vloží ji do diagramu. Pokud v diagramu již existuje třída stejného názvu, nedělá nic.
     * @param name Název vytvářené třídy.
     * @return Objekt (instance) reprezentující třídu. Pokud třída s daným názvem již existuje, vrací null.
     */
    public UMLClass createClass(String name){
        if(!listOfNames.contains(name)) {
            listOfNames.add(name);
            UMLClass objOfClass = new UMLClass(name);
            listOfClassif.add(objOfClass);
            return objOfClass;
        }
        return null;
    }

    /**
     * Odstraní instanci UML třídy z diagramu. Pokud taková třída neexistuje, nedělá nic.
     * @param name Název odstraňované třídy.
     * @param object Objekt odstraňované třídy.
     * @return void.
     */
    public UMLClass removeClass(String name, UMLClass object){
        if(listOfNames.contains(name) && listOfClassif.contains(object)) {
            // Opravdu to chceš udělat? TODO
            listOfNames.remove(name);
            listOfClassif.remove(object);
            return null;
        }
        return object;
    }
}