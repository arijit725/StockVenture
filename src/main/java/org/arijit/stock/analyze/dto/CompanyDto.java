package org.arijit.stock.analyze.dto;

public class CompanyDto {

    private String companyName;
    private int years;
    private boolean seasonal;

    private String industry;
    private long marketCap;
    private double faceValue;
    private double currentSharePrice;
    private double industryPE;

    private double ttmpe;
    private double ttmeps;


    public String getCompanyName() {
        return companyName;
    }

    public boolean isSeasonal() {
        return seasonal;
    }

    public void setSeasonal(boolean seasonal) {
        this.seasonal = seasonal;
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

    public double getTtmeps() {
        return ttmeps;
    }

    public double getTtmpe() {
        return ttmpe;
    }

    public void setTtmeps(double ttmeps) {
        this.ttmeps = ttmeps;
    }

    public void setTtmpe(double ttmpe) {
        this.ttmpe = ttmpe;
    }

    @Override
    public String toString() {
        return "CompanyDto{" +
                "companyName='" + companyName + '\'' +
                ", years=" + years +
                ", sesonal="+ seasonal +
                ", industry='" + industry + '\'' +
                ", marketCap=" + marketCap +
                ", faceValue=" + faceValue +
                ", currentSharePrice=" + currentSharePrice +
                ", industryPE=" + industryPE +
                ", peTTM=" + ttmpe +
                ", epsTTM=" + ttmeps +
                '}';
    }
}
