package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.analysisdto.RatioAnalysisInfo;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.RatiosDto;
import org.arijit.stock.analyze.enums.ValuationEnums;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.*;

/**
 * This class will evaluate different ratios, will calculate future possible ratios.
 */
public class RatiosEvaluation implements IFundamentalEvaluation{

    private static Logger logger = LogManager.getLogger(RatiosEvaluation.class);
    private RatiosEvaluation(){

    }
    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        List<RatiosDto> ratioDtoList  = fundamentalInfoDto.getRatiosDtoList();
        if(ratioDtoList.isEmpty()||ratioDtoList.size()<year)
            throw new Exception("Number of year exceeds RatioDtoList list");
//        calcForwardPE(fundamentalInfoDto,analyzedInfoDto,year);
        calcGrowth(analyzedInfoDto.getRatioAnalysisInfo(),ratioDtoList);
        evaluateMultiBagger(analyzedInfoDto,ratioDtoList);
    }


    private void calcGrowth(RatioAnalysisInfo ratioAnalysisInfo, List<RatiosDto> ratiosDtoList){
        Iterator<RatiosDto> iterator = ratiosDtoList.iterator();
        RatiosDto lastRatiosDto = null;
        Map<String, HashMap<String, String>> ratioGrowthsDtoMap = ratioAnalysisInfo.getRatioGrowthsDtoMap();
        int precision = 2;
        while(iterator.hasNext()){
            if(lastRatiosDto==null)
                lastRatiosDto = iterator.next();
            else{
                RatiosDto currentRatiosDto = iterator.next();
                double peRatioGrowth = (lastRatiosDto.getPeRatio()-currentRatiosDto.getPeRatio())/currentRatiosDto.getPeRatio();
                peRatioGrowth = (double) peRatioGrowth*100;
                ratioAnalysisInfo.addRatioGrowths(lastRatiosDto.getDate(),"peRatio",StockUtil.convertDoubleToPrecision(peRatioGrowth, precision));

                double pbRatioGrowth = (lastRatiosDto.getPbRatio()-currentRatiosDto.getPbRatio())/currentRatiosDto.getPbRatio();
                pbRatioGrowth = (double) pbRatioGrowth*100;
                ratioAnalysisInfo.addRatioGrowths(lastRatiosDto.getDate(),"pbRatio",StockUtil.convertDoubleToPrecision(pbRatioGrowth, precision));

                double roeGrowth = (lastRatiosDto.getRoe()-currentRatiosDto.getRoe())/currentRatiosDto.getRoe();
                roeGrowth = (double) roeGrowth*100;
                ratioAnalysisInfo.addRatioGrowths(lastRatiosDto.getDate(),"roe",StockUtil.convertDoubleToPrecision(roeGrowth, precision));

                double evGrowth = (lastRatiosDto.getEv()-currentRatiosDto.getEv())/currentRatiosDto.getEv();
                evGrowth = (double) evGrowth*100;
                ratioAnalysisInfo.addRatioGrowths(lastRatiosDto.getDate(),"ev",StockUtil.convertDoubleToPrecision(evGrowth, precision));

                double evEbitdaGrowth = (lastRatiosDto.getEvEbitda()-currentRatiosDto.getEvEbitda())/currentRatiosDto.getEvEbitda();
                evEbitdaGrowth = (double) evEbitdaGrowth*100;
                ratioAnalysisInfo.addRatioGrowths(lastRatiosDto.getDate(),"evEbitda",StockUtil.convertDoubleToPrecision(evEbitdaGrowth, precision));

                lastRatiosDto = currentRatiosDto;
//                RatioAnalysisInfo.RatioGrowthsDto ratioGrowthsDto = RatioAnalysisInfo.RatioGrowthsDto.create(lastRatiosDto.getDate())
//                        .setPeRatio(StockUtil.convertDoubleToPrecision(peRatioGrowth, precision))
//                        .setPbRatio(StockUtil.convertDoubleToPrecision(pbRatioGrowth,precision))
//                        .setRoe(StockUtil.convertDoubleToPrecision(roeGrowth,precision))
//                        .setEv(StockUtil.convertDoubleToPrecision(evGrowth,precision))
//                        .setEvEbitda(StockUtil.convertDoubleToPrecision(evEbitdaGrowth,precision));
            }

        }
    }
    private void evaluateMultiBagger(AnalyzedInfoDto analyzedInfoDto, List<RatiosDto> ratiosDtoList){

        double growthThreshold = 1;
        Iterator<RatiosDto> iterator = ratiosDtoList.iterator();
        RatiosDto lastRatiosDto=null;
        boolean isMultiBagger =true;
        while(iterator.hasNext()){
            if(lastRatiosDto==null){
                lastRatiosDto = iterator.next();
                continue;
            }

            RatiosDto preVRatioDto = iterator.next();
            if((lastRatiosDto.getRoe()-preVRatioDto.getRoe()>0)
            && ((lastRatiosDto.getPbRatio()-preVRatioDto.getPbRatio()<growthThreshold))){
                continue;
            }
            else{
                logger.info("Alarm in MultiBagger: between years: "+lastRatiosDto.getDate()+" - "+preVRatioDto.getDate()
                +" ROE Changes: "+(lastRatiosDto.getRoe()-preVRatioDto.getRoe()
                +" PB Changes: "+(lastRatiosDto.getPbRatio()-preVRatioDto.getPbRatio())));
                isMultiBagger =false;
                break;
            }

        }
        analyzedInfoDto.getRatioAnalysisInfo().setPossibilityOfMultiBagger(isMultiBagger);
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



    private double calcForwardPE(double currentSharePrice,double estimatedEPS ){
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


