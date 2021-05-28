package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.analysisdto.CashFlowAnalysisInfo;
import org.arijit.stock.analyze.analysisdto.RatioAnalysisInfo;
import org.arijit.stock.analyze.dto.CashFlowDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.ProfitAndLossDto;
import org.arijit.stock.analyze.dto.RatiosDto;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CashFlowEvaluation  implements IFundamentalEvaluation{

    private static Logger logger = LogManager.getLogger(CashFlowEvaluation.class);

    private boolean evaluated;

    private CashFlowEvaluation(){

    }

    @Override
    public boolean isEvaluated() {
        return evaluated;
    }

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        List<CashFlowDto> cashFlowDtoList = fundamentalInfoDto.getCashFlowDtoList();

        calcGrowth(analyzedInfoDto.getCashFlowAnalysisInfo(),cashFlowDtoList);
        calcCashFlowVsNetProfit(fundamentalInfoDto,analyzedInfoDto,year);
    }

    private void calcCashFlowVsNetProfit(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto,int year){
        double diffmargin =(double) 3/100; //taking 3% difference margin
        List<CashFlowDto> cashFlowDtoList = fundamentalInfoDto.getCashFlowDtoList().stream().limit(year).collect(Collectors.toList());
        List<ProfitAndLossDto> profitAndLossDtoList = fundamentalInfoDto.getProfitAndLossDtoList().stream().limit(year).collect(Collectors.toList());

        double operatingCashFlowSum = cashFlowDtoList.stream().mapToDouble(item->item.getCashFromOperatingActivity()).sum();
        double netProfitSum = profitAndLossDtoList.stream().mapToDouble(item->item.getNetProfit()).sum();
        double diff = netProfitSum - operatingCashFlowSum;
        double diffPercentage = diff/netProfitSum*100;
        logger.info("[Last "+year+" year analysis] Net Profit Sum : "+netProfitSum+" Cash Flow from Operating Activity: "+operatingCashFlowSum+" Actual Difference: "+diff+" Percentage of Difference: "+diffPercentage);
        String decision = "Net Profit Sum: "+netProfitSum+" Cash Flow from Operating Activity: "+operatingCashFlowSum+" Difference in Percentage: "+diffPercentage;
        if(diffPercentage>diffmargin){
            decision = decision+"<br> Analysis: Markable difference. [Alarm] Avoid Investing";
        }
        else{
            decision = decision+"<br> Analysis: Negligible Difference. No Alarm";
        }
        logger.info(decision);
        double cfoPatRatio = operatingCashFlowSum/netProfitSum;
        analyzedInfoDto.getCashFlowAnalysisInfo().setCfoPatRatio(StockUtil.convertDoubleToPrecision(cfoPatRatio,2));
        analyzedInfoDto.getCashFlowAnalysisInfo().setOperatingCashFlowVsNetProficCmp(decision);

    }

    private void calcGrowth(CashFlowAnalysisInfo cashFlowAnalysisInfo, List<CashFlowDto> cashFlowDtoList){
        Iterator<CashFlowDto> iterator = cashFlowDtoList.iterator();
        CashFlowDto lasCashFlowDto = null;
        int precision = 2;
        while(iterator.hasNext()){
            if(lasCashFlowDto==null)
                lasCashFlowDto = iterator.next();
            else{
                CashFlowDto prevCashFlowDto = iterator.next();
                double cashFromOperatingActivityGrowth = (lasCashFlowDto.getCashFromOperatingActivity()-prevCashFlowDto.getCashFromOperatingActivity())/prevCashFlowDto.getCashFromOperatingActivity();
                cashFromOperatingActivityGrowth = (double) cashFromOperatingActivityGrowth*100;
                cashFlowAnalysisInfo.addCashFlowGrowth(lasCashFlowDto.getDate(),"cashFromOperatingActivity", StockUtil.convertDoubleToPrecision(cashFromOperatingActivityGrowth, precision));

                double fixedAssestsPurchasedGrowth = (lasCashFlowDto.getFixedAssestsPurchased()-prevCashFlowDto.getFixedAssestsPurchased())/prevCashFlowDto.getFixedAssestsPurchased();
                fixedAssestsPurchasedGrowth = (double) fixedAssestsPurchasedGrowth*100;
                cashFlowAnalysisInfo.addCashFlowGrowth(lasCashFlowDto.getDate(),"fixedAssestsPurchased",StockUtil.convertDoubleToPrecision(fixedAssestsPurchasedGrowth, precision));

                try {
                    double netCashFlowGrowth = lasCashFlowDto.getNetCashFlow();
                    if(prevCashFlowDto.getNetCashFlow()>0) {
                        netCashFlowGrowth = (lasCashFlowDto.getNetCashFlow() - prevCashFlowDto.getNetCashFlow()) / prevCashFlowDto.getNetCashFlow();
                        netCashFlowGrowth = (double) netCashFlowGrowth * 100;
                    }
                    logger.info(" current Net Cash Flow : " + lasCashFlowDto.getNetCashFlow() + " prev Net CashFlow: " + prevCashFlowDto.getNetCashFlow() + " growth: " + netCashFlowGrowth);
                    cashFlowAnalysisInfo.addCashFlowGrowth(lasCashFlowDto.getDate(), "netCashFlow", StockUtil.convertDoubleToPrecision(netCashFlowGrowth, precision));
                }catch(Exception e){
                    logger.error("Unable to calculate netCashFlow Growth: current cashflow: "+lasCashFlowDto.getNetCashFlow()+" prev cash flow: "+prevCashFlowDto.getNetCashFlow(),e);
                }
                double freeCashFlowGrowth = (lasCashFlowDto.getFreeCashFlow()-prevCashFlowDto.getFreeCashFlow())/prevCashFlowDto.getFreeCashFlow();
                freeCashFlowGrowth = (double) freeCashFlowGrowth*100;
                cashFlowAnalysisInfo.addCashFlowGrowth(lasCashFlowDto.getDate(),"freeCashFlow",StockUtil.convertDoubleToPrecision(freeCashFlowGrowth, precision));

                lasCashFlowDto = prevCashFlowDto;
            }
        }
    }

    public static CashFlowEvaluation getInstance(){
        return InnerClass.getInstance();
    }

    private static class InnerClass{
        private static final CashFlowEvaluation instance = new CashFlowEvaluation();
        private  InnerClass(){

        }
        static CashFlowEvaluation getInstance() {
            return instance;
        }
    }
}
