package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.analysisdto.CashFlowAnalysisInfo;
import org.arijit.stock.analyze.analysisdto.RatioAnalysisInfo;
import org.arijit.stock.analyze.dto.CashFlowDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.RatiosDto;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

                double netCashFlowGrowth = (lasCashFlowDto.getNetCashFlow()-prevCashFlowDto.getNetCashFlow())/prevCashFlowDto.getNetCashFlow();
                netCashFlowGrowth = (double) netCashFlowGrowth*100;
                cashFlowAnalysisInfo.addCashFlowGrowth(lasCashFlowDto.getDate(),"netCashFlow",StockUtil.convertDoubleToPrecision(netCashFlowGrowth, precision));

//                double freeCashFlowGrowth = (lasCashFlowDto.getFreeCashFlow()-prevCashFlowDto.getFreeCashFlow())/prevCashFlowDto.getFreeCashFlow();
//                freeCashFlowGrowth = (double) freeCashFlowGrowth*100;
//                cashFlowAnalysisInfo.addCashFlowGrowth(lasCashFlowDto.getDate(),"freeCashFlow",StockUtil.convertDoubleToPrecision(freeCashFlowGrowth, precision));

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
