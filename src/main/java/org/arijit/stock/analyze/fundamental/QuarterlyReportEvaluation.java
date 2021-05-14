package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.QuarterlyReportDTO;
import org.arijit.stock.analyze.util.FundamentalAnalysisUtil;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Quarterly intrinsic value evaluation model needs to be evaluated in each quarter.
 * www.screener.in is a good source from where we can get basic parameters for model calculation.
 */
public class QuarterlyReportEvaluation implements IFundamentalEvaluation {

    private static final Logger logger = LogManager.getLogger(QuarterlyReportEvaluation.class);
    private boolean evaluated = false;
    @Override
    public boolean isEvaluated() {
        return evaluated;
    }

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        if(fundamentalInfoDto==null)
            throw new Exception("FundamentalInfo not found");
        if(analyzedInfoDto==null || analyzedInfoDto.getMisleneousAnalysisInfo()==null)
            throw new Exception("AnalzedInfo not found");
        calcEstimatedEPS(fundamentalInfoDto,analyzedInfoDto);
        calcGrowth(fundamentalInfoDto,analyzedInfoDto);
        calcAvgEPsGrowth(fundamentalInfoDto,analyzedInfoDto);
        evaluated = true;
    }


    private void calcGrowth(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto){
        List<QuarterlyReportDTO> quarterlyReportDTOList = fundamentalInfoDto.getQuarterlyReportDtoList();
        Iterator<QuarterlyReportDTO> it = quarterlyReportDTOList.iterator();
        QuarterlyReportDTO currentQuarterlyReportDto = null;
        while(it.hasNext()){
            if(currentQuarterlyReportDto==null)
                currentQuarterlyReportDto = it.next();
            else{
                QuarterlyReportDTO prevQuarterlyReportDto = it.next();
                double epsGrowth = 0;
                if(prevQuarterlyReportDto.getEps()!=0){
                    epsGrowth = (currentQuarterlyReportDto.getEps() - prevQuarterlyReportDto.getEps())/prevQuarterlyReportDto.getEps();
                    epsGrowth = (double)epsGrowth*100;
                }
                analyzedInfoDto.getQuarterlyReportAnalysisInfo().addQuarterlyReportGrowths(currentQuarterlyReportDto.getDate(),"eps", StockUtil.convertDoubleToPrecision(epsGrowth,2));
                currentQuarterlyReportDto = prevQuarterlyReportDto;
            }
        }
    }

    private void calcAvgEPsGrowth(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto){
        int qtr = 4; //average eps growth will be of always for last 4 quarter.
        List<QuarterlyReportDTO> quarterlyReportDTOList = fundamentalInfoDto.getQuarterlyReportDtoList().stream().limit(qtr).collect(Collectors.toList());
        Iterator<QuarterlyReportDTO> it = quarterlyReportDTOList.iterator();
        QuarterlyReportDTO currentQuarterlyReportDto = null;
        double avgGrowth = 0;
        while(it.hasNext()){
            if(currentQuarterlyReportDto==null)
                currentQuarterlyReportDto = it.next();
            else{
                QuarterlyReportDTO prevQuarterlyReportDto = it.next();
                double epsGrowth = 0;
                if(prevQuarterlyReportDto.getEps()!=0){
                    epsGrowth = (currentQuarterlyReportDto.getEps() - prevQuarterlyReportDto.getEps())/prevQuarterlyReportDto.getEps();
                    epsGrowth = (double)epsGrowth*100;
                }
                avgGrowth = avgGrowth + epsGrowth;
                currentQuarterlyReportDto = prevQuarterlyReportDto;
            }
        }
        avgGrowth = (double) avgGrowth/qtr;
        analyzedInfoDto.getQuarterlyReportAnalysisInfo().setAvgEPSGrowth(avgGrowth);
    }
    private void calcEstimatedEPS(FundamentalInfoDto fundamentalInfoDto,AnalyzedInfoDto analyzedInfoDto){
        int qtr = 4;
        List<QuarterlyReportDTO> quarterlyReportDTOS = fundamentalInfoDto.getQuarterlyReportDtoList().stream().limit(qtr).collect(Collectors.toList());
        logger.info("Last "+qtr+" years QuarterlyReport : "+quarterlyReportDTOS);
        QuarterlyReportDTO endQtrReport =  quarterlyReportDTOS.get(0);
        QuarterlyReportDTO startQtrReport = quarterlyReportDTOS.get(qtr-1);// starting from index 0
        logger.info("End year QuarterlyReport: "+endQtrReport);
        logger.info("Start year QuarterlyReport: "+startQtrReport);
        double ttmEPS = (double) quarterlyReportDTOS.stream().mapToDouble(mapper->mapper.getEps()).sum();
        logger.info("Querterly TTM EPS: "+ttmEPS);
        double estimatedEPS = 0;
        if(startQtrReport.getEps()>0) {
            estimatedEPS = calcEstimatedEPS(ttmEPS, endQtrReport.getEps(), startQtrReport.getEps(), qtr);
        }
        calcEstimatedEPS(ttmEPS,quarterlyReportDTOS,qtr);
        analyzedInfoDto.getQuarterlyReportAnalysisInfo().setEstimatedEPSCAGR(estimatedEPS);
        analyzedInfoDto.getQuarterlyReportAnalysisInfo().setTtmEPS(ttmEPS);
        logger.info("Quarterly CAGR Estimated EPS: "+analyzedInfoDto.getQuarterlyReportAnalysisInfo().getEstimatedEPSCAGR());
    }

    /**
     * There could be chances that one quarter result shows EPS as 0 or negative or too high.
     * Now if we calculate in CAGR way, and starting or ending quarter result shows such high or low eps, that will give
     * wrong impression in estimated eps. Thats why we are taking avg eps instead of cagr.
     * @param quarterlyReportDTOS
     * @param qtr
     * @return
     */
    public double calcEstimatedEPS(double ttmEPS, List<QuarterlyReportDTO> quarterlyReportDTOS, int qtr){
        double epsGrowth = 0;
        Iterator<QuarterlyReportDTO> it = quarterlyReportDTOS.iterator();
        QuarterlyReportDTO currenYearReport = null;
        while(it.hasNext()){
            if(currenYearReport==null){
                currenYearReport = it.next();
            }
            else{
                QuarterlyReportDTO prevYear = it.next();
                double tmpGrowth = (currenYearReport.getEps()-prevYear.getEps())/prevYear.getEps()*100;
                epsGrowth = epsGrowth+tmpGrowth;
                currenYearReport = prevYear;
            }
        }
        double avgEPS = epsGrowth/(qtr-1);
        double estimatedEPS = ((ttmEPS*avgEPS)/100)+ttmEPS;
        logger.info("[Quarterly] Calculated Estimated EPS: "+estimatedEPS);
        return estimatedEPS;
    }

    public double calcEstimatedEPS(double ttmEPS,double endingEPS, double startingEPS,int qtr){
        double epsCAGR = FundamentalAnalysisUtil.cagr(endingEPS, startingEPS,qtr);
        logger.info("Quarterly EPS Growth Rate CAGR: "+epsCAGR);
        double estimatedEPS = ((ttmEPS*epsCAGR)/100)+ttmEPS;
        logger.info("[Quarterly] Calculated Estimated EPS [CAGR]: "+estimatedEPS);
        return estimatedEPS;
    }
//    private void calcQtlIntrinsicValue(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto){
//        List<QuarterlyReportDTO> last4QtrReport = fundamentalInfoDto.getQuarterlyReportDtoList().stream().limit(4).collect(Collectors.toList());
//        logger.info("Last 4 quarter details: "+last4QtrReport);
//        double ttmEPS = last4QtrReport.stream().mapToDouble(mapper->mapper.getEps()).sum();
//        logger.info("Last 4 Quarter EPS sum: "+ttmEPS);
//        double avgGrowthRate = 0;
//
//        if(fundamentalInfoDto.getCompanyDto().isSeasonal()){
//            //get last 4 YOY sales growth
//            avgGrowthRate = fundamentalInfoDto.getQuarterlyReportDtoList().stream().limit(4).mapToDouble(mapper->mapper.getYoySalesGrowth()).sum();
//            avgGrowthRate = avgGrowthRate/4;
//            logger.info("Companys is Seasonal: avg of last 4 sales YOY growth :"+avgGrowthRate);
//        }
//        else{
//            //get last 2 YOY sales growth
//            avgGrowthRate = fundamentalInfoDto.getQuarterlyReportDtoList().stream().limit(2).mapToDouble(mapper->mapper.getYoySalesGrowth()).sum();
//            avgGrowthRate = avgGrowthRate/2;
//            logger.info("Companys is not Seasonal: avg of last 2 sales YOY growth :"+avgGrowthRate);
//        }
//        double estimatedEPS = ttmEPS*((avgGrowthRate/100)+1);
//        logger.info("Estimated EPS for this quarter: "+estimatedEPS);
//        double avg3mnthPERatio = analyzedInfoDto.getMisleneousAnalysisInfo().getAvg3MotnthPERatio();
//        logger.info("last 3 months average PE: "+avg3mnthPERatio);
//        double targetPrice = estimatedEPS*avg3mnthPERatio;
//        String targetPriceStr = StockUtil.convertDoubleToPrecision(targetPrice,2);
//        logger.info("Target Price using Quarterly Intrinsic Valuation: "+targetPriceStr);
//        analyzedInfoDto.getTargetPriceEstimationDto().setQuarterlyIntrinsicTargetPrice(targetPriceStr);
//
//
//    }

    public static QuarterlyReportEvaluation getInstance(){
        return InnerClass.getInstance();
    }
    private static class InnerClass{
        private static final QuarterlyReportEvaluation instance = new QuarterlyReportEvaluation();
        private  InnerClass(){

        }
        static QuarterlyReportEvaluation getInstance() {
            return instance;
        }
    }
}
