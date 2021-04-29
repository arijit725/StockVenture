package org.arijit.stock.analyze.analysisdto;

import java.util.HashMap;
import java.util.Map;

public class TargetPriceEstimationDto {

    private Map<String, Object> targetPriceMap;

    public TargetPriceEstimationDto(){

        targetPriceMap = new HashMap<>();
    }


    public void setEvebitdaValuationModelDto(EVEBITDAValuationModelDto evebitdaValuationModelDto) {
        targetPriceMap.put("evebitda",evebitdaValuationModelDto);
    }

    @Override
    public String toString() {
        return "TargetPriceEstimationDto{" +
                "targetPriceMap=" + targetPriceMap +
                '}';
    }
}
