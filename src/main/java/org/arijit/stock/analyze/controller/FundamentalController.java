package org.arijit.stock.analyze.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.BalanceSheetAnalysisInfo;
import org.arijit.stock.analyze.cache.MemCache;
import org.arijit.stock.analyze.dto.BalanceSheetDto;
import org.arijit.stock.analyze.dto.CompanyDto;
import org.arijit.stock.analyze.dto.ProfitAndLossDto;
import org.arijit.stock.analyze.service.FundamentalService;
import org.arijit.stock.analyze.util.StockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

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

    /**
     * On stock-report pageload, this url would be triggered.
     * Purpose of this url is to perform all sort of analysis.
     * If this reuturns OK, subsequent calls would get needful values.
     *
     * @param stockID
     * @param webExchange
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/generateReport/{stockID}")
    public Mono<ResponseEntity> generateReport(@PathVariable("stockID") String stockID, ServerWebExchange webExchange)throws IOException {
        logger.info("Accpeted generateReport Request: stockID: "+stockID);
        ResponseEntity<String> res = null;
        try {
            fundamentalService.analyzeStock(stockID,3);
            res = ResponseEntity.ok().build();
        }
        catch (NullPointerException e){
            logger.error("Unable to analyze stock: ",e);
            res = ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            logger.error("Unable to analyze stock: ",e);
            res = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return Mono.just(res);
    }

    @GetMapping(value = "/companydetails/{stockID}")
    public Mono<ResponseEntity> getCompanyDetails(@PathVariable("stockID") String stockID, ServerWebExchange webExchange)throws IOException {
        logger.info("Company Details Request for: stockID: "+stockID);

        ResponseEntity<String> res = null;
        try {
            CompanyDto companyDto = fundamentalService.getCompanyDetails(stockID);
            if(companyDto == null )
                res = ResponseEntity.noContent().build();
            else{
                String jsonString = StockUtil.generateJsonString(companyDto);
                logger.info("Response: companyDto jsonString: "+jsonString);
                res = ResponseEntity.ok().body(jsonString);
            }
        }
        catch (NullPointerException e){
            logger.error("Unable to analyze stock: ",e);
            res = ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            logger.error("Unable to analyze stock: ",e);
            res = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return Mono.just(res);
    }

    @GetMapping(value = "/balancesheet/{stockID}/{years}")
    public Mono<ResponseEntity> getBalancesheet(@PathVariable("stockID") String stockID, @PathVariable("years") int years, ServerWebExchange webExchange)throws IOException {
        logger.info("Balancesheet Request for: stockID: "+stockID+" years: "+years);

        ResponseEntity<String> res = null;
        try {
            List<BalanceSheetDto> balanceSheetDtoList = fundamentalService.getBalanceSheet(stockID,years);
            if(balanceSheetDtoList == null || balanceSheetDtoList.isEmpty())
                res = ResponseEntity.noContent().build();
            else{
                String jsonString = StockUtil.generateJsonString(balanceSheetDtoList);
                logger.info("Response: Balancesheet list: size: "+balanceSheetDtoList.size()+" jsonString: "+jsonString);
                res = ResponseEntity.ok().body(jsonString);
            }
        }
        catch (NullPointerException e){
            logger.error("Unable to analyze stock: ",e);
            res = ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            logger.error("Unable to analyze stock: ",e);
            res = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return Mono.just(res);
    }

    @GetMapping(value = "/balancesheetAnalysis/{stockID}/{years}")
    public Mono<ResponseEntity> getBalancesheetAnalysis(@PathVariable("stockID") String stockID, @PathVariable("years") int years, ServerWebExchange webExchange)throws IOException {
        logger.info("Balancesheet Request for: stockID: "+stockID+" years: "+years);

        ResponseEntity<String> res = null;
        try {
            BalanceSheetAnalysisInfo balanceSheetAnalysisInfo = fundamentalService.getAnalyzedBalanceSheet(stockID,years);
            if(balanceSheetAnalysisInfo == null)
                res = ResponseEntity.noContent().build();
            else{
                String jsonString = StockUtil.generateJsonString(balanceSheetAnalysisInfo);
                logger.info("Response: balanceSheetAnalysisInfo : "+jsonString);
                res = ResponseEntity.ok().body(jsonString);
            }
        }
        catch (NullPointerException e){
            logger.error("Unable to analyze stock: ",e);
            res = ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            logger.error("Unable to analyze stock: ",e);
            res = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return Mono.just(res);
    }

    @GetMapping(value = "/profitAndLoss/{stockID}/{years}")
    public Mono<ResponseEntity> getProfitAndLoss(@PathVariable("stockID") String stockID, @PathVariable("years") int years, ServerWebExchange webExchange)throws IOException {
        logger.info("Balancesheet Request for: stockID: "+stockID+" years: "+years);

        ResponseEntity<String> res = null;
        try {
            List<ProfitAndLossDto> balanceSheetDtoList = fundamentalService.getProfitAndLoss(stockID,years);
            if(balanceSheetDtoList == null || balanceSheetDtoList.isEmpty())
                res = ResponseEntity.noContent().build();
            else{
                String jsonString = StockUtil.generateJsonString(balanceSheetDtoList);
                logger.info("Response: Balancesheet list: size: "+balanceSheetDtoList.size()+" jsonString: "+jsonString);
                res = ResponseEntity.ok().body(jsonString);
            }
        }
        catch (NullPointerException e){
            logger.error("Unable to analyze stock: ",e);
            res = ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            logger.error("Unable to analyze stock: ",e);
            res = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return Mono.just(res);
    }
}
