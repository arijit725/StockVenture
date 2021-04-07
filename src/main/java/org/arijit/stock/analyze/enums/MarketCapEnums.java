package org.arijit.stock.analyze.enums;

public enum MarketCapEnums {

    LARGECAP,
    SMALLCAP,
    MIDCAP;

    public static MarketCapEnums getCapEnum(long marketCap){
        // based on market cap return value
        return LARGECAP;
    }
}
