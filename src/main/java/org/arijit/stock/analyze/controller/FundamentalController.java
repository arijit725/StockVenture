package org.arijit.stock.analyze.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping(path = "/fundamental")
public class FundamentalController {
    private static final Logger logger = LogManager.getLogger(FundamentalController.class);

    @PostMapping(value = "/companyDetails", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity> companyDetails(@RequestBody String companyDetails)throws IOException {
        logger.info("Accpeted Request: "+companyDetails);
        ResponseEntity<String> res = ResponseEntity.ok().body("Done");
        return Mono.just(res);


    }
}
