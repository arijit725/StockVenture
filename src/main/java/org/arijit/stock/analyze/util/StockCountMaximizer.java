package org.arijit.stock.analyze.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * This is an algorithm which will maximize the number of stocks based on realistic stock price list as input.
 * To use this algorithm, one need to choose a number of probable prices of stock and total amount
 * This will return how many stock of which price could be fit in that range.
 *
 * To make this algorithm work investment amount must be integer
 */
public class StockCountMaximizer {

    private static final Logger logger = LogManager.getLogger(StockCountMaximizer.class);
    private final List<Double> priceDistribution;

    private final int totalInvestmentAmount;

    private StockCountMaximizer(List<Double> priceDistribution, int totalInvestmentAmount){
        this.priceDistribution = priceDistribution;
        this.totalInvestmentAmount = totalInvestmentAmount;
    }


    public void calculateStockCount(){
        /*
        1. create 3 split of stock
        2. split ratio 25:50:25
         */
        //split ratio

        Collections.sort(priceDistribution, new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println(priceDistribution);
        double highPriceSplit = .2;
        double middlePriceSplit = .5;
        double lowPriceSplit = .3;

        double highPrice = priceDistribution.get(0);
        double middlePrice = priceDistribution.get(1);
        double lowPrice = priceDistribution.get(2);
        //keep highprice 0.25% less than price as stock
         highPrice = highPrice * (1-0.0025);

         double highInvestmentAmount = totalInvestmentAmount * highPriceSplit;
         double middleInvestmentAmount = totalInvestmentAmount * middlePriceSplit;
         double lowInvestmentAmount = totalInvestmentAmount * lowPriceSplit;

         int actualStockCount = (int) (totalInvestmentAmount / priceDistribution.get(0));
         logger.info(" Only with Original Price Calculated Stock Count:  "+ actualStockCount);
         logger.info(" High Price Investment: "+highInvestmentAmount+" Middle Price Investment: "+middleInvestmentAmount+" Low Price Investment: "+lowInvestmentAmount);
         int highStockCount = (int) (highInvestmentAmount/highPrice);

        int middleStockCount = (int) (middleInvestmentAmount/middlePrice);

        int lowStockCount = (int) (lowInvestmentAmount/middlePrice);

        logger.info("High Stock Count: "+highStockCount+" Middle Stock Count: "+middleStockCount+" Low Stock Count: "+lowStockCount +"  Total Stock: "+(highStockCount+lowStockCount+middleStockCount));

        double finalInvestmentAmount = highPrice * highStockCount + middlePrice*middleStockCount + lowPrice*lowStockCount;

        logger.info(" Final Investment Amount : "+finalInvestmentAmount);
    }
    public void calculateStockCount1(){
        int arr[][] = new int[priceDistribution.size()+1][totalInvestmentAmount+1];
        for(int i=0;i<arr.length;i++){

                arr[i][0]=0;
        }
        for(int j=0;j<arr[0].length;j++){
            //setting 0th index as 0
            arr[0][j] = 0;
        }

        for(int i=1;i<arr.length;i++){
            for(int j=1;j<arr[i].length;j++){
                double price = priceDistribution.get(i-1); //as index start from 0 in priceDistribution list
                if(j<price){
                    //no way we can make any stock of price j.
                    arr[i][j] = arr[i-1][j];
                    continue;
                }
                int div = (int) (j/price);
                if(div==0){
                    //either we can pick div number of stock with price, or we can take previous one, which ever maximum
                    arr[i][j]=Math.max(div, arr[i-1][j]);
                }else{
                    int k=1;
                    int tmp = (int) (k*price);
                    while(tmp<=j){ //sill we can accomodate some amount of stock in this j cost
                        int restamount = (int) (j- tmp);
                        int stockCount = k+arr[i-1][restamount];
                        arr[i][j] = Math.max(stockCount,arr[i-1][j]);
                        k++;
                        tmp = (int) (k*price);

                    }
                }

            }
        }
        System.out.println("Stock Array: ");
        for(int i=0;i<arr.length;i++) {
            System.out.println(Arrays.toString(arr[i]));
        }
        calcualteCount(arr);
    }

    private void calcualteCount(int[][] arr){
        HashMap<Double,Integer> countMap = new HashMap<>();
        int i = arr.length-1;
        int j = arr[0].length-1;
        while(i>=0 && j>=0 && arr[i][j]!=0){
            while(arr[i][j]==arr[i-1][j]){
                i--;
            }
            while(arr[i][j]==arr[i][j-1]){
                j--;
            }
            double price = priceDistribution.get(i-1); //as priceDistribution index starts from 0
            if(!countMap.containsKey(price)){
                countMap.put(price,1);
            }
            else{
                int count = countMap.get(price);
                count++;
                countMap.put(price,count);
            }
            j = j-i; //moving to next index.
        }

        System.out.println("countMap: " +countMap);
    }

    public static StockCountMaximizer create(List<Double> priceDistribution, int totalInvestmentAmount){
        return new StockCountMaximizer(priceDistribution,totalInvestmentAmount);
    }
}
