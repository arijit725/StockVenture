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

public class YearlyReportEvaluation implements IFundamentalEvaluation {

    private static final Logger logger = LogManager.getLogger(YearlyReportEvaluation.class);

    private boolean evaluated = false;
    private YearlyReportEvaluation(){

    }

    private void calcEstimatedEPS(FundamentalInfoDto fundamentalInfoDto,AnalyzedInfoDto analyzedInfoDto, int year){
        List<YearlyReportDto> yearlyReportDtoList = fundamentalInfoDto.getYearlyReportDtoList();
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
        double estimatedEPS = -1;
        if(tmpYear>=3)
            estimatedEPS = calcEstimatedEPS(endYearReport.getBasicEPS(),startYearReport.getBasicEPS(),tmpYear);
        analyzedInfoDto.getYearlyReportAnalysisInfo().setEstimatedEPSCAGR(estimatedEPS);
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
    public void calculateEPSGrowth(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto){
        List<YearlyReportDto> yearlyReportDtoList = fundamentalInfoDto.getYearlyReportDtoList();
        Iterator<YearlyReportDto> it = yearlyReportDtoList.iterator();
        YearlyReportDto currentYearReport = null;
        while(it.hasNext()){
            if(currentYearReport==null)
                currentYearReport = it.next();
            else{
                YearlyReportDto prevYearReport = it.next();
                double epsGrowth = (currentYearReport.getBasicEPS()-prevYearReport.getBasicEPS())/Math.abs(prevYearReport.getBasicEPS());
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
        calculateEPSGrowth(fundamentalInfoDto,analyzedInfoDto);
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
