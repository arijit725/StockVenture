package org.arijit.stock.analyze.util;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.FastMath;

public class FundamentalAnalysisUtil {

    /**
     * <pre>
     *     Formula:
     *          CAGR = (((EndingYearValue/StartingYearValue)^(1/year))-1)*100
     * </pre>
     * This will return CAGR in percentage
     * @param endingYearValue
     * @param startingYearValue
     * @param year
     * @return
     */
    public static double cagr(double endingYearValue, double startingYearValue, int year){
        double tmp = endingYearValue/startingYearValue;
        double inverseYear = (double) 1/year;
        tmp = Math.pow(tmp,inverseYear);
        tmp = tmp-1;
        tmp = tmp*100;
        return tmp;
    }

}
