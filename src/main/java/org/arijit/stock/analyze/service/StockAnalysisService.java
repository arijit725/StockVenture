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

//            double cbeta = Double.parseDouble(map.get("cbeta"));
//            analyzedInfoDto.getEconomicGrowthDCFDto().setCbeta(cbeta);

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



    public WaacDto waacCalculation(String stockID,String requestBody) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);
        try{
            logger.info("==========================WaacCalculation==================================");
            Map<String,String> map = new HashMap<>();
            Gson gson = new Gson();
            logger.info("request body: "+requestBody);
            map = gson.fromJson(requestBody, map.getClass());

            double itefy = Double.parseDouble(map.get("incomeTaxExpense"));
            analyzedInfoDto.getWaacDto().setIncomeTaxExpense(itefy);

            double ibtfy = Double.parseDouble(map.get("incomeBeforeTax"));
            analyzedInfoDto.getWaacDto().setIncomeBeforeTax(ibtfy);

            double rfr = Double.parseDouble(map.get("riskFreeRate"));
            analyzedInfoDto.getWaacDto().setRiskFreeRate(rfr);

            double mktret = Double.parseDouble(map.get("marketReturn"));
            analyzedInfoDto.getWaacDto().setMarketReturn(mktret);

            try {
                WaacCalculation.getInstance().evaluate(fundamentalInfoDto, analyzedInfoDto, 10);
            }catch (Exception e){
                logger.error(e);
            }
            return analyzedInfoDto.getWaacDto();
        }catch(Exception e){
            logger.error("Unable to evaluate ",e);
        }

        return null;
    }
    public WaacDto getWaacCalculation(String stockID) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);

        return analyzedInfoDto.getWaacDto();
    }

    public PEValuationModelDto peValuation(String stockID,String requestBody) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);
        try{
            logger.info("==========================PEValuation==================================");
            Gson gson = new Gson();
            logger.info("request body: "+requestBody);
            PEValuationModelDto peValuationModelDto = gson.fromJson(requestBody, PEValuationModelDto.class);
            logger.info("PEValuationModelDto: "+peValuationModelDto);
            analyzedInfoDto.getPeValuationModelDto().setStockPriceList(peValuationModelDto.getStockPriceList());
            analyzedInfoDto.getPeValuationModelDto().setEpsMap(peValuationModelDto.getEpsMap());
            analyzedInfoDto.getPeValuationModelDto().setMarketGrowth(peValuationModelDto.getMarketGrowth());
            analyzedInfoDto.getPeValuationModelDto().setGrowthApproach(peValuationModelDto.getGrowthApproach());
            try {
                PEValuation.getInstance().evaluate(fundamentalInfoDto, analyzedInfoDto, 7);
            }catch (Exception e){
                logger.error(e);
            }
            return analyzedInfoDto.getPeValuationModelDto();
        }catch(Exception e){
            logger.error("Unable to evaluate ",e);
        }

        return null;
    }

    public PEValuationModelDto getPEValuation(String stockID) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);

        return analyzedInfoDto.getPeValuationModelDto();
    }


    public EPSMutlipliedValuationModelDto epsMultiplierValuation(String stockID,String requestBody) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);
        try{
            logger.info("==========================EPSMultiplierValuation==================================");
            Gson gson = new Gson();
            logger.info("request body: "+requestBody);
            EPSMutlipliedValuationModelDto epsMutlipliedValuationModelDto = gson.fromJson(requestBody, EPSMutlipliedValuationModelDto.class);
            logger.info("EPSMutlipliedValuationModelDto: "+epsMutlipliedValuationModelDto);
            analyzedInfoDto.getEpsMutlipliedValuationModelDto().setGrowthRate(epsMutlipliedValuationModelDto.getGrowthRate());
            analyzedInfoDto.getEpsMutlipliedValuationModelDto().setDiscountRate(epsMutlipliedValuationModelDto.getDiscountRate());
            analyzedInfoDto.getEpsMutlipliedValuationModelDto().setEstimatedPE(epsMutlipliedValuationModelDto.getEstimatedPE());
            try {
                EPSMultiplierValuation.getInstance().evaluate(fundamentalInfoDto, analyzedInfoDto, 7);
            }catch (Exception e){
                logger.error(e);
            }
            return analyzedInfoDto.getEpsMutlipliedValuationModelDto();
        }catch(Exception e){
            logger.error("Unable to evaluate ",e);
        }

        return null;
    }

    public EPSMutlipliedValuationModelDto getEPSMultiplierValuation(String stockID) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);

        return analyzedInfoDto.getEpsMutlipliedValuationModelDto();
    }


    public NetProfitValuationModelDto netProfitValuation(String stockID,String requestBody) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);
        try{
            logger.info("==========================NetProfitValuation==================================");
            Gson gson = new Gson();
            logger.info("request body: "+requestBody);
            NetProfitValuationModelDto netProfitValuationModelDto = gson.fromJson(requestBody, NetProfitValuationModelDto.class);
            logger.info("NetProfitValuationModelDto: "+netProfitValuationModelDto);
            analyzedInfoDto.getNetProfitValuationModelDto().setDiscountRate(netProfitValuationModelDto.getDiscountRate());
            analyzedInfoDto.getNetProfitValuationModelDto().setNetprofitMap(netProfitValuationModelDto.getNetprofitMap());
            analyzedInfoDto.getNetProfitValuationModelDto().setEstimatedPE(netProfitValuationModelDto.getEstimatedPE());
            try {
                NetProfitValuation.getInstance().evaluate(fundamentalInfoDto, analyzedInfoDto, 7);
            }catch (Exception e){
                logger.error(e);
            }
            return analyzedInfoDto.getNetProfitValuationModelDto();
        }catch(Exception e){
            logger.error("Unable to evaluate ",e);
        }

        return null;
    }

    public NetProfitValuationModelDto getNetProfitValuation(String stockID) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);

        return analyzedInfoDto.getNetProfitValuationModelDto();
    }

    public DCFTwoPhaseValuationModelDto dcf2PhaseValuation(String stockID,String requestBody) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);
        try{
            logger.info("==========================DCFTwoPhaseValuation==================================");
            Gson gson = new Gson();
            logger.info("request body: "+requestBody);
            DCFTwoPhaseValuationModelDto dcfTwoPhaseValuationModelDto = gson.fromJson(requestBody, DCFTwoPhaseValuationModelDto.class);
            logger.info("DCFTwoPhaseValuationModelDto: "+dcfTwoPhaseValuationModelDto);
            analyzedInfoDto.getDcfTwoPhaseValuationModelDto().setDiscountRate(dcfTwoPhaseValuationModelDto.getDiscountRate());
            analyzedInfoDto.getDcfTwoPhaseValuationModelDto().setGrowthRate1(dcfTwoPhaseValuationModelDto.getGrowthRate1());
            analyzedInfoDto.getDcfTwoPhaseValuationModelDto().setGrowthRate2(dcfTwoPhaseValuationModelDto.getGrowthRate2());
            analyzedInfoDto.getDcfTwoPhaseValuationModelDto().setTerminalGrowthRate(dcfTwoPhaseValuationModelDto.getTerminalGrowthRate());
            analyzedInfoDto.getDcfTwoPhaseValuationModelDto().setFcfMap(dcfTwoPhaseValuationModelDto.getFcfMap());
            try {
                DCFTwoPhaseValuation.getInstance().evaluate(fundamentalInfoDto, analyzedInfoDto, 7);
            }catch (Exception e){
                logger.error(e);
            }
            return analyzedInfoDto.getDcfTwoPhaseValuationModelDto();
        }catch(Exception e){
            logger.error("Unable to evaluate ",e);
        }

        return null;
    }

    public BenjaminGrahamValuationModelDto getGrahamValuation(String stockID) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);

        return analyzedInfoDto.getBenjaminGrahamValuationModelDto();
    }


    public BenjaminGrahamValuationModelDto grahamValuation(String stockID,String requestBody) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);
        try{
            logger.info("==========================BenjaminGrahamValuation==================================");
            Gson gson = new Gson();
            logger.info("request body: "+requestBody);
            BenjaminGrahamValuationModelDto benjaminGrahamValuationModelDto = gson.fromJson(requestBody, BenjaminGrahamValuationModelDto.class);
            logger.info("DCFTwoPhaseValuationModelDto: "+benjaminGrahamValuationModelDto);
            analyzedInfoDto.getBenjaminGrahamValuationModelDto().setGrowthRate(benjaminGrahamValuationModelDto.getGrowthRate());
            analyzedInfoDto.getBenjaminGrahamValuationModelDto().setAaaBondY(benjaminGrahamValuationModelDto.getAaaBondY());
            analyzedInfoDto.getBenjaminGrahamValuationModelDto().setPeZero(benjaminGrahamValuationModelDto.getPeZero());
            analyzedInfoDto.getBenjaminGrahamValuationModelDto().setRepoRate(benjaminGrahamValuationModelDto.getRepoRate());

            try {
                BenjaminGrahamValuation.getInstance().evaluate(fundamentalInfoDto, analyzedInfoDto, 7);
            }catch (Exception e){
                logger.error(e);
            }
            return analyzedInfoDto.getBenjaminGrahamValuationModelDto();
        }catch(Exception e){
            logger.error("Unable to evaluate ",e);
        }

        return null;
    }

    public DCFTwoPhaseValuationModelDto getDCF2PhaseValuation(String stockID) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);

        return analyzedInfoDto.getDcfTwoPhaseValuationModelDto();
    }
    public AverageGrowthDto getAverageGrowthRate(String stockID) throws Exception {
        FundamentalInfoDto fundamentalInfoDto = MemCache.getInstance().getDetails(stockID);
        if(fundamentalInfoDto==null)
            throw new Exception("could not find stock");
        AnalyzedInfoDto analyzedInfoDto = MemCache.getInstance().getAnalyzedDetails(stockID);
        if(analyzedInfoDto==null)
            throw new Exception("Could not find analysis for stock with id: "+stockID);
        AverageGrowthCalculation.getInstance().evaluate(fundamentalInfoDto,analyzedInfoDto,-1);
        return analyzedInfoDto.getAverageGrowthDto();
    }
}
