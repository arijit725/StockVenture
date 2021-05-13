package org.arijit.stock.analyze.analysisdto;

import org.arijit.stock.analyze.util.StockUtil;

public class QuarterlyReportAnalysisInfo {
    private double estimatedEPSCAGR;
    private double ttmEPS;

    private String estimatedEPSCAGRStr;
    private String ttmEPSStr;

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


    @Override
    public String toString() {
        return "QuarterlyReportAnalysisInfo [" +
                "estimatedEPS=" + estimatedEPSCAGR +
                ']';
    }
}
