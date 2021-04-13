package org.arijit.stock.analyze.analysisdto;

import java.util.List;

public class BalanceSheetAnalysisInfo {

    private double totalShareChangePercentage;
    private int increaseIncidentInTotalShare;
    private double reservesChangePercentage;
    private double debtChangePercentage;

    private List<String> analysisStatement;

    public double getTotalShareChangePercentage() {
        return totalShareChangePercentage;
    }

    public void setTotalShareChangePercentage(double totalShareChangePercentage) {
        this.totalShareChangePercentage = totalShareChangePercentage;
    }

    public int getIncreaseIncidentInTotalShare() {
        return increaseIncidentInTotalShare;
    }

    public void setIncreaseIncidentInTotalShare(int increaseIncidentInTotalShare) {
        this.increaseIncidentInTotalShare = increaseIncidentInTotalShare;
    }

    public double getReservesChangePercentage() {
        return reservesChangePercentage;
    }

    public void setReservesChangePercentage(double reservesChangePercentage) {
        this.reservesChangePercentage = reservesChangePercentage;
    }

    public List<String> getAnalysisStatement() {
        return analysisStatement;
    }

    public void setAnalysisStatement(List<String> analysisStatement) {
        this.analysisStatement = analysisStatement;
    }

    public double getDebtChangePercentage() {
        return debtChangePercentage;
    }

    public void setDebtChangePercentage(double debtChangePercentage) {
        this.debtChangePercentage = debtChangePercentage;
    }
}
