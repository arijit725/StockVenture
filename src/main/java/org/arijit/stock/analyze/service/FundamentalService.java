package org.arijit.stock.analyze.service;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.analysisdto.BalanceSheetAnalysisInfo;
import org.arijit.stock.analyze.cache.MemCache;
import org.arijit.stock.analyze.dto.*;
import org.arijit.stock.analyze.fundamental.BalanceSheetEvaluation;
import org.arijit.stock.analyze.fundamental.FundamentalAnalysisEvaluation;
import org.arijit.stock.analyze.fundamental.YearlyReportEvaluation;
import org.arijit.stock.analyze.util.StockUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FundamentalService {
    private static final Logger logger = LogManager.getLogger(FundamentalService.class);

    public String updateCompanyDetails(String companyDetailsJson){
        Gson gson = new Gson();
        CompanyDto companyDto = gson.fromJson(companyDetailsJson, CompanyDto.class);
        logger.info("ComanpyDto: "+companyDto);
        String stockID = StockUtil.generateID();
        MemCache.getInstance().insertCompanyDetails(stockID,companyDto);
        return stockID;
    }

    public void updateBalanceSheetList(String stockID, String baancesheetDetails) throws Exception {
        Gson gson = new Gson();
        BalanceSheetDto[] balanceSheetDtos = gson.fromJson(baancesheetDetails,BalanceSheetDto[].class);
        logger.info(Arrays.toString(balanceSheetDtos));
        if(balanceSheetDtos==null || balanceSheetDtos.length == 0)
            throw new Exception("Balancesheet data not found");
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        for(BalanceSheetDto balanceSheetDto:balanceSheetDtos){
            fundamentalInfoDto.addBalanceSheetDto(balanceSheetDto);
        }
        fundamentalInfoDto.build();
    }

    public void updateProfitAndLossDetails(String stockID, String profitAndLossDetails) throws Exception {
        Gson gson = new Gson();
        ProfitAndLossDto[] profitAndLossDtos = gson.fromJson(profitAndLossDetails, ProfitAndLossDto[].class);
        logger.info(Arrays.toString(profitAndLossDtos));
        if(profitAndLossDtos==null || profitAndLossDtos.length == 0)
            throw new Exception("ProfitAndLoss data not found");
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        for(ProfitAndLossDto profitAndLossDto:profitAndLossDtos){
            fundamentalInfoDto.addProfitAndLossDto(profitAndLossDto);
        }
        fundamentalInfoDto.build();
    }

    public void updateYearlyReportDetails(String stockID, String yearlyReportDetails) throws Exception {
        Gson gson = new Gson();
        YearlyReportDto[] yearlyReportDtos = gson.fromJson(yearlyReportDetails, YearlyReportDto[].class);
        logger.info(Arrays.toString(yearlyReportDtos));
        if(yearlyReportDtos==null || yearlyReportDtos.length == 0)
            throw new Exception("Yearly Report Details data not found");
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        for(YearlyReportDto yearlyReportDto:yearlyReportDtos){
            fundamentalInfoDto.addYearlyReportDto(yearlyReportDto);
        }
        fundamentalInfoDto.build();
    }

    public void updateRatioDetails(String stockID, String ratioDetails) throws Exception {
        Gson gson = new Gson();
        RatiosDto[] ratiosDtos = gson.fromJson(ratioDetails, RatiosDto[].class);
        logger.info(Arrays.toString(ratiosDtos));
        if(ratiosDtos==null || ratiosDtos.length == 0)
            throw new Exception("Ratio data not found");
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        for(RatiosDto ratiosDto:ratiosDtos){
            fundamentalInfoDto.addRatiosDto(ratiosDto);
        }
        fundamentalInfoDto.build();
    }

    public CompanyDto getCompanyDetails(String stockID) throws Exception {

        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        return fundamentalInfoDto.getCompanyDto();
    }

    /**
     * This method will analyze stock with last  number of "years" data
     *
     * @param stockID
     * @param years
     * @throws Exception
     */
    public void analyzeStock(String stockID, int years) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        FundamentalAnalysisEvaluation.getInstance().evaluate(fundamentalInfoDto,years);
    }

    public List<BalanceSheetDto> getBalanceSheet(String stockID, int years) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        if(fundamentalInfoDto.getBalanceSheetDtoList().size()<years)
            throw new Exception("Years excced balancesheet list size");
        return fundamentalInfoDto.getBalanceSheetDtoList().stream().limit(years).collect(Collectors.toList());
    }

    public List<ProfitAndLossDto> getProfitAndLoss(String stockID, int years) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        if(fundamentalInfoDto.getProfitAndLossDtoList().size()<years)
            throw new Exception("Years excced ProfitAndLossDto list size");
        return fundamentalInfoDto.getProfitAndLossDtoList().stream().limit(years).collect(Collectors.toList());
    }

    public BalanceSheetAnalysisInfo getAnalyzedBalanceSheet(String stockID, int years) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);
        try{
            logger.info("==========================BalanceSheetEvaluation==================================");
            BalanceSheetEvaluation.getInstance().evaluate(fundamentalInfoDto,analyzedInfoDto,years);
        }catch(Exception e){
            logger.error("Unable to evaluate ",e);
        }
        return analyzedInfoDto.getBalanceSheetAnalysisInfo();
    }
}