package org.arijit.stock.analyze.service;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.*;
import org.arijit.stock.analyze.cache.MemCache;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.fundamental.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StockAnalysisService {
    private static final Logger logger = LogManager.getLogger(StockAnalysisService.class);

    public YearlyReportAnalysisInfo getAnalyzedYearlyReport(String stockID, int years) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        try{
            logger.info("==========================YearlyReportEvaluation==================================");
            YearlyReportEvaluation.getInstance().evaluate(fundamentalInfoDto,analyzedInfoDto,years);
        }catch(Exception e){
            logger.error("Unable to evaluate ",e);
        }
        return analyzedInfoDto.getYearlyReportAnalysisInfo();
    }

    public QuarterlyReportAnalysisInfo getAnalyzedQuarterlyReport(String stockID, int years) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        try{
            logger.info("==========================QuarterlyReportEvaluation==================================");
            QuarterlyReportEvaluation.getInstance().evaluate(fundamentalInfoDto,analyzedInfoDto,years);
        }catch(Exception e){
            logger.error("Unable to evaluate ",e);
        }
        return analyzedInfoDto.getQuarterlyReportAnalysisInfo();
    }

    public CashFlowAnalysisInfo getAnalyzedCashFlow(String stockID, int years) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        try{
            logger.info("==========================CashFlowEvaluation==================================");
            CashFlowEvaluation.getInstance().evaluate(fundamentalInfoDto,analyzedInfoDto,years);
        }catch(Exception e){
            logger.error("Unable to evaluate ",e);
        }
        return analyzedInfoDto.getCashFlowAnalysisInfo();
    }

    public String quarterlyIntrinsicValuation(String stockID,String requestBody) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);
        try{
            logger.info("==========================QuarterlyIntrinsicValueEvaluation==================================");
            Map<String,String> map = new HashMap<>();
            Gson gson = new Gson();
            map = gson.fromJson(requestBody, map.getClass());
            double avgPE = Double.parseDouble(map.get("peAvg"));
            analyzedInfoDto.getMisleneousAnalysisInfo().setAvg3MotnthPERatio(avgPE);
            StockValuation.getInstance().quarterlyinrinsicValuation(fundamentalInfoDto,analyzedInfoDto);
            return analyzedInfoDto.getTargetPriceEstimationDto().getQuarterlyIntrinsicTargetPrice();
        }catch(Exception e){
            logger.error("Unable to evaluate ",e);
        }

        return null;
    }

    public String getQuarterlyIntrinsicValuation(String stockID) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);

        return analyzedInfoDto.getTargetPriceEstimationDto().getQuarterlyIntrinsicTargetPrice();
    }

    public EVEBITDAValuationModelDto getEVEbitdaValuation(String stockID, int years) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);
        EVEBITDAValuation.getInstance().evaluate(fundamentalInfoDto,analyzedInfoDto,years);
        return analyzedInfoDto.getEvebitdaValuationModelDto();
    }

    public EconomicGrowthDCFDto economicDCFValuation(String stockID,String requestBody) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);
        try{
            logger.info("==========================EconomicDCFValuation==================================");
            Map<String,String> map = new HashMap<>();
            Gson gson = new Gson();
            logger.info("request body: "+requestBody);
            map = gson.fromJson(requestBody, map.getClass());

            double perceptualGrowthRate = Double.parseDouble(map.get("growR"));
            analyzedInfoDto.getEconomicGrowthDCFDto().setPerpertualGrowthRate(perceptualGrowthRate);
//            double iefy = Double.parseDouble(map.get("iefy"));
//            analyzedInfoDto.getEconomicGrowthDCFDto().setIefy(iefy);

            double itefy = Double.parseDouble(map.get("itefy"));
            analyzedInfoDto.getEconomicGrowthDCFDto().setItefy(itefy);

            double ibtfy = Double.parseDouble(map.get("ibtfy"));
            analyzedInfoDto.getEconomicGrowthDCFDto().setIbtfy(ibtfy);

            double rfr = Double.parseDouble(map.get("rfr"));
            analyzedInfoDto.getEconomicGrowthDCFDto().setRfr(rfr);

            double cbeta = Double.parseDouble(map.get("cbeta"));
            analyzedInfoDto.getEconomicGrowthDCFDto().setCbeta(cbeta);

            double mktret = Double.parseDouble(map.get("mktret"));
            analyzedInfoDto.getEconomicGrowthDCFDto().setMktret(mktret);

            double cashEQDCF = Double.parseDouble(map.get("cashEQDCF"));
            analyzedInfoDto.getEconomicGrowthDCFDto().setLasFYCashEquivalent(cashEQDCF);
//            double debt = Double.parseDouble(map.get("debtDCF"));
//            analyzedInfoDto.getEconomicGrowthDCFDto().setLastFYDebt(debt);
            double marginOfSafty = Double.parseDouble(map.get("margR"));
            analyzedInfoDto.getEconomicGrowthDCFDto().setMarginOfSafty(marginOfSafty);
            try {
                EconomicDCFValuation.getInstance().evaluate(fundamentalInfoDto, analyzedInfoDto, 10);
            }catch (Exception e){
                logger.error(e);
            }
            EPSMultiplierValuation.getInstance().evaluate(fundamentalInfoDto,analyzedInfoDto,5);
            return analyzedInfoDto.getEconomicGrowthDCFDto();
        }catch(Exception e){
            logger.error("Unable to evaluate ",e);
        }

        return null;
    }
    public EconomicGrowthDCFDto getEconomicDCFValuation(String stockID) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);

        return analyzedInfoDto.getEconomicGrowthDCFDto();
    }
}
