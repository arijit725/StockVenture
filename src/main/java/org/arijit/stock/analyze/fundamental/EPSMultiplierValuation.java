package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.analysisdto.EPSMutlipliedValuationModelDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.YearlyReportDto;
import org.arijit.stock.analyze.util.FundamentalAnalysisUtil;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.*;
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
        analyzedInfoDto.getEpsMutlipliedValuationModelDto().setEvluated(false);
        double epsTTM = fundamentalInfoDto.getCompanyDto().getTtmeps();
        double growthRate = analyzedInfoDto.getEpsMutlipliedValuationModelDto().getGrowthRate();
        double disCountRate = analyzedInfoDto.getEpsMutlipliedValuationModelDto().getDiscountRate();
        double estimatedPERatio = analyzedInfoDto.getEpsMutlipliedValuationModelDto().getEstimatedPE();
        analyzedInfoDto.getEpsMutlipliedValuationModelDto().setCurrentPrice(fundamentalInfoDto.getCompanyDto().getCurrentSharePrice());
        logger.info("EPS TTM: "+epsTTM+" growthRate: "+growthRate+" discountRate: "+disCountRate+ "Estimated peRatio: "+estimatedPERatio);
        Map<Integer, EPSMutlipliedValuationModelDto.ProjectionDto> projectionDtoMap = analyzedInfoDto.getEpsMutlipliedValuationModelDto().getProjectionDtoMap();
        calcEPSProjection(projectionDtoMap,epsTTM,growthRate);
        logger.info("Projected Valuation with estimated EPS : "+projectionDtoMap);
        caclIntrinsicprojection(projectionDtoMap,estimatedPERatio,disCountRate);
        logger.info("Projected Valuation with estimated Intrinsic Value : "+projectionDtoMap);

        preciseProjection(projectionDtoMap);
        double targetPrice = projectionDtoMap.get(1).getIntrinsicValue();

        analyzedInfoDto.getEpsMutlipliedValuationModelDto().setFinalIntrinsicValue(targetPrice);
        logger.info("Target Price: "+targetPrice);
        analyzedInfoDto.getEpsMutlipliedValuationModelDto().setEvluated(true);
    }

    /**
     * Calculate Intrinsic value in reverse fasion
     * @param projectionDtoMap
     * @param esimatedPE
     * @param discountRate
     */
    private void caclIntrinsicprojection(Map<Integer, EPSMutlipliedValuationModelDto.ProjectionDto> projectionDtoMap,double esimatedPE, double discountRate){
        EPSMutlipliedValuationModelDto.ProjectionDto lastYearProjection = projectionDtoMap.get(5);
        double lastYearIntrinsicValue = lastYearProjection.getTtmEPS()*esimatedPE;
        lastYearProjection.setIntrinsicValue(lastYearIntrinsicValue);
        logger.info("5th year projection: "+lastYearProjection);
        double intrinsicValue = lastYearIntrinsicValue;
        double dRate = (double)discountRate/100;

        for(int i=4;i>0;i--){
            EPSMutlipliedValuationModelDto.ProjectionDto projectionDto = projectionDtoMap.get(i);
            double newIntrinsicValue = intrinsicValue/(1+dRate);
            projectionDto.setIntrinsicValue(newIntrinsicValue);
            intrinsicValue = newIntrinsicValue;
        }
    }
    private void calcEPSProjection(Map<Integer, EPSMutlipliedValuationModelDto.ProjectionDto> projectionDtoMap, double ttmEPS, double growthRate){
        Iterator<Map.Entry<Integer, EPSMutlipliedValuationModelDto.ProjectionDto>> it = projectionDtoMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<Integer, EPSMutlipliedValuationModelDto.ProjectionDto> entry = it.next();
            if(entry.getKey()==1){
                //this is first year, so projected eps  == ttm eps
                entry.getValue().setTtmEPS(ttmEPS);
                continue;
            }
            double projectedEPS = calcEstimatedEPS(ttmEPS,growthRate);
            entry.getValue().setTtmEPS(projectedEPS);
            //update ttmEPS with (n-1) year ttmeps
            ttmEPS = projectedEPS;
        }
    }

    private void preciseProjection(Map<Integer, EPSMutlipliedValuationModelDto.ProjectionDto> projectionDtoMap){
        projectionDtoMap.entrySet().stream().forEach(item->{
            double intrinsicValue = item.getValue().getIntrinsicValue();
            intrinsicValue = Double.parseDouble(StockUtil.convertDoubleToPrecision(intrinsicValue,2));
            item.getValue().setIntrinsicValue(intrinsicValue);

            double eps = item.getValue().getTtmEPS();
            eps = Double.parseDouble(StockUtil.convertDoubleToPrecision(eps,2));
            item.getValue().setTtmEPS(eps);
        });
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
