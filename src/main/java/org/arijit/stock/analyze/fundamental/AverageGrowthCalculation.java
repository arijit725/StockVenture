package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.CashFlowDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.ProfitAndLossDto;
import org.arijit.stock.analyze.util.FundamentalAnalysisUtil;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class AverageGrowthCalculation implements  IFundamentalEvaluation{
    private static Logger logger = LogManager.getLogger(AverageGrowthCalculation.class);

    boolean isEvaluated = false;

    @Override
    public boolean isEvaluated() {
        return isEvaluated;
    }

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        calcFCFGrowthRate(fundamentalInfoDto, analyzedInfoDto);
        calcNetProfitGrowthRate(fundamentalInfoDto,analyzedInfoDto);
    }


    /**
     * Here we are taking last 10 years data, and divide them into progressive 5 years and progressive 3 years chunck,
     * then we calculate CAGR for each chunk and take average
     * This way we avoid sudden growth of any particual chunk and we normalize overall growth rate.
     * While using this growth rate do a cross verification with analyst view.
     * @param fundamentalInfoDto
     * @param analyzedInfoDto
     */
    private void calcFCFGrowthRate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto) {
        List<CashFlowDto> cashFlowDtoList = fundamentalInfoDto.getCashFlowDtoList().stream().limit(10).collect(Collectors.toList());
        List<Double> growthList = new ArrayList<>();
        int startIndex = 0;

        int chunk = 6; //we will take 3 chunk of 5 year period
        while(chunk>0){
            CashFlowDto endDto = cashFlowDtoList.get(startIndex);
            int endIndex = startIndex+4;
            if(endIndex>=cashFlowDtoList.size()){
                logger.info("No more Start year data present to create 5 years period ending at: "+endDto.getDate());
                break;
            }
            CashFlowDto startDto = cashFlowDtoList.get(endIndex);
            if(startDto.getFreeCashFlow()!=0) {
                String period = startDto.getDate() + " to " + endDto.getDate();
                double periodCagr = FundamentalAnalysisUtil.cagr(endDto.getFreeCashFlow(), startDto.getFreeCashFlow(), 5);
                if (!Double.isNaN(periodCagr)) {

                    logger.info("[Calculated CAGR ] [End Date: " + endDto.getDate() + " FCF: " + endDto.getFreeCashFlow() + "] [ Start Date: " + startDto.getDate() + " FCF: " + startDto.getFreeCashFlow() + "] CAGR: " + periodCagr);
                    periodCagr = Double.parseDouble(StockUtil.convertDoubleToPrecision(periodCagr, 3));
                    analyzedInfoDto.getAverageGrowthDto().getFcfGrowthDto().addPeriodGrowth(period, periodCagr);
                    growthList.add(periodCagr);
                }
            }
            startIndex++;
            chunk--;
        }

        //divide into four 3 years period
        startIndex = 0;

        chunk = 9; //we will take 3 chunk of 5 year period
        while(chunk>0){
            CashFlowDto endDto = cashFlowDtoList.get(startIndex);
            int endIndex = startIndex+2;
            if(endIndex>=cashFlowDtoList.size()){
                logger.info("No more Start year data present to create 5 years period ending at: "+endDto.getDate());
                break;
            }
            CashFlowDto startDto = cashFlowDtoList.get(endIndex);
            if(startDto.getFreeCashFlow()!=0) {
                String period = startDto.getDate() + " to " + endDto.getDate();
                double periodCagr = FundamentalAnalysisUtil.cagr(endDto.getFreeCashFlow(), startDto.getFreeCashFlow(), 3);
                if (!Double.isNaN(periodCagr)) {
                    logger.info("[Calculated CAGR ] [End Date: " + endDto.getDate() + " FCF: " + endDto.getFreeCashFlow() + "] [ Start Date: " + startDto.getDate() + " FCF: " + startDto.getFreeCashFlow() + "] CAGR: " + periodCagr);
                    periodCagr = Double.parseDouble(StockUtil.convertDoubleToPrecision(periodCagr, 3));
                    analyzedInfoDto.getAverageGrowthDto().getFcfGrowthDto().addPeriodGrowth(period, periodCagr);
                    growthList.add(periodCagr);
                }
            }
            startIndex++;
            chunk--;
        }

        double averageCagrGrowth = growthList.stream().mapToDouble(item->item).average().getAsDouble();
        averageCagrGrowth = Double.parseDouble(StockUtil.convertDoubleToPrecision(averageCagrGrowth,2));
        logger.info("Average Forcasted FCF growth : "+averageCagrGrowth);
        analyzedInfoDto.getAverageGrowthDto().getFcfGrowthDto().setGrowthRate(averageCagrGrowth);
    }

    private double caclGrowth(double endYear, double startYear){
        double growth = (endYear-startYear)/startYear*100;
        return growth;
    }

    /**
     * Here we are taking last 5 years data, and divide them into progressive 3 years chunck and one 5 years chunk,
     * then we calculate CAGR for each chunk and take average
     * This way we avoid sudden growth of any particual chunk and we normalize overall growth rate.
     * While using this growth rate do a cross verification with analyst view.
     * @param fundamentalInfoDto
     * @param analyzedInfoDto
     */
    private void calcNetProfitGrowthRate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto) {

        List<ProfitAndLossDto> profitAndLossDtoList = fundamentalInfoDto.getProfitAndLossDtoList().stream().limit(5).collect(Collectors.toList());
        List<Double> growthList = new ArrayList<>();
        int startIndex = 0;

        //calcuate 5 years CAGR
        ProfitAndLossDto endDto = profitAndLossDtoList.get(startIndex);
        int endIndex = startIndex+4;
        if(endIndex>=profitAndLossDtoList.size()){
            logger.info("No more Start year data present to create 5 years period ending at: "+endDto.getDate());
        }
        else {
            ProfitAndLossDto startDto = profitAndLossDtoList.get(endIndex);
            String period = startDto.getDate() + " to " + endDto.getDate();
            double periodCagr = Double.NaN;
            int yrCount = 5;
            while(Double.isNaN(periodCagr) && yrCount>=3){
                periodCagr = FundamentalAnalysisUtil.cagr(endDto.getNetProfit(), startDto.getNetProfit(), yrCount);
                yrCount--;
            }
            if(!Double.isNaN(periodCagr)) {
                logger.info("[Calculated CAGR ] [End Date: " + endDto.getDate() + " FCF: " + endDto.getNetProfit() + "] [ Start Date: " + startDto.getDate() + " FCF: " + startDto.getNetProfit() + "] CAGR: " + periodCagr);
                periodCagr = Double.parseDouble(StockUtil.convertDoubleToPrecision(periodCagr, 3));
                analyzedInfoDto.getAverageGrowthDto().getNetProfitGrowthDto().addPeriodGrowth(period, periodCagr);
                growthList.add(periodCagr);
            }

        }

        //divide into four 3 years period
        startIndex = 0;
        int chunk = 9; //we will take 3 chunk of 5 year period
        while(chunk>0){
            endDto = profitAndLossDtoList.get(startIndex);
            endIndex = startIndex+2;
            if(endIndex>=profitAndLossDtoList.size()){
                logger.info("No more Start year data present to create 3 years period ending at: "+endDto.getDate());
                break;
            }
            ProfitAndLossDto startDto = profitAndLossDtoList.get(endIndex);
            String period = startDto.getDate()+" to "+endDto.getDate();
            double periodCagr = FundamentalAnalysisUtil.cagr(endDto.getNetProfit(), startDto.getNetProfit(),3) ;
            if (!Double.isNaN(periodCagr)) {
                logger.info("[Calculated CAGR ] [End Date: " + endDto.getDate() + " FCF: " + endDto.getNetProfit() + "] [ Start Date: " + startDto.getDate() + " FCF: " + startDto.getNetProfit() + "] CAGR: " + periodCagr);
                periodCagr = Double.parseDouble(StockUtil.convertDoubleToPrecision(periodCagr, 3));
                analyzedInfoDto.getAverageGrowthDto().getNetProfitGrowthDto().addPeriodGrowth(period, periodCagr);
                growthList.add(periodCagr);
            }
            startIndex++;
            chunk--;
        }

        double averageCagrGrowth = growthList.stream().mapToDouble(item->item).average().getAsDouble();
        averageCagrGrowth = Double.parseDouble(StockUtil.convertDoubleToPrecision(averageCagrGrowth,2));
        logger.info("Average Forcasted NetProfit growth : "+averageCagrGrowth);
        analyzedInfoDto.getAverageGrowthDto().getNetProfitGrowthDto().setGrowthRate(averageCagrGrowth);
    }

    public static AverageGrowthCalculation getInstance(){
        return InnerClass.getInstance();
    }

    private static class InnerClass{
        private static final AverageGrowthCalculation instance = new AverageGrowthCalculation();
        private  InnerClass(){

        }
        static AverageGrowthCalculation getInstance() {
            return instance;
        }
    }
}
