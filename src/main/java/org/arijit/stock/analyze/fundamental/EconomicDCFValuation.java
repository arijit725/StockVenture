package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.analysisdto.EconomicGrowthDCFDto;
import org.arijit.stock.analyze.dto.BalanceSheetDto;
import org.arijit.stock.analyze.dto.CashFlowDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.enums.ValuationEnums;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * In This DCF approach we consider economic growth rate and discount rate.
 * Based on that we calculate Intrinsic value of stock.
 * Discount rate = percentage of return for stock.
 * The whole algorithm is based on accurately guessing economic growth rate and discount rate.
 *
 * DCF gives valuation of stock for n years down the line.
 * So use it very cautiously.
 *
 * DCF model can be used for valuation of a project, company, stock, bond or any income producing asset.
 *
 * The DCF method can be used for the companies which have positive Free cash flows and these FCFF can be reasonably forecasted.
 * So, it cannot be used for new and small companies or industries which have greater exposure to seasonal or economic cycles.
 */
public class EconomicDCFValuation implements  IFundamentalEvaluation{

    private static Logger logger = LogManager.getLogger(EconomicDCFValuation.class);

    boolean isEvaluated = false;

    @Override
    public boolean isEvaluated() {
        return isEvaluated;
    }

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        int fcfavgYear = year;
        if(year<10)
            throw new Exception("DCF Model Can not be built with out 10 years data;");
        List<CashFlowDto> prevCashFlowList = fundamentalInfoDto.getCashFlowDtoList().stream().limit(year).collect(Collectors.toList());
        if(prevCashFlowList.size()<10)
            throw new Exception("DCF Model Can not be built with out 10 years data;");

        calcWACC(fundamentalInfoDto,analyzedInfoDto);
        EconomicGrowthDCFDto dcfDto = analyzedInfoDto.getEconomicGrowthDCFDto();
        prevCashFlowList.stream().forEach(cashFlowDto -> {dcfDto.addPastNYearsFreeCashFlow(cashFlowDto.getDate(), StockUtil.convertDoubleToPrecision(cashFlowDto.getFreeCashFlow(),2));});

        BalanceSheetDto lastYearBalancesheet = fundamentalInfoDto.getBalanceSheetDtoList().get(0);
        logger.info("Last year balancesheet: "+lastYearBalancesheet);
        // getting average FCF of last 10 years
        double averageFCF = prevCashFlowList.stream().limit(fcfavgYear).mapToDouble(mapper->mapper.getFreeCashFlow()).average().getAsDouble();
        logger.info("Average FCF: "+averageFCF);
        double perpetualGrowthRate = dcfDto.getPerpertualGrowthRate();

        double discountRate = dcfDto.getDiscountRate();

        logger.info("Perceptual Growth Rate: "+perpetualGrowthRate+" Discount Rate: "+discountRate);

        //setting average FCF as next years projected FCF
        Map<Integer, Double> projectedFCFMap = calcProjectedFCF(averageFCF,year);
        logger.info("Projected FCF Map: "+projectedFCFMap);

        projectedFCFMap.entrySet().stream().forEach(item->{
                String value = StockUtil.convertDoubleToPrecision(item.getValue(),2);
                dcfDto.getNextNYearsFreeCashFlow().put(item.getKey(),value);
        });

        Map<Integer, Double> pvFCFMap =  calcPVofProjectedFCF(projectedFCFMap,discountRate);
        logger.info("PV of Projected FCF Map : "+pvFCFMap);

        pvFCFMap.entrySet().stream().forEach(item->{
                int pvFcFyear = item.getKey();
                String value = StockUtil.convertDoubleToPrecision(item.getValue(),2);
                dcfDto.getNextNYearsFCFPV().put(pvFcFyear,value);
        });

        double terminalValue = calcTerminalValue(averageFCF,perpetualGrowthRate,discountRate);
        logger.info("Terminal Value : "+terminalValue);
        dcfDto.setTv(terminalValue);

        double pvOfTerminalValue = calcPV(terminalValue,discountRate,year);
        logger.info("PV of "+year+" year Terminal Value: "+pvOfTerminalValue);
        dcfDto.setTvPv(pvOfTerminalValue);

        double sumOfPVFCF = pvFCFMap.values().stream().mapToDouble(Double::doubleValue).sum();
        sumOfPVFCF = sumOfPVFCF+pvOfTerminalValue;
        logger.info("Sum of PV FCF: "+sumOfPVFCF);
        dcfDto.setSumOfFcF(sumOfPVFCF);

        double cash = dcfDto.getLasFYCashEquivalent();
        double debt = dcfDto.getLastFYDebt();

        double equityValue = sumOfPVFCF + cash - debt;
        logger.info("Equity Value : "+equityValue);
        dcfDto.setEquityValue(equityValue);

        double shareOutstanding = lastYearBalancesheet.getEquityShareCapital() / fundamentalInfoDto.getCompanyDto().getFaceValue();
        logger.info("Share Outsanding: "+shareOutstanding);
        dcfDto.setShareOutStanding(shareOutstanding);

        double estimatedPricePerShare = equityValue / shareOutstanding;
        logger.info("Estimated Price Per Share: "+estimatedPricePerShare);

        double tmp = Double.parseDouble(StockUtil.convertDoubleToPrecision(estimatedPricePerShare,2));

        dcfDto.setTargetPrice(tmp);

        double marginOfSafty = dcfDto.getMarginOfSafty();

        estimatedPricePerShare = estimatedPricePerShare - (estimatedPricePerShare*marginOfSafty/100);
        logger.info("Estimated Price Per Share after Margin of Safty: "+estimatedPricePerShare);
        tmp = Double.parseDouble(StockUtil.convertDoubleToPrecision(estimatedPricePerShare,2));
        dcfDto.setPriceAfterMarginOfSafty(tmp);

        double currentSharePrice = fundamentalInfoDto.getCompanyDto().getCurrentSharePrice();

        double upside = (estimatedPricePerShare-currentSharePrice)/currentSharePrice*100;
        logger.info("Upside: "+upside);
        tmp = Double.parseDouble(StockUtil.convertDoubleToPrecision(upside,4));
        dcfDto.setUpside(tmp);

        ValuationEnums valuationEnums = ValuationEnums.NO_BUY;
        if(upside>discountRate)
            valuationEnums = ValuationEnums.BUY;
        logger.info("Valuation: "+valuationEnums);
        dcfDto.setDecision(valuationEnums);

        logger.info(dcfDto);

    }

    /**
     * Calculating weighted average cost of capital
     * @param fundamentalInfoDto
     * @param analyzedInfoDto
     */
    private void calcWACC(FundamentalInfoDto fundamentalInfoDto,AnalyzedInfoDto analyzedInfoDto){
        double interestExpense = analyzedInfoDto.getEconomicGrowthDCFDto().getIefy();
        double totalDebt = fundamentalInfoDto.getBalanceSheetDtoList().get(0).getDebt();
        double incomeTaxExpense = analyzedInfoDto.getEconomicGrowthDCFDto().getItefy();
        double incomeBeforeTax = analyzedInfoDto.getEconomicGrowthDCFDto().getIbtfy();
        double riskFreeRate = analyzedInfoDto.getEconomicGrowthDCFDto().getRfr();
        double companyBeta = analyzedInfoDto.getEconomicGrowthDCFDto().getCbeta();

        double marketReturn = 10; ///making this constant for time being. This should be any index average return over last 10 years.
        double marketCapital = fundamentalInfoDto.getCompanyDto().getMarketCap();


        double costOfDebt = 0;
        if(totalDebt>0){
            costOfDebt = (double)(interestExpense/totalDebt)*100;
        }
        logger.info("Interest Expense: "+incomeTaxExpense+" Total Debt: "+0+" cost of debt: "+costOfDebt);

        double effectiveTaxRate =(double) (incomeTaxExpense/incomeBeforeTax)*100;
        logger.info("Income Tax Expense: "+incomeTaxExpense+" Income Before Tax: "+incomeBeforeTax+ " Effective Tax Rate: "+effectiveTaxRate);

        double costOfEquity = riskFreeRate +(companyBeta *(marketReturn-riskFreeRate));
        logger.info(" Risk Free Rate: "+riskFreeRate+" company Beta: "+companyBeta+" Market Return: "+marketReturn+" cost of Equity: "+costOfEquity);
        double total = totalDebt + marketCapital;

        double weightOfDebt = (totalDebt/total)*100;
        double weightOfEquity = (marketCapital/total)*100;

        logger.info("Market Capital: "+marketCapital+" weight Of Debt: "+weightOfDebt+" weight Of Equity: "+weightOfEquity);

        double wacc = ( weightOfDebt*costOfDebt*(1-effectiveTaxRate))+(weightOfEquity*costOfEquity);

        double tmp = Double.parseDouble(StockUtil.convertDoubleToPrecision(wacc,2));
        analyzedInfoDto.getEconomicGrowthDCFDto().setDiscountRate(tmp);

        logger.info("Calculated WACC (Discout Rate): "+tmp);
    }

    private Map<Integer, Double> calcPVofProjectedFCF(Map<Integer,Double> projectedFCFMap, double discountRate){
        TreeMap<Integer, Double> pvFCFMap= new TreeMap<>((o1, o2) -> o1.compareTo(o2));

        Iterator<Map.Entry<Integer,Double>> it = projectedFCFMap.entrySet().iterator();
//        int year = 1;
        while(it.hasNext()){
            Map.Entry<Integer,Double> projectedFCF = it.next();
            int year = projectedFCF.getKey();
            double fcf = projectedFCF.getValue();
            double pv = calcPV(fcf,discountRate,year);
            pvFCFMap.put(year,pv);
        }

        return pvFCFMap;
    }

    /**
     * Assuming past 10 year average FCF will be maintained in next 10 years
     * @param averageFCF
     * @param year
     * @return
     */
    private Map<Integer,Double> calcProjectedFCF(double averageFCF, int year){
        TreeMap<Integer, Double> projectedFCFMap= new TreeMap<>((o1, o2) -> o1.compareTo(o2));
        for(int i=0;i<year;i++){
            projectedFCFMap.put(i+1,averageFCF);
        }

        return projectedFCFMap;
    }

    private double calcTerminalValue(double averageFCF, double perpetualGrowthRate, double discountRate){
        perpetualGrowthRate = (double) perpetualGrowthRate/100;
        discountRate = (double) discountRate/100;

        double denominator = discountRate - perpetualGrowthRate;
        double tmp = (double) 1+perpetualGrowthRate;

        tmp = averageFCF*tmp;
        tmp = tmp/denominator;
        return tmp;
    }


    /**
     * Calculating present value
     */
    private double calcPV(double fcf, double discountRate, double year){
        // discount rate will be in percentage, so dividing by 100 to get rate.
        double dr = (double) discountRate/100;
        double tmp = (double)(1+dr);
        tmp = Math.pow(tmp,year);
        double pv = (double)fcf/tmp;
        return pv;
    }

    public static EconomicDCFValuation getInstance(){
        return Inner.getInstance();
    }



    private static class Inner{
        private static final EconomicDCFValuation instance = new EconomicDCFValuation();
        private  Inner(){

        }
        static EconomicDCFValuation getInstance() {
            return instance;
        }
    }

}
