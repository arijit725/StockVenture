package org.arijit.stock.analyze.analysisdto;

import java.util.HashMap;
import java.util.Map;

public class YearlyReportAnalysisInfo {

    private double estimatedEPSCAGR;
    private Map<String, String> epsGrowthRate;
    private double averageEPS;

    public YearlyReportAnalysisInfo(){
        epsGrowthRate = new HashMap<>();
    }
    public void setAverageEPS(double averageEPS) {
        this.averageEPS = averageEPS;
    }

    public void addEpsGrowthRate(String date, String epsGrowth) {
        this.epsGrowthRate.put(date,epsGrowth);
    }

    public Map<String, String> getEpsGrowthRate() {
        return epsGrowthRate;
    }

    public double getAverageEPS() {
        return averageEPS;
    }

    public void setEstimatedEPSCAGR(double estimatedEPSCAGR) {
        this.estimatedEPSCAGR = estimatedEPSCAGR;
    }

    public double getEstimatedEPSCAGR() {
        return estimatedEPSCAGR;
    }

    @Override
    public String toString() {
        return "YearlyReportAnalysisInfo{" +
                "estimatedEPSCAGR=" + estimatedEPSCAGR +
                ", epsGrowthRate=" + epsGrowthRate +
                ", averageEPS=" + averageEPS +
                '}';
    }
}
