package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.util.StockUtil;

public class BenjaminGrahamValuation implements  IFundamentalEvaluation {

    private static Logger logger = LogManager.getLogger(BenjaminGrahamValuation.class);

    private boolean evaluated = false;
    @Override
    public boolean isEvaluated() {
        return evaluated;
    }

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        analyzedInfoDto.getBenjaminGrahamValuationModelDto().setEvluated(false);
        double epsTTM = fundamentalInfoDto.getCompanyDto().getTtmeps();
        double epsGrowthRate = analyzedInfoDto.getBenjaminGrahamValuationModelDto().getGrowthRate();
        double aaaBondY = analyzedInfoDto.getBenjaminGrahamValuationModelDto().getAaaBondY();
        double repoRate = analyzedInfoDto.getBenjaminGrahamValuationModelDto().getRepoRate();
        double zPe = analyzedInfoDto.getBenjaminGrahamValuationModelDto().getPeZero();
        logger.info(" Eps TTM: "+epsTTM+" eps Growth Rate: "+epsGrowthRate+" AAA Bond Yield: "+aaaBondY+" repo rate: "+repoRate);
        double neumerator = zPe + (1*epsGrowthRate);
        neumerator = neumerator * epsTTM * repoRate;
        double intrinsicValue = neumerator/aaaBondY;
        intrinsicValue = Double.parseDouble(StockUtil.convertDoubleToPrecision(intrinsicValue,2));

        logger.info("Intrinisc Value: "+intrinsicValue);
        analyzedInfoDto.getBenjaminGrahamValuationModelDto().setFinalIntrinsicValue(intrinsicValue);
        analyzedInfoDto.getBenjaminGrahamValuationModelDto().setEvluated(true);
    }


    public static BenjaminGrahamValuation getInstance(){
        return Inner.getInstance();
    }



    private static class Inner{
        private static final BenjaminGrahamValuation instance = new BenjaminGrahamValuation();
        private  Inner(){

        }
        static BenjaminGrahamValuation getInstance() {
            return instance;
        }
    }
}
