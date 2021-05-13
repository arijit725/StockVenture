package org.arijit.stock.analyze.service;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.analysisdto.QuarterlyReportAnalysisInfo;
import org.arijit.stock.analyze.analysisdto.RatioAnalysisInfo;
import org.arijit.stock.analyze.analysisdto.YearlyReportAnalysisInfo;
import org.arijit.stock.analyze.cache.MemCache;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.fundamental.QuarterlyReportEvaluation;
import org.arijit.stock.analyze.fundamental.RatiosEvaluation;
import org.arijit.stock.analyze.fundamental.StockValuation;
import org.arijit.stock.analyze.fundamental.YearlyReportEvaluation;
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
}
