package org.arijit.stock.analyze.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.service.FundamentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping(path = "/fundamental")
public class FundamentalController {
    private static final Logger logger = LogManager.getLogger(FundamentalController.class);

    @Autowired
    private FundamentalService fundamentalService;

    @PostMapping(value = "/companyDetails", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> companyDetails(@RequestBody String companyDetails)throws IOException {
        logger.info("Accpeted Request: "+companyDetails);

        String stockID =  fundamentalService.updateCompanyDetails(companyDetails);
        ResponseEntity<String> res = ResponseEntity.ok().body(stockID);
        return Mono.just(res);
    }

    @PostMapping(value = "/balancesheetDetails", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> balancesheetDetails(@RequestBody String balancesheetDetails, ServerWebExchange webExchange)throws IOException {
        String stockID = webExchange.getRequest().getHeaders().get("x-stockid").get(0);
        logger.info("Accpeted Request: stockID: "+stockID+balancesheetDetails);
        ResponseEntity<String> res = null;
        try {
            fundamentalService.updateBalanceSheetList(stockID,balancesheetDetails);
            logger.info("Balancesheet details updated in memory.");
            res = ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            res = ResponseEntity.notFound().build();
        }

        return Mono.just(res);
    }

    @PostMapping(value = "/profitAndLossDetails", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> profitAndLossDetails(@RequestBody String profitAndLossDetails, ServerWebExchange webExchange)throws IOException {
        String stockID = webExchange.getRequest().getHeaders().get("x-stockid").get(0);
        logger.info("Accpeted Profitandloss Request: stockID: "+stockID+profitAndLossDetails);
        ResponseEntity<String> res = null;
        try {
            fundamentalService.updateProfitAndLossDetails(stockID,profitAndLossDetails);
            logger.info("Profit And Loss details updated in memory.");
            res = ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            res = ResponseEntity.notFound().build();
        }

        return Mono.just(res);
    }

    @PostMapping(value = "/yearlyReportDetails", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> yearlyReportDetails(@RequestBody String yearlyReportDetails, ServerWebExchange webExchange)throws IOException {
        String stockID = webExchange.getRequest().getHeaders().get("x-stockid").get(0);
        logger.info("Accpeted yearlyReport Request: stockID: "+stockID+yearlyReportDetails);
        ResponseEntity<String> res = null;
        try {
            fundamentalService.updateYearlyReportDetails(stockID,yearlyReportDetails);
            logger.info("Yearly Report details updated in memory.");
            res = ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            res = ResponseEntity.notFound().build();
        }

        return Mono.just(res);
    }

    @PostMapping(value = "/ratioDetails", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> ratioDetails(@RequestBody String ratioDetails, ServerWebExchange webExchange)throws IOException {
        String stockID = webExchange.getRequest().getHeaders().get("x-stockid").get(0);
        logger.info("Accpeted ratioDetails Request: stockID: "+stockID+ratioDetails);
        ResponseEntity<String> res = null;
        try {
            fundamentalService.updateRatioDetails(stockID,ratioDetails);
            logger.info("Ratio details updated in memory.");
            res = ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            res = ResponseEntity.notFound().build();
        }

        return Mono.just(res);
    }
}
