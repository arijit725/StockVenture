package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.YearlyReportDto;
import org.arijit.stock.analyze.util.FundamentalAnalysisUtil;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EPSMultiplierValuation implements  IFundamentalEvaluation {

    private static Logger logger = LogManager.getLogger(EPSMultiplierValuation.class);

    private boolean evaluated = false;
    @Override
    public boolean isEvaluated() {
        return evaluated;
    }

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        double epsTTM = fundamentalInfoDto.getCompanyDto().getTtmeps();
        double growthRate = calcGrowthRate(fundamentalInfoDto,analyzedInfoDto,year);
        double disCountRate = analyzedInfoDto.getEconomicGrowthDCFDto().getDiscountRate();

        double peRatio = analyzedInfoDto.getRatioAnalysisInfo().getForwardPEAnalysis().getForwardPE();
        double marginOfSafty = analyzedInfoDto.getEconomicGrowthDCFDto().getMarginOfSafty();

        logger.info("EPS TTM: "+epsTTM+" growthRate: "+growthRate+" discountRate: "+disCountRate+ "Forward peRatio: "+peRatio);

        List<ProjectedValuation> projectedValuationList = new ArrayList<>(5);
        double presentEPS = epsTTM;
        for(int i=0;i<5;i++){
            int j = i+1;
            ProjectedValuation projectedValuation = new ProjectedValuation();
            projectedValuation.year=j;
            projectedValuation.estimatedEPS = calcEstimatedEPS(presentEPS,growthRate);
            presentEPS = projectedValuation.estimatedEPS;
            projectedValuationList.add(projectedValuation);
        }
        logger.info("Projected Valuation with estimated EPS : "+projectedValuationList);
        //at this point presentEPS must be updated with 5th year estimated eps.
        double estimatedEPS = presentEPS;
        for(int i=projectedValuationList.size()-1;i>=0;i--){
            ProjectedValuation projectedValuation = projectedValuationList.get(i);
            double terminalValuation = projectedValuation.estimatedEPS * peRatio;
            terminalValuation =  terminalValuation /(1+disCountRate);
            projectedValuation.intrinsicValue = terminalValuation;
        }

        logger.info("Projected Valuation with Intrinsic Value :"+projectedValuationList);
        double targetPrice = projectedValuationList.get(0).intrinsicValue;

        double mrgTargetPrice = targetPrice - (targetPrice*marginOfSafty/100);
        logger.info("Target Price: "+targetPrice+" After Margin of Safty: "+mrgTargetPrice);
    }

    /**
     * Calculate estimated eps
     */
    public double calcEstimatedEPS(double presentValue, double growthRate){
        double projectedValue = presentValue*(1+(growthRate/100));
        return projectedValue;
    }
    /**
     * Here we will calculate CAGR growth rate of eps for last 5 years.
     * Also we will assume that for next 5 years, this growth rate will be carried on.
     * @param fundamentalInfoDto
     * @param analyzedInfoDto
     * @param year
     * @return
     */
    private double calcGrowthRate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year){
        List<YearlyReportDto> yearlyReportDtoList = fundamentalInfoDto.getYearlyReportDtoList().stream().limit(year).collect(Collectors.toList());
        YearlyReportDto endingYearReport= yearlyReportDtoList.get(0);//index starts from 0
        YearlyReportDto startingYearReport = yearlyReportDtoList.get(year-1);
        double cagrEPS = FundamentalAnalysisUtil.cagr(endingYearReport.getBasicEPS(), startingYearReport.getBasicEPS(),year);
        return cagrEPS;
    }

    private static class ProjectedValuation{
        int year;
        double estimatedEPS;
        double intrinsicValue;

        @Override
        public String toString() {
            return "ProjectedValuation{" +
                    "year=" + year +
                    ", estimatedEPS=" + estimatedEPS +
                    ", intrinsicValue=" + intrinsicValue +
                    '}';
        }
    }

    public static EPSMultiplierValuation getInstance(){
        return Inner.getInstance();
    }



    private static class Inner{
        private static final EPSMultiplierValuation instance = new EPSMultiplierValuation();
        private  Inner(){

        }
        static EPSMultiplierValuation getInstance() {
            return instance;
        }
    }
}
