package org.arijit.test.fundamental.analysis;

import org.arijit.stock.analyze.dto.*;
import org.arijit.stock.analyze.fundamental.EVEBITDAValuation;
import org.arijit.stock.analyze.fundamental.FundamentalAnalysisEvaluation;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestFundamentalAnalysis {

    public static void main(String args[]) throws Exception {
        String companyName = "Hero Motocorp Ltd";
        int years = 5;

        FundamentalInfoDto fundamentalInfoDto = tcsDataSet();


        FundamentalAnalysisEvaluation.getInstance().evaluate(fundamentalInfoDto, 3);
    }


    private static FundamentalInfoDto tcsDataSet1() throws Exception {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyName("TCS");
        companyDto.setCurrentSharePrice(3460.00);
        companyDto.setFaceValue(1);
        companyDto.setIndustry("Computers- software");
        companyDto.setIndustryPE(35.04);
        companyDto.setMarketCap(1250500);
        FundamentalInfoDto tcsFundamental = FundamentalInfoDto.builder(companyDto.getYears());
        tcsFundamental.setCompanyDto(companyDto);


        tcsFundamental
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2019")
                        .setTotalShareCapital(375.00)
                        .setEquityShareCapital(375.00)
                        .setReserves(78523.00)
                        .setDebt(0)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2018")
                        .setTotalShareCapital(191.00)
                        .setEquityShareCapital(191.00)
                        .setReserves(75675.00)
                        .setDebt(220.00)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2017")
                        .setTotalShareCapital(197.00)
                        .setEquityShareCapital(197.00)
                        .setReserves(77825.00)
                        .setDebt(244.00)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2016")
                        .setTotalShareCapital(197.00)
                        .setEquityShareCapital(197.00)
                        .setReserves(64816.00)
                        .setDebt(163.00)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2015")
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

        return tcsFundamental.build();
    }


    private static FundamentalInfoDto tcsDataSet() throws Exception {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyName("TCS");
        companyDto.setCurrentSharePrice(3460.00);
        companyDto.setFaceValue(1);
        companyDto.setIndustry("Computers- software");
        companyDto.setIndustryPE(35.04);
        companyDto.setMarketCap(1250500);
        FundamentalInfoDto tcsFundamental = FundamentalInfoDto.builder(companyDto.getYears());


        tcsFundamental
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2019")
                        .setTotalShareCapital(375.00)
                        .setEquityShareCapital(375.00)
                        .setReserves(78523.00)
                        .setDebt(0)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2018")
                        .setTotalShareCapital(191.00)
                        .setEquityShareCapital(191.00)
                        .setReserves(75675.00)
                        .setDebt(220.00)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2017")
                        .setTotalShareCapital(197.00)
                        .setEquityShareCapital(197.00)
                        .setReserves(77825.00)
                        .setDebt(244.00)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2016")
                        .setTotalShareCapital(197.00)
                        .setEquityShareCapital(197.00)
                        .setReserves(64816.00)
                        .setDebt(163.00)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2015")
                        .setTotalShareCapital(195.87)
                        .setEquityShareCapital(195.87)
                        .setReserves(45220.57)
                        .setDebt(250.27)
                        .build());


        tcsFundamental
                .addYearlyReportDto(YearlyReportDto
                        .builder()
                        .setDate("Mar-2019")
                        .setBasicEPS(79.34)
                        .build())
                .addYearlyReportDto(YearlyReportDto
                        .builder()
                        .setDate("Mar-2018")
                        .setBasicEPS(131.15)
                        .build())
                .addYearlyReportDto(YearlyReportDto
                        .builder()
                        .setDate("Mar-2017")
                        .setBasicEPS(120.04)
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

        return tcsFundamental.build();
    }
}
