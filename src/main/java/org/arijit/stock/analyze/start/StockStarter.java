package org.arijit.stock.analyze.start;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.cache.MemCache;
import org.arijit.stock.analyze.dto.*;
import org.arijit.stock.analyze.util.DateUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

@SpringBootApplication
@ComponentScan(basePackages = {"org.arijit.stock"})
@EnableSwagger2
public class StockStarter implements ApplicationRunner {

    private static final Logger logger = LogManager.getLogger(StockStarter.class);
    public static void main(String args[]) {
        SpringApplication.run(StockStarter.class);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Date date = DateUtil.convertToDate("Jul-2001");

        logger.info(date+"=== "+ DateUtil.convertToEpochMilli("Jul-2001"));

        //for testing only
        FundamentalInfoDto fundamentalInfoDto = tcsDataSet();
        MemCache.getInstance().insertDummyFundamentalInfo(fundamentalInfoDto);
    }
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("org.arijit")).build();
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
