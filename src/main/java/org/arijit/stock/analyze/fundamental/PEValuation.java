package org.arijit.stock.analyze.fundamental;

import io.swagger.models.auth.In;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.analysisdto.EconomicGrowthDCFDto;
import org.arijit.stock.analyze.analysisdto.PEValuationModelDto;
import org.arijit.stock.analyze.dto.BalanceSheetDto;
import org.arijit.stock.analyze.dto.CashFlowDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.YearlyReportDto;
import org.arijit.stock.analyze.enums.ValuationEnums;
import org.arijit.stock.analyze.util.DateUtil;
import org.arijit.stock.analyze.util.FundamentalAnalysisUtil;
import org.arijit.stock.analyze.util.StockUtil;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * PE valuation Model is calculated based on last 7 years data.
 */
public class PEValuation implements  IFundamentalEvaluation{

    private static Logger logger = LogManager.getLogger(PEValuation.class);

    boolean isEvaluated = false;

    @Override
    public boolean isEvaluated() {
        return isEvaluated;
    }

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {

        logger.info("Evaluating PEValuation");
        calcMonthlyEPSPE(fundamentalInfoDto,analyzedInfoDto);
        calcAveragePE(fundamentalInfoDto,analyzedInfoDto);
        calcPriceEstimation(fundamentalInfoDto,analyzedInfoDto);
    }


    private double calcEPSGrowth(PEValuationModelDto peValuationModelDto){
        Map<String, Double> epsMap  = peValuationModelDto.getEpsMap();
        TreeMap<String, Double> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                try {
                    long d1 = DateUtil.convertToEpochMilli(o1);
                    long d2 = DateUtil.convertToEpochMilli(o2);
                    return Long.compare(d2, d1);
                } catch (ParseException e) {
                    logger.error("Unable to convert Dates to ecpochInMillis: ", e);
                }
                return 0;
            }
        });
        epsMap = epsMap.entrySet().stream().filter(item->item.getValue()!=null).collect(Collectors.toMap(
                                                                                                Map.Entry::getKey,
                                                                                                Map.Entry::getValue));
        map.putAll(epsMap);
        logger.info("EPS Map After sorting: "+map);
        Iterator<Map.Entry<String, Double>> it = map.entrySet().iterator();
        Map.Entry<String,Double> currentYearEPS = null;
        Map.Entry<String,Double> startYearEPS = null;
        Map.Entry<String,Double> endYearEPS = null;
        Map<String,Double> growthMap = new HashMap<>();
        while(it.hasNext()) {
            if (currentYearEPS == null) {
                currentYearEPS = it.next();
                endYearEPS = currentYearEPS;

            } else {
                Map.Entry<String, Double> prevYearEPS = it.next();
                if (prevYearEPS.getValue() > 0) {
                    double growth = (currentYearEPS.getValue() - prevYearEPS.getValue()) / prevYearEPS.getValue();
                    growth = growth * 100;
                    logger.info("[Average Growth] Start Year: " + prevYearEPS + " End Year: " + currentYearEPS + " Growth %: " + growth);
                    growthMap.put(currentYearEPS.getKey(), growth);
                }
                currentYearEPS = prevYearEPS;
                startYearEPS = prevYearEPS;
            }
        }
        logger.info("EPS GrowthMap : "+growthMap);
        double averageEPSGrowth = growthMap.entrySet().stream().mapToDouble(item->item.getValue()).average().getAsDouble();
        int year = epsMap.size();
        double cagrGrowth = FundamentalAnalysisUtil.cagr(endYearEPS.getValue(),startYearEPS.getValue(),year);
        logger.info("End Year EPS: "+endYearEPS+"Star Year EPS: "+startYearEPS+" year: "+year+" CAGR Growth: "+cagrGrowth);
        String growthApproach = peValuationModelDto.getGrowthApproach();
        switch(growthApproach){
            case "CAGR":
                return cagrGrowth;
            case "Average":
                return averageEPSGrowth;
        }
        return cagrGrowth;
    }

    private void calcPriceEstimation(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto){
        List<YearlyReportDto> yearlyReportDtoList = fundamentalInfoDto.getYearlyReportDtoList();
//        yearlyReportDtoList.stream().forEach(report->logger.info(" Date: "+report.getDate()+" EPS Growth Rate: "+report.getEpsGrowthRate()));
//        double avgEPSGrowth = yearlyReportDtoList.stream().mapToDouble(mapper->mapper.getEpsGrowthRate()).sum();
//        avgEPSGrowth = (double) avgEPSGrowth/(yearlyReportDtoList.size() -1);
        double avgEPSGrowth = calcEPSGrowth(analyzedInfoDto.getPeValuationModelDto());
        logger.info("Average EPS Growth: "+avgEPSGrowth);
        double shortenAvgEPSGrowth = Double.parseDouble(StockUtil.convertDoubleToPrecision(avgEPSGrowth,2));
        analyzedInfoDto.getPeValuationModelDto().setEPSGrowthRate(shortenAvgEPSGrowth);
        YearlyReportDto lastyearlyReportDto = yearlyReportDtoList.get(0);
        logger.info("Last FY EPS: date: "+lastyearlyReportDto.getDate()+" eps: "+lastyearlyReportDto.getBasicEPS());
        String marketGrowth = analyzedInfoDto.getPeValuationModelDto().getMarketGrowth();
        if(marketGrowth.equalsIgnoreCase("Average")) {
            avgEPSGrowth = avgEPSGrowth / 2;
            logger.info("Adjusted Average EPS growth based on Market Growth: "+avgEPSGrowth);
        }

        double estimatedEPS = lastyearlyReportDto.getBasicEPS()*(1+(double)avgEPSGrowth/100);
        logger.info("Estimated EPS: "+estimatedEPS);
        estimatedEPS = Double.parseDouble(StockUtil.convertDoubleToPrecision(estimatedEPS,2));
        analyzedInfoDto.getPeValuationModelDto().setEstimatedEPS(estimatedEPS);

        double targetPrice7yrPE = analyzedInfoDto.getPeValuationModelDto().getAvgPE7Years()*estimatedEPS;
        double targetPrice4YrPE = analyzedInfoDto.getPeValuationModelDto().getAvgPE4Years()*estimatedEPS;

        double fairValueTargetPrice = (targetPrice4YrPE+targetPrice7yrPE)/2;

        targetPrice4YrPE = Double.parseDouble(StockUtil.convertDoubleToPrecision(targetPrice4YrPE,2));
        targetPrice7yrPE = Double.parseDouble(StockUtil.convertDoubleToPrecision(targetPrice7yrPE,2));
        fairValueTargetPrice = Double.parseDouble(StockUtil.convertDoubleToPrecision(fairValueTargetPrice,2));

        analyzedInfoDto.getPeValuationModelDto().setTargetPrice4yrPE(targetPrice4YrPE);
        analyzedInfoDto.getPeValuationModelDto().setTargetPrice7yrPE(targetPrice7yrPE);
        analyzedInfoDto.getPeValuationModelDto().setFairValuedTargetPrice(fairValueTargetPrice);

        analyzedInfoDto.getPeValuationModelDto().setCurrentSharePrice(fundamentalInfoDto.getCompanyDto().getCurrentSharePrice());
        logger.info("Target Price with 7Years PE: "+targetPrice7yrPE+" Target Price with 4Years PE: "+targetPrice4YrPE+" Fair value Target Price: "+fairValueTargetPrice);
        analyzedInfoDto.getPeValuationModelDto().setEvluated(true);

    }

    private void calcAveragePE(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto){
        PEValuationModelDto peValuationModelDto = analyzedInfoDto.getPeValuationModelDto();
        List<PEValuationModelDto.StockPrice> stockPriceList = peValuationModelDto.getStockPriceList();
        Collections.sort(stockPriceList,Collections.reverseOrder());
        logger.info("StockPriceList size: "+stockPriceList.size());
        stockPriceList.stream().forEach(item->logger.info("Stockprice: "+item));
        int yearsToMonth7 = 12*7 ;
        double avg7YearsPE = stockPriceList.stream().limit(yearsToMonth7).mapToDouble(item->item.getPe()).average().getAsDouble();
        int yearsToMonth4 = 12*4;
        double avg4YearsPE = stockPriceList.stream().limit(yearsToMonth4).mapToDouble(item->item.getPe()).average().getAsDouble();

        logger.info("Average PE: 7years: "+avg7YearsPE+" 4 years: "+avg4YearsPE);
        avg4YearsPE = Double.parseDouble(StockUtil.convertDoubleToPrecision(avg4YearsPE,2));
        avg7YearsPE = Double.parseDouble(StockUtil.convertDoubleToPrecision(avg7YearsPE,2));

        peValuationModelDto.setAvgPE4Years(avg4YearsPE);
        peValuationModelDto.setAvgPE7Years(avg7YearsPE);

    }


    private void calcMonthlyEPSPE(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto) throws ParseException {
        PEValuationModelDto peValuationModelDto = analyzedInfoDto.getPeValuationModelDto();

        List<PEValuationModelDto.StockPrice> stockPriceList = peValuationModelDto.getStockPriceList();

        Map<String, Double> epsMap = peValuationModelDto.getEpsMap();

        Iterator<PEValuationModelDto.StockPrice> it = stockPriceList.iterator();

        while(it.hasNext()){
            PEValuationModelDto.StockPrice stockPrice = it.next();
            String date = stockPrice.getDate();
            long epochDate = DateUtil.convertToEpochMilli(date);
            String yearStr = date.split("-")[1];
            int year = Integer.parseInt(yearStr);
            String fyDate = "Mar-"+year;
            long fyEpoch = DateUtil.convertToEpochMilli(fyDate);
            while(fyEpoch<epochDate){
                year++;
                fyDate = "Mar-"+year;
                fyEpoch = DateUtil.convertToEpochMilli(fyDate);
            }
            logger.info("Stock Date:"+date +" FY Date: "+fyDate);
            Double eps = epsMap.get(fyDate);
            //now there could be chance that if you are middle of current financial year, you will not get any eps for current FY.
            // fot that use TTM EPS for the current FY
            if(eps==null){
                eps = analyzedInfoDto.getQuarterlyReportAnalysisInfo().getTtmEPS();
                logger.info("Using TTM EPS for current FY: eps: "+eps);
            }
            stockPrice.setFyEPS(eps);
            double pe = stockPrice.getClose()/eps;
            pe= Double.parseDouble(StockUtil.convertDoubleToPrecision(pe,2));
            stockPrice.setPe(pe);
            logger.info("Stock Date:"+date +" FY Date: "+fyDate+"StockPrice: "+stockPrice.getClose()+" EPS: "+eps +" PE: "+pe);
        }
    }


    public static PEValuation getInstance(){
        return Inner.getInstance();
    }



    private static class Inner{
        private static final PEValuation instance = new PEValuation();
        private  Inner(){

        }
        static PEValuation getInstance() {
            return instance;
        }
    }

}
