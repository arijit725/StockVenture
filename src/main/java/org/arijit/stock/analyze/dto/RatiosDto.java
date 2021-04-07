package org.arijit.stock.analyze.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.util.DateUtil;

import java.text.ParseException;

/**
 * All the Ratios and percentage that we get would be present here.
 *
 * @author Arijit Ghosh
 */
public class RatiosDto implements Comparable<String > {
    /*this is current industry standard PE ratio*/

    private static final Logger logger = LogManager.getLogger(RatiosDto.class);


    private String date;
    private double industryPE;

    /**This is company PE Ratio.
     * This indicate if value of a stock is undervalued or overvalued.
     * For UnderValued PE growth potential may not be strong enough.
     * For overValued PE growth potential may be high
     * Always we Need to check for other ratios indications before decision.
     */

    private double peRatio;

    /**
     * PB ratio signifies how much premium we are paying per share in terms of book value.
     * For a good company PBRatio ranges between 3 to 6.
     */
    private double pbRatio;

    /**
     * Return on equity.
     * For a good company ROE should be >20.
     * If ROE decliens but still remails >20, we should consider this as netural.
     * where there is increase in ROE and decline / slow grow in PB ratio there could be high chance this stock would
     * become multibagger.
     */
    private double roe;


    /*Enterprise value: This information will be taken from Ratios page*/
    private double ev;

    /**
     * EV/EBITDA ratio for an FY: This information will be taken from Ratios page
     */
    private double evEbitda;

    private RatiosDto(){

    }

    public RatiosDto setDate(String date) {
        this.date = date;
        return this;
    }

    public RatiosDto setIndustryPE(double industryPE) {
        this.industryPE = industryPE;
        return this;
    }

    public RatiosDto setPeRatio(double peRatio) {
        this.peRatio = peRatio;
        return this;
    }

    public RatiosDto setPbRatio(double pbRatio) {
        this.pbRatio = pbRatio;
        return this;
    }

    public RatiosDto setRoe(double roe) {
        this.roe = roe;
        return this;
    }

    public RatiosDto setEv(double ev) {
        this.ev = ev;
        return this;
    }

    public RatiosDto setEvEbitda(double evEbitda) {
        this.evEbitda = evEbitda;
        return this;
    }

    public String getDate() {
        return date;
    }

    public double getIndustryPE() {
        return industryPE;
    }

    public double getPeRatio() {
        return peRatio;
    }

    public double getPbRatio() {
        return pbRatio;
    }

    public double getRoe() {
        return roe;
    }

    public double getEv() {
        return ev;
    }

    public double getEvEbitda() {
        return evEbitda;
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

    public static RatiosDto builder(){
        return new RatiosDto();
    }

    public RatiosDto build() throws Exception {
        validate();
        return this;
    }

    private void validate() throws Exception {
        if(this.date==null)
            throw new Exception("Date is missing in balancesheet");
    }
}
