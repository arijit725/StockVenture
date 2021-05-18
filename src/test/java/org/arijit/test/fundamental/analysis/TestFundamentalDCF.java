package org.arijit.test.fundamental.analysis;

import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.*;
import org.arijit.stock.analyze.fundamental.EconomicDCFValuation;

public class TestFundamentalDCF {

    public static void main(String args[]) throws Exception {
        FundamentalInfoDto info = tcsDataSet1();
        AnalyzedInfoDto analyzedInfoDto = AnalyzedInfoDto.create();
        analyzedInfoDto.getEconomicGrowthDCFDto().setLastFYDebt(11160);
        analyzedInfoDto.getEconomicGrowthDCFDto().setLasFYCashEquivalent(2882);

        analyzedInfoDto.getEconomicGrowthDCFDto().setMarginOfSafty(15);
        analyzedInfoDto.getEconomicGrowthDCFDto().setPerpertualGrowthRate(2.50);
        analyzedInfoDto.getEconomicGrowthDCFDto().setDiscountRate(8.50);

        EconomicDCFValuation.getInstance().evaluate(info,analyzedInfoDto,10);

    }

    private static FundamentalInfoDto tcsDataSet1() throws Exception {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyName("TCS");
        companyDto.setCurrentSharePrice(30.43);
        companyDto.setFaceValue(1);
        companyDto.setIndustry("Computers- software");
        companyDto.setIndustryPE(35.04);
        companyDto.setMarketCap(1250500);
        FundamentalInfoDto tcsFundamental = FundamentalInfoDto.builder(companyDto.getYears());
        tcsFundamental.setCompanyDto(companyDto);


        tcsFundamental
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2021")
                        .setTotalShareCapital(637.79)
                        .setEquityShareCapital(637.79)
                        .setReserves(78523.00)
                        .setDebt(0)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2020")
                        .setTotalShareCapital(191.00)
                        .setEquityShareCapital(191.00)
                        .setReserves(75675.00)
                        .setDebt(220.00)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2019")
                        .setTotalShareCapital(197.00)
                        .setEquityShareCapital(197.00)
                        .setReserves(77825.00)
                        .setDebt(244.00)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2018")
                        .setTotalShareCapital(197.00)
                        .setEquityShareCapital(197.00)
                        .setReserves(64816.00)
                        .setDebt(163.00)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2017")
                        .setTotalShareCapital(195.87)
                        .setEquityShareCapital(195.87)
                        .setReserves(45220.57)
                        .setDebt(250.27)
                        .build());


        tcsFundamental
                .addYearlyReportDto(YearlyReportDto
                        .builder()
                        .setDate("Mar-2019")
                        .setBasicEPS(185.14)
                        .build())
                .addYearlyReportDto(YearlyReportDto
                        .builder()
                        .setDate("Mar-2018")
                        .setBasicEPS(131.15)
                        .build())
                .addYearlyReportDto(YearlyReportDto
                        .builder()
                        .setDate("Mar-2017")
                        .setBasicEPS(158.26)
                        .build())
                .addYearlyReportDto(YearlyReportDto
                        .builder()
                        .setDate("Mar-2016")
                        .setBasicEPS(117.11)
                        .build())
                .addYearlyReportDto(YearlyReportDto
                        .builder()
                        .setDate("Mar-2015")
                        .setBasicEPS(98.31)
                        .build());

        tcsFundamental
                .addRatiosDto(RatiosDto
                        .builder()
                        .setDate("Mar-2019")
                        .setEv(741250.00)
                        .setEvEbitda(17.40)
                        .setPeRatio(24.10)
//                        .setRoe()
                        .setPbRatio(9.51))
                .addRatiosDto(RatiosDto
                        .builder()
                        .setDate("Mar-2018")
                        .setEv(540949.30)
                        .setEvEbitda(16.10)
                        .setPeRatio(10.62)
//                        .setRoe()
                        .setPbRatio(7.17))
                .addRatiosDto(RatiosDto
                        .builder()
                        .setDate("Mar-2017")
                        .setEv(477854.70)
                        .setEvEbitda(15.09)
                        .setPeRatio(9.11)
//                        .setRoe()
                        .setPbRatio(6.14))
                .addRatiosDto(RatiosDto
                        .builder()
                        .setDate("Mar-2016")
                        .setEv(491018.85)
                        .setEvEbitda(15.94)
                        .setPeRatio(10.23)
//                        .setRoe()
                        .setPbRatio(7.62))
                .addRatiosDto(RatiosDto
                        .builder()
                        .setDate("Mar-2015")
                        .setEv(482638.45)
                        .setEvEbitda(18.93)
                        .setPeRatio(10.98)
//                        .setRoe()
                        .setPbRatio(10.98));

        tcsFundamental.addCashFlowDto(CashFlowDto
                .builder()
                .setDate("Mar-2021")
                .setCashFromOperatingActivity(1490)
                .setFixedAssestsPurchased(0))
                .addCashFlowDto(CashFlowDto
                        .builder()
                        .setDate("Mar-2020")
                        .setCashFromOperatingActivity(1580)
                        .setFixedAssestsPurchased(0))
                .addCashFlowDto(CashFlowDto
                        .builder()
                        .setDate("Mar-2019")
                        .setCashFromOperatingActivity(508)
                        .setFixedAssestsPurchased(0))
                .addCashFlowDto(CashFlowDto
                        .builder()
                        .setDate("Mar-2018")
                        .setCashFromOperatingActivity(1983)
                        .setFixedAssestsPurchased(0))
                .addCashFlowDto(CashFlowDto
                        .builder()
                        .setDate("Mar-2017")
                        .setCashFromOperatingActivity(2698)
                        .setFixedAssestsPurchased(0))
                .addCashFlowDto(CashFlowDto
                        .builder()
                        .setDate("Mar-2016")
                        .setCashFromOperatingActivity(4982)
                        .setFixedAssestsPurchased(0))
                .addCashFlowDto(CashFlowDto
                        .builder()
                        .setDate("Mar-2015")
                        .setCashFromOperatingActivity(3824)
                        .setFixedAssestsPurchased(0))
                .addCashFlowDto(CashFlowDto
                        .builder()
                        .setDate("Mar-2014")
                        .setCashFromOperatingActivity(1132)
                        .setFixedAssestsPurchased(0))
                .addCashFlowDto(CashFlowDto
                        .builder()
                        .setDate("Mar-2013")
                        .setCashFromOperatingActivity(1846)
                        .setFixedAssestsPurchased(0))
                .addCashFlowDto(CashFlowDto
                        .builder()
                        .setDate("Mar-2012")
                        .setCashFromOperatingActivity(3489)
                        .setFixedAssestsPurchased(0));

        return tcsFundamental.build();
    }
}
