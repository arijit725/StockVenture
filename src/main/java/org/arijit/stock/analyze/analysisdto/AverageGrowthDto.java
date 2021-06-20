package org.arijit.stock.analyze.analysisdto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AverageGrowthDto {

    private FCFGrowthDto fcfGrowthDto;
    private NetProfitGrowthDto netProfitGrowthDto;

    public AverageGrowthDto(){
        fcfGrowthDto = new FCFGrowthDto();
        netProfitGrowthDto = new NetProfitGrowthDto();
    }

    public FCFGrowthDto getFcfGrowthDto() {
        return fcfGrowthDto;
    }

    public NetProfitGrowthDto getNetProfitGrowthDto() {
        return netProfitGrowthDto;
    }

    public static class FCFGrowthDto{

        private Map<String,Double> growthMap;
        private double growthRate;

         private FCFGrowthDto(){
            growthMap = new HashMap<>();
        }

        public void addPeriodGrowth(String period, double growthRate) {
            this.growthMap.put(period,growthRate);
        }

        public Map<String, Double> getGrowthMap() {
            return growthMap;
        }
        public double getGrowthRate() {
            return growthRate;
        }

        public void setGrowthRate(double growthRate) {
            this.growthRate = growthRate;
        }

        @Override
        public String toString() {
            return "FCFGrowthDto{" +
                    "growthMap=" + growthMap +
                    ", growthRate=" + growthRate +
                    '}';
        }
    }

    public static class NetProfitGrowthDto{
        private Map<String, Double> growthMap;
        private double growthRate;

        private NetProfitGrowthDto(){
            growthMap = new HashMap<>();
        }

        public void addPeriodGrowth(String period, double growthRate) {
            this.growthMap.put(period,growthRate);
        }

        public Map<String, Double> getGrowthMap() {
            return growthMap;
        }

        public double getGrowthRate() {
            return growthRate;
        }

        public void setGrowthRate(double growthRate) {
            this.growthRate = growthRate;
        }

        @Override
        public String toString() {
            return "NetProfitGrowthDto{" +
                    "growthMap=" + growthMap +
                    ", growthRate=" + growthRate +
                    '}';
        }
    }
}
