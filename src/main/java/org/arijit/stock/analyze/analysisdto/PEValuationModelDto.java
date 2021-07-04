package org.arijit.stock.analyze.analysisdto;

import com.google.gson.annotations.SerializedName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.util.DateUtil;

import java.text.ParseException;
import java.util.*;

/**
 * This class will hold data required for PE Valuation.
 * This is a target price estimation technique
 */
public class PEValuationModelDto {
    private static final Logger logger = LogManager.getLogger(PEValuationModelDto.class);

    @SerializedName("stockpriceList")
    private List<StockPrice> stockPriceList;
    private Map<String, Double> epsMap;

    private double currentSharePrice;

    private String marketGrowth;
    private String growthApproach;

    private double avgPE7Years;
    private double avgPE4Years;

    private double EPSGrowthRate;

    private double estimatedEPS;

    private double targetPrice7yrPE;

    private double targetPrice4yrPE;

    private double fairValuedTargetPrice;

    private boolean evluated;

    public static Logger getLogger() {
        return logger;
    }

    public void setEvluated(boolean evluated) {
        this.evluated = evluated;
    }

    public boolean isEvluated() {
        return evluated;
    }

    public String getGrowthApproach() {
        return growthApproach;
    }

    public void setGrowthApproach(String growthApproach) {
        this.growthApproach = growthApproach;
    }

    public void setMarketGrowth(String marketGrowth) {
        this.marketGrowth = marketGrowth;
    }

    public String getMarketGrowth() {
        return marketGrowth;
    }

    public double getCurrentSharePrice() {
        return currentSharePrice;
    }

    public void setCurrentSharePrice(double currentSharePrice) {
        this.currentSharePrice = currentSharePrice;
    }

    public double getFairValuedTargetPrice() {
        return fairValuedTargetPrice;
    }

    public double getTargetPrice4yrPE() {
        return targetPrice4yrPE;
    }

    public double getTargetPrice7yrPE() {
        return targetPrice7yrPE;
    }

    public void setFairValuedTargetPrice(double fairValuedTargetPrice) {
        this.fairValuedTargetPrice = fairValuedTargetPrice;
    }

    public void setTargetPrice4yrPE(double targetPrice4yrPE) {
        this.targetPrice4yrPE = targetPrice4yrPE;
    }

    public void setTargetPrice7yrPE(double targetPrice7yrPE) {
        this.targetPrice7yrPE = targetPrice7yrPE;
    }


    public double getEstimatedEPS() {
        return estimatedEPS;
    }

    public void setEstimatedEPS(double estimatedEPS) {
        this.estimatedEPS = estimatedEPS;
    }

    public double getAvgPE7Years() {
        return avgPE7Years;
    }

    public void setAvgPE7Years(double avgPE7Years) {
        this.avgPE7Years = avgPE7Years;
    }

    public double getEPSGrowthRate() {
        return EPSGrowthRate;
    }

    public void setEPSGrowthRate(double EPSGrowthRate) {
        this.EPSGrowthRate = EPSGrowthRate;
    }

    public double getAvgPE4Years() {
        return avgPE4Years;
    }

    public void setAvgPE4Years(double avgPE4Years) {
        this.avgPE4Years = avgPE4Years;
    }

    public List<StockPrice> getStockPriceList() {
        return stockPriceList;
    }

    public void setStockPriceList(List<StockPrice> stockPriceList) {
        Collections.sort(stockPriceList);
        this.stockPriceList = stockPriceList;
    }


    public Map<String, Double> getEpsMap() {
        return epsMap;
    }

    public void setEpsMap(Map<String, Double> epsMap) {
        TreeMap<String, Double> treeMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                try {
                    long date1 = DateUtil.convertToEpochMilli(o1);
                    long date2 = DateUtil.convertToEpochMilli(o2);
                    return Long.compare(date2,date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        treeMap.putAll(epsMap);
        this.epsMap = epsMap;
    }

    @Override
    public String toString() {
        return "PEValuationModelDto{" +
                "stockPriceList=" + stockPriceList +
                ", epsMap=" + epsMap +
                '}';
    }

    public static class StockPrice implements Comparable<StockPrice>{
       private  String date;
       private double open;
       private double high;
       private double low;
       private double close;
       private double fyEPS;
       private double pe;

        public double getFyEPS() {
            return fyEPS;
        }

        public double getPe() {
            return pe;
        }

        public void setFyEPS(double fyEPS) {
            this.fyEPS = fyEPS;
        }

        public void setPe(double pe) {
            this.pe = pe;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getOpen() {
            return open;
        }

        public void setOpen(double open) {
            this.open = open;
        }

        public double getHigh() {
            return high;
        }

        public void setHigh(double high) {
            this.high = high;
        }

        public double getLow() {
            return low;
        }

        public void setLow(double low) {
            this.low = low;
        }

        public double getClose() {
            return close;
        }

        public void setClose(double close) {
            this.close = close;
        }

        @Override
        public String toString() {
            return "StockPrice{" +
                    "date='" + date + '\'' +
                    ", open=" + open +
                    ", high=" + high +
                    ", low=" + low +
                    ", close=" + close +
                    ", fyEPS=" + fyEPS +
                    ", pe=" + pe +
                    '}';
        }

        @Override
        public int compareTo(StockPrice stockPrice) {
            try {
                long d1 = DateUtil.convertToEpochMilli(this.date);
                long d2 = DateUtil.convertToEpochMilli(stockPrice.date);
                return Long.compare(d1,d2);
            } catch (ParseException e) {
                logger.error("Unable to convert Dates to ecpochInMillis: "+stockPrice.date+" , "+date,e);
            }
            return 0;
        }
    }
}
