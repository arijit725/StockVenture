package org.arijit.stock.analyze.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class will contain fundamental information require for analysis
 */
public final class FundamentalInfoDto {

    private final String companyName;
    private final int years;

    private String industry;
    private long marketCap;
    private double faceValue;
    private double currentSharePrice;
    private double industryPE;

    private List<BalanceSheetDto> balanceSheetDtoList;
    private List<ProfitAndLossDto> profitAndLossDtoList;
    private List<RatiosDto> ratiosDtoList;
    private List<YearlyReportDto> yearlyReportDtoList;


    private FundamentalInfoDto(String companyName,int years){
        this.companyName = companyName;
        this.years = years;
    }

    public static FundamentalInfoDto builder(String companyName, int years){
        FundamentalInfoDto fundamentalInfoDto = new FundamentalInfoDto(companyName,years);
        fundamentalInfoDto.balanceSheetDtoList = new ArrayList<>(years);
        fundamentalInfoDto.profitAndLossDtoList = new ArrayList<>(years);
        fundamentalInfoDto.ratiosDtoList = new ArrayList<>(years);
        fundamentalInfoDto.yearlyReportDtoList = new ArrayList<>(years);
        return fundamentalInfoDto;
    }

    public FundamentalInfoDto setIndustry(String industry) {
        this.industry = industry;
        return this;
    }

    public FundamentalInfoDto setMarketCap(long marketCap) {
        this.marketCap = marketCap;
        return this;
    }

    public FundamentalInfoDto setFaceValue(double faceValue) {
        this.faceValue = faceValue;
        return this;
    }

    public FundamentalInfoDto setCurrentSharePrice(double currentSharePrice) {
        this.currentSharePrice = currentSharePrice;
        return this;
    }

    public FundamentalInfoDto setIndustryPE(double industryPE) {
        this.industryPE = industryPE;
        return this;
    }

    public FundamentalInfoDto addBalanceSheetDto(BalanceSheetDto balanceSheetDto) {
        this.balanceSheetDtoList.add(balanceSheetDto);
        return this;
    }

    public FundamentalInfoDto addProfitAndLossDto(ProfitAndLossDto profitAndLossDto) {
        this.profitAndLossDtoList.add(profitAndLossDto);
        return this;
    }

    public FundamentalInfoDto addRatiosDto(RatiosDto ratiosDto) {
        this.ratiosDtoList.add(ratiosDto);
        return this;
    }

    public FundamentalInfoDto addYearlyReportDto(YearlyReportDto yearlyReportDto) {
        this.yearlyReportDtoList.add(yearlyReportDto);
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getYears() {
        return years;
    }

    public String getIndustry() {
        return industry;
    }

    public long getMarketCap() {
        return marketCap;
    }

    public List<BalanceSheetDto> getBalanceSheetDtoList() {
        return balanceSheetDtoList;
    }

    public List<ProfitAndLossDto> getProfitAndLossDtoList() {
        return profitAndLossDtoList;
    }

    public List<RatiosDto> getRatiosDtoList() {
        return ratiosDtoList;
    }

    public List<YearlyReportDto> getYearlyReportDtoList() {
        return yearlyReportDtoList;
    }

    public double getFaceValue() {
        return faceValue;
    }

    public double getCurrentSharePrice() {
        return currentSharePrice;
    }

    public double getIndustryPE() {
        return industryPE;
    }

    public FundamentalInfoDto build(){
        Collections.sort(this.balanceSheetDtoList,Collections.reverseOrder());
        Collections.sort(this.ratiosDtoList,Collections.reverseOrder());
        Collections.sort(this.yearlyReportDtoList,Collections.reverseOrder());
        Collections.sort(this.profitAndLossDtoList,Collections.reverseOrder());
        return this;
    }

    @Override
    public String toString() {
        return "FundamentalInfoDto{" +
                "companyName='" + companyName + '\'' +
                ", years=" + years +
                ", industry='" + industry + '\'' +
                ", marketCap=" + marketCap +
                ", faceValue=" + faceValue +
                ", currentSharePrice=" + currentSharePrice +
                ", industryPE=" + industryPE +
                ", balanceSheetDtoList=" + balanceSheetDtoList +
                ", profitAndLossDtoList=" + profitAndLossDtoList +
                ", ratiosDtoList=" + ratiosDtoList +
                ", yearlyReportDtoList=" + yearlyReportDtoList +
                '}';
    }
}
