package org.arijit.stock.analyze.cache;

import org.arijit.stock.analyze.dto.CompanyDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;

import java.util.HashMap;
import java.util.Map;

public class MemCache {
    private static MemCache instance = new MemCache();
    private Map<String, Map<String, FundamentalInfoDto>> stockCache;

    private MemCache(){
        this.stockCache = new HashMap<>();
    }

    public static MemCache getInstance() {
        return instance;
    }

    public FundamentalInfoDto getDetails(String userID, String stockID){
        return stockCache.get(userID).get(stockID);
    }


    public void insertCompanyDetails(String userID, String stockID, CompanyDto companyDto){
        if(!stockCache.containsKey(userID)){
            stockCache.put(userID,new HashMap<>());
        }
        if(!stockCache.get(userID).containsKey(stockID)){
            stockCache.get(userID).put(stockID,FundamentalInfoDto.builder(companyDto.getYears()));
        }
        stockCache.get(userID).get(stockID).setCompanyDto(companyDto);
    }
}
