package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.enums.ValuationEnums;

/**
 * This class will evaluate different ratios, will calculate future possible ratios.
 */
public class RatiosEvaluation implements IFundamentalEvaluation{

    private static Logger logger = LogManager.getLogger(RatiosEvaluation.class);
    private RatiosEvaluation(){

    }


    private void calcForwardPE(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year){
//        double currentSharePrice,FundamentalInfoDto fundamentalInfoDto,int year
        double currentSharePrice = fundamentalInfoDto.getCompanyDto().getCurrentSharePrice();
        double estimatedEPS = analyzedInfoDto.getYearlyReportAnalysisInfo().getEstimatedEPS();
        logger.info("currentSharePrice: "+currentSharePrice+" estimatedEPS: "+estimatedEPS);
        double forwardPERatio = calcForwardPE(currentSharePrice,estimatedEPS);

        double currentPERatio = fundamentalInfoDto.getRatiosDtoList().get(0).getPeRatio();
        ValuationEnums valuationEnums = analyzeForwardPE(currentPERatio,forwardPERatio);

        logger.info("CurrentPERation: "+currentPERatio+" ForwardPERatio: "+forwardPERatio+" Valuation: "+valuationEnums);

        analyzedInfoDto.getRatioAnalysisInfo().setForwardPERatio(forwardPERatio);
        analyzedInfoDto.getRatioAnalysisInfo().setForwardPEValuation(valuationEnums);
    }



    public double calcForwardPE(double currentSharePrice,double estimatedEPS ){
        logger.info("currentSharePrice: "+currentSharePrice+" estimatedEPS: "+estimatedEPS);
        double forwardPERatio = currentSharePrice / estimatedEPS;
        logger.info("Calculated forward PE: "+forwardPERatio);
        return forwardPERatio;

    }
    private ValuationEnums analyzeForwardPE(double currentPE,double forwardPE){
        if(forwardPE<currentPE)
            return ValuationEnums.UNDER_VALUED;
        if(forwardPE==currentPE)
            return ValuationEnums.FAIR_VALUED;
        else
            return ValuationEnums.OVER_VALUED;
    }

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        calcForwardPE(fundamentalInfoDto,analyzedInfoDto,year);
    }


    public static RatiosEvaluation getInstance(){
        return InnerClass.getInstance();
    }



    private static class InnerClass{
        private static final RatiosEvaluation instance = new RatiosEvaluation();
        private  InnerClass(){

        }
        static RatiosEvaluation getInstance() {
            return instance;
        }
    }
}


