package org.arijit.stock.analyze.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.util.DateUtil;

import java.text.ParseException;
import java.util.Date;

/**
 * YearlyReport dto will contain the informations that we need from a company yearly report.
 * yearly report would be for last 3 or 5 or 7 years.
 *
 * @author Arijit Ghosh
 */
public class YearlyReportDto implements Comparable<String>{
    private static final Logger logger = LogManager.getLogger(YearlyReportDto.class);
    private String date;

    /**
     * Basic EPS under EPS Before Extra Ordinary
     */
    private double basicEPS;

    private YearlyReportDto(){

    }

    public double getBasicEPS() {
        return basicEPS;
    }

    public YearlyReportDto setBasicEPS(double basicEPS) {
        this.basicEPS = basicEPS;
        return this;
    }

    @Override
    public int compareTo(String date2) {
        //this method will sort balancesheet based on date
        try {
            long d1 = DateUtil.convertToEpochMilli(this.date);
            long d2 = DateUtil.convertToEpochMilli(date2);
            return Long.compare(d1,d2);
        } catch (ParseException e) {
            logger.error("Unable to convert Dates to ecpochInMillis: "+date2+" , "+date,e);
        }
        return 0;
    }

    public static YearlyReportDto builder(){
        return new YearlyReportDto();
    }

    public void build() throws Exception {
        validate();
    }

    private void validate() throws Exception {
        if(this.date==null)
            throw new Exception("Date is missing in balancesheet");
    }


}
