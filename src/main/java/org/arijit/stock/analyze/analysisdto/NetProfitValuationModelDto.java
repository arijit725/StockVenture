package org.arijit.stock.analyze.analysisdto;

import java.util.Map;
import java.util.TreeMap;

public class NetProfitValuationModelDto {

   private double discountRate;
    private Map<String,Double> netprofitMap;
    private double profitCAGRGrowth10yrs;
    private double profitCAGRGrowth5yrs;
    private double estimatedMarketCapital;
    private double discountedMarketCapital;
    private double finalIntrinsicValue;
    private double estimatedProfit;
    private double outstandingShare;

    private Map<Integer, Double> projectionDtoMap;

    private boolean evluated;

    public NetProfitValuationModelDto(){
        projectionDtoMap = new TreeMap<>();
    }

    public double getEstimatedProfit() {
        return estimatedProfit;
    }

    public double getOutstandingShare() {
        return outstandingShare;
    }

    public void setOutstandingShare(double outstandingShare) {
        this.outstandingShare = outstandingShare;
    }

    public void setEstimatedProfit(double estimatedProfit) {
        this.estimatedProfit = estimatedProfit;
    }

    public double getDiscountedMarketCapital() {
        return discountedMarketCapital;
    }

    public void setDiscountedMarketCapital(double discountedMarketCapital) {
        this.discountedMarketCapital = discountedMarketCapital;
    }

    public double getEstimatedMarketCapital() {
        return estimatedMarketCapital;
    }

    public void setEstimatedMarketCapital(double estimatedMarketCapital) {
        this.estimatedMarketCapital = estimatedMarketCapital;
    }

    public Map<Integer, Double> getProjectionDtoMap() {
        return projectionDtoMap;
    }

    public double getProfitCAGRGrowth5yrs() {
        return profitCAGRGrowth5yrs;
    }

    public double getProfitCAGRGrowth10yrs() {
        return profitCAGRGrowth10yrs;
    }

    public void setProfitCAGRGrowth5yrs(double profitCAGRGrowth5yrs) {
        this.profitCAGRGrowth5yrs = profitCAGRGrowth5yrs;
    }

    public void setProfitCAGRGrowth10yrs(double profitCAGRGrowth10yrs) {
        this.profitCAGRGrowth10yrs = profitCAGRGrowth10yrs;
    }


    public Map<String, Double> getNetprofitMap() {
        return netprofitMap;
    }

    public void setNetprofitMap(Map<String, Double> netprofitMap) {
        this.netprofitMap = netprofitMap;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getFinalIntrinsicValue() {
        return finalIntrinsicValue;
    }

    public void setFinalIntrinsicValue(double finalIntrinsicValue) {
        this.finalIntrinsicValue = finalIntrinsicValue;
    }

    public void addProjectedNetProfit(int year, double netProfit){
        projectionDtoMap.put(year,netProfit);
    }

    public void setEvluated(boolean evluated) {
        this.evluated = evluated;
    }

    public boolean isEvluated() {
        return evluated;
    }

    @Override
    public String toString() {
        return "NetProfitValuationModelDto{" +
                ", discountRate=" + discountRate +
                ", netprofitMap=" + netprofitMap +
                ", finalIntrinsicValue=" + finalIntrinsicValue +
                ", projectionDtoMap=" + projectionDtoMap +
                ", evluated=" + evluated +
                '}';
    }
}
