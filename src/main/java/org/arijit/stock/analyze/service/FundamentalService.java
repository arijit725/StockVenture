package org.arijit.stock.analyze.service;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.cache.MemCache;
import org.arijit.stock.analyze.dto.*;
import org.arijit.stock.analyze.fundamental.FundamentalAnalysisEvaluation;
import org.arijit.stock.analyze.util.StockUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class FundamentalService {
    private static final Logger logger = LogManager.getLogger(FundamentalService.class);

    private final String userID = "III"; // at this time we are not supporting for multiple user, so userID set to fix value.
    public String updateCompanyDetails(String companyDetailsJson){
        Gson gson = new Gson();
        CompanyDto companyDto = gson.fromJson(companyDetailsJson, CompanyDto.class);
        logger.info("ComanpyDto: "+companyDto);
        String stockID = StockUtil.generateID();
        MemCache.getInstance().insertCompanyDetails(userID,stockID,companyDto);
        return stockID;
    }

    public void updateBalanceSheetList(String stockID, String baancesheetDetails) throws Exception {
        Gson gson = new Gson();
        BalanceSheetDto[] balanceSheetDtos = gson.fromJson(baancesheetDetails,BalanceSheetDto[].class);
        logger.info(Arrays.toString(balanceSheetDtos));
        if(balanceSheetDtos==null || balanceSheetDtos.length == 0)
            throw new Exception("Balancesheet data not found");
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(userID,stockID);
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
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(userID,stockID);
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
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(userID,stockID);
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
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(userID,stockID);
        for(RatiosDto ratiosDto:ratiosDtos){
            fundamentalInfoDto.addRatiosDto(ratiosDto);
        }
        fundamentalInfoDto.build();
    }

    /**
     * This method will analyze stock with last  number of "years" data
     *
     * @param stockID
     * @param years
     * @throws Exception
     */
    public void analyzeStock(String stockID, int years) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(userID,stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        FundamentalAnalysisEvaluation.getInstance().evaluate(fundamentalInfoDto,years);
    }
}
