package org.arijit.stock.analyze.dto;

import com.google.gson.annotations.SerializedName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.util.DateUtil;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

/**
 * Balancesheet dto will contain the informations that we need from a company balancesheet.
 * Balancesheet informations would be for last 3 or 5 or 7 years.
 *
 * @author Arijit Ghosh
 */

public final class BalanceSheetDto implements Comparable<BalanceSheetDto> {
    private static final Logger logger = LogManager.getLogger(BalanceSheetDto.class);

    /*<pre>
     * Good Company Balancesheet                        Average Company Balancesheet                Poor Company Balancesheet
     * ========================================         ========================================    ========================================
     * share capital = constant/almost constant         share capital = dilutes                     share capital = dilutes/increasing
     * Reserves = increasing                            Reserves = almost constant/ slow growth     Reserves = Constant or reduce
     * Debt = decreasing                                Debt = almost constant/increasing           Debt = Increasing
     *
     * Avoid Companies instantly if balancesheet shows poor.
     *
     * </pre>*/


    /*if it is yearly, normally it comes as Mar'YY.
    But sometimes we may want to check balancesheet quarterly, for that purpose we need to
    give provision for other dates like JUN'YY or SEPT'YY or DEC'YY*/
    private String date;
    /* this is same as share capital or share outstanding.
    for a good balancesheet, share capital should be constant,and not suppose to dilutes/increase.
    This should be read as per old format as well as new format
     */
    @SerializedName(value = "total_share_capital")
    private double totalShareCapital;

    @SerializedName(value = "equity_share_capital")
    private double equityShareCapital;
    /*
    For a good company reserves should always increase. This is also called as Revenue
    Will be found in old format
    * */
    private double reserves;
    /* For a good company debt should always decrease. Will be found in old format
    * In new format Debt  = Long Term Borrowings + Short Term Borrowings
    */
    private double debt;

    private BalanceSheetDto(){

    }

    public String getDate() {
        return date;
    }

    public BalanceSheetDto setDate(String date) {
        this.date = date;
        return this;
    }

    public double getTotalShareCapital() {
        return totalShareCapital;
    }

    public BalanceSheetDto setTotalShareCapital(double totalShareCapital) {
        this.totalShareCapital = totalShareCapital;
        return this;
    }

    public double getReserves() {
        return reserves;
    }

    public BalanceSheetDto setReserves(double reserves) {
        this.reserves = reserves;
        return this;
    }

    public double getDebt() {
        return debt;
    }

    public BalanceSheetDto setDebt(double debt) {
        this.debt = debt;
        return this;
    }

    public BalanceSheetDto setEquityShareCapital(double equityShareCapital) {
        this.equityShareCapital = equityShareCapital;
        return this;
    }

    public double getEquityShareCapital() {
        return equityShareCapital;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceSheetDto that = (BalanceSheetDto) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    @Override
    public int compareTo(BalanceSheetDto balanceSheetDto) {
        //this method will sort balancesheet based on date
        try {
            long d1 = DateUtil.convertToEpochMilli(this.date);
            long d2 = DateUtil.convertToEpochMilli(balanceSheetDto.date);
            return Long.compare(d1,d2);
        } catch (ParseException e) {
            logger.error("Unable to convert Dates to ecpochInMillis: "+balanceSheetDto.date+" , "+date,e);
        }
        return 0;
    }

    public static BalanceSheetDto builder(){
        return new BalanceSheetDto();
    }

    public BalanceSheetDto build() throws Exception {
        validate();
        return this;
    }
    private void validate() throws Exception {
        if(this.date==null)
            throw new Exception("Date is missing in balancesheet");
        if(this.totalShareCapital==0)
            throw new Exception("Total Share Capital is missing in balancesheet");
    }

    @Override
    public String toString() {
        return "BalanceSheetDto[" +
                "date='" + date + '\'' +
                ", totalShareCapital=" + totalShareCapital +
                ", reserves=" + reserves +
                ", debt=" + debt +
                ']';
    }
}
