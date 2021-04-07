package org.arijit.test.fundamental.analysis;

import org.arijit.stock.analyze.dto.BalanceSheetDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.RatiosDto;

public class TestFundamentalAnalysis {

    public static void main(String args[]) throws Exception {
        String companyName = "Hero Motocorp Ltd";
        int years = 5;
        FundamentalInfoDto fundamentalInfoDto = FundamentalInfoDto.builder(companyName,years)
                .setIndustry("Auto")
                .setMarketCap(58767)
                .addBalanceSheetDto(BalanceSheetDto
                        .builder().setDate("MAR-2020")
                        .setTotalShareCapital(39.95)
                        .setReserves(14350.90)
                        .setDebt(209.90).build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder().setDate("MAR-2019")
                        .setTotalShareCapital(39.95)
                        .setReserves(13070.87)
                        .setDebt(308.52).build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder().setDate("MAR-2018")
                        .setTotalShareCapital(39.94)
                        .setReserves(11971.46)
                        .setDebt(225.00	).build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder().setDate("MAR-2017")
                        .setTotalShareCapital(39.94)
                        .setReserves(10275.57	)
                        .setDebt(247.98).build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder().setDate("MAR-2016")
                        .setTotalShareCapital(39.94)
                        .setReserves(8794.17)
                        .setDebt(230.04).build())
                .addRatiosDto(RatiosDto
                        .builder()
                        .setDate("Mar-2020")
                        .setPeRatio(8.76)
                        .setEv(31602.29)
                        .setEvEbitda(6.67)
                        .setPbRatio(2.25)
                        .setRoe(25.70)
                        .build())
                .addRatiosDto(RatiosDto
                        .builder()
                        .setDate("Mar-2019")
                        .setPeRatio(14.81)
                        .setEv(31602.29)
                        .setEvEbitda(6.67)
                        .setPbRatio(2.25)
                        .setRoe(25.70)
                        .build())
                .build();



        System.out.println(fundamentalInfoDto.toString());
    }
}
