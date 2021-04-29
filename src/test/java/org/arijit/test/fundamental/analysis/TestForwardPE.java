package org.arijit.test.fundamental.analysis;

import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.fundamental.RatiosEvaluation;
import org.arijit.stock.analyze.fundamental.YearlyReportEvaluation;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestForwardPE {

    public static void main(String args[]){

        double estimatedEPS=YearlyReportEvaluation.getInstance().calcEstimatedEPS(185.14,158.26,3);
//        RatiosEvaluation.getInstance().calcForwardPE(3460.00,estimatedEPS);
    }
}
