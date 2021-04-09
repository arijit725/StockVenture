package org.arijit.stock.analyze.analysisdto;

import org.arijit.stock.analyze.enums.ValuationEnums;

public class RatioAnalysisInfo {

    private double forwardPERatio;
    private ValuationEnums forwardPEValuation;

    public void setForwardPERatio(double forwardPERatio) {
        this.forwardPERatio = forwardPERatio;
    }

    public void setForwardPEValuation(ValuationEnums forwardPEValuation) {
        this.forwardPEValuation = forwardPEValuation;
    }

    public double getForwardPERatio() {
        return forwardPERatio;
    }

    public ValuationEnums getForwardPEValuation() {
        return forwardPEValuation;
    }

    @Override
    public String toString() {
        return "RatioAnalysisInfo{" +
                "forwardPERatio=" + forwardPERatio +
                ", forwardPEValuation=" + forwardPEValuation +
                '}';
    }
}
