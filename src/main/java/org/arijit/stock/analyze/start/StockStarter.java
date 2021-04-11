package org.arijit.stock.analyze.start;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    }
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("org.arijit")).build();
    }
}
