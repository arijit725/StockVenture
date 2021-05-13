package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.analysisdto.BalanceSheetAnalysisInfo;
import org.arijit.stock.analyze.dto.BalanceSheetDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.score.ScorService;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class BalanceSheetEvaluation implements IFundamentalEvaluation{

    private static final Logger logger = LogManager.getLogger(BalanceSheetEvaluation.class);

    private boolean evaluated = false;
    @Override
    public boolean isEvaluated() {
        return evaluated;
    }

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        List<BalanceSheetDto> balanceSheetDtoList = fundamentalInfoDto.getBalanceSheetDtoList();
        if(balanceSheetDtoList.isEmpty()||balanceSheetDtoList.size()<year)
            throw new Exception("Number of year exceeds BalancesSheetDto list");
        balanceSheetDtoList = fundamentalInfoDto.getBalanceSheetDtoList().stream().limit(year).collect(Collectors.toList());
        BalanceSheetDto endingYearBalancesheetDto = balanceSheetDtoList.get(0);
        BalanceSheetDto startingYearBalancesheetDto = balanceSheetDtoList.get(year-1);
        logger.info("Starting Year Balancesheet Dto: "+startingYearBalancesheetDto);
        logger.info("Ending Year Balancesheet Dto: "+endingYearBalancesheetDto);

        double endYearTotalShareCapital = endingYearBalancesheetDto.getTotalShareCapital();
        double startYearTotalShareCapital = startingYearBalancesheetDto.getTotalShareCapital();

        double changeRatio = (endYearTotalShareCapital-startYearTotalShareCapital)/startYearTotalShareCapital;
        changeRatio = (double)changeRatio*100;
        logger.info("Percentage Change in TotalShareCapital: "+changeRatio);
        analyzedInfoDto.getBalanceSheetAnalysisInfo().setTotalShareChangePercentage(changeRatio);

        int increaseCountInShareCapital = growthInShareCapital(balanceSheetDtoList);
        analyzedInfoDto.getBalanceSheetAnalysisInfo().setIncreaseIncidentInTotalShare(increaseCountInShareCapital);
        logger.info("Change incident count in Total Share Capital: "+increaseCountInShareCapital);
        double endYearReserves = endingYearBalancesheetDto.getReserves();
        double startingYearReserves = startingYearBalancesheetDto.getReserves();

        double reserveChangeRatio = (endYearReserves-startingYearReserves)/startingYearReserves;
        reserveChangeRatio = (double)reserveChangeRatio*100;

        logger.info("Percentage Change in Reserves: "+reserveChangeRatio);
        analyzedInfoDto.getBalanceSheetAnalysisInfo().setReservesChangePercentage(reserveChangeRatio);

        double endYearDebt = endingYearBalancesheetDto.getDebt();
        double startingYearDebt = startingYearBalancesheetDto.getDebt();

        double debtChangeRatio = (endYearDebt-startingYearDebt)/startingYearDebt;
        debtChangeRatio = (double) debtChangeRatio*100;

        logger.info("Percentage Change in Debt: "+debtChangeRatio);
        analyzedInfoDto.getBalanceSheetAnalysisInfo().setDebtChangePercentage(debtChangeRatio);

        debtToReserveRatio(analyzedInfoDto.getBalanceSheetAnalysisInfo(),balanceSheetDtoList);

        ScorService.getInstance().getBalancesheetScore().score(fundamentalInfoDto,analyzedInfoDto,year);

        evaluated = true;
    }

    private void debtToReserveRatio(BalanceSheetAnalysisInfo balanceSheetAnalysisInfo, List<BalanceSheetDto> balanceSheetDtos){
        BalanceSheetDto lastBalanceSheetDto = null;
        Iterator<BalanceSheetDto> iterator = balanceSheetDtos.iterator();
        while(iterator.hasNext()){
            if(lastBalanceSheetDto==null){
                BalanceSheetDto tmpBalancesheetDto = iterator.next();
                String date = tmpBalancesheetDto.getDate();
                double changeRatio = (tmpBalancesheetDto.getDebt()/tmpBalancesheetDto.getReserves());
                String changeRatioStr = StockUtil.convertDoubleToPrecision(changeRatio,4);
                balanceSheetAnalysisInfo.getDebtToReserveRatioMap().put(date,changeRatioStr);
            }
        }
    }

    private void scoreBalanceSheet(BalanceSheetAnalysisInfo balanceSheetAnalysisInfo){
        double grossTotalSharePercentageChangeMargin = 5;
        double reservePercentageChangeMargin = 1;

    }

    private int growthInShareCapital(List<BalanceSheetDto> balanceSheetDtoList ){
        int changeCount = 0;
        Iterator<BalanceSheetDto> it = balanceSheetDtoList.iterator();
        BalanceSheetDto lastBalanceSheetDto = null;
        while(it.hasNext()){
            if(lastBalanceSheetDto==null){
                lastBalanceSheetDto = it.next();
                continue;
            }
            BalanceSheetDto tmpBalancesheetDto = it.next();
            if(lastBalanceSheetDto.getTotalShareCapital()>tmpBalancesheetDto.getTotalShareCapital()){
                //total share capital is diluting
                changeCount++;
            }
            lastBalanceSheetDto = tmpBalancesheetDto;
        }
        return changeCount;
    }

    public static BalanceSheetEvaluation getInstance(){
        return Inner.getInstance();
    }

    private static class Inner{
        private static final BalanceSheetEvaluation instance = new BalanceSheetEvaluation();
        private  Inner(){

        }
        static BalanceSheetEvaluation getInstance() {
            return instance;
        }
    }
}
