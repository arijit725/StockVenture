package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.analysisdto.BalanceSheetAnalysisInfo;
import org.arijit.stock.analyze.dto.BalanceSheetDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.enums.AnalysisEnums;
import org.arijit.stock.analyze.enums.ColorEnums;
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
        /*When analyzing each time, we should clear analysis statement*/
        analyzedInfoDto.getBalanceSheetAnalysisInfo().clearAnalysisStatement();

        balanceSheetDtoList = fundamentalInfoDto.getBalanceSheetDtoList().stream().limit(year).collect(Collectors.toList());
        BalanceSheetDto endingYearBalancesheetDto = balanceSheetDtoList.get(0);
        BalanceSheetDto startingYearBalancesheetDto = balanceSheetDtoList.get(year-1);
        logger.info("Starting Year Balancesheet Dto: "+startingYearBalancesheetDto);
        logger.info("Ending Year Balancesheet Dto: "+endingYearBalancesheetDto);

        calcAvgGrowth(fundamentalInfoDto,analyzedInfoDto,year);

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

        double debtChangeRatio = endYearDebt;
        if(startingYearDebt>0) {
            debtChangeRatio = (endYearDebt - startingYearDebt) / startingYearDebt;
            debtChangeRatio = (double) debtChangeRatio * 100;
        }

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

    private void calcAvgGrowth(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int years){
        List<BalanceSheetDto> balanceSheetDtoList = fundamentalInfoDto.getBalanceSheetDtoList().stream().limit(years).collect(Collectors.toList());
        BalanceSheetDto currentBalanceSheetDto = null;

        Iterator<BalanceSheetDto> it = balanceSheetDtoList.iterator();
        double totalGrowth = 0;
        double eqAvgGrowth = 0;
        double reserveAvgGrowth = 0;
        double debtAvgGrowth = 0;
        double reserveIncreaseCount  = 0;
        double reserveDecreaseCount  = 0;
        while(it.hasNext()){
            if(currentBalanceSheetDto==null)
                currentBalanceSheetDto = it.next();
            else{
                BalanceSheetDto prevBalanceSheetDto = it.next();

                try {
                    double totalShareCapitalGrowth = 0;
                    if (prevBalanceSheetDto.getTotalShareCapital() > 0) {
                        totalShareCapitalGrowth = (currentBalanceSheetDto.getTotalShareCapital() - prevBalanceSheetDto.getTotalShareCapital());
                        totalShareCapitalGrowth = (double) totalShareCapitalGrowth / prevBalanceSheetDto.getTotalShareCapital() * 100;
                        totalGrowth = totalGrowth + totalShareCapitalGrowth;
                    }
                    String totalShareCapitalGrowthString = StockUtil.convertDoubleToPrecision(totalShareCapitalGrowth, 2);
                    analyzedInfoDto.getBalanceSheetAnalysisInfo().addBalanceSheetGrowths(currentBalanceSheetDto.getDate(), "total_share_capital", totalShareCapitalGrowthString);
                }catch(Exception e){
                    logger.error("unable to calculate total share capital growth",e);
                }

                try {
                    double equityShareCapitalGrowth = 0;
                    if (prevBalanceSheetDto.getEquityShareCapital() > 0) {
                        equityShareCapitalGrowth = (currentBalanceSheetDto.getEquityShareCapital() - prevBalanceSheetDto.getEquityShareCapital());
                        equityShareCapitalGrowth = (double) equityShareCapitalGrowth / prevBalanceSheetDto.getEquityShareCapital() * 100;
                        eqAvgGrowth = eqAvgGrowth + equityShareCapitalGrowth;
                    }
                    String growthString = StockUtil.convertDoubleToPrecision(equityShareCapitalGrowth, 2);
                    analyzedInfoDto.getBalanceSheetAnalysisInfo().addBalanceSheetGrowths(currentBalanceSheetDto.getDate(), "equity_share_capital", growthString);
                }catch(Exception e){
                    logger.error("unable to calculate total share capital growth",e);
                }


                try {
                    double reservesGrowth = 0;
                    if (prevBalanceSheetDto.getReserves() > 0) {
                        reservesGrowth = (currentBalanceSheetDto.getReserves() - prevBalanceSheetDto.getReserves());
                        reservesGrowth = (double) reservesGrowth / prevBalanceSheetDto.getReserves() * 100;
                        reserveAvgGrowth = reserveAvgGrowth + reservesGrowth;
                    }
                    String growthString = StockUtil.convertDoubleToPrecision(reservesGrowth, 2);
                    analyzedInfoDto.getBalanceSheetAnalysisInfo().addBalanceSheetGrowths(currentBalanceSheetDto.getDate(), "reserves", growthString);
                }catch(Exception e){
                    logger.error("unable to calculate total share capital growth",e);
                }

                try{
                    if(currentBalanceSheetDto.getReserves()>=prevBalanceSheetDto.getReserves()){
                        reserveIncreaseCount++;
                    }
                    else if(currentBalanceSheetDto.getReserves()<prevBalanceSheetDto.getReserves()){
                        reserveDecreaseCount++;
                    }
                }catch(Exception e){
                    logger.error("Unable to measure reserve growth count",e);
                }
                try {
                    double debtsGrowth = 0;
                    if (prevBalanceSheetDto.getDebt() > 0) {
                        debtsGrowth = (currentBalanceSheetDto.getDebt() - prevBalanceSheetDto.getDebt());
                        debtsGrowth = (double) debtsGrowth / prevBalanceSheetDto.getDebt() * 100;
                        debtAvgGrowth = debtAvgGrowth + debtsGrowth;
                    }
                    String growthString = StockUtil.convertDoubleToPrecision(debtsGrowth, 2);
                    analyzedInfoDto.getBalanceSheetAnalysisInfo().addBalanceSheetGrowths(currentBalanceSheetDto.getDate(), "debt", growthString);
                }catch(Exception e){
                    logger.error("unable to calculate growth in Debt",e);
                }


                currentBalanceSheetDto = prevBalanceSheetDto;
            }
        }
        /*from 5 years data we can get 4 years growth data point, hence decrementing years by 1*/
        int growthYear = years-1;

        totalGrowth =(double) totalGrowth/growthYear;
        String totalAvgGrowthStatement = " Average Dilution in Total Share Capital: "+StockUtil.convertDoubleToPrecision(totalGrowth,2);
        if(totalGrowth<=0)
            analyzedInfoDto.getBalanceSheetAnalysisInfo().addAnalysisStatement(totalAvgGrowthStatement, AnalysisEnums.ANALYZED_GOOD);
        else
            analyzedInfoDto.getBalanceSheetAnalysisInfo().addAnalysisStatement(totalAvgGrowthStatement,AnalysisEnums.ANALYZED_BAD);

        eqAvgGrowth =(double) eqAvgGrowth/(growthYear);
        String eqAvgGrowthStatement = " Average Dilution in Equity Share Capital: "+StockUtil.convertDoubleToPrecision(eqAvgGrowth,2);
        if(eqAvgGrowth<=0)
            analyzedInfoDto.getBalanceSheetAnalysisInfo().addAnalysisStatement(eqAvgGrowthStatement,AnalysisEnums.ANALYZED_GOOD);
        else
            analyzedInfoDto.getBalanceSheetAnalysisInfo().addAnalysisStatement(eqAvgGrowthStatement,AnalysisEnums.ANALYZED_BAD);

        if(eqAvgGrowth==0 && balanceSheetDtoList.get(0).getEquityShareCapital()==currentBalanceSheetDto.getEquityShareCapital()){
            String stmt="(+) Equity share capital has not diluted over "+years+" year";
            analyzedInfoDto.getBalanceSheetAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_VERY_GOOD);

        }

        reserveAvgGrowth = (double) reserveAvgGrowth / growthYear;
        String reserveAvgGrowthStatement = " Average Reserve Increase: "+StockUtil.convertDoubleToPrecision(reserveAvgGrowth,2);
        if(reserveAvgGrowth>0)
            analyzedInfoDto.getBalanceSheetAnalysisInfo().addAnalysisStatement(reserveAvgGrowthStatement,AnalysisEnums.ANALYZED_GOOD);
        else if(reserveAvgGrowth == 0)
            analyzedInfoDto.getBalanceSheetAnalysisInfo().addAnalysisStatement(reserveAvgGrowthStatement,AnalysisEnums.ANALYZED_NEUTRAL);
        else
            analyzedInfoDto.getBalanceSheetAnalysisInfo().addAnalysisStatement(reserveAvgGrowthStatement,AnalysisEnums.ANALYZED_BAD);

        debtAvgGrowth = (double) debtAvgGrowth / growthYear;

        if(debtAvgGrowth<=0) {
            String debtAvgGrowthStatement = " Average Debt Decreased: " + StockUtil.convertDoubleToPrecision(debtAvgGrowth, 2);
            analyzedInfoDto.getBalanceSheetAnalysisInfo().addAnalysisStatement(debtAvgGrowthStatement, AnalysisEnums.ANALYZED_GOOD);
        }
        else {
            String debtAvgGrowthStatement = " Average Debt Increased: " + StockUtil.convertDoubleToPrecision(debtAvgGrowth, 2);
            analyzedInfoDto.getBalanceSheetAnalysisInfo().addAnalysisStatement(debtAvgGrowthStatement, AnalysisEnums.ANALYZED_BAD);
        }

        if(reserveIncreaseCount == growthYear){
            String stmt= "(+) Reserves is increasing continuously";
            analyzedInfoDto.getBalanceSheetAnalysisInfo().addAnalysisStatement(stmt, AnalysisEnums.ANALYZED_VERY_GOOD);
        }
        else if(reserveDecreaseCount == growthYear){
            String stmt= "(-) Reserves is decreasing continuously";
            analyzedInfoDto.getBalanceSheetAnalysisInfo().addAnalysisStatement(stmt, AnalysisEnums.ANALYZED_BAD);
        }

        if(debtAvgGrowth == 0 && balanceSheetDtoList.get(0).getDebt()==0){
            String stmt= "(+) Company is a zero debt company. This is a very positive sign for company growth";
            analyzedInfoDto.getBalanceSheetAnalysisInfo().addAnalysisStatement(stmt, AnalysisEnums.ANALYZED_VERY_GOOD);
        }
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
