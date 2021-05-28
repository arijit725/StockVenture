package org.arijit.stock.analyze.score;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.ProfitAndLossDto;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ProfitAndLossScore implements IScore{
    private static final Logger logger = LogManager.getLogger(ProfitAndLossScore.class);
    //Equity Share Capital weight
    private static final int SGW=5;
    private static final int PW = 7;
    // debt weight.
    private static final int IW=3;


    @Override
    public void score(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int years) {

        List<ProfitAndLossDto> profitAndLossDtoList = fundamentalInfoDto.getProfitAndLossDtoList().stream().limit(years).collect(Collectors.toList());
        Iterator<ProfitAndLossDto> it = profitAndLossDtoList.iterator();
        double salesGrowth = 0;
        double profitGrowth=0;
        double interestGrowth = 0;

        ProfitAndLossDto currentProfitAndLossDto = null;
        int i=0;
        while(it.hasNext()){
            if(currentProfitAndLossDto==null)
                currentProfitAndLossDto = it.next();
            else{
                double pow = (double)1/i;
                ProfitAndLossDto lastYearProfitAndLossDto = it.next();

                double tmpSGrowth = calcSalesGrowth(currentProfitAndLossDto,lastYearProfitAndLossDto);
                if(tmpSGrowth<0){
                    double d = Math.abs(tmpSGrowth);
                    tmpSGrowth = -1*Math.pow(d,pow); //we should always raise power to positive absolute value
                }
                else {
                    tmpSGrowth = Math.pow(tmpSGrowth, pow); // the more recent year, importance is more, more older year imortance is less
                }
                salesGrowth = salesGrowth + tmpSGrowth;

                //here we are calculating growth ratio. Also newer growth has higher impact than older growth.
                double tmpRGrowth = calcProfitGrowth(currentProfitAndLossDto,lastYearProfitAndLossDto);
                if(tmpRGrowth<0){
                    double d = Math.abs(tmpRGrowth);
                    tmpRGrowth = -1 * Math.pow(d,pow); //we should always raise power to positive absolute value
                }else{
                    tmpRGrowth = Math.pow(tmpRGrowth,pow);
                }
                profitGrowth = profitGrowth + tmpRGrowth;

                // if debt is not growing that is a positive indication. So to get that sign default debt growth is marked as 1.
                double tmpDGrowth = 1-calcInterestGrowth(currentProfitAndLossDto.getInterest(), lastYearProfitAndLossDto.getInterest());
                if(tmpDGrowth<0){
                    double d = Math.abs(tmpDGrowth);
                    tmpDGrowth = -1*d;
                }else {
                    tmpDGrowth = Math.pow(tmpDGrowth, pow);
                }
                logger.info("Current Year Interest: "+currentProfitAndLossDto.getInterest()+" last year Interest: "+lastYearProfitAndLossDto.getInterest()+" growth: "+tmpDGrowth);
                interestGrowth = interestGrowth + tmpDGrowth;

                currentProfitAndLossDto = lastYearProfitAndLossDto;
            }
            i++;
        }

        logger.info("Sales Growth: "+salesGrowth+" Profit Growth: "+profitGrowth+" Interest Growth: "+interestGrowth);
        salesGrowth = SGW * salesGrowth;
        profitGrowth = PW*profitGrowth;
        interestGrowth = IW * interestGrowth;
        logger.info("[weighted] Sales Growth: "+salesGrowth+" Profit Growth: "+profitGrowth+" Interest Growth: "+interestGrowth);
        analyzedInfoDto.getProfitAndLossAnalysisInfo().setNetSalesScore(StockUtil.convertDoubleToPrecision(salesGrowth,2));
        analyzedInfoDto.getProfitAndLossAnalysisInfo().setNetProfitScore(StockUtil.convertDoubleToPrecision(profitGrowth,2));
        analyzedInfoDto.getProfitAndLossAnalysisInfo().setInterestScore(StockUtil.convertDoubleToPrecision(interestGrowth,2));
        double score = salesGrowth+profitGrowth+interestGrowth;

        if(isNetSalesGrowContinuously(profitAndLossDtoList)){
            String stmt = "Net Sales is growing continuously=> Positive Sign";
            logger.info(stmt);

            score = score+years; //this is a definitly plus sign that reserve is growing continuously
        }

        if(isNetProfitGrowsContinuously(profitAndLossDtoList)){
            logger.info("Net Profit is growing continuously=> Positive Sign");
            score = score+years; //this is a definitly plus sign that reserve is growing continuously
        }

        if(isInterestSameOrReducing(profitAndLossDtoList)){
            logger.info("Debt is same or reducing continuously=> Positive Sign");
            score = score+years;
        }

        logger.info("Calculated score: "+score);

        analyzedInfoDto.getProfitAndLossAnalysisInfo().setProfiAndLossScore(StockUtil.convertDoubleToPrecision(score,3));
    }


    /**
     * If Net Sales is growing continuously, it is a good blanacesheet.
     * @param profitAndLossDtoList
     * @return
     */
    private boolean isNetProfitGrowsContinuously(List<ProfitAndLossDto> profitAndLossDtoList){
        Iterator<ProfitAndLossDto> it = profitAndLossDtoList.iterator();
        ProfitAndLossDto currentProfitAndLossDto = null;
        while(it.hasNext()) {
            if (currentProfitAndLossDto == null)
                currentProfitAndLossDto = it.next();
            else {
                ProfitAndLossDto lastYearProfitAndLOssDto = it.next();
                if(currentProfitAndLossDto.getNetProfit()<lastYearProfitAndLOssDto.getNetProfit())
                    return false;
                currentProfitAndLossDto = lastYearProfitAndLOssDto;
            }
        }
        return true;
    }

    /**
     * If Net Sales is growing continuously, it is a good blanacesheet.
     * @param profitAndLossDtoList
     * @return
     */
    private boolean isNetSalesGrowContinuously(List<ProfitAndLossDto> profitAndLossDtoList){
        Iterator<ProfitAndLossDto> it = profitAndLossDtoList.iterator();
        ProfitAndLossDto currentProfitAndLossDto = null;
        while(it.hasNext()) {
            if (currentProfitAndLossDto == null)
                currentProfitAndLossDto = it.next();
            else {
                ProfitAndLossDto lastYearProfitAndLOssDto = it.next();
                if(currentProfitAndLossDto.getNetSales()<lastYearProfitAndLOssDto.getNetSales())
                    return false;
                currentProfitAndLossDto = lastYearProfitAndLOssDto;
            }
        }
        return true;
    }

    /**
     * If Interest is same or reducing but no event of growing, it is a good sign.
     * @param profitAndLossDtoList
     * @return
     */
    private boolean isInterestSameOrReducing(List<ProfitAndLossDto> profitAndLossDtoList){
        Iterator<ProfitAndLossDto> it = profitAndLossDtoList.iterator();
        ProfitAndLossDto currentYearProfitAndLossDto = null;
        while(it.hasNext()) {
            if (currentYearProfitAndLossDto == null)
                currentYearProfitAndLossDto = it.next();
            else {
                ProfitAndLossDto lastYearProfitAndLossDto = it.next();
                if(currentYearProfitAndLossDto.getInterest()>lastYearProfitAndLossDto.getInterest())
                    return false;
                currentYearProfitAndLossDto = lastYearProfitAndLossDto;
            }
        }
        return true;
    }


    private double calcSalesGrowth(ProfitAndLossDto currentYearDto, ProfitAndLossDto lastYearDto){
        double netSalesGrowth = (currentYearDto.getNetSales()-lastYearDto.getNetSales())/lastYearDto.getNetSales();
        logger.info("FY: " + currentYearDto.getDate()+" -" +lastYearDto.getDate()+" Net Sales Growth: "+netSalesGrowth);
        return netSalesGrowth;
    }

    private double calcProfitGrowth(ProfitAndLossDto currentYearDto, ProfitAndLossDto lastYearDto){
        double netProfitGrowth = (currentYearDto.getNetProfit()-lastYearDto.getNetProfit())/lastYearDto.getNetProfit();
        logger.info("FY: " + currentYearDto.getDate()+" -" +lastYearDto.getDate()+" Net Profit Growth: "+netProfitGrowth);
        return netProfitGrowth;
    }


    private double calcInterestGrowth(double currentYearInterest, double lastYearInterest){
        if(lastYearInterest==0)
            return currentYearInterest;
        return (currentYearInterest-lastYearInterest)/lastYearInterest;
    }

}
