package org.arijit.stock.analyze.dto;

import org.apache.commons.math3.analysis.function.Divide;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.util.DateUtil;

import java.text.ParseException;

/**
 * ProfitAndLoss dto will contain the informations that we need from a company Profit and Loss statement.
 * Profit and Loss statement informations would be for last 3 or 5 or 7 years.
 *
 * @author Arijit Ghosh
 */
public final class ProfitAndLossDto implements Comparable<String> {
    private static final Logger logger = LogManager.getLogger(ProfitAndLossDto.class);

    /**
     * <pre>
     *          - Shows health of business operation.
     *          - Shows trends in business cycle.
     *          - If you have significant subsidized company check for consolidated result, else look for standalone result.
     * </pre>
     */

    /*if it is yearly, normally it comes as Mar'YY.
        But sometimes we may want to check balancesheet quarterly, for that purpose we need to
        give provision for other dates like JUN'YY or SEPT'YY or DEC'YY*/
    private String date;

    /**
     * For a good company Net sales amount should always grow.
     */
    private double netSales;

    /**
     * For a good company say in FY17 comsumption of Raw material is incresed and we see a growth in net sales in FY18.
     * Again same trends appear in FY19. Which means The raw meterials are contributing to company growth. So if in current
     * FY comsumption of Raw Material is increased, we can say that in next year there is a chance that netsales will increase.
     *
     * Note: there could be industry where raw materials does not matter. In that case consider this to 0.
     */
    private double consumptionRawMaterial;

    /**
     * If employee cost is growing and again net sales is growing, this means company growing in right direction
     */
    private double employeeCost;

    /**
     * PBIT: Profit before interest and tax. Profit should always grow for a good company.
     */
    private double pbit;

    /**
     * This is the interest paid on top of debt. If Interest is increased, this means debt is growing.
     * This is bad for a company.
     */
    private double interest;

    /**
     * Net Profit for a good company grows
     */
    private double netProfit;

    /* this would be calculated internally*/
    private double netProfitVsNetSalesRatio;

    private ProfitAndLossDto(){

    }

    public String getDate() {
        return date;
    }

    public ProfitAndLossDto setDate(String date) {
        this.date = date;
        return this;
    }

    public double getNetSales() {
        return netSales;
    }

    public ProfitAndLossDto setNetSales(double netSales) {
        this.netSales = netSales;
        return this;
    }

    public double getConsumptionRawMaterial() {
        return consumptionRawMaterial;
    }

    public ProfitAndLossDto setConsumptionRawMaterial(double consumptionRawMaterial) {
        this.consumptionRawMaterial = consumptionRawMaterial;
        return this;
    }

    public double getEmployeeCost() {
        return employeeCost;
    }

    public ProfitAndLossDto setEmployeeCost(double employeeCost) {
        this.employeeCost = employeeCost;
        return this;
    }

    public double getPbit() {
        return pbit;
    }

    public ProfitAndLossDto setPbit(double pbit) {
        this.pbit = pbit;
        return this;
    }

    public double getInterest() {
        return interest;
    }

    public ProfitAndLossDto setInterest(double interest) {
        this.interest = interest;
        return this;
    }

    public double getNetProfit() {
        return netProfit;
    }

    public ProfitAndLossDto setNetProfit(double netProfit) {
        this.netProfit = netProfit;
        return this;
    }

    public double getNetProfitVsNetSalesRatio() {
        return netProfitVsNetSalesRatio;
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

    public static ProfitAndLossDto builder(){
        return new ProfitAndLossDto();
    }

    public void build() throws Exception {
        validate();
        calculateNetProfitVsNetSalesRatio();
        logger.info("NetProfitVsNetSalesRatio: "+this.netProfitVsNetSalesRatio);
    }

    private void validate() throws Exception {
        if(this.date==null)
            throw new Exception("Date is missing in balancesheet");
    }

    private void calculateNetProfitVsNetSalesRatio(){
         this.netProfitVsNetSalesRatio = this.netProfit/this.netSales;
    }

    @Override
    public String toString() {
        return "ProfitAndLossDto[" +
                "date='" + date + '\'' +
                ", netSales=" + netSales +
                ", consumptionRawMaterial=" + consumptionRawMaterial +
                ", employeeCost=" + employeeCost +
                ", pbit=" + pbit +
                ", interest=" + interest +
                ", netProfit=" + netProfit +
                ", netProfitVsNetSalesRatio=" + netProfitVsNetSalesRatio +
                ']';
    }
}
