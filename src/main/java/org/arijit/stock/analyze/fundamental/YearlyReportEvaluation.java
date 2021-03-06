package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.YearlyReportDto;
import org.arijit.stock.analyze.util.FundamentalAnalysisUtil;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class YearlyReportEvaluation implements IFundamentalEvaluation {

    private static final Logger logger = LogManager.getLogger(YearlyReportEvaluation.class);

    private boolean evaluated = false;
    private YearlyReportEvaluation(){

    }

    private void calcEstimatedEPS(FundamentalInfoDto fundamentalInfoDto,AnalyzedInfoDto analyzedInfoDto, int year){
        List<YearlyReportDto> yearlyReportDtoList = fundamentalInfoDto.getYearlyReportDtoList().stream().limit(year).collect(Collectors.toList());
        YearlyReportDto endYearReport =  yearlyReportDtoList.get(0);
        int startYear = year -1;
        int tmpYear = year;
        YearlyReportDto startYearReport = null;
        while(tmpYear>=3) {
            startYearReport = yearlyReportDtoList.get(startYear);// starting from index 0
            if(startYearReport.getBasicEPS()>0)
                break;
            startYear--;
            tmpYear--;
        }
        logger.info("End year YearlyReport: "+endYearReport);
        logger.info("Start year YearlyReport: "+startYearReport);

//        double epsCAGR = FundamentalAnalysisUtil.cagr(endYearReport.getBasicEPS(), startYearReport.getBasicEPS(),year);
//        double estimatedEPS = ((endYearReport.getBasicEPS()*epsCAGR)/100)+endYearReport.getBasicEPS();
        try {
            double estimatedEPS = -1;
            if(tmpYear>=3) {
                //below 3 years we can not calculate estimated EPS
                estimatedEPS = calcEstimatedEPS(endYearReport.getBasicEPS(), startYearReport.getBasicEPS(), tmpYear);
                estimatedEPS = Double.parseDouble(StockUtil.convertDoubleToPrecision(estimatedEPS, 2));
                analyzedInfoDto.getYearlyReportAnalysisInfo().setEstimatedEPSCAGR(estimatedEPS);

                double epsCAGR = FundamentalAnalysisUtil.cagr(endYearReport.getBasicEPS(), startYearReport.getBasicEPS(),year);
                epsCAGR = Double.parseDouble(StockUtil.convertDoubleToPrecision(epsCAGR,2));
                analyzedInfoDto.getYearlyReportAnalysisInfo().setCagrGrowthEstimatedEPS(epsCAGR);

            }
        }catch(Exception e){
            logger.error("unable to calculate estimated EPS",e);
        }

        double avgEPS = yearlyReportDtoList.stream().mapToDouble(mapper->mapper.getEpsGrowthRate()).sum();
        avgEPS = (double) avgEPS/(year-1); // we do not have first year growth
        avgEPS = Double.parseDouble(StockUtil.convertDoubleToPrecision(avgEPS,2));
        analyzedInfoDto.getYearlyReportAnalysisInfo().setAvgGrowthEstimatedEPS(avgEPS);
    }

    public double calcEstimatedEPS(double endingEPS, double startingEPS,int year){
        double epsCAGR = FundamentalAnalysisUtil.cagr(endingEPS, startingEPS,year);
        logger.info("Yearly EPS Growth Rate (CAGR) : "+epsCAGR);
        double estimatedEPS = ((endingEPS*epsCAGR)/100)+endingEPS;
        logger.info("Calculated Estimated EPS: "+estimatedEPS);
        return estimatedEPS;
    }

    private double calcAvgEps(FundamentalInfoDto fundamentalInfoDto, int years){
        double avgEPS = fundamentalInfoDto.getYearlyReportDtoList().stream().limit(years).mapToDouble(mapper->mapper.getBasicEPS()).sum();
        avgEPS = (double) avgEPS/years;
        return avgEPS;
    }
    public void calculateEPSGrowth(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year){
        List<YearlyReportDto> yearlyReportDtoList = fundamentalInfoDto.getYearlyReportDtoList().stream().limit(year).collect(Collectors.toList());
        Iterator<YearlyReportDto> it = yearlyReportDtoList.iterator();
        YearlyReportDto currentYearReport = null;
        while(it.hasNext()){
            if(currentYearReport==null)
                currentYearReport = it.next();
            else{
                YearlyReportDto prevYearReport = it.next();
                double currentEPS = currentYearReport.getBasicEPS()<0?0:currentYearReport.getBasicEPS();
                double prevEPS = prevYearReport.getBasicEPS()<0?0:prevYearReport.getBasicEPS();
//                double epsGrowth = (currentYearReport.getBasicEPS()-prevYearReport.getBasicEPS())/Math.abs(prevYearReport.getBasicEPS());
                double epsGrowth = 0;
                if(prevEPS > 0){
                    epsGrowth = (currentEPS-prevEPS)/Math.abs(prevEPS);
                }
                else{
                    logger.warn("Prev Year EPS is <= 0: "+prevYearReport);
                }
                epsGrowth = (double) epsGrowth*100;
                logger.info("CurrentYear EPS: "+currentYearReport.getBasicEPS()+" PrevYear EPS: "+prevYearReport.getBasicEPS()+" gowth Percentage: "+epsGrowth);

                currentYearReport.setEpsGrowthRate(epsGrowth);
                analyzedInfoDto.getYearlyReportAnalysisInfo().addEpsGrowthRate(currentYearReport.getDate(), StockUtil.convertDoubleToPrecision(epsGrowth,2));
                currentYearReport = prevYearReport;
            }
        }
    }

    @Override
    public boolean isEvaluated() {
        return evaluated;
    }

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        calcEstimatedEPS(fundamentalInfoDto,analyzedInfoDto,year);
        calculateEPSGrowth(fundamentalInfoDto,analyzedInfoDto,year);
        double avgEPS=calcAvgEps(fundamentalInfoDto,year);
        analyzedInfoDto.getYearlyReportAnalysisInfo().setAverageEPS(avgEPS);
        logger.info("Yearly Report Analysis: "+analyzedInfoDto.getYearlyReportAnalysisInfo());
        evaluated = true;
    }

    public static YearlyReportEvaluation getInstance(){
        return InnerClass.getInstance();
    }



    private static class InnerClass{
        private static final YearlyReportEvaluation instance = new YearlyReportEvaluation();
        private  InnerClass(){

        }
        static YearlyReportEvaluation getInstance() {
            return instance;
        }
    }
}
