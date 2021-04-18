package org.arijit.stock.analyze.analysisdto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.enums.ValuationEnums;
import org.arijit.stock.analyze.util.DateUtil;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatioAnalysisInfo {

    private static final Logger logger = LogManager.getLogger(RatioAnalysisInfo.class);

    private double forwardPERatio;
    private ValuationEnums forwardPEValuation;
    private boolean possibilityOfMultiBagger;

    private boolean isROEIncreasingContinuously;
    private boolean isPBDecreasingConinuously;

    private Map<String, HashMap<String,String>> ratioGrowthsDtoMap;

    public RatioAnalysisInfo(){
        ratioGrowthsDtoMap = new HashMap<>();
    }
    public void setForwardPERatio(double forwardPERatio) {
        this.forwardPERatio = forwardPERatio;
    }

    public void setForwardPEValuation(ValuationEnums forwardPEValuation) {
        this.forwardPEValuation = forwardPEValuation;
    }


    public void addRatioGrowths(String date, String ratioAttribute, String value){
        if(!ratioGrowthsDtoMap.containsKey(date)){
            ratioGrowthsDtoMap.put(date,new HashMap<>());
        }
        ratioGrowthsDtoMap.get(date).put(ratioAttribute,value);
    }

    public Map<String, HashMap<String,String>> getRatioGrowthsDtoMap() {
        return ratioGrowthsDtoMap;
    }

    public double getForwardPERatio() {
        return forwardPERatio;
    }

    public ValuationEnums getForwardPEValuation() {
        return forwardPEValuation;
    }

    public void setPossibilityOfMultiBagger(boolean possibilityOfMultiBagger) {
        this.possibilityOfMultiBagger = possibilityOfMultiBagger;
    }

    public boolean isPossibilityOfMultiBagger() {
        return possibilityOfMultiBagger;
    }

    @Override
    public String toString() {
        return "RatioAnalysisInfo{" +
                "forwardPERatio=" + forwardPERatio +
                ", forwardPEValuation=" + forwardPEValuation +
                '}';
    }
}
