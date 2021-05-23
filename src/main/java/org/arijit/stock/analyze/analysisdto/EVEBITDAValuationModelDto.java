package org.arijit.stock.analyze.analysisdto;

import java.util.HashMap;

public class EVEBITDAValuationModelDto {

    private String forcastedEV;
    private String expectedEBITDA;
    private String targetPrice;
    private String targetPriceAfterMarginOfSafty;
    private String upside;

    public EVEBITDAValuationModelDto(){

    }


    public String getUpside() {
        return upside;
    }

    public void setUpside(String upside) {
        this.upside = upside;
    }

    public void setTargetPrice(String targetPrice) {
        this.targetPrice = targetPrice;
    }

    public String getForcastedEV() {
        return forcastedEV;
    }

    public void setForcastedEV(String forcastedEV) {
        this.forcastedEV = forcastedEV;
    }

    public String getExpectedEBITDA() {
        return expectedEBITDA;
    }

    public void setExpectedEBITDA(String expectedEBITDA) {
        this.expectedEBITDA = expectedEBITDA;
    }

    public String getTargetPriceAfterMarginOfSafty() {
        return targetPriceAfterMarginOfSafty;
    }

    public void setTargetPriceAfterMarginOfSafty(String targetPriceAfterMarginOfSafty) {
        this.targetPriceAfterMarginOfSafty = targetPriceAfterMarginOfSafty;
    }

    @Override
    public String toString() {
        return "EVEBITDAValuationModelDto{" +
                "forcastedEV='" + forcastedEV + '\'' +
                ", expectedEBITDA='" + expectedEBITDA + '\'' +
                ", targetPrice='" + targetPrice + '\'' +
                ", targetPriceAfterMarginOfSafty='" + targetPriceAfterMarginOfSafty + '\'' +
                '}';
    }
}
