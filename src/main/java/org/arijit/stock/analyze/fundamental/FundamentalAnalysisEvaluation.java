package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;

/**
 * This class is wrapper for all fundamental analysis.
 *
 */
public class FundamentalAnalysisEvaluation{

    private static final Logger logger = LogManager.getLogger(FundamentalAnalysisEvaluation.class);

    private void doFundamentalAnalysis(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year){
        /**
         * 1. Do Balance sheet analysis
         * 2. Do Profit and Loss Analysis
         * 3. Do Yearly report Analysis
         * 4. Do Ratio Analysis
         * 5. DO valuation Analysis
         * 6. Do Target Price Calculation
         */


        try{
            logger.info("==========================BalanceSheetEvaluation==================================");
            BalanceSheetEvaluation.getInstance().evaluate(fundamentalInfoDto,analyzedInfoDto,year);
        }catch(Exception e){
            logger.error("Unable to evaluate ",e);
        }

        try{
            logger.info("==========================YearlyReportEvaluation==================================");
            YearlyReportEvaluation.getInstance().evaluate(fundamentalInfoDto,analyzedInfoDto,year);
        }catch(Exception e){
            logger.error("Unable to evaluate ",e);
        }

        try {
            // if you want to pass seperate year read year from cofiguration in future.
            logger.info("==========================RatiosEvaluation==================================");
            RatiosEvaluation.getInstance().evaluate(fundamentalInfoDto,analyzedInfoDto,year);
        } catch (Exception e) {
            logger.error("Unable to evaluate EV/EBITDA model",e);
        }

        try {
            logger.info("==========================EVEBITDAValuation==================================");
            // if you want to pass seperate year read year from cofiguration in future.
            EVEBITDAValuation.getInstance().evaluate(fundamentalInfoDto,analyzedInfoDto,year);
        } catch (Exception e) {
            logger.error("Unable to evaluate EV/EBITDA model",e);
        }
    }


    public void evaluate(FundamentalInfoDto fundamentalInfoDto, int year) {
        AnalyzedInfoDto analyzedInfoDto = AnalyzedInfoDto.create();
        doFundamentalAnalysis(fundamentalInfoDto,analyzedInfoDto,year);
    }



    public static FundamentalAnalysisEvaluation getInstance(){
        return FundamentalAnalysisEvaluationInner.getInstance();
    }

    private static class FundamentalAnalysisEvaluationInner{
        private static FundamentalAnalysisEvaluation instance = new FundamentalAnalysisEvaluation();
        private FundamentalAnalysisEvaluationInner(){
            logger.info("Initialized FundamentalAnalysisEvaluation.");
        }
        public static FundamentalAnalysisEvaluation getInstance(){
            return instance;
        }
    }


}
