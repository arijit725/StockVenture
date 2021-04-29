package org.arijit.stock.analyze.analysisdto;

import java.util.HashMap;

public class EVEBITDAValuationModelDto {

    HashMap<String, String> priceMap;

    public EVEBITDAValuationModelDto(){
        priceMap = new HashMap<>();

    }
    public void setEntryPrice(String entryPrice) {
        this.priceMap.put("entryPrice",entryPrice);
    }

    public void setTargetPrice(String targetPrice) {
        this.priceMap.put("targetPrice",targetPrice);
    }

    public String getEntryPrice() {
        return this.priceMap.get("entryPrice");
    }

    public String getTargetPrice() {
        return this.priceMap.get("targetPrice");
    }
}
