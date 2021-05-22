package org.arijit.stock.analyze.enums;

public enum  ColorEnums {

    GREEN("green"),
    BLUE("blue"),
    RED("red");

    private String color;

    private ColorEnums(String color){
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
