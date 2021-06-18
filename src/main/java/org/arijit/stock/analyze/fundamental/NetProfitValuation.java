package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.analysisdto.EPSMutlipliedValuationModelDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.YearlyReportDto;
import org.arijit.stock.analyze.util.DateUtil;
import org.arijit.stock.analyze.util.FundamentalAnalysisUtil;
import org.arijit.stock.analyze.util.StockUtil;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class NetProfitValuation implements  IFundamentalEvaluation {

    private static Logger logger = LogManager.getLogger(NetProfitValuation.class);

    private boolean evaluated = false;
    @Override
    public boolean isEvaluated() {
        return evaluated;
    }

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        analyzedInfoDto.getNetProfitValuationModelDto().setEvluated(false);
        double endYearProfit = calcNetProfitGrowth(fundamentalInfoDto,analyzedInfoDto);
        double estimated10thYearProfit = calcNetPofitProjected(fundamentalInfoDto,analyzedInfoDto,endYearProfit);
        calcMarketCapital(fundamentalInfoDto,analyzedInfoDto, estimated10thYearProfit);
        double numberOfOutStandingShare = fundamentalInfoDto.getCompanyDto().getMarketCap()/fundamentalInfoDto.getCompanyDto().getCurrentSharePrice();
        double targetPrice = analyzedInfoDto.getNetProfitValuationModelDto().getDiscountedMarketCapital()/numberOfOutStandingShare;
        numberOfOutStandingShare = Double.parseDouble(StockUtil.convertDoubleToPrecision(numberOfOutStandingShare,2));
        analyzedInfoDto.getNetProfitValuationModelDto().setOutstandingShare(numberOfOutStandingShare);
        targetPrice = Double.parseDouble(StockUtil.convertDoubleToPrecision(targetPrice,2));
        logger.info("Target Price: "+targetPrice);
        analyzedInfoDto.getNetProfitValuationModelDto().setFinalIntrinsicValue(targetPrice);
        analyzedInfoDto.getNetProfitValuationModelDto().setEvluated(true);
    }


    public double calcNetProfitGrowth(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto){
        Map<Long, Double> netprofitMap= new TreeMap<>();
        analyzedInfoDto.getNetProfitValuationModelDto().getNetprofitMap().entrySet().stream().forEach(item->{
            try {
                long epoch = DateUtil.convertToEpochMilli(item.getKey());
                netprofitMap.put(epoch,item.getValue());
            } catch (ParseException e) {
                logger.error("Unable to convert date to epoch: "+item.getKey(),e);
            }
        });
        logger.info("NetProfit sorted Map: "+netprofitMap);
        int years = netprofitMap.size();
        double startYearProfit = netprofitMap.entrySet().stream().findFirst().get().getValue();
        double endYearProfit = 0;
        double year5Profit = 0;
        int count =0;
        Iterator<Map.Entry<Long,Double>> it = netprofitMap.entrySet().iterator();
        while(it.hasNext()){
            endYearProfit = it.next().getValue();
            if(count ==4){
                year5Profit = endYearProfit;
            }
            count++;
        }

        double endYearcagr = FundamentalAnalysisUtil.cagr(endYearProfit,startYearProfit,years);
        double tmp = Double.parseDouble(StockUtil.convertDoubleToPrecision(endYearcagr,2));
        analyzedInfoDto.getNetProfitValuationModelDto().setProfitCAGRGrowth10yrs(tmp);
        if(year5Profit>0){
            double year5CAGR = FundamentalAnalysisUtil.cagr(year5Profit,startYearProfit,5);
            year5CAGR = Double.parseDouble(StockUtil.convertDoubleToPrecision(year5CAGR,2));
            analyzedInfoDto.getNetProfitValuationModelDto().setProfitCAGRGrowth5yrs(year5CAGR);
        }
        logger.info("Estimated Profit Growth after 10 years: "+endYearcagr);
        return endYearProfit;
    }


    public double calcNetPofitProjected(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, double endYearProfit){
        double profitGrowth = analyzedInfoDto.getNetProfitValuationModelDto().getProfitCAGRGrowth10yrs();
        for(int i=1;i<=10;i++){
            double profit =(endYearProfit*profitGrowth/100)+endYearProfit;
            double tmpProfit = Double.parseDouble(StockUtil.convertDoubleToPrecision(profit,2));
            analyzedInfoDto.getNetProfitValuationModelDto().addProjectedNetProfit(i,tmpProfit);
            endYearProfit = profit;
        }
        logger.info("Estimated Profit After 10 years: "+endYearProfit);
        double tmp = Double.parseDouble(StockUtil.convertDoubleToPrecision(endYearProfit,2));
        analyzedInfoDto.getNetProfitValuationModelDto().setEstimatedProfit(tmp);
        return endYearProfit;
    }

    public void calcMarketCapital(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto,double estimated10thYearProfit){
        /*
        Here calculate estimated market capital after 10 years and discounted market capital after 10 years.
         */
        double industryPE = fundamentalInfoDto.getCompanyDto().getIndustryPE();
        double discountRate = analyzedInfoDto.getNetProfitValuationModelDto().getDiscountRate();
        double estimatedMarketCapital = estimated10thYearProfit*industryPE;
        double denominator = 1+ (discountRate/100);
        denominator = Math.pow(denominator,10);
        double discountedMarketCapital = estimatedMarketCapital/denominator;
        estimatedMarketCapital = Double.parseDouble(StockUtil.convertDoubleToPrecision(estimatedMarketCapital,2));
        discountedMarketCapital = Double.parseDouble(StockUtil.convertDoubleToPrecision(discountedMarketCapital,2));
        logger.info("Estimated Market Capital: "+estimatedMarketCapital+" Discounted Market Capital: "+discountedMarketCapital);
        analyzedInfoDto.getNetProfitValuationModelDto().setEstimatedMarketCapital(estimatedMarketCapital);
        analyzedInfoDto.getNetProfitValuationModelDto().setDiscountedMarketCapital(discountedMarketCapital);
    }

    public static NetProfitValuation getInstance(){
        return Inner.getInstance();
    }



    private static class Inner{
        private static final NetProfitValuation instance = new NetProfitValuation();
        private  Inner(){

        }
        static NetProfitValuation getInstance() {
            return instance;
        }
    }
}
