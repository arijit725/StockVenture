package org.arijit.stock.analyze.analysisdto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EPSMutlipliedValuationModelDto {

    private double currentPrice;
    private double growthRate;
    private double discountRate;
    private double estimatedPE;
    private double finalIntrinsicValue;

    private Map<Integer, ProjectionDto> projectionDtoMap;

    public EPSMutlipliedValuationModelDto(){
        projectionDtoMap = new TreeMap<>();
        projectionDtoMap.put(1,new ProjectionDto(1));
        projectionDtoMap.put(2,new ProjectionDto(2));
        projectionDtoMap.put(3,new ProjectionDto(3));
        projectionDtoMap.put(4,new ProjectionDto(4));
        projectionDtoMap.put(5,new ProjectionDto(5));
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(double growthRate) {
        this.growthRate = growthRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getEstimatedPE() {
        return estimatedPE;
    }

    public void setEstimatedPE(double estimatedPE) {
        this.estimatedPE = estimatedPE;
    }

    public double getFinalIntrinsicValue() {
        return finalIntrinsicValue;
    }

    public void setFinalIntrinsicValue(double finalIntrinsicValue) {
        this.finalIntrinsicValue = finalIntrinsicValue;
    }

    public ProjectionDto getProjectionDtoMap(int year) {
        return projectionDtoMap.get(year);
    }

    public void addProjectedEPS(int year, double ttmEPS){
        projectionDtoMap.get(1).setTtmEPS(ttmEPS);
    }

    public void addProjectedIntrinsicValue(int year, double intrinsicValue){
        projectionDtoMap.get(1).setIntrinsicValue(intrinsicValue);
    }

    @Override
    public String toString() {
        return "EPSMutlipliedValuationModelDto{" +
                "currentPrice=" + currentPrice +
                ", growthRate=" + growthRate +
                ", discountRate=" + discountRate +
                ", estimatedPE=" + estimatedPE +
                ", finalIntrinsicValue=" + finalIntrinsicValue +
                ", projectionDtoMap=" + projectionDtoMap +
                '}';
    }

    public static class ProjectionDto{
        private final int year;
        private double ttmEPS;
        private double intrinsicValue;

        ProjectionDto(int year){
            this.year = year;
        }
        public double getIntrinsicValue() {
            return intrinsicValue;
        }

        public int getYear() {
            return year;
        }

        public double getTtmEPS() {
            return ttmEPS;
        }

        public void setTtmEPS(double ttmEPS) {
            this.ttmEPS = ttmEPS;
        }

        public void setIntrinsicValue(double intrinsicValue) {
            this.intrinsicValue = intrinsicValue;
        }

        @Override
        public String toString() {
            return "ProjectionDto{" +
                    "year=" + year +
                    ", ttmEPS=" + ttmEPS +
                    ", intrinsicValue=" + intrinsicValue +
                    '}';
        }
    }
}
