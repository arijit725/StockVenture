package org.arijit.stock.analyze.score;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.BalanceSheetDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class BalancesheetScore implements IScore{
    private static final Logger logger = LogManager.getLogger(BalancesheetScore.class);
    //Equity Share Capital weight
    private static final int EQW=5;

    //reserve and surplus weight
    private static final int RSRVW = 2;

    // debt weight.
    private static final int DW=5;


    @Override
    public void score(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int years) {
        try {
            int RSVGROW = years;
            int EQSAME = years;
            int DEBTDOWN = years;
            List<BalanceSheetDto> balanceSheetDtoList = fundamentalInfoDto.getBalanceSheetDtoList().stream().limit(years).collect(Collectors.toList());
            Iterator<BalanceSheetDto> it = balanceSheetDtoList.iterator();
            double equityShareCapitalGrowth = 0;
            double reserveGrowth = 0;

            double debtGrowth = 0;

            BalanceSheetDto currentYearBalancesheet = null;
            int i = 0;
            while (it.hasNext()) {
                if (currentYearBalancesheet == null)
                    currentYearBalancesheet = it.next();
                else {
                    double pow = (double) 1 / i;
                    BalanceSheetDto lastYearBalancesheet = it.next();

                    //here equity growth can be 0, so to signify equity weightage adding 1 to calcualted value.
                    // also subtracting from 1 as if equity share capital is decreased, it would be a good sign and for increase, its a bad sign.

                    //the more recent year, importance is more, more older year importance is less.
                    double tmpGrowth = 1 - calcEquityShareCapitalGrowth(currentYearBalancesheet.getEquityShareCapital(), lastYearBalancesheet.getEquityShareCapital());
                    if(tmpGrowth<0){
                        double d = Math.abs(tmpGrowth);
                        tmpGrowth = -1*Math.pow(d,pow);
                    }else{
                        tmpGrowth = Math.pow(tmpGrowth, pow);
                    }

                    logger.info("Current Year equity share capital: " + currentYearBalancesheet.getEquityShareCapital() + " last year equity share capital: " + lastYearBalancesheet.getEquityShareCapital() + " growth: " + tmpGrowth);
                    equityShareCapitalGrowth = equityShareCapitalGrowth + tmpGrowth;

                    double tmpRGrowth = calcReserveGrowth(currentYearBalancesheet.getReserves(), lastYearBalancesheet.getReserves());
                    // the more recent year, importance is more, more older year imortance is less
                    if(tmpRGrowth<0){
                        double d = Math.abs(tmpRGrowth);
                        tmpRGrowth = -1*Math.pow(d,pow);
                    }
                    else {
                        tmpRGrowth = Math.pow(tmpRGrowth, pow);
                    }
                    logger.info("Current Year Reserve: " + currentYearBalancesheet.getReserves() + " last year Reserve: " + lastYearBalancesheet.getReserves() + " growth: " + tmpRGrowth);
                    reserveGrowth = reserveGrowth + tmpRGrowth;

                    // if debt is not growing that is a positive indication. So to get that sign default debt growth is marked as 1.
                    double tmpDGrowth = 1 - calcDebtGrowth(currentYearBalancesheet.getDebt(), lastYearBalancesheet.getDebt());
                    if(tmpDGrowth<0){
                        double d = Math.abs(tmpDGrowth);
                        tmpDGrowth = -1*Math.pow(d,pow);
                    }else {
                        tmpDGrowth = Math.pow(tmpDGrowth, pow);
                    }
                    logger.info("Current Year Debt: " + currentYearBalancesheet.getDebt() + " last year Debt: " + lastYearBalancesheet.getDebt() + " growth: " + tmpDGrowth);
                    debtGrowth = debtGrowth + tmpDGrowth;

                    currentYearBalancesheet = lastYearBalancesheet;
                }
                i++;
            }

            logger.info("equityShareCapitalScore: " + equityShareCapitalGrowth + " reserveScore: " + reserveGrowth + " debtScore: " + debtGrowth);
            equityShareCapitalGrowth = EQW * equityShareCapitalGrowth;
            reserveGrowth = RSRVW * reserveGrowth;
            debtGrowth = DW * debtGrowth;
            logger.info("[Weighted] equityShareCapitalScore: " + equityShareCapitalGrowth + " reserveScore: " + reserveGrowth + " debtScore: " + debtGrowth);

            analyzedInfoDto.getBalanceSheetAnalysisInfo().setEquityCapitalScore(StockUtil.convertDoubleToPrecision(equityShareCapitalGrowth,2));
            analyzedInfoDto.getBalanceSheetAnalysisInfo().setDebtScore(StockUtil.convertDoubleToPrecision(debtGrowth,2));
            analyzedInfoDto.getBalanceSheetAnalysisInfo().setReserveScore(StockUtil.convertDoubleToPrecision(reserveGrowth,2));


            double score = equityShareCapitalGrowth + reserveGrowth + debtGrowth;
            if (isReserveGrowContinuously(balanceSheetDtoList)) {
                logger.info("Reserve is growing continuously=> Positive Sign");
                score = score + RSVGROW; //this is a definitly plus sign that reserve is growing continuously
            }

            if (isEquityShareDecreaseOrSame(balanceSheetDtoList)) {
                logger.info("Equity Share is same or reducing continuously=> Positive Sign");
                score = score + EQSAME; //if total equity share is not growing this is positive sign.
            }

            if (isDebtSameOrReducing(balanceSheetDtoList)) {
                logger.info("Debt is same or reducing continuously=> Positive Sign");
                score = score + DEBTDOWN;
            }

            logger.info("Calculated score: " + score);

            analyzedInfoDto.getBalanceSheetAnalysisInfo().setBalanceSheetScore(StockUtil.convertDoubleToPrecision(score, 3));
        }catch(Exception e){
            logger.error("Unable to score Balancesheet: ",e);
        }
    }

    /**
     * If Equity share is same or reducing means promoters has strong hold in company, it is good sign.
     * @param balanceSheetDtoList
     * @return
     */
    private boolean isEquityShareDecreaseOrSame(List<BalanceSheetDto> balanceSheetDtoList){
        Iterator<BalanceSheetDto> it = balanceSheetDtoList.iterator();
        BalanceSheetDto currentYearBalancesheet = null;
        while(it.hasNext()) {
            if (currentYearBalancesheet == null)
                currentYearBalancesheet = it.next();
            else {
                BalanceSheetDto lastYearBalancesheet = it.next();
                if(currentYearBalancesheet.getEquityShareCapital()>lastYearBalancesheet.getEquityShareCapital())
                    return false;
                currentYearBalancesheet = lastYearBalancesheet;
            }
        }
        return true;
    }

    /**
     * If Reserve is growing continuously, it is a good blanacesheet.
     * @param balanceSheetDtoList
     * @return
     */
    private boolean isReserveGrowContinuously(List<BalanceSheetDto> balanceSheetDtoList){
        Iterator<BalanceSheetDto> it = balanceSheetDtoList.iterator();
        BalanceSheetDto currentYearBalancesheet = null;
        while(it.hasNext()) {
            if (currentYearBalancesheet == null)
                currentYearBalancesheet = it.next();
            else {
                BalanceSheetDto lastYearBalancesheet = it.next();
                if(currentYearBalancesheet.getReserves()<lastYearBalancesheet.getReserves())
                    return false;
                currentYearBalancesheet = lastYearBalancesheet;
            }
        }
        return true;
    }

    /**
     * If Debt is same or reducing but no event of growing, it is a good sign.
     * @param balanceSheetDtoList
     * @return
     */
    private boolean  isDebtSameOrReducing(List<BalanceSheetDto> balanceSheetDtoList){
        Iterator<BalanceSheetDto> it = balanceSheetDtoList.iterator();
        BalanceSheetDto currentYearBalancesheet = null;
        while(it.hasNext()) {
            if (currentYearBalancesheet == null)
                currentYearBalancesheet = it.next();
            else {
                BalanceSheetDto lastYearBalancesheet = it.next();
                if(currentYearBalancesheet.getDebt()>lastYearBalancesheet.getDebt())
                    return false;
                currentYearBalancesheet = lastYearBalancesheet;
            }
        }
        return true;
    }


    private double calcEquityShareCapitalGrowth(double currentYearEquity, double lastYearEquity){
        return (currentYearEquity-lastYearEquity)/lastYearEquity;
    }

    private double calcReserveGrowth(double currentYearReserve, double lastYearReserve){
        return (currentYearReserve-lastYearReserve)/lastYearReserve;
    }

    private double calcDebtGrowth(double currentYearDebt, double lastYearDebt){
        if(lastYearDebt==0)
            return currentYearDebt;
        return (currentYearDebt-lastYearDebt)/lastYearDebt;
    }

}
