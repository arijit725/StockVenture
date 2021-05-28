package org.arijit.stock.analyze.analysisdto;

import org.arijit.stock.analyze.enums.AnalysisEnums;
import org.arijit.stock.analyze.util.DateUtil;
import org.arijit.stock.analyze.util.StockUtil;

import java.text.ParseException;
import java.util.*;

public class ProfitAndLossAnalysisInfo {

    private double netSalesGrowthPercentage;
    private boolean isSalesGrowthContinuous;
    private double rawMaterialGrowthPercentage;
    private double PBITGrowthPercentage;
    private double interestDecreasePercentage;
    private double netProfitGrowthPercentage;
    private String profiAndLossScore;
    private String netSalesScore;
    private String netProfitScore;
    private String interestScore;

    private Map<String,String> netProfitVsSalesRatio;

    private Map<String, HashMap<String,String>> growthsDtoMap;
    private List<String> analysisStatement;

    public ProfitAndLossAnalysisInfo(){
        this.netProfitVsSalesRatio = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                try {
                    long date1 = DateUtil.convertToEpochMilli(o1);
                    long date2 = DateUtil.convertToEpochMilli(o2);
                    return Long.compare(date2,date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        growthsDtoMap = new HashMap<>();
        analysisStatement = new ArrayList<>();
    }

    public Map<String, String> getNetProfitVsSalesRatio() {
        return netProfitVsSalesRatio;
    }

    public double getNetSalesGrowthPercentage() {
        return netSalesGrowthPercentage;
    }

    public void setNetSalesGrowthPercentage(double netSalesGrowthPercentage) {
        this.netSalesGrowthPercentage = netSalesGrowthPercentage;
    }

    public boolean isSalesGrowthContinuous() {
        return isSalesGrowthContinuous;
    }

    public void setSalesGrowthContinuous(boolean salesGrowthContinuous) {
        isSalesGrowthContinuous = salesGrowthContinuous;
    }

    public double getRawMaterialGrowthPercentage() {
        return rawMaterialGrowthPercentage;
    }

    public void setRawMaterialGrowthPercentage(double rawMaterialGrowthPercentage) {
        this.rawMaterialGrowthPercentage = rawMaterialGrowthPercentage;
    }

    public double getPBITGrowthPercentage() {
        return PBITGrowthPercentage;
    }

    public void setPBITGrowthPercentage(double PBITGrowthPercentage) {
        this.PBITGrowthPercentage = PBITGrowthPercentage;
    }

    public double getInterestDecreasePercentage() {
        return interestDecreasePercentage;
    }

    public void setInterestDecreasePercentage(double interestDecreasePercentage) {
        this.interestDecreasePercentage = interestDecreasePercentage;
    }

    public double getNetProfitGrowthPercentage() {
        return netProfitGrowthPercentage;
    }

    public void setNetProfitGrowthPercentage(double netProfitGrowthPercentage) {
        this.netProfitGrowthPercentage = netProfitGrowthPercentage;
    }

    public String getProfiAndLossScore() {
        return profiAndLossScore;
    }

    public void setProfiAndLossScore(String profiAndLossScore) {
        this.profiAndLossScore = profiAndLossScore;
    }

    public void setInterestScore(String interestScore) {
        this.interestScore = interestScore;
    }

    public void setNetProfitScore(String netProfitScore) {
        this.netProfitScore = netProfitScore;
    }

    public void setNetSalesScore(String netSalesScore) {
        this.netSalesScore = netSalesScore;
    }

    public String getInterestScore() {
        return interestScore;
    }

    public String getNetProfitScore() {
        return netProfitScore;
    }

    public String getNetSalesScore() {
        return netSalesScore;
    }

    public void addGrowths(String date, String attribute, String value){
        if(!growthsDtoMap.containsKey(date)){
            growthsDtoMap.put(date,new HashMap<>());
        }
        growthsDtoMap.get(date).put(attribute,value);
    }

    public void clearAnalysisStatement(){
        this.analysisStatement.clear();
    }

    public void addAnalysisStatement(String statement, AnalysisEnums analysisEnums){
        String stmt = StockUtil.createAnalysisStatement(statement,analysisEnums);
        this.analysisStatement.add(stmt);
    }

    @Override
    public String toString() {
        return "ProfitAndLossAnalysisInfo{" +
                "netSalesGrowthPercentage=" + netSalesGrowthPercentage +
                ", isSalesGrowthContinuous=" + isSalesGrowthContinuous +
                ", rawMaterialGrowthPercentage=" + rawMaterialGrowthPercentage +
                ", PBITGrowthPercentage=" + PBITGrowthPercentage +
                ", interestDecreasePercentage=" + interestDecreasePercentage +
                ", netProfitGrowthPercentage=" + netProfitGrowthPercentage +
                ", profiAndLossScore='" + profiAndLossScore + '\'' +
                ", netSalesScore='" + netSalesScore + '\'' +
                ", netProfitScore='" + netProfitScore + '\'' +
                ", interestScore='" + interestScore + '\'' +
                ", netProfitVsSalesRatio=" + netProfitVsSalesRatio +
                ", growthsDtoMap=" + growthsDtoMap +
                '}';
    }
}
