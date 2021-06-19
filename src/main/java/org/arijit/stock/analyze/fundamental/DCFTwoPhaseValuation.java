package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.analysisdto.DCFTwoPhaseValuationModelDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.util.DateUtil;
import org.arijit.stock.analyze.util.FundamentalAnalysisUtil;
import org.arijit.stock.analyze.util.StockUtil;

import java.text.ParseException;
import java.util.*;

public class DCFTwoPhaseValuation implements  IFundamentalEvaluation {

    private static Logger logger = LogManager.getLogger(DCFTwoPhaseValuation.class);

    private boolean evaluated = false;
    @Override
    public boolean isEvaluated() {
        return evaluated;
    }

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        analyzedInfoDto.getNetProfitValuationModelDto().setEvluated(false);
        double averageCashFlow = calcAverageCashFlow(fundamentalInfoDto,analyzedInfoDto);
        double year10EstimatedFCF = calcEstimatedFCF(fundamentalInfoDto, analyzedInfoDto, averageCashFlow);
        calcPVCashFlow(fundamentalInfoDto, analyzedInfoDto,year10EstimatedFCF);

        double numberOfOutStandingShare = fundamentalInfoDto.getCompanyDto().getMarketCap()/fundamentalInfoDto.getCompanyDto().getCurrentSharePrice();
        double targetPrice = analyzedInfoDto.getDcfTwoPhaseValuationModelDto().getDiscountedMarketCap()/numberOfOutStandingShare;
        numberOfOutStandingShare = Double.parseDouble(StockUtil.convertDoubleToPrecision(numberOfOutStandingShare,2));
        analyzedInfoDto.getDcfTwoPhaseValuationModelDto().setNumberOfOutstandingShare(numberOfOutStandingShare);
        targetPrice = Double.parseDouble(StockUtil.convertDoubleToPrecision(targetPrice,2));
        logger.info("Target Price: "+targetPrice);
        analyzedInfoDto.getDcfTwoPhaseValuationModelDto().setFinalIntrinsicValue(targetPrice);
        analyzedInfoDto.getDcfTwoPhaseValuationModelDto().setEvluated(true);
    }

    private void calcPVCashFlow(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto,double year10EstimatedFCF){

        Map<Integer,DCFTwoPhaseValuationModelDto.ProjectionDto> projectionDtoMap = analyzedInfoDto.getDcfTwoPhaseValuationModelDto()
                .getProjectionDtoMap();
        double sumofPV = projectionDtoMap.entrySet().stream().map(item->item.getValue()).mapToDouble(item->item.getPvFCF()).sum();
        analyzedInfoDto.getDcfTwoPhaseValuationModelDto().setSumOfPV(Double.parseDouble(StockUtil.convertDoubleToPrecision(sumofPV,2)));
        logger.info("Sum of PV of FCF: "+sumofPV);
        double terminalGrowthRate = analyzedInfoDto.getDcfTwoPhaseValuationModelDto().getTerminalGrowthRate();
        double discountRate = analyzedInfoDto.getDcfTwoPhaseValuationModelDto().getDiscountRate();
        double neumerator = 1 + (terminalGrowthRate/100);
        double denominator = (discountRate/100)-(terminalGrowthRate/100);
        double terminalValue = year10EstimatedFCF*(neumerator)/denominator;
        logger.info("Terminal Value: "+terminalValue);
        analyzedInfoDto.getDcfTwoPhaseValuationModelDto().setTerminalValue(Double.parseDouble(StockUtil.convertDoubleToPrecision(terminalValue,2)));
        double totalPVofCashFlow = sumofPV + terminalValue;
//        double equityValue = totalPVofCashFlow + cash - debt;
        logger.info("Total PV of CashFlow: "+totalPVofCashFlow);
        analyzedInfoDto.getDcfTwoPhaseValuationModelDto().setTotalPV(Double.parseDouble(StockUtil.convertDoubleToPrecision(totalPVofCashFlow,2)));
        analyzedInfoDto.getDcfTwoPhaseValuationModelDto().setDiscountedMarketCap(Double.parseDouble(StockUtil.convertDoubleToPrecision(totalPVofCashFlow,2)));
    }
    private double calcEstimatedFCF(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, double averageCashFlow){

        double finalYearEstimatedFCF = 0;
        double prevCashFlow = averageCashFlow;
        double discountRate = analyzedInfoDto.getDcfTwoPhaseValuationModelDto().getDiscountRate();
        double growthRate1 = (double)analyzedInfoDto.getDcfTwoPhaseValuationModelDto().getGrowthRate1()/100;
        //calculateing estimated FCF based on growth rate 1
        for(int i=1;i<=5;i++){
            double cashFlow = (prevCashFlow * growthRate1) + prevCashFlow;
            analyzedInfoDto.getDcfTwoPhaseValuationModelDto().addProjectedFCF(i, cashFlow);
            analyzedInfoDto.getDcfTwoPhaseValuationModelDto().addGrwothRate(i,analyzedInfoDto.getDcfTwoPhaseValuationModelDto().getGrowthRate1());

            double denominator = 1+(discountRate/100);
            denominator = Math.pow(denominator,i);
            double cashFlowPV = cashFlow/denominator;
            analyzedInfoDto.getDcfTwoPhaseValuationModelDto().addPVOfProjectedFCF(i,cashFlowPV);
            prevCashFlow = cashFlow;
        }
        //calculateing estimated FCF based on growth rate 2
        double growthRate2 = (double)analyzedInfoDto.getDcfTwoPhaseValuationModelDto().getGrowthRate2()/100;
        for(int i=6;i<=10;i++){
            double cashFlow = (prevCashFlow * growthRate2) + prevCashFlow;
            analyzedInfoDto.getDcfTwoPhaseValuationModelDto().addProjectedFCF(i, cashFlow);
            analyzedInfoDto.getDcfTwoPhaseValuationModelDto().addGrwothRate(i,analyzedInfoDto.getDcfTwoPhaseValuationModelDto().getGrowthRate2());

            double denominator = 1+(discountRate/100);
            denominator = Math.pow(denominator,i);
            double cashFlowPV = cashFlow/denominator;
            analyzedInfoDto.getDcfTwoPhaseValuationModelDto().addPVOfProjectedFCF(i,cashFlowPV);
            prevCashFlow = cashFlow;
        }
        logger.info("Projected PV Dto: "+analyzedInfoDto.getDcfTwoPhaseValuationModelDto().getProjectionDtoMap());
        finalYearEstimatedFCF = prevCashFlow;
        logger.info("10th Year FCF: "+finalYearEstimatedFCF);
        analyzedInfoDto.getDcfTwoPhaseValuationModelDto().setYear10FCF(Double.parseDouble(StockUtil.convertDoubleToPrecision(finalYearEstimatedFCF,2)));
        return finalYearEstimatedFCF;
    }

    private double calcAverageCashFlow(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto){
        Map<Long, Double> fcfMap= new TreeMap<>(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o2.compareTo(o1);
            }
        });
        analyzedInfoDto.getDcfTwoPhaseValuationModelDto().getFcfMap().entrySet().stream().forEach(item->{
            try {
                long epoch = DateUtil.convertToEpochMilli(item.getKey());
                fcfMap.put(epoch,item.getValue());
            } catch (ParseException e) {
                logger.error("Unable to convert date to epoch: "+item.getKey(),e);
            }
        });
        logger.info("FCF sorted Map: "+fcfMap);
        int count =0;
        double fcfSum = 0;
        Iterator<Map.Entry<Long,Double>> it = fcfMap.entrySet().iterator();
        while(count< 3 && it.hasNext()){
            fcfSum = fcfSum + it.next().getValue();
            count++;
        }
        double fcfAverge = fcfSum/3;
        logger.info("Average FCF [3 years]: "+fcfAverge);
        analyzedInfoDto.getDcfTwoPhaseValuationModelDto().setInitalAvgFCF(
                Double.parseDouble(StockUtil.convertDoubleToPrecision(fcfAverge,2)));
        return fcfAverge;
    }

    public static DCFTwoPhaseValuation getInstance(){
        return Inner.getInstance();
    }



    private static class Inner{
        private static final DCFTwoPhaseValuation instance = new DCFTwoPhaseValuation();
        private  Inner(){

        }
        static DCFTwoPhaseValuation getInstance() {
            return instance;
        }
    }
}
