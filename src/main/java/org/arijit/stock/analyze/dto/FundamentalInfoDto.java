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

    private String stockID;
    private CompanyDto companyDto;
    private List<BalanceSheetDto> balanceSheetDtoList;
    private List<ProfitAndLossDto> profitAndLossDtoList;
    private List<RatiosDto> ratiosDtoList;
    private List<YearlyReportDto> yearlyReportDtoList;
    private List<QuarterlyReportDTO> quarterlyReportDtoList;
    private List<CashFlowDto> cashFlowDtoList;

    private FundamentalInfoDto(){

    }

    public static FundamentalInfoDto builder(int years){
        FundamentalInfoDto fundamentalInfoDto = new FundamentalInfoDto();
        fundamentalInfoDto.balanceSheetDtoList = new ArrayList<>(years);
        fundamentalInfoDto.profitAndLossDtoList = new ArrayList<>(years);
        fundamentalInfoDto.ratiosDtoList = new ArrayList<>(years);
        fundamentalInfoDto.yearlyReportDtoList = new ArrayList<>(years);
        fundamentalInfoDto.quarterlyReportDtoList = new ArrayList<>(years);
        fundamentalInfoDto.cashFlowDtoList = new ArrayList<>(years);
        return fundamentalInfoDto;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public String getStockID() {
        return stockID;
    }

    public FundamentalInfoDto setCompanyDto(CompanyDto companyDto){
        this.companyDto = companyDto;
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

    public FundamentalInfoDto addCashFlowDto(CashFlowDto cashFlowDto) {
        this.cashFlowDtoList.add(cashFlowDto);
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

    public FundamentalInfoDto addQuarterlyReportDto(QuarterlyReportDTO quarterlyReportDto) {
        this.quarterlyReportDtoList.add(quarterlyReportDto);
        return this;
    }

    public CompanyDto getCompanyDto() {
        return this.companyDto;
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

    public List<QuarterlyReportDTO> getQuarterlyReportDtoList() {
        return quarterlyReportDtoList;
    }

    public void clearBalancesheetDtos(){
        this.balanceSheetDtoList.clear();
    }

    public void clearProfitAndLossDtos(){
        this.profitAndLossDtoList.clear();
    }

    public void clearYearlyReportDtos(){
        this.yearlyReportDtoList.clear();
    }

    public void clearQuarterlyReportDtos(){
        this.quarterlyReportDtoList.clear();
    }

    public void clearRatiosDtos(){
        this.ratiosDtoList.clear();
    }

    public FundamentalInfoDto build(){
        Collections.sort(this.balanceSheetDtoList,Collections.reverseOrder());
        Collections.sort(this.ratiosDtoList,Collections.reverseOrder());
        Collections.sort(this.yearlyReportDtoList,Collections.reverseOrder());
        Collections.sort(this.profitAndLossDtoList,Collections.reverseOrder());
        Collections.sort(this.quarterlyReportDtoList,Collections.reverseOrder());
        Collections.sort(this.cashFlowDtoList,Collections.reverseOrder());
        return this;
    }

    @Override
    public String toString() {
        return "FundamentalInfoDto{" +
                "stockID='" + stockID + '\'' +
                ", companyDto=" + companyDto +
                ", balanceSheetDtoList=" + balanceSheetDtoList +
                ", profitAndLossDtoList=" + profitAndLossDtoList +
                ", ratiosDtoList=" + ratiosDtoList +
                ", yearlyReportDtoList=" + yearlyReportDtoList +
                ", quarterlyReportDtoList=" + quarterlyReportDtoList +
                ", cashFlowDtoList=" + cashFlowDtoList +
                '}';
    }
}
