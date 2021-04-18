package org.arijit.stock.analyze.analysisdto;

import org.arijit.stock.analyze.util.DateUtil;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class ProfitAndLossAnalysisInfo {

    private double netSalesGrowthPercentage;
    private boolean isSalesGrowthContinuous;
    private double rawMaterialGrowthPercentage;
    private double PBITGrowthPercentage;
    private double interestDecreasePercentage;
    private double netProfitGrowthPercentage;

    private Map<String,String> netProfitVsSalesRatio;

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
}
