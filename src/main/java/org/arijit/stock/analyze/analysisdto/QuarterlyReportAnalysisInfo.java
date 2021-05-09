package org.arijit.stock.analyze.analysisdto;

public class QuarterlyReportAnalysisInfo {
    private double estimatedEPS;
    public void setEstimatedEPS(double estimatedEPS) {
        this.estimatedEPS = estimatedEPS;
    }

    public double getEstimatedEPS() {
        return estimatedEPS;
    }

    @Override
    public String toString() {
        return "QuarterlyReportAnalysisInfo [" +
                "estimatedEPS=" + estimatedEPS +
                ']';
    }
}
