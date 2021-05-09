package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.QuarterlyReportDTO;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.List;
import java.util.stream.Collectors;

public class StockValuation {
    private static final Logger logger = LogManager.getLogger(StockValuation.class);

    private StockValuation(){
    }

    public void quarterlyinrinsicValuation(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto) throws Exception {
        if(!QuarterlyReportEvaluation.getInstance().isEvaluated())
            throw new Exception("QuarterlyReports are not Evaluated Yet");
        if(!RatiosEvaluation.getInstance().isEvaluated())
            throw new Exception("Ratios are not evaluated yet");
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

    public static StockValuation getInstance(){
        return InnerClass.getInstance();
    }

    private static class InnerClass{
        private static final StockValuation instance = new StockValuation();

        private  InnerClass(){

        }
        static StockValuation getInstance() {
            return instance;
        }
    }
}
