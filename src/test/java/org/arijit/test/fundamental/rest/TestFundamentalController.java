package org.arijit.test.fundamental.rest;

import org.arijit.stock.analyze.dto.*;
import org.arijit.stock.analyze.start.StockStarter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Collections;


@SpringBootTest(classes = StockStarter.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestFundamentalController {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public static void  setupServer(){
        System.out.println("Setting up rest server");

    }
    @Test
    public void testGenerateReport(){
        CompanyDto companyDto = new CompanyDto();

        companyDto.setCompanyName("TCS");
        companyDto.setCurrentSharePrice(3460.00);
        companyDto.setFaceValue(1);
        companyDto.setIndustry("Computers- software");
        companyDto.setIndustryPE(35.04);
        companyDto.setMarketCap(1250500);
        companyDto.setTtmpe(21.22);
        companyDto.setTtmeps(87.67);

        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity("http://localhost:"+port+"/fundamental/companyDetails", companyDto, String.class);
        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        String stockId = responseEntity.getBody();
        this.restTemplate.getRestTemplate().setInterceptors(Collections.singletonList(((request, body, execution) -> {
            request.getHeaders()
                    .add("x-stockid", stockId);
            request.getHeaders().add("Content-type","application/json; charset=utf-8");
            return execution.execute(request, body);
        })));
        String dummyJson="{\"action\":\"updateStock\"}";
        responseEntity = this.restTemplate
                .postForEntity("http://localhost:"+port+"/fundamental/storeDetails",dummyJson, String.class);
        System.out.println(responseEntity.getBody());
        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
    }


    private static FundamentalInfoDto tcsDataSet() throws Exception {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyName("TCS");
        companyDto.setCurrentSharePrice(3460.00);
        companyDto.setFaceValue(1);
        companyDto.setIndustry("Computers- software");
        companyDto.setIndustryPE(35.04);
        companyDto.setMarketCap(1250500);
        companyDto.setTtmpe(21.22);
        companyDto.setTtmeps(87.67);
        FundamentalInfoDto tcsFundamental = FundamentalInfoDto.builder(companyDto.getYears());

        tcsFundamental.setCompanyDto(companyDto);

        tcsFundamental
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2020")
                        .setTotalShareCapital(375.01)
                        .setEquityShareCapital(375.01)
                        .setReserves(78523.00)
                        .setDebt(0)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2019")
                        .setTotalShareCapital(191.00)
                        .setEquityShareCapital(191.00)
                        .setReserves(75675.00)
                        .setDebt(220.00)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2018")
                        .setTotalShareCapital(197.00)
                        .setEquityShareCapital(197.00)
                        .setReserves(77825.00)
                        .setDebt(244.00)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2017")
                        .setTotalShareCapital(197.00)
                        .setEquityShareCapital(197.00)
                        .setReserves(64816.00)
                        .setDebt(163.00)
                        .build())
                .addBalanceSheetDto(BalanceSheetDto
                        .builder()
                        .setDate("Mar-2016")
                        .setTotalShareCapital(195.87)
                        .setEquityShareCapital(195.87)
                        .setReserves(45220.57)
                        .setDebt(250.27)
                        .build());


        tcsFundamental
                .addProfitAndLossDto(ProfitAndLossDto
                        .builder()
                        .setDate("Mar-2020")
                        .setNetSales(131306.00)
                        .setConsumptionRawMaterial(0)
                        .setEmployeeCost(64906.00)
                        .setPbit(45112.00)
                        .setInterest(743.00)
                        .setNetProfit(33260.00))
                .addProfitAndLossDto(ProfitAndLossDto
                        .builder()
                        .setDate("Mar-2019")
                        .setNetSales(121306.00)
                        .setConsumptionRawMaterial(0)
                        .setEmployeeCost(64906.00)
                        .setPbit(45112.00)
                        .setInterest(753.00)
                        .setNetProfit(33250.00))
                .addProfitAndLossDto(ProfitAndLossDto
                        .builder()
                        .setDate("Mar-2018")
                        .setNetSales(111306.00)
                        .setConsumptionRawMaterial(0)
                        .setEmployeeCost(64906.00)
                        .setPbit(45112.00)
                        .setInterest(713.00)
                        .setNetProfit(33240.00))
                .addProfitAndLossDto(ProfitAndLossDto
                        .builder()
                        .setDate("Mar-2017")
                        .setNetSales(141306.00)
                        .setConsumptionRawMaterial(0)
                        .setEmployeeCost(64906.00)
                        .setPbit(45112.00)
                        .setInterest(723.00)
                        .setNetProfit(33230.00))
                .addProfitAndLossDto(ProfitAndLossDto
                        .builder()
                        .setDate("Mar-2016")
                        .setNetSales(151306.00)
                        .setConsumptionRawMaterial(0)
                        .setEmployeeCost(64906.00)
                        .setPbit(45112.00)
                        .setInterest(733.00)
                        .setNetProfit(33220.00));



        tcsFundamental
                .addYearlyReportDto(YearlyReportDto
                        .builder()
                        .setDate("Mar-2020")
                        .setBasicEPS(79.34)
                        .build())
                .addYearlyReportDto(YearlyReportDto
                        .builder()
                        .setDate("Mar-2019")
                        .setBasicEPS(131.15)
                        .build())
                .addYearlyReportDto(YearlyReportDto
                        .builder()
                        .setDate("Mar-2018")
                        .setBasicEPS(120.04)
                        .build())
                .addYearlyReportDto(YearlyReportDto
                        .builder()
                        .setDate("Mar-2017")
                        .setBasicEPS(117.11)
                        .build())
                .addYearlyReportDto(YearlyReportDto
                        .builder()
                        .setDate("Mar-2016")
                        .setBasicEPS(98.31)
                        .build());

        tcsFundamental
                .addRatiosDto(RatiosDto
                        .builder()
                        .setDate("Mar-2020")
                        .setEv(741250.00)
                        .setEvEbitda(17.40)
                        .setPeRatio(24.10)
                        .setRoe(41.39)
                        .setPbRatio(9.51))
                .addRatiosDto(RatiosDto
                        .builder()
                        .setDate("Mar-2019")
                        .setEv(540949.30)
                        .setEvEbitda(16.10)
                        .setPeRatio(10.62)
                        .setRoe(44.72)
                        .setPbRatio(7.17))
                .addRatiosDto(RatiosDto
                        .builder()
                        .setDate("Mar-2018")
                        .setEv(477854.70)
                        .setEvEbitda(15.09)
                        .setPeRatio(9.11)
                        .setRoe(38.10)
                        .setPbRatio(6.14))
                .addRatiosDto(RatiosDto
                        .builder()
                        .setDate("Mar-2017")
                        .setEv(491018.85)
                        .setEvEbitda(15.94)
                        .setPeRatio(10.23)
                        .setRoe(33.27)
                        .setPbRatio(7.62))
                .addRatiosDto(RatiosDto
                        .builder()
                        .setDate("Mar-2016")
                        .setEv(482638.45)
                        .setEvEbitda(18.93)
                        .setPeRatio(10.98)
                        .setRoe(30.31)
                        .setPbRatio(10.98));

        return tcsFundamental.build();

    }
}
