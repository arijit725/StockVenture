package org.arijit.stock.analyze.controller;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.analysisdto.BalanceSheetAnalysisInfo;
import org.arijit.stock.analyze.analysisdto.ProfitAndLossAnalysisInfo;
import org.arijit.stock.analyze.analysisdto.RatioAnalysisInfo;
import org.arijit.stock.analyze.analysisdto.TargetPriceEstimationDto;
import org.arijit.stock.analyze.cache.MemCache;
import org.arijit.stock.analyze.dto.*;
import org.arijit.stock.analyze.parser.BalanceSheetPDFParser;
import org.arijit.stock.analyze.service.FundamentalService;
import org.arijit.stock.analyze.util.StockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.Arrays;
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

    @PostMapping(value = "/storeDetails", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> storeDetails(@RequestBody String ratioDetails, ServerWebExchange webExchange)throws IOException {
        String stockID = webExchange.getRequest().getHeaders().get("x-stockid").get(0);
        logger.info("Accpeted ratioDetails Request: stockID: "+stockID);
        ResponseEntity<String> res = null;
        try {
            fundamentalService.storeStock(stockID);
            logger.info("StockDetails  in memory.");
            res = ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Uable to store stock details: ",e);
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
        logger.info("ProfitAndLoss Request for: stockID: "+stockID+" years: "+years);

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

    @GetMapping(value = "/profitAndLossAnalysis/{stockID}/{years}")
    public Mono<ResponseEntity> getProfitAndLossAnalysis(@PathVariable("stockID") String stockID, @PathVariable("years") int years, ServerWebExchange webExchange)throws IOException {
        logger.info("ProfitAndLoss analysis Request for: stockID: "+stockID+" years: "+years);

        ResponseEntity<String> res = null;
        try {
            ProfitAndLossAnalysisInfo profitAndLossAnalysisInfo = fundamentalService.getAnalyzedProfitAndLoss(stockID,years);
            if(profitAndLossAnalysisInfo == null)
                res = ResponseEntity.noContent().build();
            else{
                String jsonString = StockUtil.generateJsonString(profitAndLossAnalysisInfo);
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


    @GetMapping(value = "/ratios/{stockID}/{years}")
    public Mono<ResponseEntity> getRatios(@PathVariable("stockID") String stockID, @PathVariable("years") int years, ServerWebExchange webExchange)throws IOException {
        logger.info("Ratios Request for: stockID: "+stockID+" years: "+years);

        ResponseEntity<String> res = null;
        try {
            List<RatiosDto> balanceSheetDtoList = fundamentalService.getRatios(stockID,years);
            if(balanceSheetDtoList == null || balanceSheetDtoList.isEmpty())
                res = ResponseEntity.noContent().build();
            else{
                String jsonString = StockUtil.generateJsonString(balanceSheetDtoList);
                logger.info("Response: getRatios list: size: "+balanceSheetDtoList.size()+" jsonString: "+jsonString);
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

    @GetMapping(value = "/ratioAnalysis/{stockID}/{years}")
    public Mono<ResponseEntity> getRatioAnalysis(@PathVariable("stockID") String stockID, @PathVariable("years") int years, ServerWebExchange webExchange)throws IOException {
        logger.info("Ratio analysis Request for: stockID: "+stockID+" years: "+years);

        ResponseEntity<String> res = null;
        try {
            RatioAnalysisInfo ratioAnalysisInfo = fundamentalService.getAnalyzedRatios(stockID,years);
            if(ratioAnalysisInfo == null)
                res = ResponseEntity.noContent().build();
            else{
                String jsonString = StockUtil.generateJsonString(ratioAnalysisInfo);
                logger.info("Response: ratioAnalysisInfo : "+jsonString);
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


    @GetMapping(value = "/targetPrice/{stockID}/{years}")
    public Mono<ResponseEntity> getargetPrice(@PathVariable("stockID") String stockID, @PathVariable("years") int years, ServerWebExchange webExchange)throws IOException {
        logger.info("Ratio analysis Request for: stockID: "+stockID+" years: "+years);

        ResponseEntity<String> res = null;
        try {
            TargetPriceEstimationDto targetPriceEstimationDto = fundamentalService.getTargetPriceEstimation(stockID,years);
            if(targetPriceEstimationDto == null)
                res = ResponseEntity.noContent().build();
            else{
                String jsonString = StockUtil.generateJsonString(targetPriceEstimationDto);
                logger.info("Response: targetPriceEstimationDto : "+jsonString);
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


    @GetMapping(value = "/listindustry")
    public Mono<ResponseEntity> listIndustry(ServerWebExchange webExchange)throws IOException {
        ResponseEntity<String> res = null;
        try {
            List<String> industryList = fundamentalService.listIndustry();
            String jsonString = StockUtil.generateJsonString(industryList);
            res = ResponseEntity.ok(jsonString);
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

    @GetMapping(value = "/liststock/{industry}")
    public Mono<ResponseEntity> listStock(@PathVariable("industry") String industry, ServerWebExchange webExchange)throws IOException {
        ResponseEntity<String> res = null;
        try {
            List<String> stockList = fundamentalService.listStock(industry);
            String jsonString = StockUtil.generateJsonString(stockList);
            res = ResponseEntity.ok(jsonString);
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


    @GetMapping(value = "/loadanalysis/{industry}/{companyname}")
    public Mono<ResponseEntity> loadAnalysis(@PathVariable("industry") String industry,@PathVariable("companyname") String companyName, ServerWebExchange webExchange)throws IOException {
        ResponseEntity<String> res = null;
        try {
            logger.info("Loadind details for Industry : "+industry+" Company: "+companyName);
            String stockID = fundamentalService.loadAnalysis(industry,companyName);
            res = ResponseEntity.ok(stockID);
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

    @PostMapping(value = "/uploadbl", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity> uploadBook(@RequestPart("balancesheet") FilePart filePart,@RequestPart("stockid") String stockID)throws IOException {
        logger.info("Acceepted balancesheet for : stockID :"+stockID+" fileName: "+filePart.filename());
        String basePathStr = "/home/arijit/Documents/aditi";
//        Path tempFile = Files.createTempFile("/home/arijit/IdeaProjects/libraryAdmin/fileloadtest/", filePart.filename());
        Path tempFile = Paths.get(basePathStr+"/"+filePart.filename());
        if(!tempFile.toFile().exists())
            tempFile.toFile().delete();
        tempFile.toFile().createNewFile();
        //NOTE method one
        AsynchronousFileChannel channel =
                AsynchronousFileChannel.open(tempFile, StandardOpenOption.WRITE);
        DataBufferUtils.write(filePart.content(), channel, 0)
                .doOnComplete(() -> {
                    logger.info("File writing finish at: "+tempFile.toString());
                })
                .subscribe();


        BalanceSheetPDFParser balanceSheetPDFParser = new BalanceSheetPDFParser(tempFile.toFile());

        try {
            fundamentalService.updateBalancesheetFromPDF(stockID,tempFile.toFile());
            ResponseEntity res = ResponseEntity.status(HttpStatus.ACCEPTED).body("parsed and processed");
            logger.info("Parsing done");
            return Mono.just(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Not supported");
        return Mono.just(responseEntity);

    }
}
