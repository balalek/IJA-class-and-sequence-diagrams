package com.uml;

import java.util.*;


/**
 * Třída reprezentuje operaci, která má své jméno, návratový typ a seznam argumentů.
 * Je odvozena (rozšiřuje) od třídy UMLAttribute, od které přejímá název a návratový typ
 * Argument je reprezentován třídou UMLAttribute. Lze použít jako součást UML klasifikátoru třída nebo rozhraní.
 */
public class UMLOperation extends UMLAttribute{

    private static List<UMLAttribute> listOfArg = new LinkedList<>();

    /**
     * Konstruktor pro vytvoření operace s daným názvem a návratovým typem.
     * @param name Název operace.
     * @param type Návratový typ operace.
     */
    public UMLOperation(String name, UMLClassifier type){
        super(name, type);
    }

    /**
     * Konstruktor pro vytvoření konstruktoru s názvem třídy.
     * @param name Název operace.
     */
    public UMLOperation(String name){
        super(name);
    }

    /**
     * Tovární metoda pro vytvoření instance operace.
     * @param name Název operace.
     * @param type Návratový typ operace.
     * @param list Seznam argumentů operace.
     * @return Objekt reprezentující operaci v diagramu UML.
     */
    public static UMLOperation create(String name, UMLClassifier type, List<UMLAttribute> list){
        UMLOperation objOfOper = new UMLOperation(name, type);
        /*for(UMLAttribute arg : args){
            listOfArg.add(arg);   
        }*/
        listOfArg.addAll(list);
        return objOfOper;
    }

    /**
     * Tovární metoda pro vytvoření instance konstruktoru.
     * @param name Název operace.
     * @param list Seznam argumentů operace.
     * @return Objekt reprezentující operaci v diagramu UML.
     */
    public static UMLOperation create(String name, List<UMLAttribute> list){
        UMLOperation objOfOper = new UMLOperation(name);
        /*for(UMLAttribute arg : args){
            listOfArg.add(arg);
        }*/
        listOfArg.addAll(list);
        return objOfOper;
    }

    /**
     * Přidá nový argument do seznamu argumentů. Argument se vloží na konec seznamu. Pokud v seznamu již existuje argument stejného názvu, operaci neprovede.
     * @param arg Vkládaný argument.
     * @return Úspěch operace - true, pokud se podařilo vložit, jinak false.
     */
    public boolean addArgument(UMLAttribute arg){
        if(!listOfArg.contains(arg)) listOfArg.add(arg);
        else return false;
        return true;
    }

    /**
     * Vrací nemodifikovatelný seznam argumentů. Lze využít pro zobrazení.
     * @return Nemodifikovatelný seznam argumentů.
     */
    public List<UMLAttribute> getArguments(){
        return Collections.unmodifiableList(listOfArg);
    }

}