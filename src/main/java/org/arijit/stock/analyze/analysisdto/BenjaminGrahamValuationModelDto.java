package org.arijit.stock.analyze.analysisdto;

public class BenjaminGrahamValuationModelDto {
    private double growthRate;
    private double aaaBondY;
    private double repoRate;
    private double peZero;
    private boolean evluated;

    private double finalIntrinsicValue;

    public double getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(double growthRate) {
        this.growthRate = growthRate;
    }

    public double getAaaBondY() {
        return aaaBondY;
    }

    public void setAaaBondY(double aaaBondY) {
        this.aaaBondY = aaaBondY;
    }

    public double getRepoRate() {
        return repoRate;
    }

    public void setRepoRate(double repoRate) {
        this.repoRate = repoRate;
    }

    public double getPeZero() {
        return peZero;
    }

    public void setPeZero(double peZero) {
        this.peZero = peZero;
    }

    public boolean isEvluated() {
        return evluated;
    }

    public void setEvluated(boolean evluated) {
        this.evluated = evluated;
    }

    public double getFinalIntrinsicValue() {
        return finalIntrinsicValue;
    }

    public void setFinalIntrinsicValue(double finalIntrinsicValue) {
        this.finalIntrinsicValue = finalIntrinsicValue;
    }

    @Override
    public String toString() {
        return "BenjaminGrahamValuationModelDto{" +
                "growthRate=" + growthRate +
                ", aaaBondY=" + aaaBondY +
                ", repoRate=" + repoRate +
                ", peZero=" + peZero +
                '}';
    }
}
