/**
 * @author Martin Baláž
 */
package com.component;

import com.google.gson.annotations.Expose;

/**
 * Třída, která byla vytvořena speciálně pro účely načítání šipek ze souborů
 */
public class HelpLoadArrow {

    // Atributy
    @Expose
    public String arrowType = "";
    @Expose
    public String from = "";
    @Expose
    public String to = "";

    // GETry a SETry
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getArrowType() {
        return arrowType;
    }
    public void setArrowType(String arrowType) {
        this.arrowType = arrowType;
    }

    /**
     * Konstruktor pro vytvoření vztahu mezi třídami pro načítání ze souboru
     * @param from Odkaz na třídu od kud povede šipka
     * @param to Odkaz na třídu kam povede šipka
     * @param arrowType Typ vztahu mezi třídami
     */
    public HelpLoadArrow(String from, String to, String arrowType){
        this.from = from;
        this.to = to;
        this.arrowType = arrowType;
    }
}