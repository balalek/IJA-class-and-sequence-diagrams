package com.uml;
import java.util.*;

/**
 * Třída (její instance) reprezentuje model třídy z jazyka UML. Rozšiřuje třídu UMLClassifier. Obsahuje seznam atributů a operací (metod). Třída může být abstraktní.
 */
public class UMLClass extends UMLClassifier{

    private boolean abstractClass;
    private List<UMLAttribute> listOfAttr = new LinkedList<>();

    /**
     * Vytvoří instanci reprezentující model třídy z jazyka UML. Třída není abstraktní.
     * @param name Název třídy (klasifikátoru).
     */ 
    public UMLClass(String name){
        super(name);
        abstractClass = false;
    }

    /**
     * Test, zda objekt reprezentuje model abstraktní třídy.
     * @return Pokud je třída abstraktní, vrací true. Jinak vrací false.
     */
    public boolean isAbstract(){
        return abstractClass;
    }

    /**
     * Změní informaci objektu, zda reprezentuje abstraktní třídu.
     * @param isAbstract Zda se jedná o abstraktní třídu nebo ne.
     */
    public void setAbstract(boolean isAbstract){
        abstractClass = isAbstract;
    }

    /**
     * Vloží atribut do modelu UML třídy. Atribut se vloží na konec seznamu (poslední položka). Pokud již třída obsahuje atribut stejného jména, nedělá nic.
     * @param attr Vkládaný atribut.
     * @return Úspěch akce (pokud se podařilo vložit, vrací true, jinak false).
     */ 
    public boolean addAttribute(UMLAttribute attr){
        if(!listOfAttr.contains(attr)) listOfAttr.add(attr);
        else return false;
        return true;
    }

    /**
     * Odstrani polozky ze seznamu
     */
    public void removeAttributes(){
        listOfAttr.clear();
    }

    /**
     * Vrací pozici atributu v seznamu atributů. Pozice se indexuje od hodnoty 0. Pokud třída daný atribut neobsahuje, vrací -1.
     * @param attr Hledaný atribut
     * @return Pozice atributu.
     */
    public int getAttrPosition(UMLAttribute attr){
        return listOfAttr.indexOf(attr);
    }

    /**
     * Přesune pozici atributu na nově zadanou. Pozice se indexuje od hodnoty 0. Pokud třída daný atribut neobsahuje, nic neprovádí a vrací -1.
     * Při přesunu na pozici pos se všechny stávající položky (atributy) od pozice pos (včetně) posunou o jednu pozici doprava.
     * @param attr Přesunovaný atribut
     * @param pos Nová pozice
     * @return Úspech operace.
     */
    public int moveAttrAtPosition(UMLAttribute attr, int pos){
        int index;
        if(!listOfAttr.contains(attr)) return -1;
        index = listOfAttr.indexOf(attr);
        // odstrani se ze seznamu presouvany prvek a vse se shiftne doleva
        listOfAttr.remove(index);
        // prida se prvek predany parametry na dane misto a vse se shiftne doprava
        listOfAttr.add(pos, attr);
        return 0;
    }

    /**
     * Vrací nemodifikovatelný seznam atributů. Lze využít pro zobrazení atributů třídy.
     * @return Nemodifikovatelný seznam atributů.
     */
    public List<UMLAttribute> getAttributes(){
        return Collections.unmodifiableList(listOfAttr);
    }
}