package org.arijit.stock.analyze.analysisdto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.enums.AnalysisEnums;
import org.arijit.stock.analyze.enums.ValuationEnums;
import org.arijit.stock.analyze.util.DateUtil;
import org.arijit.stock.analyze.util.StockUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatioAnalysisInfo {

    private static final Logger logger = LogManager.getLogger(RatioAnalysisInfo.class);

    private ForwardPEAnalysis forwardPEAnalysis;
    private PEGRatioAnalysis pegRatioAnalysis;
    private boolean possibilityOfMultiBagger;


    private String ttmPEAnalysis;

    private List<String> analysisStatement;

    private Map<String, HashMap<String,String>> ratioGrowthsDtoMap;


    public RatioAnalysisInfo(){
        ratioGrowthsDtoMap = new HashMap<>();
        forwardPEAnalysis = new ForwardPEAnalysis();
        pegRatioAnalysis = new PEGRatioAnalysis();
        analysisStatement = new ArrayList<>();
    }

    public void setPEGRatio(String pegRatio){
        this.pegRatioAnalysis.setPegRatio(pegRatio);
    }

    public void setPEGValuation(ValuationEnums pegValuation){
        this.pegRatioAnalysis.setValuation(pegValuation);
    }

    public PEGRatioAnalysis getPegRatioAnalysis() {
        return pegRatioAnalysis;
    }

    public void setForwardPERatio(String forwardPERatio) {
        forwardPEAnalysis.setForwardPE(forwardPERatio);
    }

    public void setCurrentTTMPERatio(String ttmpeRatio){
        forwardPEAnalysis.setCurrentPE(ttmpeRatio);
    }

    public void setYearlyForwardPERatio(String yearlyForwardPERatio){
        forwardPEAnalysis.setYearlyForwardPE(yearlyForwardPERatio);
    }

    public void setQuarterlyForwardPERatio(String quarterlyForwardPERatio){
        forwardPEAnalysis.setQuarterlyForwardPE(quarterlyForwardPERatio);
    }

    public void setForwardPEValuation(ValuationEnums valuation){
        forwardPEAnalysis.setValuation(valuation);
    }


    public ForwardPEAnalysis getForwardPEAnalysis() {
        return forwardPEAnalysis;
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

    public void setPossibilityOfMultiBagger(boolean possibilityOfMultiBagger) {
        this.possibilityOfMultiBagger = possibilityOfMultiBagger;
    }

    public void clear(){
        this.analysisStatement.clear();
    }

    public void addAnalysisStatement(String statement, AnalysisEnums analysisEnums){
        String stmt = StockUtil.createAnalysisStatement(statement,analysisEnums);
        this.analysisStatement.add(stmt);
    }

    public boolean isPossibilityOfMultiBagger() {
        return possibilityOfMultiBagger;
    }

    public void setTtmPEAnalysis(String ttmPEAnalysis) {
        this.ttmPEAnalysis = ttmPEAnalysis;
    }


    @Override
    public String toString() {
        return "RatioAnalysisInfo{" +
                "forwardPEAnalysis=" + forwardPEAnalysis +
                ", pegRatioAnalysis=" + pegRatioAnalysis +
                ", possibilityOfMultiBagger=" + possibilityOfMultiBagger +
                ", ttmPEAnalysis='" + ttmPEAnalysis + '\'' +
                ", ratioGrowthsDtoMap=" + ratioGrowthsDtoMap +
                '}';
    }

    public static class PEGRatioAnalysis{
        private String pegRatio;
        private ValuationEnums valuation;

        public void setValuation(ValuationEnums valuation) {
            this.valuation = valuation;
        }

        public void setPegRatio(String pegRatio) {
            this.pegRatio = pegRatio;
        }

        public String getPegRatio() {
            return pegRatio;
        }

        public ValuationEnums getValuation() {
            return valuation;
        }

        @Override
        public String toString() {
            return "PEGRatioAnalysis{" +
                    "pegRatio='" + pegRatio + '\'' +
                    ", valuation=" + valuation +
                    '}';
        }
    }
    public static class ForwardPEAnalysis{
        private String currentPE;
        private String forwardPE;
        private String yearlyForwardPE;
        private String quarterlyForwardPE;
        private ValuationEnums valuation;

        public void setCurrentPE(String currentPE) {
            this.currentPE = currentPE;
        }

        public void setForwardPE(String forwardPE) {
            this.forwardPE = forwardPE;
        }

        public void setYearlyForwardPE(String yearlyForwardPE) {
            this.yearlyForwardPE = yearlyForwardPE;
        }

        public void setQuarterlyForwardPE(String quarterlyForwardPE) {
            this.quarterlyForwardPE = quarterlyForwardPE;
        }

        public void setValuation(ValuationEnums valuation) {
            this.valuation = valuation;
        }

        public double getForwardPE(){
            return Double.parseDouble(forwardPE);
        }
        @Override
        public String toString() {
            return "ForwardPEAnalysis{" +
                    "currentPE=" + currentPE +
                    ", forwardPE=" + forwardPE +
                    ", yearlyForwardPE=" + yearlyForwardPE +
                    ", quarterlyForwardPE=" + quarterlyForwardPE +
                    ", valuation=" + valuation +
                    '}';
        }
    }
}
