package org.arijit.stock.analyze.analysisdto;

import org.arijit.stock.analyze.util.DateUtil;

import java.text.ParseException;
import java.util.*;

public class BalanceSheetAnalysisInfo {

    private double totalShareChangePercentage;
    private int increaseIncidentInTotalShare;
    private double reservesChangePercentage;
    private double debtChangePercentage;

    private Map<String,String> debtToReserveRatioMap;

    private List<String> analysisStatement;

    private String balanceSheetScore;

    public BalanceSheetAnalysisInfo(){
        debtToReserveRatioMap = new TreeMap<>(new Comparator<String>() {
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

    public String getBalanceSheetScore() {
        return balanceSheetScore;
    }

    public void setBalanceSheetScore(String balanceSheetScore) {
        this.balanceSheetScore = balanceSheetScore;
    }

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

    public Map<String, String> getDebtToReserveRatioMap() {
        return debtToReserveRatioMap;
    }

    @Override
    public String toString() {
        return "BalanceSheetAnalysisInfo{" +
                "totalShareChangePercentage=" + totalShareChangePercentage +
                ", increaseIncidentInTotalShare=" + increaseIncidentInTotalShare +
                ", reservesChangePercentage=" + reservesChangePercentage +
                ", debtChangePercentage=" + debtChangePercentage +
                ", debtToReserveRatioMap=" + debtToReserveRatioMap +
                ", analysisStatement=" + analysisStatement +
                ", balanceSheetScore='" + balanceSheetScore + '\'' +
                '}';
    }
}
