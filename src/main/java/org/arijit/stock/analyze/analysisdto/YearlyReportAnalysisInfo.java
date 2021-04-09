package org.arijit.stock.analyze.analysisdto;

public class YearlyReportAnalysisInfo {

    private double estimatedEPS;

    public void setEstimatedEPS(double estimatedEPS) {
        this.estimatedEPS = estimatedEPS;
    }

    public double getEstimatedEPS() {
        return estimatedEPS;
    }

    @Override
    public String toString() {
        return "YearlyReportAnalysisInfo [" +
                "estimatedEPS=" + estimatedEPS +
                ']';
    }
}
