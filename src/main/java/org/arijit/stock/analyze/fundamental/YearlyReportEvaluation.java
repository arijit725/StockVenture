package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.YearlyReportDto;
import org.arijit.stock.analyze.util.FundamentalAnalysisUtil;

import java.util.List;

public class YearlyReportEvaluation implements IFundamentalEvaluation {

    private static final Logger logger = LogManager.getLogger(YearlyReportEvaluation.class);

    private YearlyReportEvaluation(){

    }

    private void calcEstimatedEPS(FundamentalInfoDto fundamentalInfoDto,AnalyzedInfoDto analyzedInfoDto, int year){
        List<YearlyReportDto> yearlyReportDtoList = fundamentalInfoDto.getYearlyReportDtoList();
        YearlyReportDto endYearReport =  yearlyReportDtoList.get(0);
        YearlyReportDto startYearReport = yearlyReportDtoList.get(year-1);// starting from index 0
        logger.info("End year YearlyReport: "+endYearReport);
        logger.info("Start year YearlyReport: "+startYearReport);

//        double epsCAGR = FundamentalAnalysisUtil.cagr(endYearReport.getBasicEPS(), startYearReport.getBasicEPS(),year);
//        double estimatedEPS = ((endYearReport.getBasicEPS()*epsCAGR)/100)+endYearReport.getBasicEPS();
        double estimatedEPS = calcEstimatedEPS(endYearReport.getBasicEPS(),startYearReport.getBasicEPS(),year);
        logger.info("Estimated EPS: "+estimatedEPS);
        analyzedInfoDto.getYearlyReportAnalysisInfo().setEstimatedEPS(estimatedEPS);

    }

    public double calcEstimatedEPS(double endingEPS, double startingEPS,int year){
        double epsCAGR = FundamentalAnalysisUtil.cagr(endingEPS, startingEPS,year);

        double estimatedEPS = ((endingEPS*epsCAGR)/100)+endingEPS;
        logger.info("Calculated Estimated EPS: "+estimatedEPS);
        return estimatedEPS;
    }
    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        calcEstimatedEPS(fundamentalInfoDto,analyzedInfoDto,year);
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
