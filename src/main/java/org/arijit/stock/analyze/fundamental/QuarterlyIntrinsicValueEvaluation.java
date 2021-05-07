package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.QuarterlyReportDTO;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Quarterly intrinsic value evaluation model needs to be evaluated in each quarter.
 * www.screener.in is a good source from where we can get basic parameters for model calculation.
 */
public class QuarterlyIntrinsicValueEvaluation implements IFundamentalEvaluation {

    private static final Logger logger = LogManager.getLogger(QuarterlyIntrinsicValueEvaluation.class);

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        if(fundamentalInfoDto==null)
            throw new Exception("FundamentalInfo not found");
        if(analyzedInfoDto==null || analyzedInfoDto.getMisleneousAnalysisInfo()==null)
            throw new Exception("AnalzedInfo not found");
        calcQtlIntrinsicValue(fundamentalInfoDto,analyzedInfoDto);
    }

    public void calcQtlIntrinsicValue(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto){
        List<QuarterlyReportDTO> last4QtrReport = fundamentalInfoDto.getQuarterlyReportDtoList().stream().limit(4).collect(Collectors.toList());
        logger.info("Last 4 quarter details: "+last4QtrReport);
        double ttmEPS = last4QtrReport.stream().mapToDouble(mapper->mapper.getEps()).sum();
        logger.info("Last 4 Quarter EPS sum: "+ttmEPS);
        double avgGrowthRate = 0;

        if(fundamentalInfoDto.getCompanyDto().isSeasonal()){
            //get last 4 YOY sales growth
            avgGrowthRate = fundamentalInfoDto.getQuarterlyReportDtoList().stream().limit(4).mapToDouble(mapper->mapper.getYoySalesGrowth()).sum();
            avgGrowthRate = avgGrowthRate/4;
            logger.info("Companys is Seasonal: avg of last 4 sales YOY growth :"+avgGrowthRate);
        }
        else{
            //get last 2 YOY sales growth
            avgGrowthRate = fundamentalInfoDto.getQuarterlyReportDtoList().stream().limit(2).mapToDouble(mapper->mapper.getYoySalesGrowth()).sum();
            avgGrowthRate = avgGrowthRate/2;
            logger.info("Companys is not Seasonal: avg of last 2 sales YOY growth :"+avgGrowthRate);
        }
        double estimatedEPS = ttmEPS*((avgGrowthRate/100)+1);
        logger.info("Estimated EPS for this quarter: "+estimatedEPS);
        double avg3mnthPERatio = analyzedInfoDto.getMisleneousAnalysisInfo().getAvg3MotnthPERatio();
        logger.info("last 3 months average PE: "+avg3mnthPERatio);
        double targetPrice = estimatedEPS*avg3mnthPERatio;
        String targetPriceStr = StockUtil.convertDoubleToPrecision(targetPrice,2);
        logger.info("Target Price using Quarterly Intrinsic Valuation: "+targetPriceStr);
        analyzedInfoDto.getTargetPriceEstimationDto().setQuarterlyIntrinsicTargetPrice(targetPriceStr);


    }

    public static QuarterlyIntrinsicValueEvaluation getInstance(){
        return InnerClass.getInstance();
    }
    private static class InnerClass{
        private static final QuarterlyIntrinsicValueEvaluation instance = new QuarterlyIntrinsicValueEvaluation();
        private  InnerClass(){

        }
        static QuarterlyIntrinsicValueEvaluation getInstance() {
            return instance;
        }
    }
}
