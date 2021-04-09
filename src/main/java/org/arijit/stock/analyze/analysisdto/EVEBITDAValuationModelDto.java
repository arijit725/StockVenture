package org.arijit.stock.analyze.analysisdto;

public class EVEBITDAValuationModelDto {

    private double targetPrice;
    private double entryPrice;
    EVEBITDAValuationModelDto(){

    }
    public void setEntryPrice(double entryPrice) {
        this.entryPrice = entryPrice;
    }

    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }

    public double getEntryPrice() {
        return entryPrice;
    }

    public double getTargetPrice() {
        return targetPrice;
    }
}
