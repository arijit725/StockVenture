package org.arijit.stock.analyze.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.util.DateUtil;

import java.text.ParseException;

public class QuarterlyReportDTO implements Comparable<QuarterlyReportDTO> {
    private static final Logger logger = LogManager.getLogger(QuarterlyReportDTO.class);

    /**
     * Earning price per share
     * Get this information from www.screener.in
     */
    private String date;

    private double eps;
    private double yoySalesGrowth;


    private QuarterlyReportDTO(){

    }

    public String getDate() {
        return date;
    }

    public QuarterlyReportDTO setDate(String date) {
        this.date = date;
        return this;
    }

    public double getEps() {
        return eps;
    }

    public double getYoySalesGrowth() {
        return yoySalesGrowth;
    }

    public static QuarterlyReportDTO builder(){
        return new QuarterlyReportDTO();
    }

    public QuarterlyReportDTO build() throws Exception {
        validate();
        return this;
    }

    @Override
    public int compareTo(QuarterlyReportDTO quarterlyReportDTO) {
        try {
            long d1 = DateUtil.convertToEpochMilli(this.date);
            long d2 = DateUtil.convertToEpochMilli(quarterlyReportDTO.date);
            return Long.compare(d1,d2);
        } catch (ParseException e) {
            logger.error("Unable to convert Dates to ecpochInMillis: "+quarterlyReportDTO.date+" , "+date,e);
        }
        return 0;
    }

    private void validate() throws Exception {
        if(this.date==null)
            throw new Exception("Date is missing in balancesheet");
    }

    @Override
    public String toString() {
        return "QuarterlyReportDTO{" +
                "date='" + date + '\'' +
                ", eps=" + eps +
                ", yoySalesGrowth=" + yoySalesGrowth +
                '}';
    }
}
