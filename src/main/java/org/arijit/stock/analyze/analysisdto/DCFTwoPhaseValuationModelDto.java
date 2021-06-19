package org.arijit.stock.analyze.analysisdto;

import java.util.Map;
import java.util.TreeMap;

public class DCFTwoPhaseValuationModelDto {

    private double growthRate1;
    private double growthRate2;
    private double discountRate;
    private double terminalGrowthRate;
    private double initalAvgFCF;
    private double year10FCF;
    private double sumOfPV;
    private double totalPV;
    private double terminalValue;
    private double finalIntrinsicValue;
    private Map<String, Double> fcfMap;
    private Map<Integer, ProjectionDto> projectionDtoMap;
    private double discountedMarketCap;
    private double numberOfOutstandingShare;
    private boolean evluated;


    public DCFTwoPhaseValuationModelDto(){
        projectionDtoMap = new TreeMap<>();
        projectionDtoMap.put(1,new ProjectionDto(1));
        projectionDtoMap.put(2,new ProjectionDto(2));
        projectionDtoMap.put(3,new ProjectionDto(3));
        projectionDtoMap.put(4,new ProjectionDto(4));
        projectionDtoMap.put(5,new ProjectionDto(5));
        projectionDtoMap.put(6,new ProjectionDto(6));
        projectionDtoMap.put(7,new ProjectionDto(7));
        projectionDtoMap.put(8,new ProjectionDto(8));
        projectionDtoMap.put(9,new ProjectionDto(9));
        projectionDtoMap.put(10,new ProjectionDto(10));
    }

    public double getNumberOfOutstandingShare() {
        return numberOfOutstandingShare;
    }

    public void setNumberOfOutstandingShare(double numberOfOutstandingShare) {
        this.numberOfOutstandingShare = numberOfOutstandingShare;
    }

    public double getInitalAvgFCF() {
        return initalAvgFCF;
    }

    public double getYear10FCF() {
        return year10FCF;
    }

    public void setInitalAvgFCF(double initalAvgFCF) {
        this.initalAvgFCF = initalAvgFCF;
    }

    public void setYear10FCF(double year10FCF) {
        this.year10FCF = year10FCF;
    }

    public double getDiscountedMarketCap() {
        return discountedMarketCap;
    }

    public void setDiscountedMarketCap(double discountedMarketCap) {
        this.discountedMarketCap = discountedMarketCap;
    }

    public double getSumOfPV() {
        return sumOfPV;
    }

    public void setSumOfPV(double sumOfPV) {
        this.sumOfPV = sumOfPV;
    }

    public double getTotalPV() {
        return totalPV;
    }

    public void setTotalPV(double totalPV) {
        this.totalPV = totalPV;
    }

    public double getTerminalValue() {
        return terminalValue;
    }

    public void setTerminalValue(double terminalValue) {
        this.terminalValue = terminalValue;
    }

    public double getGrowthRate1() {
        return growthRate1;
    }

    public void setGrowthRate1(double growthRate1) {
        this.growthRate1 = growthRate1;
    }

    public double getGrowthRate2() {
        return growthRate2;
    }

    public Map<String, Double> getFcfMap() {
        return fcfMap;
    }

    public void setFcfMap(Map<String, Double> fcfMap) {
        this.fcfMap = fcfMap;
    }

    public void setGrowthRate2(double growthRate2) {
        this.growthRate2 = growthRate2;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getTerminalGrowthRate() {
        return terminalGrowthRate;
    }

    public void setTerminalGrowthRate(double terminalGrowthRate) {
        this.terminalGrowthRate = terminalGrowthRate;
    }

    public double getFinalIntrinsicValue() {
        return finalIntrinsicValue;
    }

    public void setFinalIntrinsicValue(double finalIntrinsicValue) {
        this.finalIntrinsicValue = finalIntrinsicValue;
    }

    public ProjectionDto getProjectionDto(int year) {
        return projectionDtoMap.get(year);
    }

    public Map<Integer, ProjectionDto> getProjectionDtoMap() {
        return projectionDtoMap;
    }

    public void addProjectedFCF(int year, double fcf){
        projectionDtoMap.get(year).setEstimatedFCF(fcf);
    }
    public void addPVOfProjectedFCF(int year, double fcf){
        projectionDtoMap.get(year).setPvFCF(fcf);
    }
    public void addGrwothRate(int year, double growthRate) { projectionDtoMap.get(year).setGrowthRate(growthRate);}


    public void setEvluated(boolean evluated) {
        this.evluated = evluated;
    }

    public boolean isEvluated() {
        return evluated;
    }

    @Override
    public String toString() {
        return "DCFTwoPhaseValuationModelDto{" +
                "growthRate1=" + growthRate1 +
                ", growthRate2=" + growthRate2 +
                ", discountRate=" + discountRate +
                ", terminalGrowthRate=" + terminalGrowthRate +
                ", finalIntrinsicValue=" + finalIntrinsicValue +
                ", fcfMap=" + fcfMap +
                ", projectionDtoMap=" + projectionDtoMap +
                ", evluated=" + evluated +
                '}';
    }

    public static class ProjectionDto{
        private final int year;
        private double estimatedFCF;
        private double growthRate;
        private double pvFCF;

        ProjectionDto(int year){
            this.year = year;
        }

        public int getYear() {
            return year;
        }

        public double getEstimatedFCF() {
            return estimatedFCF;
        }

        public void setEstimatedFCF(double estimatedFCF) {
            this.estimatedFCF = estimatedFCF;
        }

        public double getGrowthRate() {
            return growthRate;
        }

        public void setGrowthRate(double growthRate) {
            this.growthRate = growthRate;
        }

        public double getPvFCF() {
            return pvFCF;
        }

        public void setPvFCF(double pvFCF) {
            this.pvFCF = pvFCF;
        }

        @Override
        public String toString() {
            return "ProjectionDto{" +
                    "year=" + year +
                    ", estimatedFCF=" + estimatedFCF +
                    ", growthRate=" + growthRate +
                    ", pvFCF=" + pvFCF +
                    '}';
        }
    }
}
