package org.arijit.stock.analyze.enums;

public enum ValuationEnums {
    UNDER_VALUED(1),
    FAIR_VALUED(2),
    OVER_VALUED(3),
    AVOID(-1);

    private int code;
    private ValuationEnums(int n){
        this.code = n;
    }

    public int getCode() {
        return code;
    }
}
