package org.arijit.stock.analyze.start;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.util.DateUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

@SpringBootApplication
//@ComponentScan(basePackages = {"org.arijit.library.admin"})
@EnableSwagger2
public class StockStarter implements ApplicationRunner {

    private static final Logger logger = LogManager.getLogger(StockStarter.class);
    public static void main(String args[]) {

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Date date = DateUtil.convertToDate("Jul-2001");

        logger.info(date+"=== "+ DateUtil.convertToEpochMilli("Jul-2001"));
    }
}
