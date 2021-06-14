package org.arijit.stock.analyze.analysisdto;

import org.arijit.stock.analyze.enums.AnalysisEnums;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuarterlyReportAnalysisInfo {
    private double estimatedEPSCAGR;
    private double ttmEPS;
    private double avgEPSGrowth; //eps growth for last4 quarter;
    private Map<String, HashMap<String,String>> quarterlyReportGrowthsDtoMap;


    private List<String> analysisStatement;
    private String estimatedEPSCAGRStr;
    private String ttmEPSStr;

    public QuarterlyReportAnalysisInfo(){
        quarterlyReportGrowthsDtoMap = new HashMap<>();
        analysisStatement = new ArrayList<>();
    }

    public double getAvgEPSGrowth() {
        return avgEPSGrowth;
    }

    public void setAvgEPSGrowth(double avgEPSGrowth) {
        this.avgEPSGrowth = avgEPSGrowth;
    }

    public void setEstimatedEPSCAGR(double estimatedEPSCAGR) {
        this.estimatedEPSCAGR = estimatedEPSCAGR;
        this.estimatedEPSCAGRStr = StockUtil.convertDoubleToPrecision(estimatedEPSCAGR,2);
    }

    public void setTtmEPS(double ttmEPS) {
        this.ttmEPS = ttmEPS;
        this.ttmEPSStr = StockUtil.convertDoubleToPrecision(ttmEPS,2);
    }

    public double getTtmEPS() {
        return ttmEPS;
    }

    public double getEstimatedEPSCAGR() {
        return estimatedEPSCAGR;
    }


    public void addQuarterlyReportGrowths(String date, String attribute, String value){
        if(!quarterlyReportGrowthsDtoMap.containsKey(date)){
            quarterlyReportGrowthsDtoMap.put(date,new HashMap<>());
        }
        quarterlyReportGrowthsDtoMap.get(date).put(attribute,value);
    }

    public void clear(){
        this.analysisStatement.clear();
    }

    public void addAnalysisStatement(String statement, AnalysisEnums analysisEnums){
        String stmt = StockUtil.createAnalysisStatement(statement,analysisEnums);
        this.analysisStatement.add(stmt);
    }

    @Override
    public String toString() {
        return "QuarterlyReportAnalysisInfo [" +
                "estimatedEPS=" + estimatedEPSCAGR +
                ']';
    }
}
