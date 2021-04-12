package org.arijit.stock.analyze.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class will contain fundamental information require for analysis
 */
public final class FundamentalInfoDto {

//    private  String companyName;
//    private  int years;
//
//    private String industry;
//    private long marketCap;
//    private double faceValue;
//    private double currentSharePrice;
//    private double industryPE;

    private CompanyDto companyDto;
    private List<BalanceSheetDto> balanceSheetDtoList;
    private List<ProfitAndLossDto> profitAndLossDtoList;
    private List<RatiosDto> ratiosDtoList;
    private List<YearlyReportDto> yearlyReportDtoList;

    private FundamentalInfoDto(){

    }

    public static FundamentalInfoDto builder(int years){
        FundamentalInfoDto fundamentalInfoDto = new FundamentalInfoDto();
        fundamentalInfoDto.balanceSheetDtoList = new ArrayList<>(years);
        fundamentalInfoDto.profitAndLossDtoList = new ArrayList<>(years);
        fundamentalInfoDto.ratiosDtoList = new ArrayList<>(years);
        fundamentalInfoDto.yearlyReportDtoList = new ArrayList<>(years);
        return fundamentalInfoDto;
    }

    public FundamentalInfoDto setCompanyDto(CompanyDto companyDto){
        this.companyDto = companyDto;
        return this;
    }

//    public FundamentalInfoDto setIndustry(String industry) {
//        this.industry = industry;
//        return this;
//    }

//    public FundamentalInfoDto setMarketCap(long marketCap) {
//        this.marketCap = marketCap;
//        return this;
//    }

//    public FundamentalInfoDto setFaceValue(double faceValue) {
//        this.faceValue = faceValue;
//        return this;
//    }

//    public FundamentalInfoDto setCurrentSharePrice(double currentSharePrice) {
//        this.currentSharePrice = currentSharePrice;
//        return this;
//    }

//    public FundamentalInfoDto setIndustryPE(double industryPE) {
//        this.industryPE = industryPE;
//        return this;
//    }

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

    public CompanyDto getCompanyDto() {
        return companyDto;
    }

//    public String getCompanyName() {
//        return companyName;
//    }

//    public int getYears() {
//        return years;
//    }

//    public String getIndustry() {
//        return industry;
//    }

//    public long getMarketCap() {
//        return marketCap;
//    }

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

//    public double getFaceValue() {
//        return faceValue;
//    }

//    public double getCurrentSharePrice() {
//        return currentSharePrice;
//    }
//
//    public double getIndustryPE() {
//        return industryPE;
//    }

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
                "companyDto=" + companyDto +
                ", balanceSheetDtoList=" + balanceSheetDtoList +
                ", profitAndLossDtoList=" + profitAndLossDtoList +
                ", ratiosDtoList=" + ratiosDtoList +
                ", yearlyReportDtoList=" + yearlyReportDtoList +
                '}';
    }
}
