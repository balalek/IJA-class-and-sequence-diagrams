package com.component;

import com.google.gson.annotations.Expose;

public class HelpLoadArrow {

    @Expose
    public String arrowType = "";
    @Expose
    public String from = "";
    @Expose
    public String to = "";

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

    public HelpLoadArrow(String from, String to, String arrowType){
        this.from = from;
        this.to = to;
        this.arrowType = arrowType;
    }
}
