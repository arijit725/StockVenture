package org.arijit.stock.analyze.analysisdto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.HashMap;
import java.util.Map;

public class CashFlowAnalysisInfo {

    private static final Logger logger = LogManager.getLogger(CashFlowAnalysisInfo.class);

    private double freeCashFlow;
    private String freeCashFlowStr;

    private Map<String, HashMap<String,String>> cashFlowGrowthsDtoMap;

    private String operatingCashFlowVsNetProficCmp;

    private String cfoPatRatio;

    public CashFlowAnalysisInfo(){
        cashFlowGrowthsDtoMap = new HashMap<>();
    }

    public double getFreeCashFlow() {
        return freeCashFlow;
    }

    public String getFreeCashFlowStr() {
        return freeCashFlowStr;
    }

    public Map<String, HashMap<String, String>> getCashFlowGrowthsDtoMap() {
        return cashFlowGrowthsDtoMap;
    }

    public void setFreeCashFlow(double freeCashFlow) {
        this.freeCashFlow = freeCashFlow;
        this.freeCashFlowStr = StockUtil.convertDoubleToPrecision(freeCashFlow,2);
    }

    public void addCashFlowGrowth(String date, String attribure, String growth){
        if(!cashFlowGrowthsDtoMap.containsKey(date)){
            cashFlowGrowthsDtoMap.put(date,new HashMap<>());
        }
        cashFlowGrowthsDtoMap.get(date).put(attribure,growth);
    }

    public String getOperatingCashFlowVsNetProficCmp() {
        return operatingCashFlowVsNetProficCmp;
    }

    public void setOperatingCashFlowVsNetProficCmp(String operatingCashFlowVsNetProficCmp) {
        this.operatingCashFlowVsNetProficCmp = operatingCashFlowVsNetProficCmp;
    }

    public void setCfoPatRatio(String cfoPatRatio) {
        this.cfoPatRatio = cfoPatRatio;
    }

    public String getCfoPatRatio() {
        return cfoPatRatio;
    }

    @Override
    public String toString() {
        return "CashFlowAnalysisInfo{" +
                ", cashFlowGrowthsDtoMap=" + cashFlowGrowthsDtoMap +
                ", freeCashFlowStr="+freeCashFlowStr+
                ", operatingCashFlowVsNetProficCmp= "+operatingCashFlowVsNetProficCmp+
                ",cfoPatRatio= "+cfoPatRatio+
                '}';
    }
}
