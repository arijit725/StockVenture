package org.arijit.stock.analyze.cache;

import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.CompanyDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;

import java.util.HashMap;
import java.util.Map;

public class MemCache {
    private static MemCache instance = new MemCache();
    private Map<String, FundamentalInfoDto> stockCache;
    private Map<String, AnalyzedInfoDto> stockAnalysisCache;

    private MemCache(){
        this.stockCache = new HashMap<>();
        this.stockAnalysisCache = new HashMap<>();
    }

    public static MemCache getInstance() {
        return instance;
    }

    public FundamentalInfoDto getDetails(String stockID){
        return stockCache.get(stockID);
    }

    public AnalyzedInfoDto getAnalyzedDetails(String stockID){
        return stockAnalysisCache.get(stockID);
    }

    public void insertDummyFundamentalInfo(FundamentalInfoDto fundamentalInfoDto){
            stockCache.put("dummy",fundamentalInfoDto);
            stockAnalysisCache.put("dummy",AnalyzedInfoDto.create());
    }

    public void insertFundamentalInfo(String stockID, FundamentalInfoDto fundamentalInfoDto){
        stockCache.put(stockID,fundamentalInfoDto);
        stockAnalysisCache.put(stockID,AnalyzedInfoDto.create());
    }


    public void insertCompanyDetails(String stockID, CompanyDto companyDto){
        if(!stockCache.containsKey(stockID)){
            stockCache.put(stockID,FundamentalInfoDto.builder(companyDto.getYears()));
            stockAnalysisCache.put(stockID,AnalyzedInfoDto.create());
        }

        stockCache.get(stockID).setCompanyDto(companyDto);
    }
}
