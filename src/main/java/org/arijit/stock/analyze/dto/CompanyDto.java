package org.arijit.stock.analyze.dto;

public class CompanyDto {

    private String companyName;
    private int years;

    private String industry;
    private long marketCap;
    private double faceValue;
    private double currentSharePrice;
    private double industryPE;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public long getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(long marketCap) {
        this.marketCap = marketCap;
    }

    public double getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(double faceValue) {
        this.faceValue = faceValue;
    }

    public double getCurrentSharePrice() {
        return currentSharePrice;
    }

    public void setCurrentSharePrice(double currentSharePrice) {
        this.currentSharePrice = currentSharePrice;
    }

    public double getIndustryPE() {
        return industryPE;
    }

    public void setIndustryPE(double industryPE) {
        this.industryPE = industryPE;
    }

    @Override
    public String toString() {
        return "CompanyDto{" +
                "companyName='" + companyName + '\'' +
                ", years=" + years +
                ", industry='" + industry + '\'' +
                ", marketCap=" + marketCap +
                ", faceValue=" + faceValue +
                ", currentSharePrice=" + currentSharePrice +
                ", industryPE=" + industryPE +
                '}';
    }
}
