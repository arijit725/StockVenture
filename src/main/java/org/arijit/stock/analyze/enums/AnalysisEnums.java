package org.arijit.stock.analyze.enums;

public enum  AnalysisEnums {
    ANALYZED_GOOD("analyzedgood"),
    ANALYZED_BAD("analyzedbad"),
    ANALYZED_NEUTRAL("analyzedneutral");


    private String value;

    private AnalysisEnums(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
