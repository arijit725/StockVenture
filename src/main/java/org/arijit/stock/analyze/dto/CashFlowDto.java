package org.arijit.stock.analyze.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.util.DateUtil;

import java.text.ParseException;
import java.util.Objects;

public class CashFlowDto implements Comparable<CashFlowDto> {

    private static final Logger logger = LogManager.getLogger(CashFlowDto.class);

    private String date;

    private double cashFromOperatingActivity;
    /*fixedAssestsPurchased is equal to capital expendeture*/
    private double fixedAssestsPurchased;

    private double netCashFlow;

//    private double freeCashFlow;

    private CashFlowDto(){

    }

    public String getDate() {
        return date;
    }

    public double getCashFromOperatingActivity() {
        return cashFromOperatingActivity;
    }

    public double getFixedAssestsPurchased() {
        return fixedAssestsPurchased;
    }

    public double getNetCashFlow() {
        return netCashFlow;
    }

    public CashFlowDto setDate(String date) {
        this.date = date;
        return this;
    }

    public CashFlowDto setCashFromOperatingActivity(double cashFromOperatingActivity) {
        this.cashFromOperatingActivity = cashFromOperatingActivity;
        return this;
    }

    public CashFlowDto setFixedAssestsPurchased(double fixedAssestsPurchased) {
        this.fixedAssestsPurchased = Math.abs(fixedAssestsPurchased);
        return this;
    }

    public CashFlowDto setNetCashFlow(double netCashFlow) {
        this.netCashFlow = netCashFlow;
        return this;
    }

//    public double getFreeCashFlow() {
//        return freeCashFlow;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashFlowDto that = (CashFlowDto) o;
        return Double.compare(that.cashFromOperatingActivity, cashFromOperatingActivity) == 0 &&
                Double.compare(that.fixedAssestsPurchased, fixedAssestsPurchased) == 0 &&
                Double.compare(that.netCashFlow, netCashFlow) == 0 &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, cashFromOperatingActivity, fixedAssestsPurchased, netCashFlow);
    }

    @Override
    public String toString() {
        return "CashFlowDto{" +
                "date='" + date + '\'' +
                ", cashFromOperatingActivity=" + cashFromOperatingActivity +
                ", fixedAssestsPurchased=" + fixedAssestsPurchased +
                ", netCashFlow=" + netCashFlow +
//                ", freeCashFlow=" + freeCashFlow +
                '}';
    }

    public static CashFlowDto builder(){
        return new CashFlowDto();
    }

    public CashFlowDto build() throws Exception {
        validate();
        generateFreeCashFLow();
        return this;
    }

    private void generateFreeCashFLow(){

//        freeCashFlow = cashFromOperatingActivity-Math.abs(fixedAssestsPurchased);
    }
    private void validate() throws Exception {
        if(this.date==null)
            throw new Exception("Date is missing in balancesheet");
    }


    @Override
    public int compareTo(CashFlowDto cashFlowDto) {
        //this method will sort balancesheet based on date
        try {
            long d1 = DateUtil.convertToEpochMilli(this.date);
            long d2 = DateUtil.convertToEpochMilli(cashFlowDto.date);
            return Long.compare(d1,d2);
        } catch (ParseException e) {
            logger.error("Unable to convert Dates to ecpochInMillis: "+cashFlowDto.date+" , "+date,e);
        }
        return 0;
    }
}

