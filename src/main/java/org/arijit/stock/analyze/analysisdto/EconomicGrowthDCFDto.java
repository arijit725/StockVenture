package org.arijit.stock.analyze.analysisdto;

import org.arijit.stock.analyze.enums.ValuationEnums;

import java.util.HashMap;
import java.util.Map;

public class EconomicGrowthDCFDto {
    private Map<String, String> pastNYearsFreeCashFlow;
    private Map<Integer, String> nextNYearsFreeCashFlow;
    private Map<Integer,String> nextNYearsFCFPV;

    /*This is mainly economy growth rate.*/
    private double perpertualGrowthRate;

    /*Return of investment in percentage*/
    private double discountRate;

    /*Terminal Value*/
    private double tv;

    /*Present Value of Terminal value*/
    private double tvPv;

    /*sum of projected FCF + PV of terminal value*/
    private double sumOfFcF;

    /*Cash or Cash Equivalent of last previous financial year*/
    private double lasFYCashEquivalent;

    /*Debt of last previous financial year*/
    private double lastFYDebt;

    private double equityValue;
    private double shareOutStanding;
    private double targetPrice;
    private double marginOfSafty;
    private double priceAfterMarginOfSafty;
    private double upside;

    private ValuationEnums decision;


    public EconomicGrowthDCFDto(){
        this.pastNYearsFreeCashFlow = new HashMap<>();
        this.nextNYearsFreeCashFlow = new HashMap<>();
        this.nextNYearsFCFPV = new HashMap<>();
    }

    public void addPastNYearsFreeCashFlow(String date, String fcf) {
        this.pastNYearsFreeCashFlow.put(date,fcf);
    }

    public void addNextNYearsFreeCashFlow(Integer year, String fcf) {
        this.nextNYearsFreeCashFlow.put(year, fcf);
    }

    public void addNextNYearsFCFPV(Integer year, String fcf) {
        this.nextNYearsFCFPV.put(year,fcf);
    }

    public void setPerpertualGrowthRate(double perpertualGrowthRate) {
        this.perpertualGrowthRate = perpertualGrowthRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public void setTv(double tv) {
        this.tv = tv;
    }

    public void setTvPv(double tvPv) {
        this.tvPv = tvPv;
    }

    public void setLasFYCashEquivalent(double lasFYCashEquivalent) {
        this.lasFYCashEquivalent = lasFYCashEquivalent;
    }

    public void setLastFYDebt(double lastFYDebt) {
        this.lastFYDebt = lastFYDebt;
    }

    public void setSumOfFcF(double sumOfFcF) {
        this.sumOfFcF = sumOfFcF;
    }

    public void setEquityValue(double equityValue) {
        this.equityValue = equityValue;
    }

    public void setShareOutStanding(double shareOutStanding) {
        this.shareOutStanding = shareOutStanding;
    }

    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }

    public void setMarginOfSafty(double marginOfSafty) {
        this.marginOfSafty = marginOfSafty;
    }

    public void setUpside(double upside) {
        this.upside = upside;
    }

    public void setPriceAfterMarginOfSafty(double priceAfterMarginOfSafty) {
        this.priceAfterMarginOfSafty = priceAfterMarginOfSafty;
    }

    public void setDecision(ValuationEnums decision) {
        this.decision = decision;
    }

    public Map<String, String> getPastNYearsFreeCashFlow() {
        return pastNYearsFreeCashFlow;
    }

    public double getSumOfFcF() {
        return sumOfFcF;
    }

    public double getPriceAfterMarginOfSafty() {
        return priceAfterMarginOfSafty;
    }

    public Map<Integer, String> getNextNYearsFreeCashFlow() {
        return nextNYearsFreeCashFlow;
    }

    public Map<Integer, String> getNextNYearsFCFPV() {
        return nextNYearsFCFPV;
    }

    public double getPerpertualGrowthRate() {
        return perpertualGrowthRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public double getTv() {
        return tv;
    }

    public double getTvPv() {
        return tvPv;
    }

    public double getLasFYCashEquivalent() {
        return lasFYCashEquivalent;
    }

    public double getLastFYDebt() {
        return lastFYDebt;
    }

    public double getEquityValue() {
        return equityValue;
    }

    public double getShareOutStanding() {
        return shareOutStanding;
    }

    public double getTargetPrice() {
        return targetPrice;
    }


    public double getMarginOfSafty() {
        return marginOfSafty;
    }

    public double getUpside() {
        return upside;
    }

    public ValuationEnums getDecision() {
        return decision;
    }

    @Override
    public String toString() {
        return "EconomicGrowthDCFDto{" +
                "pastNYearsFreeCashFlow=" + pastNYearsFreeCashFlow +
                ", nextNYearsFreeCashFlow=" + nextNYearsFreeCashFlow +
                ", nextNYearsFCFPV=" + nextNYearsFCFPV +
                ", perpecualGrowthRate=" + perpertualGrowthRate +
                ", discountRate=" + discountRate +
                ", tv=" + tv +
                ", tvPv=" + tvPv +
                ", lasFYCashEquivalent=" + lasFYCashEquivalent +
                ", lastFYDebt=" + lastFYDebt +
                ", equityValue=" + equityValue +
                ", shareOutStanding=" + shareOutStanding +
                ", targetPrice=" + targetPrice +
                ", upside=" + upside +
                ", decision=" + decision +
                '}';
    }
}
