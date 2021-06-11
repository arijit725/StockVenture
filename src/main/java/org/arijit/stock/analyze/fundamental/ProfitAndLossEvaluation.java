package org.arijit.stock.analyze.fundamental;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.BalanceSheetDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.ProfitAndLossDto;
import org.arijit.stock.analyze.dto.YearlyReportDto;
import org.arijit.stock.analyze.enums.AnalysisEnums;
import org.arijit.stock.analyze.score.ScorService;
import org.arijit.stock.analyze.util.FundamentalAnalysisUtil;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ProfitAndLossEvaluation implements IFundamentalEvaluation {

    private static final Logger logger = LogManager.getLogger(ProfitAndLossEvaluation.class);

    private boolean evaluated = false;
    @Override
    public boolean isEvaluated() {
        return evaluated;
    }

    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        clearValuation(analyzedInfoDto);
        List<ProfitAndLossDto> profitAndLossDtoList  = fundamentalInfoDto.getProfitAndLossDtoList();
        if(profitAndLossDtoList.isEmpty()||profitAndLossDtoList.size()<year) {
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement("Number of year exceeds ProfitAndLossDto present in list",AnalysisEnums.ANALYZED_BAD);
            throw new Exception("Number of year exceeds ProfitAndLossDto list");
        }
        profitAndLossDtoList = fundamentalInfoDto.getProfitAndLossDtoList().stream().limit(year).collect(Collectors.toList());
        ProfitAndLossDto endingYearProfitAndLossDto = profitAndLossDtoList.get(0);
        ProfitAndLossDto startingYearProfitAndLossDto = profitAndLossDtoList.get(year-1);
        netSalesGrowthPercentage(analyzedInfoDto,endingYearProfitAndLossDto,startingYearProfitAndLossDto);
        isContinuousNetSalesGrowth(analyzedInfoDto,profitAndLossDtoList);
        rawMaterialGrowthPercentage(analyzedInfoDto,endingYearProfitAndLossDto,startingYearProfitAndLossDto);
        pbitGrowthPercentage(analyzedInfoDto,endingYearProfitAndLossDto,startingYearProfitAndLossDto);
        netProfitGrowthPercnentage(analyzedInfoDto,endingYearProfitAndLossDto,startingYearProfitAndLossDto);
        interestDecreasePercentage(analyzedInfoDto,endingYearProfitAndLossDto,startingYearProfitAndLossDto);
        netSalesVsProfitRatio(analyzedInfoDto,profitAndLossDtoList);

        calcAvgGrowth(fundamentalInfoDto,analyzedInfoDto,year);
        calcCAGRGrowth(fundamentalInfoDto, analyzedInfoDto,year);
        ScorService.getInstance().getProfitAndLossScore().score(fundamentalInfoDto,analyzedInfoDto,year);
        evaluated = true;
    }


    private void calcCAGRGrowth(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int years){
        List<ProfitAndLossDto> profitAndLossDtoList = fundamentalInfoDto.getProfitAndLossDtoList().stream().limit(years).collect(Collectors.toList());
        ProfitAndLossDto endProfitAndLossDto = profitAndLossDtoList.get(0); //0th index gives recent year data
        ProfitAndLossDto startProftAndLossDto = profitAndLossDtoList.get(profitAndLossDtoList.size()-1); // last index gives starting year data
        logger.info("[CAGR Analysis]: endProfitAndLossDto: "+endProfitAndLossDto+" startProftAndLossDto: "+startProftAndLossDto);
        double cagrNetProfitGrowth = FundamentalAnalysisUtil.cagr(endProfitAndLossDto.getNetProfit(), startProftAndLossDto.getNetProfit(),years);
        double cagrNetSalesGrowth = FundamentalAnalysisUtil.cagr(endProfitAndLossDto.getNetSales(), startProftAndLossDto.getNetSales(),years);
        /*
        Below are growth threshold which we consider very good.
        Unless our opinion remains neutral
        */
        String netprofitGrowthstmt = "Net Profit CAGR Growth over "+years+" year: "+cagrNetProfitGrowth;
        String netSalesGrowthStmt = "Net Sales CAGR Growth over "+years+" year: "+cagrNetSalesGrowth;

        double netProfitGrowtCAGRThreshold = 30;
        double netSalesGrowthCAGRThreshold = 25;

        if(cagrNetProfitGrowth>netProfitGrowtCAGRThreshold){
            netprofitGrowthstmt="(+) "+netprofitGrowthstmt;
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(netprofitGrowthstmt,AnalysisEnums.ANALYZED_VERY_GOOD);
        }
        else if(cagrNetProfitGrowth<0){
            netprofitGrowthstmt = "(-) "+netprofitGrowthstmt;
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(netprofitGrowthstmt,AnalysisEnums.ANALYZED_BAD);
        }
        else{
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(netprofitGrowthstmt,AnalysisEnums.ANALYZED_NEUTRAL);
        }

        if(cagrNetProfitGrowth>netSalesGrowthCAGRThreshold){
            netSalesGrowthStmt="(+) "+netSalesGrowthStmt;
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(netSalesGrowthStmt,AnalysisEnums.ANALYZED_VERY_GOOD);
        }
        else if(cagrNetProfitGrowth<0){
            netSalesGrowthStmt = "(-) "+netSalesGrowthStmt;
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(netSalesGrowthStmt,AnalysisEnums.ANALYZED_BAD);
        }
        else{
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(netSalesGrowthStmt,AnalysisEnums.ANALYZED_NEUTRAL);
        }
    }
    private void calcAvgGrowth(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int years){
        List<ProfitAndLossDto> profitAndLossDtoList = fundamentalInfoDto.getProfitAndLossDtoList().stream().limit(years).collect(Collectors.toList());
        ProfitAndLossDto currentProfitAndLossDto = null;

        Iterator<ProfitAndLossDto> it = profitAndLossDtoList.iterator();
        double netSalesAvgGrowth = 0;
        int netSalesIncreaseCount = 0;
        int netSalesDecreaseCount = 0;

        double rawMaterialAVGGrowth = 0;
        int rawMaterialIncrease = 0;
        int rawMaterialDecrease = 0;


        double employeeCostAvgGrowth = 0;
        int employeeCostIncrease = 0;
        int employeeCostDecrease = 0;


        double interestAvgGrowth = 0;
        int interestIncrease = 0;
        int interestDecrease = 0;

        double netProfitAvgGrowth = 0;
        int netProfitIncreaseCount = 0;
        int netProfitDecreaseCount = 0;

        while(it.hasNext()){
            if(currentProfitAndLossDto==null)
                currentProfitAndLossDto = it.next();
            else{
                ProfitAndLossDto prevProfitAndLossDto = it.next();
                /*Net Sales analysis*/
                try {
                    double netSalesGrowth = 0;
                    if (prevProfitAndLossDto.getNetSales() != 0) {
                        netSalesGrowth = (currentProfitAndLossDto.getNetSales() - prevProfitAndLossDto.getNetSales());
                        netSalesGrowth = (double) netSalesGrowth / prevProfitAndLossDto.getNetSales() * 100;
                        netSalesAvgGrowth = netSalesAvgGrowth + netSalesGrowth;
                    }
                    String netSalesGrowthString = StockUtil.convertDoubleToPrecision(netSalesGrowth, 2);
                    analyzedInfoDto.getProfitAndLossAnalysisInfo().addGrowths(currentProfitAndLossDto.getDate(), "netSales", netSalesGrowthString);
                }catch(Exception e){
                    logger.error("unable to calculate net sales growth",e);
                }

                if(currentProfitAndLossDto.getNetSales()>prevProfitAndLossDto.getNetSales()){
                    netSalesIncreaseCount++;
                }
                else if(currentProfitAndLossDto.getNetSales()<prevProfitAndLossDto.getNetSales()){
                    netSalesDecreaseCount++;
                }

                /*Consumption of Raw Material Analysis*/
                try {
                    double rawMaterialsGrowth = 0;
                    if (prevProfitAndLossDto.getConsumptionRawMaterial() != 0) {
                        rawMaterialsGrowth = (currentProfitAndLossDto.getConsumptionRawMaterial() - prevProfitAndLossDto.getConsumptionRawMaterial());
                        rawMaterialsGrowth = (double) rawMaterialsGrowth / prevProfitAndLossDto.getConsumptionRawMaterial() * 100;
                        rawMaterialAVGGrowth = rawMaterialAVGGrowth + rawMaterialsGrowth;
                    }
                    String rawMaterialsGrowthString = StockUtil.convertDoubleToPrecision(rawMaterialsGrowth, 2);
                    analyzedInfoDto.getProfitAndLossAnalysisInfo().addGrowths(currentProfitAndLossDto.getDate(), "consumptionRawMaterial", rawMaterialsGrowthString);
                }catch(Exception e){
                    logger.error("unable to calculate net sales growth",e);
                }

                if(currentProfitAndLossDto.getConsumptionRawMaterial()>prevProfitAndLossDto.getConsumptionRawMaterial()){
                    rawMaterialIncrease++;
                }
                else if(currentProfitAndLossDto.getConsumptionRawMaterial()<prevProfitAndLossDto.getConsumptionRawMaterial()){
                    rawMaterialDecrease++;
                }


                /*Employee Cost Analysis*/
                try {
                    double eployeeCostGrowth = 0;
                    if (prevProfitAndLossDto.getEmployeeCost() != 0) {
                        eployeeCostGrowth = (currentProfitAndLossDto.getEmployeeCost() - prevProfitAndLossDto.getEmployeeCost());
                        eployeeCostGrowth = (double) eployeeCostGrowth / prevProfitAndLossDto.getEmployeeCost() * 100;
                        employeeCostAvgGrowth = employeeCostAvgGrowth + eployeeCostGrowth;
                    }
                    String eployeeCostGrowthString = StockUtil.convertDoubleToPrecision(eployeeCostGrowth, 2);
                    analyzedInfoDto.getProfitAndLossAnalysisInfo().addGrowths(currentProfitAndLossDto.getDate(), "employeeCost", eployeeCostGrowthString);
                }catch(Exception e){
                    logger.error("unable to calculate employeeCost growth",e);
                }

                if(currentProfitAndLossDto.getEmployeeCost()>prevProfitAndLossDto.getEmployeeCost()){
                    employeeCostIncrease++;
                }
                else if(currentProfitAndLossDto.getEmployeeCost()<prevProfitAndLossDto.getEmployeeCost()){
                    employeeCostIncrease++;
                }

                /*PBIT Analysis*/
                try {
                    double pbitGrowth = 0;
                    if (prevProfitAndLossDto.getPbit() != 0) {
                        pbitGrowth = (currentProfitAndLossDto.getPbit() - prevProfitAndLossDto.getPbit());
                        pbitGrowth = (double) pbitGrowth / prevProfitAndLossDto.getPbit() * 100;
                    }
                    String pbitGrowthString = StockUtil.convertDoubleToPrecision(pbitGrowth, 2);
                    analyzedInfoDto.getProfitAndLossAnalysisInfo().addGrowths(currentProfitAndLossDto.getDate(), "pbit", pbitGrowthString);
                }catch(Exception e){
                    logger.error("unable to calculate PBIT growth",e);
                }


                /*Net Profit analysis*/
                try {
                    double interestGrowth = 0;
                    if (prevProfitAndLossDto.getInterest() != 0) {
                        interestGrowth = (currentProfitAndLossDto.getInterest() - prevProfitAndLossDto.getInterest());
                        interestGrowth = (double) interestGrowth / prevProfitAndLossDto.getInterest() * 100;
                        interestAvgGrowth = interestAvgGrowth + interestGrowth;
                    }
                    String interestAvgGrowthString = StockUtil.convertDoubleToPrecision(interestGrowth, 2);
                    analyzedInfoDto.getProfitAndLossAnalysisInfo().addGrowths(currentProfitAndLossDto.getDate(), "interest", interestAvgGrowthString);
                }catch(Exception e){
                    logger.error("unable to calculate interest growth",e);
                }

                if(currentProfitAndLossDto.getInterest()>prevProfitAndLossDto.getInterest()){
                    interestIncrease++;
                }
                else if(currentProfitAndLossDto.getInterest()<prevProfitAndLossDto.getInterest()){
                    interestDecrease++;
                }

                /*Net Profit analysis*/
                try {
                    double netProfitGrowth = 0;
                    if (prevProfitAndLossDto.getNetProfit() !=0) {
                        netProfitGrowth = (currentProfitAndLossDto.getNetProfit() - prevProfitAndLossDto.getNetProfit());
                        netProfitGrowth = (double) netProfitGrowth / Math.abs(prevProfitAndLossDto.getNetProfit()) * 100;
                        netProfitAvgGrowth = netProfitAvgGrowth + netProfitGrowth;
                    }
                    String netProfitAvgGrowthString = StockUtil.convertDoubleToPrecision(netProfitGrowth, 2);
                    analyzedInfoDto.getProfitAndLossAnalysisInfo().addGrowths(currentProfitAndLossDto.getDate(), "netProfit", netProfitAvgGrowthString);
                }catch(Exception e){
                    logger.error("unable to calculate net profit growth",e);
                }

                if(currentProfitAndLossDto.getNetProfit()>prevProfitAndLossDto.getNetProfit()){
                    netProfitIncreaseCount++;
                }
                else if(currentProfitAndLossDto.getNetProfit()<prevProfitAndLossDto.getNetProfit()){
                    netProfitDecreaseCount++;
                }




                currentProfitAndLossDto = prevProfitAndLossDto;
            }
        }
        /*from 5 years data we can get 4 years growth data point, hence decrementing years by 1*/
        int growthYear = years-1;

        /* Net Sales Analysis*/
        netSalesAvgGrowth =(double) netSalesAvgGrowth/growthYear;
        String netSalesAvgGrowthStatement = " Average Growth in Net Sales: "+StockUtil.convertDoubleToPrecision(netSalesAvgGrowth,2);
        if(netSalesAvgGrowth>0)
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(netSalesAvgGrowthStatement, AnalysisEnums.ANALYZED_GOOD);
        else
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(netSalesAvgGrowthStatement,AnalysisEnums.ANALYZED_BAD);
        if(netSalesIncreaseCount==growthYear){
            String stmt = "(+) Net Sales is increasing continuously over years.";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_GOOD);
        }
        else if(netSalesDecreaseCount==growthYear){
            String stmt = "(-) Net Sales is decreasing continuously over years.";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_BAD);
        }



        /* Raw Material Analysis*/
//        rawMaterialAVGGrowth =(double) rawMaterialAVGGrowth/growthYear;
//        String rawMaterialAVGGrowthStatement = " Average Growth in Raw Material : "+StockUtil.convertDoubleToPrecision(rawMaterialAVGGrowth,2);
//        if(rawMaterialAVGGrowth>0)
//            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(rawMaterialAVGGrowthStatement, AnalysisEnums.ANALYZED_GOOD);
//        else
//            analyzedInfoDto.getBalanceSheetAnalysisInfo().addAnalysisStatement(rawMaterialAVGGrowthStatement,AnalysisEnums.ANALYZED_BAD);

        if(netSalesIncreaseCount==growthYear && rawMaterialIncrease ==growthYear){
            String stmt = "(+) Raw material cost is increasing continuously over years along with Net sales. Company is growting";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_GOOD);
        }
        else if(netSalesDecreaseCount==growthYear && rawMaterialIncrease ==growthYear){
            String stmt = "(-) Raw Material cost is increasing continuously over years but sales reducing. Company may be not in profitable position.";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_BAD);
        }
        else if(netSalesIncreaseCount==growthYear && rawMaterialDecrease == growthYear){
            String stmt = "(!) Raw Material cost is decreasing continuously over years but sales increasing. May be company needs less raw material for increasing sales.";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_NEUTRAL);
        }
        else if(netProfitDecreaseCount == growthYear && rawMaterialDecrease == growthYear){
            String stmt = "(-) Raw Material cost is decreasing continuously over years along with sales reducing. Company migh be reducing its span.";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_BAD);
        }


        /* Employee Cost Analysis*/
        if(netSalesIncreaseCount==growthYear && employeeCostIncrease ==growthYear){
            String stmt = "(+) Employee cost is increasing continuously over years along with Net sales. Company is growting";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_GOOD);
        }
        else if(netSalesDecreaseCount==growthYear && employeeCostIncrease ==growthYear){
            String stmt = "(-) Employee cost is increasing continuously over years but sales reducing. Company may be not in profitable position.";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_BAD);
        }
        else if(netSalesIncreaseCount==growthYear && employeeCostDecrease == growthYear){
            String stmt = "(!)Employee cost is decreasing continuously over years but sales increasing. May be company needs less Employee for increasing sales. Check News if investing in automated plant";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_NEUTRAL);
        }
        else if(netProfitDecreaseCount == growthYear && employeeCostDecrease == growthYear){
            String stmt = "(-) Employee cost is decreasing continuously over years along with sales reducing. Company migh be reducing its span.";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_BAD);
        }

        if(netSalesDecreaseCount == growthYear && employeeCostDecrease == growthYear && rawMaterialDecrease == growthYear){
            String stmt = "[RED FLAG] Employee cost and Raw matetirals is decreasing continuously over years along with sales reducing. Company migh be reducing its span.";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_BAD);
        }


        /* Interest Analysis*/
        interestAvgGrowth =(double) interestAvgGrowth/growthYear;
        String interestAvgGrowthStatement = "Average Growth in Interest Paid : "+StockUtil.convertDoubleToPrecision(interestAvgGrowth,2);
        if(interestAvgGrowth<=0)
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(interestAvgGrowthStatement, AnalysisEnums.ANALYZED_GOOD);
        else
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(interestAvgGrowthStatement,AnalysisEnums.ANALYZED_BAD);
        if(interestAvgGrowth==0 && profitAndLossDtoList.get(0).getInterest()==0){
            String stmt = "(+) No Inerest Paid by the company for last "+years+" years";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_VERY_GOOD);
        }
        if(interestDecrease==growthYear){
            String stmt = "(+) Paid Interest Amount is decreasing continuously over years.";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_GOOD);
        }
        else if(interestIncrease==growthYear){
            String stmt = "(-) Paid Interest Amount is increasing continuously over years.";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_BAD);
        }

        /* Net Profit Analysis*/
        netProfitAvgGrowth =(double) netProfitAvgGrowth/growthYear;
        String netProfitAvgGrowthGrowthStatement = " Average Growth in Net Profit : "+StockUtil.convertDoubleToPrecision(netProfitAvgGrowth,2);
        if(netProfitAvgGrowth>0)
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(netProfitAvgGrowthGrowthStatement, AnalysisEnums.ANALYZED_GOOD);
        else
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(netProfitAvgGrowthGrowthStatement,AnalysisEnums.ANALYZED_BAD);
        if(netProfitIncreaseCount==growthYear){
            String stmt = "(+) Net Profit is increasing continuously over years.";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_GOOD);
        }
        else if(netProfitDecreaseCount==growthYear){
            String stmt = "(-) Net Profit is decreasing continuously over years.";
            analyzedInfoDto.getProfitAndLossAnalysisInfo().addAnalysisStatement(stmt,AnalysisEnums.ANALYZED_BAD);
        }
    }

    private void interestDecreasePercentage(AnalyzedInfoDto analyzedInfoDto,ProfitAndLossDto endingYearProfitAndLossDto, ProfitAndLossDto startingYearProfitAndLossDto){

        double endYearInterest = endingYearProfitAndLossDto.getInterest();
        double startYearInterest = startingYearProfitAndLossDto.getInterest();
        double changeInPercentage = endYearInterest;
        if(startYearInterest!=0) {
            changeInPercentage = (double) (endYearInterest - startYearInterest) / startYearInterest;
            changeInPercentage = (double) changeInPercentage * 100;
        }
        analyzedInfoDto.getProfitAndLossAnalysisInfo().setInterestDecreasePercentage(changeInPercentage);
    }


    private void netProfitGrowthPercnentage(AnalyzedInfoDto analyzedInfoDto,ProfitAndLossDto endingYearProfitAndLossDto, ProfitAndLossDto startingYearProfitAndLossDto){

        double endYearRawMaterial = endingYearProfitAndLossDto.getNetProfit();
        double startYearRawMaterial = startingYearProfitAndLossDto.getNetProfit();

        double changeInPercentage  = (double) (endYearRawMaterial-startYearRawMaterial)/startYearRawMaterial;
        changeInPercentage = (double) changeInPercentage*100;

        analyzedInfoDto.getProfitAndLossAnalysisInfo().setNetProfitGrowthPercentage(changeInPercentage);
    }

    private void pbitGrowthPercentage(AnalyzedInfoDto analyzedInfoDto,ProfitAndLossDto endingYearProfitAndLossDto, ProfitAndLossDto startingYearProfitAndLossDto){

        double endYearPBit = endingYearProfitAndLossDto.getPbit();
        double startYearPBit = startingYearProfitAndLossDto.getPbit();

        if(startYearPBit==0){
            //we are making lowest value set.
            startYearPBit=0.00001;
        }
        double changeInPercentage  = (double) (endYearPBit-startYearPBit)/startYearPBit;
        changeInPercentage = (double) changeInPercentage*100;

        analyzedInfoDto.getProfitAndLossAnalysisInfo().setPBITGrowthPercentage(changeInPercentage);
    }

    private void rawMaterialGrowthPercentage(AnalyzedInfoDto analyzedInfoDto,ProfitAndLossDto endingYearProfitAndLossDto, ProfitAndLossDto startingYearProfitAndLossDto){

        double endYearRawMaterial = endingYearProfitAndLossDto.getConsumptionRawMaterial();
        double startYearRawMaterial = startingYearProfitAndLossDto.getConsumptionRawMaterial();
        if(startYearRawMaterial==0){
            analyzedInfoDto.getProfitAndLossAnalysisInfo().setRawMaterialGrowthPercentage(100);
            return;
        }
        double changeInPercentage  = (double) (endYearRawMaterial-startYearRawMaterial)/startYearRawMaterial;
        changeInPercentage = (double) changeInPercentage*100;

        analyzedInfoDto.getProfitAndLossAnalysisInfo().setRawMaterialGrowthPercentage(changeInPercentage);
    }

    private void isContinuousNetSalesGrowth(AnalyzedInfoDto analyzedInfoDto, List<ProfitAndLossDto> profitAndLossDtoList){
        Iterator<ProfitAndLossDto> iterator = profitAndLossDtoList.iterator();
        boolean isContinuousChange = true;
        ProfitAndLossDto currentProfitAndLossDto = null;
        while(iterator.hasNext()){
            if(currentProfitAndLossDto==null)
                currentProfitAndLossDto = iterator.next();
            else {
                ProfitAndLossDto lastProfitAndLossDto = iterator.next();

                    if (currentProfitAndLossDto.getNetSales() < lastProfitAndLossDto.getNetSales()) {
                        // previous year sale is more than current year sale
                        analyzedInfoDto.getProfitAndLossAnalysisInfo().setSalesGrowthContinuous(false);
                        return;
                    }
                currentProfitAndLossDto = lastProfitAndLossDto;
            }
            }
        analyzedInfoDto.getProfitAndLossAnalysisInfo().setSalesGrowthContinuous(isContinuousChange);
    }

    private void netSalesGrowthPercentage(AnalyzedInfoDto analyzedInfoDto,ProfitAndLossDto endingYearProfitAndLossDto, ProfitAndLossDto startingYearProfitAndLossDto){

            double endYearNetSales = endingYearProfitAndLossDto.getNetSales();
            double startingYearNeSales = startingYearProfitAndLossDto.getNetSales();

            double changeInPercentage  = (double) (endYearNetSales-startingYearNeSales)/startingYearNeSales;
            changeInPercentage = (double) changeInPercentage*100;

            analyzedInfoDto.getProfitAndLossAnalysisInfo().setNetSalesGrowthPercentage(changeInPercentage);
    }


    private void netSalesVsProfitRatio(AnalyzedInfoDto analyzedInfoDto, List<ProfitAndLossDto> profitAndLossDtoList){
        Iterator<ProfitAndLossDto> iterator = profitAndLossDtoList.iterator();
        while(iterator.hasNext()){
            ProfitAndLossDto tmp = iterator.next();
            double ratio = tmp.getNetProfit()/tmp.getNetSales();
            String ratioStr = StockUtil.convertDoubleToPrecision(ratio,4);
            analyzedInfoDto.getProfitAndLossAnalysisInfo().getNetProfitVsSalesRatio().put(tmp.getDate(),ratioStr);
        }

    }

    public void clearValuation(AnalyzedInfoDto analyzedInfoDto){
        analyzedInfoDto.getProfitAndLossAnalysisInfo().clearAnalysisStatement();
    }
    public static ProfitAndLossEvaluation getInstance(){
        return Inner.getInstance();
    }

    private static class Inner{
        private static final ProfitAndLossEvaluation instance = new ProfitAndLossEvaluation();
        private  Inner(){

        }
        static ProfitAndLossEvaluation getInstance() {
            return instance;
        }
    }
}
