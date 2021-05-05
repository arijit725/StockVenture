package org.arijit.stock.analyze.fundamental;

import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.ProfitAndLossDto;
import org.arijit.stock.analyze.util.StockUtil;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ProfitAndLossEvaluation implements IFundamentalEvaluation {
    @Override
    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception {
        List<ProfitAndLossDto> profitAndLossDtoList  = fundamentalInfoDto.getProfitAndLossDtoList();
        if(profitAndLossDtoList.isEmpty()||profitAndLossDtoList.size()<year)
            throw new Exception("Number of year exceeds BalancesSheetDto list");
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

    }

    private void interestDecreasePercentage(AnalyzedInfoDto analyzedInfoDto,ProfitAndLossDto endingYearProfitAndLossDto, ProfitAndLossDto startingYearProfitAndLossDto){

        double endYearRawMaterial = endingYearProfitAndLossDto.getInterest();
        double startYearRawMaterial = startingYearProfitAndLossDto.getInterest();

        double changeInPercentage  = (double) (endYearRawMaterial-startYearRawMaterial)/startYearRawMaterial;
        changeInPercentage = (double) changeInPercentage*100;

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
        ProfitAndLossDto lastProfitAndLossDto = null;
        boolean isContinuousChange = true;
        while(iterator.hasNext()){
            ProfitAndLossDto profitAndLossDto = iterator.next();
            if(lastProfitAndLossDto==null){
                lastProfitAndLossDto = profitAndLossDto;
            }
            else{

                if(profitAndLossDto.getNetSales()> lastProfitAndLossDto.getNetSales()){
                    // previous year sale is more than current year sale
                    isContinuousChange = false;
                    break;
                }
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
