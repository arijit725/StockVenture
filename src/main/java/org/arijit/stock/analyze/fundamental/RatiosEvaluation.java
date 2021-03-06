package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.analysisdto.RatioAnalysisInfo;
import org.arijit.stock.analyze.dto.*;
import org.arijit.stock.analyze.enums.AnalysisEnums;
import org.arijit.stock.analyze.enums.ValuationEnums;
import org.arijit.stock.analyze.util.FundamentalAnalysisUtil;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class will evaluate different ratios, will calculate future possible ratios.
 */
public class RatiosEvaluation implements IFundamentalEvaluation{

    private static Logger logger = LogManager.getLogger(RatiosEvaluation.class);

    private boolean evaluated;
    private RatiosEvaluation(){

    }

    @Override
    public boolean isEvaluated() {
        return evaluated;
    }

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        List<RatiosDto> ratioDtoList  = fundamentalInfoDto.getRatiosDtoList();
        if(ratioDtoList.isEmpty()||ratioDtoList.size()<year)
            throw new Exception("Number of year exceeds RatioDtoList list");
        analyzedInfoDto.getRatioAnalysisInfo().clear();
        try {
            calcForwardPE(fundamentalInfoDto, analyzedInfoDto, year);
        }catch(Exception e){
            logger.error("Unable to calculate forwardPE",e);
        }
        try {
            calcGrowth(analyzedInfoDto.getRatioAnalysisInfo(), ratioDtoList);
        }catch (Exception e){
            logger.error("Unable to calcuation ratio growth",e);
        }
        try {
            calcPEGRatio(fundamentalInfoDto, analyzedInfoDto, year);
        }catch (Exception e){
            logger.error("Unable to calculate PEG ratio",e);
        }
        try {
            evaluateMultiBagger(analyzedInfoDto, ratioDtoList);
        }catch (Exception e){
            logger.error("Unable to evaluate Mutlibagger",e);
        }
        try {
            evaluatePE(analyzedInfoDto, fundamentalInfoDto.getCompanyDto());
        }catch (Exception e){
            logger.error("Unable to evaluate PE",e);
        }
        try {
            generateAnalysisStatement(fundamentalInfoDto, analyzedInfoDto, year);
        }catch (Exception e){
            logger.error("unable to generate analysis statement");
        }
        try {
            peRatioAnalysis(fundamentalInfoDto, analyzedInfoDto, year);
        }catch (Exception e){
            logger.error("Unable to evaluate pe",e);
        }
        evaluated = true;
    }


    private void generateAnalysisStatement(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto,int years){
        List<RatiosDto>  ratiosDtoList = fundamentalInfoDto.getRatiosDtoList().stream().limit(years).collect(Collectors.toList());
        RatiosDto currentRatiosDto = null;

        int peIncreaseCount = 0;

        int roeReduceCount = 0;
        int roeIncreaseCount = 0;

        int pbReduceCount = 0;
        int pbIncreaseCount = 0;

        int debtToEquityReduceCount = 0;
        int debtToEquityIncreaseCount = 0;

        Iterator<RatiosDto> it = ratiosDtoList.iterator();
        while(it.hasNext()){
            if(currentRatiosDto==null){
                currentRatiosDto = it.next();
            }
            else{
                RatiosDto lastRatiosDto = it.next();

                if(currentRatiosDto.getPeRatio()>lastRatiosDto.getPeRatio()){
                    peIncreaseCount++;
                }

                if(currentRatiosDto.getRoe()<lastRatiosDto.getRoe()){
                    roeReduceCount++;
                }
                else if(currentRatiosDto.getRoe()>lastRatiosDto.getRoe()){
                    roeIncreaseCount++;
                }

                if(currentRatiosDto.getPbRatio()<lastRatiosDto.getPbRatio()){
                    pbReduceCount++;
                }
                else if(currentRatiosDto.getPbRatio()>lastRatiosDto.getPbRatio()){
                    pbIncreaseCount++;
                }

                if(currentRatiosDto.getDebtToEquityRatio()<lastRatiosDto.getDebtToEquityRatio()){
                    debtToEquityReduceCount++;
                }
                else if(currentRatiosDto.getDebtToEquityRatio()>lastRatiosDto.getDebtToEquityRatio()){
                    debtToEquityIncreaseCount++;
                }

                currentRatiosDto = lastRatiosDto;
            }
        }
        int continuousCOunt = years-1;
        if(peIncreaseCount == continuousCOunt){
            String statement = "(+) PE ratio is continuously increasing over years. This Could be potential Growth Company [may be for short term]";
            analyzedInfoDto.getRatioAnalysisInfo().addAnalysisStatement(statement, AnalysisEnums.ANALYZED_NEUTRAL);
        }
        if(pbIncreaseCount==continuousCOunt){
            String statement = "PB ratio is continuously increasing over years.";
            analyzedInfoDto.getRatioAnalysisInfo().addAnalysisStatement(statement, AnalysisEnums.ANALYZED_BAD);
        }
        else if(pbReduceCount==continuousCOunt){
            String statement = "PB ratio is continuously decreasing over years. This is a very GOOD sign";
            analyzedInfoDto.getRatioAnalysisInfo().addAnalysisStatement(statement, AnalysisEnums.ANALYZED_GOOD);
        }

        if(roeReduceCount==continuousCOunt){
            String statement = "ROE is continuously decreasing over years. This is a very BAD sign";
            analyzedInfoDto.getRatioAnalysisInfo().addAnalysisStatement(statement, AnalysisEnums.ANALYZED_BAD);
        }
        else if(roeIncreaseCount==continuousCOunt){
            String statement = "ROE is continuously increasing over years. This is a very GOOD sign.";
            analyzedInfoDto.getRatioAnalysisInfo().addAnalysisStatement(statement, AnalysisEnums.ANALYZED_GOOD);
        }
//        else{
//            String statement = "ROE trends is not conclusive";
//            analyzedInfoDto.getRatioAnalysisInfo().addAnalysisStatement(statement,AnalysisEnums.ANALYZED_NEUTRAL);
//        }


        if(debtToEquityReduceCount==continuousCOunt){
            String statement = "Debt-To-Equity ratio is continuously decreasing over years. This is a very GOOD sign";
            analyzedInfoDto.getRatioAnalysisInfo().addAnalysisStatement(statement, AnalysisEnums.ANALYZED_GOOD);
        }
        else if(debtToEquityIncreaseCount==continuousCOunt){
            String statement = "Debt-To-Equity ratio is continuously increasing over years. This is a very BAD sign.";
            analyzedInfoDto.getRatioAnalysisInfo().addAnalysisStatement(statement, AnalysisEnums.ANALYZED_BAD);
        }
        else if(debtToEquityIncreaseCount==0 && debtToEquityReduceCount==0 && ratiosDtoList.get(0).getDebtToEquityRatio()==0){
            String statement = "Debt-To-Equity ratio is consistently 0. This is a very GOOD sign.";
            analyzedInfoDto.getRatioAnalysisInfo().addAnalysisStatement(statement, AnalysisEnums.ANALYZED_GOOD);
        }

        RatiosDto lastYearRatioDto = fundamentalInfoDto.getRatiosDtoList().get(0);

        if(lastYearRatioDto.getEvEbitda()>=6 && lastYearRatioDto.getEvEbitda()<=10){
            String statement = "(+) EV/EBITDA is with in range for ideal investment";
            analyzedInfoDto.getRatioAnalysisInfo().addAnalysisStatement(statement,AnalysisEnums.ANALYZED_GOOD);
        }
    }

    private void peRatioAnalysis(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int years){
        List<YearlyReportDto> yearlyReportDtoList = fundamentalInfoDto.getYearlyReportDtoList().stream().limit(years).collect(Collectors.toList());
        List<RatiosDto> ratiosDtoList = fundamentalInfoDto.getRatiosDtoList().stream().limit(years).collect(Collectors.toList());

        double ratioDiffThreshold = 0.5;
        double epsDiffThreshold = 0.5;

        int ratioDecreaseCount = 0;
        int epsDecreaseCount = 0;
        int ratioIncreaseCount = 0;
        int epsIncreaseCount = 0;

        Iterator<RatiosDto> rit = ratiosDtoList.iterator();
        RatiosDto currentRatioDto = null;
        while(rit.hasNext()){
            if(currentRatioDto==null)
                currentRatioDto = rit.next();
            else{
                RatiosDto prevRatioDto = rit.next();
                if((currentRatioDto.getPeRatio()-prevRatioDto.getPeRatio())<=ratioDiffThreshold)
                    ratioDecreaseCount++;
                else
                    ratioIncreaseCount++;
                currentRatioDto =prevRatioDto;
            }
        }
        Iterator<YearlyReportDto> yit = yearlyReportDtoList.iterator();
        YearlyReportDto curentYearlyReportDto = null;
        while(yit.hasNext()){
            if(curentYearlyReportDto==null)
                curentYearlyReportDto = yit.next();
            else{
                YearlyReportDto prevYearlyReportDto = yit.next();
                if((curentYearlyReportDto.getBasicEPS()-prevYearlyReportDto.getBasicEPS())<=epsDiffThreshold)
                    epsDecreaseCount++;
                else
                    epsIncreaseCount++;
                curentYearlyReportDto =prevYearlyReportDto;
            }
        }
        int continuousCOunt = years-1;
        if(ratioDecreaseCount==continuousCOunt && epsDecreaseCount ==continuousCOunt){
            String statement = "(-) PE ratio and EPS continuously decreasing together. This is very bad sign.";
            analyzedInfoDto.getRatioAnalysisInfo().addAnalysisStatement(statement, AnalysisEnums.ANALYZED_BAD);
        }
        else if(ratioDecreaseCount==continuousCOunt && epsIncreaseCount ==continuousCOunt){
            String statement = "(-) PE ratio decreasing but EPS increasing indicates that stock price is not growing. Check why";
            analyzedInfoDto.getRatioAnalysisInfo().addAnalysisStatement(statement, AnalysisEnums.ANALYZED_BAD);
        }
        else if(ratioDecreaseCount == continuousCOunt){
            String statement = "(-) PE ratio decreasing over year continuously.";
            analyzedInfoDto.getRatioAnalysisInfo().addAnalysisStatement(statement, AnalysisEnums.ANALYZED_BAD);
        }

        if(ratioIncreaseCount == continuousCOunt && epsIncreaseCount ==continuousCOunt){
            String statement = "(+) PE ratio and EPS continuously increasing together. Very Good sign";
            analyzedInfoDto.getRatioAnalysisInfo().addAnalysisStatement(statement, AnalysisEnums.ANALYZED_VERY_GOOD);
        }
        else if(ratioIncreaseCount ==continuousCOunt && epsDecreaseCount ==continuousCOunt){
            String statement = "(-) PE ration is increasing but EPS decreasing continuously. Check if stock price is gorwing based on company long term investment plan or due to market sentiment. May be good for swing trading.";
            analyzedInfoDto.getRatioAnalysisInfo().addAnalysisStatement(statement, AnalysisEnums.ANALYZED_BAD);
        }

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
                RatiosDto prevRatiosDto = iterator.next();


                double peRatioGrowth = (lastRatiosDto.getPeRatio()-prevRatiosDto.getPeRatio());
                if(prevRatiosDto.getPeRatio()!=0) {
                    peRatioGrowth = (double) peRatioGrowth/Math.abs(prevRatiosDto.getPeRatio());
                    peRatioGrowth = (double) peRatioGrowth * 100;
                }
                ratioAnalysisInfo.addRatioGrowths(lastRatiosDto.getDate(),"peRatio",StockUtil.convertDoubleToPrecision(peRatioGrowth, precision));


                double pbRatioGrowth = (lastRatiosDto.getPbRatio()-prevRatiosDto.getPbRatio());
                if(prevRatiosDto.getPbRatio()!=0) {
                    pbRatioGrowth = (double) pbRatioGrowth/Math.abs(prevRatiosDto.getPbRatio());
                    pbRatioGrowth = (double) pbRatioGrowth * 100;
                }
                ratioAnalysisInfo.addRatioGrowths(lastRatiosDto.getDate(),"pbRatio",StockUtil.convertDoubleToPrecision(pbRatioGrowth, precision));

                double roeGrowth = (lastRatiosDto.getRoe() - prevRatiosDto.getRoe());
                if(prevRatiosDto.getRoe()!=0) {
                    roeGrowth = (double) roeGrowth/Math.abs(prevRatiosDto.getRoe());
                    roeGrowth = (double) roeGrowth * 100;
                }
                ratioAnalysisInfo.addRatioGrowths(lastRatiosDto.getDate(), "roe", StockUtil.convertDoubleToPrecision(roeGrowth, precision));

                double evGrowth = (lastRatiosDto.getEv()-prevRatiosDto.getEv())/prevRatiosDto.getEv();
                evGrowth = (double) evGrowth*100;
                ratioAnalysisInfo.addRatioGrowths(lastRatiosDto.getDate(),"ev",StockUtil.convertDoubleToPrecision(evGrowth, precision));

                double evEbitdaGrowth = (lastRatiosDto.getEvEbitda()-prevRatiosDto.getEvEbitda())/prevRatiosDto.getEvEbitda();
                evEbitdaGrowth = (double) evEbitdaGrowth*100;
                ratioAnalysisInfo.addRatioGrowths(lastRatiosDto.getDate(),"evEbitda",StockUtil.convertDoubleToPrecision(evEbitdaGrowth, precision));


                double debtToEquityGrowth = (lastRatiosDto.getDebtToEquityRatio()-prevRatiosDto.getDebtToEquityRatio());
                if(prevRatiosDto.getDebtToEquityRatio()==0)
                    ratioAnalysisInfo.addRatioGrowths(lastRatiosDto.getDate(),"debtToEquityRatio",Double.toString(lastRatiosDto.getDebtToEquityRatio()));
                else {
                    debtToEquityGrowth = debtToEquityGrowth/prevRatiosDto.getDebtToEquityRatio();
                    debtToEquityGrowth = (double) debtToEquityGrowth*100;
                    ratioAnalysisInfo.addRatioGrowths(lastRatiosDto.getDate(),"debtToEquityRatio",StockUtil.convertDoubleToPrecision(debtToEquityGrowth, precision));
                }
                lastRatiosDto = prevRatiosDto;
            }
        }
    }

    private void evaluatePE(AnalyzedInfoDto analyzedInfoDto, CompanyDto companyDto){
        double ttmPE = companyDto.getTtmpe();
        double industryPE = companyDto.getIndustryPE();
        double peGap = (double)(industryPE*20/100);
        double lowerPEThreshold = industryPE-peGap;
        double upperThreshold = industryPE+peGap;

        String peAnalysis = "TTM PE Valuation:";

        if(lowerPEThreshold<ttmPE && upperThreshold>ttmPE){
            logger.info("Stock is Fair Valued. [TTM PE is between PE Thresold (20% of Industry PE) : upperThreshold: "+upperThreshold+" lowerThreshold: "+lowerPEThreshold);
            peAnalysis = peAnalysis+" Stock is Fair Valued. [ (lower, actual,upper)="+lowerPEThreshold+" , "+ttmPE+" , "+upperThreshold+" ]";
        }
        else if(ttmPE<lowerPEThreshold){
            peAnalysis = peAnalysis+" Stock is Under Valued.[ (lower, actual,upper)="+lowerPEThreshold+" , "+ttmPE+" , "+upperThreshold+" ]";
        }
        else if(ttmPE>upperThreshold){
            peAnalysis = peAnalysis+" Stock is Over Valued.[ (lower, actual,upper)="+lowerPEThreshold+" , "+ttmPE+" , "+upperThreshold+" ]";
        }

        analyzedInfoDto.getRatioAnalysisInfo().setTtmPEAnalysis(peAnalysis);
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

    private void calcPEGRatio(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int years){
        List<YearlyReportDto> yearlyReportDtoList= fundamentalInfoDto.getYearlyReportDtoList().stream().limit(years).collect(Collectors.toList());
        logger.info("yearlyReportDtoList: "+yearlyReportDtoList);
//        double avgEPS = yearlyReportDtoList.stream().mapToDouble(mapper->mapper.getEpsGrowthRate()).sum();
//        if(years>=fundamentalInfoDto.getYearlyReportDtoList().size())
//            years=years-1; // if we are reaching maximum input of yearly report, we can not consider last one as there gorwoth will be 0.
//        avgEPS = (double) avgEPS/(years);
        double avgEPS = yearlyReportDtoList.stream().mapToDouble(mapper->mapper.getEpsGrowthRate()).sum();
        avgEPS = (double) avgEPS / (years-1); // we do not have first year growth
        double currentTTMPE = fundamentalInfoDto.getCompanyDto().getTtmpe();
        logger.info("Average EPS for last "+years+" years :: "+avgEPS+" Current TTM PE Ratio :"+currentTTMPE);
        double pegRatio = (double) currentTTMPE/avgEPS;
        logger.info("PEGRatio : "+pegRatio);


        ValuationEnums pegValuation = ValuationEnums.FAIR_VALUED;
        if(pegRatio<=0)
            pegValuation = ValuationEnums.AVOID;
        else if(pegRatio>=4)
            pegValuation = ValuationEnums.OVER_VALUED;
        analyzedInfoDto.getRatioAnalysisInfo().setPEGRatio(StockUtil.convertDoubleToPrecision(pegRatio,2));
        analyzedInfoDto.getRatioAnalysisInfo().setPEGValuation(pegValuation);
        logger.info("PEG Valuation: "+analyzedInfoDto.getRatioAnalysisInfo().getPegRatioAnalysis());
    }

    private void calcForwardPE(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year){
//        double currentSharePrice,FundamentalInfoDto fundamentalInfoDto,int year
        double currentSharePrice = fundamentalInfoDto.getCompanyDto().getCurrentSharePrice();
        double estimatedEPS = analyzedInfoDto.getYearlyReportAnalysisInfo().getEstimatedEPSCAGR();
        double estimatedEPS3Year = eps3yrsCAGR(fundamentalInfoDto);
        logger.info("currentSharePrice: "+currentSharePrice+" estimatedEPS: "+estimatedEPS+" estimatedEPS3Year: "+estimatedEPS3Year);
        double yearlyforwardPERatio = 0;
        if(estimatedEPS3Year!=0) {
            yearlyforwardPERatio = calcForwardPE(currentSharePrice, estimatedEPS3Year);
        }

        double qtrForwardPERatio = caclQtrForwardPE(fundamentalInfoDto,analyzedInfoDto);
//        double currentPERatio = fundamentalInfoDto.getRatiosDtoList().get(0).getPeRatio();
        double currentPERatio = fundamentalInfoDto.getCompanyDto().getTtmpe();
        double forwardPERatio = 0;
        if(yearlyforwardPERatio!=0 && qtrForwardPERatio!=0) {
            forwardPERatio = (double) (qtrForwardPERatio + yearlyforwardPERatio) / 2;
        }
        else if(yearlyforwardPERatio!=0 && qtrForwardPERatio == 0){
            forwardPERatio = yearlyforwardPERatio;
        }

        ValuationEnums valuationEnums = analyzeForwardPE(currentPERatio,forwardPERatio);
        logger.info("CurrentPERation: "+currentPERatio+" Yearly ForwardPERatio: "+yearlyforwardPERatio+"  Quarterly ForwardPERatio: "+qtrForwardPERatio+" ForwardPE: "+forwardPERatio+" Valuation: "+valuationEnums);

        analyzedInfoDto.getRatioAnalysisInfo().setCurrentTTMPERatio(StockUtil.convertDoubleToPrecision(currentPERatio,2));
        analyzedInfoDto.getRatioAnalysisInfo().setForwardPERatio(StockUtil.convertDoubleToPrecision(forwardPERatio,2));
        analyzedInfoDto.getRatioAnalysisInfo().setForwardPEValuation(valuationEnums);
        analyzedInfoDto.getRatioAnalysisInfo().setQuarterlyForwardPERatio(StockUtil.convertDoubleToPrecision(qtrForwardPERatio,2));
        analyzedInfoDto.getRatioAnalysisInfo().setYearlyForwardPERatio(StockUtil.convertDoubleToPrecision(yearlyforwardPERatio,2));

        logger.info("Forward PE Analysis Report: "+analyzedInfoDto.getRatioAnalysisInfo().getForwardPEAnalysis());
    }


    private double eps3yrsCAGR(FundamentalInfoDto fundamentalInfoDto){
        List<YearlyReportDto> yearlyReportDtoList = fundamentalInfoDto.getYearlyReportDtoList().stream().limit(3).collect(Collectors.toList());
        YearlyReportDto startDto = yearlyReportDtoList.get(2); //we want 3rd year data
        YearlyReportDto endDto = yearlyReportDtoList.get(0);
        double cagr = FundamentalAnalysisUtil.cagr(endDto.getBasicEPS(), startDto.getBasicEPS(),3);
        double estimatedEPS = endDto.getBasicEPS()*(1+(cagr/100));
        return estimatedEPS;
    }

    private double caclQtrForwardPE(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto){
//        double currentSharePrice,FundamentalInfoDto fundamentalInfoDto,int year
        double currentSharePrice = fundamentalInfoDto.getCompanyDto().getCurrentSharePrice();
//        double estimatedEPS = analyzedInfoDto.getQuarterlyReportAnalysisInfo().getEstimatedEPSCAGR();
        double estimatedEPS = analyzedInfoDto.getQuarterlyReportAnalysisInfo().getTtmEPS(); //instead of CAGR EPS we are using ttm EPS.
        double forwardPERatio = 0;
        if(estimatedEPS!=0){
            forwardPERatio = calcForwardPE(currentSharePrice,estimatedEPS);
        }
        logger.info("[Quarterly ] estimatedEPS: "+estimatedEPS);

        return forwardPERatio;

    }

    private double calcForwardPE(double currentSharePrice,double estimatedEPS ){
        double forwardPERatio = currentSharePrice / estimatedEPS;
        logger.debug("Calculated forward PE: "+forwardPERatio);
        return forwardPERatio;

    }
    private ValuationEnums analyzeForwardPE(double currentPE,double forwardPE){
        double threshold = 20;
        double maxForwardPE = (double)currentPE*threshold/100+currentPE;
        double minForwardPE = currentPE-(double)currentPE*threshold/100;
        if(forwardPE<=minForwardPE)
            return ValuationEnums.UNDER_VALUED;
        if(forwardPE>=maxForwardPE)
            return ValuationEnums.OVER_VALUED;
        else
            return ValuationEnums.FAIR_VALUED;
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


