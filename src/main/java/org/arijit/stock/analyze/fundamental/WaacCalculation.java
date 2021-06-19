package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.util.StockUtil;

/**
 * WAAC gives us discount rate.
 * This discount rate is used in DCF, NetProfit, EPS and other valuation model to get idea about present price of future of a company
 */
public class WaacCalculation implements  IFundamentalEvaluation{

    private static Logger logger = LogManager.getLogger(WaacCalculation.class);

    boolean isEvaluated = false;

    @Override
    public boolean isEvaluated() {
        return isEvaluated;
    }

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        calcWACC(fundamentalInfoDto,analyzedInfoDto);
    }

    /**
     * Calculating weighted average cost of capital
     * @param fundamentalInfoDto
     * @param analyzedInfoDto
     */
    private void calcWACC(FundamentalInfoDto fundamentalInfoDto,AnalyzedInfoDto analyzedInfoDto){
//        double interestExpense = analyzedInfoDto.getEconomicGrowthDCFDto().getIefy();
        double interestExpense = fundamentalInfoDto.getProfitAndLossDtoList().get(0).getInterest();
        double totalDebt = fundamentalInfoDto.getBalanceSheetDtoList().get(0).getDebt();
        double incomeTaxExpense = analyzedInfoDto.getWaacDto().getIncomeTaxExpense();
        double incomeBeforeTax = analyzedInfoDto.getWaacDto().getIncomeBeforeTax();
        double riskFreeRate = analyzedInfoDto.getWaacDto().getRiskFreeRate();
//        double companyBeta = analyzedInfoDto.getEconomicGrowthDCFDto().getCbeta();

        double companyBeta = fundamentalInfoDto.getCompanyDto().getCompanyBeta();
        double marketReturn = analyzedInfoDto.getWaacDto().getMarketReturn();
        double marketCapital = fundamentalInfoDto.getCompanyDto().getMarketCap();


        double costOfDebt = 0;
        if(totalDebt>0){
            costOfDebt = (double)(interestExpense/totalDebt)*100;
        }
        logger.info("Interest Expense: "+incomeTaxExpense+" Total Debt: "+totalDebt +" cost of debt: "+costOfDebt);

        double effectiveTaxRate =(double) (incomeTaxExpense/incomeBeforeTax)*100;
        logger.info("Income Tax Expense: "+incomeTaxExpense+" Income Before Tax: "+incomeBeforeTax+ " Effective Tax Rate: "+effectiveTaxRate);

        double costOfEquity = riskFreeRate +(companyBeta *(marketReturn-riskFreeRate));
        logger.info(" Risk Free Rate: "+riskFreeRate+" company Beta: "+companyBeta+" Market Return: "+marketReturn+" cost of Equity: "+costOfEquity);
        double total = totalDebt + marketCapital;

        double weightOfDebt = (totalDebt/total)*100;
        double weightOfEquity = (marketCapital/total)*100;

        logger.info("Market Capital: "+marketCapital+" weight Of Debt: "+weightOfDebt+" weight Of Equity: "+weightOfEquity);

        double codE = costOfDebt*(1-(effectiveTaxRate/100));
        logger.info("Weight of Debt: "+weightOfDebt+" cost Of Debt: "+costOfDebt+" effectiveTaxRate: "+effectiveTaxRate+" codE: "+codE+ " weight of equity: "+weightOfEquity+" Cost of Equity: "+costOfEquity);

        double wacc = ( weightOfDebt*codE)+(weightOfEquity*costOfEquity);
        wacc = (double)wacc/100; //converting to percentage
        double tmp = Double.parseDouble(StockUtil.convertDoubleToPrecision(wacc,2));
        analyzedInfoDto.getWaacDto().setDiscountRate(tmp);

        logger.info("Calculated WACC (Discout Rate): "+tmp);
    }

    public static WaacCalculation getInstance(){
        return Inner.getInstance();
    }



    private static class Inner{
        private static final WaacCalculation instance = new WaacCalculation();
        private  Inner(){

        }
        static WaacCalculation getInstance() {
            return instance;
        }
    }

}
