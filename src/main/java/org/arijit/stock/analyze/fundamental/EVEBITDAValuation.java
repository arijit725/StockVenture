package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.analysisdto.EVEBITDAValuationModelDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.RatiosDto;
import org.arijit.stock.analyze.util.FundamentalAnalysisUtil;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.List;

/**
 * EV/EBITDA valuation gives information for a certain target price, what could be probable entry price for a stock.
 */
public class EVEBITDAValuation implements IFundamentalEvaluation{


    private static final Logger logger = LogManager.getLogger(EVEBITDAValuation.class);

    private boolean evaluated;
    @Override
    public boolean isEvaluated() {
        return evaluated;
    }

    /**
     * Here we are considering 5 years ratio trends by default
     */
//    private final int year = 5;

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        double debtForCurrentFY = fundamentalInfoDto.getBalanceSheetDtoList().get(0).getDebt();
        double equityShareCapital = fundamentalInfoDto.getBalanceSheetDtoList().get(0).getEquityShareCapital();
        double curentSharePrice = fundamentalInfoDto.getCompanyDto().getCurrentSharePrice();
        calculateEVEBITDAValuation(curentSharePrice,fundamentalInfoDto.getRatiosDtoList(),debtForCurrentFY,equityShareCapital, fundamentalInfoDto.getCompanyDto().getFaceValue(),analyzedInfoDto,year);
    }

    private void calculateEVEBITDAValuation(double currentSharePrice, List<RatiosDto> ratiosDtoList, double debtForCurrentFY, double equityShareCapital, double faceValue, AnalyzedInfoDto analyzedInfoDto,int year) throws Exception {
        if(ratiosDtoList==null || ratiosDtoList.isEmpty())
            throw new Exception("RatioDtoList is empty. No ratio present");
        if(ratiosDtoList.size()<year)
            throw new Exception("RatioDtoList does not have entries for last "+year +" year");
        RatiosDto starttingYearDto = ratiosDtoList.get(year-1);//index starts from 0
        logger.info("Starting Year Ratios details: "+starttingYearDto);
        RatiosDto endingYearDto = ratiosDtoList.get(0);
        logger.info("Ending Year Ratios Details: "+endingYearDto);
        double endingYearEbitda = calculateEBITDA(endingYearDto.getEv(),endingYearDto.getEvEbitda());
        logger.info("Ending Year EBITDA: "+endingYearEbitda);
        double startingYearEbitda = calculateEBITDA(starttingYearDto.getEv(),starttingYearDto.getEvEbitda());
        logger.info("Starting year EBITDA: "+startingYearEbitda);

        double ebitdaCAGR = FundamentalAnalysisUtil.cagr(endingYearEbitda,startingYearEbitda,year);
        logger.info("CAGR of EBITDA for last "+year+ " year: "+ebitdaCAGR);
        double expectedEBITDA = (endingYearEbitda * ebitdaCAGR/100)+endingYearEbitda;
        logger.info("Expected EBITDA: "+expectedEBITDA);
        double endingYearEV = endingYearDto.getEv();
        double forcastedEV = ((endingYearEV/endingYearEbitda)*expectedEBITDA)-debtForCurrentFY;
        logger.info("Forcasted EV: "+forcastedEV);

        //https://www.sptulsian.com/f/p/what-does-equity-share-capital-mean
        double outstandingShare = equityShareCapital/faceValue;
        double targetPrice = forcastedEV/outstandingShare;
        double entryPrice = (double)(targetPrice*2)/3; //standard norm is to pick 2/3 as entry price for safe margin.

        logger.info("EV/EBDIT valuation model outcome: targetPrice: "+Math.ceil(targetPrice)+" EntryPrice: "+Math.ceil(entryPrice));
        EVEBITDAValuationModelDto evebitdaValuationModelDto = new EVEBITDAValuationModelDto();
        int precision = 2;
        evebitdaValuationModelDto.setForcastedEV(StockUtil.convertDoubleToPrecision(forcastedEV,precision));
        evebitdaValuationModelDto.setExpectedEBITDA(StockUtil.convertDoubleToPrecision(expectedEBITDA,precision));
        evebitdaValuationModelDto.setTargetPrice(StockUtil.convertDoubleToPrecision(targetPrice,precision));

        double marginOfSafty = 20;
        double targetPriceAfterMarginOfSafty = targetPrice-(targetPrice*marginOfSafty/100);
        evebitdaValuationModelDto.setTargetPriceAfterMarginOfSafty(StockUtil.convertDoubleToPrecision(targetPriceAfterMarginOfSafty,precision));

        double upside = (targetPriceAfterMarginOfSafty-currentSharePrice)/currentSharePrice*100;
        evebitdaValuationModelDto.setUpside(StockUtil.convertDoubleToPrecision(upside,precision));

        analyzedInfoDto.getEvebitdaValuationModelDto().setForcastedEV(StockUtil.convertDoubleToPrecision(forcastedEV,precision));
        analyzedInfoDto.getEvebitdaValuationModelDto().setExpectedEBITDA(StockUtil.convertDoubleToPrecision(expectedEBITDA,precision));
        analyzedInfoDto.getEvebitdaValuationModelDto().setTargetPrice(StockUtil.convertDoubleToPrecision(targetPrice,precision));
        analyzedInfoDto.getEvebitdaValuationModelDto().setTargetPriceAfterMarginOfSafty(StockUtil.convertDoubleToPrecision(targetPriceAfterMarginOfSafty,precision));
        analyzedInfoDto.getEvebitdaValuationModelDto().setUpside(StockUtil.convertDoubleToPrecision(upside,precision));

        analyzedInfoDto.getTargetPriceEstimationDto().setEvebitdaValuationModelDto(evebitdaValuationModelDto);
        evaluated = true;
    }

    private double calculateEBITDA(double ev, double evEbitdaRatio){
        double ebitda = ev/evEbitdaRatio;
        return ebitda;
    }




    public static EVEBITDAValuation getInstance(){
        return EVEBITDAValuationInner.getInstance();
    }



    private static class EVEBITDAValuationInner{
            private static final EVEBITDAValuation instance = new EVEBITDAValuation();
            private  EVEBITDAValuationInner(){

            }
         static EVEBITDAValuation getInstance() {
            return instance;
        }
    }
}
