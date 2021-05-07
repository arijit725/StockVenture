package org.arijit.stock.analyze.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.dto.BalanceSheetDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;

import java.io.File;
import java.util.*;

public class BalanceSheetPDFParser extends AbstractPDFProcessor {
    private static final Logger logger = LogManager.getLogger(BalanceSheetPDFParser.class);

    private String[] dataPoints = {"Total Share Capital","Equity Share Capital","Reserves and Surplus","Long Term Borrowings","Short Term Borrowings"};

    public BalanceSheetPDFParser(File file){
        super(file);
        super.setDataPoints(dataPoints);
    }

    public void generateDto(FundamentalInfoDto fundamentalInfoDto) throws Exception {
        Map<String, Map<String, String>> filteredContents = getContents();
        logger.info("filteredContents: "+filteredContents);
        if(filteredContents.isEmpty())
            return;
        Iterator<Map.Entry<String, Map<String,String>>> it = filteredContents.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Map<String,String>> entry = it.next();
            String date = entry.getKey();
            BalanceSheetDto balanceSheetDto = BalanceSheetDto
                    .builder()
                    .setDate(date);
            Iterator<Map.Entry<String,String>> it1 = entry.getValue().entrySet().iterator();
            while(it1.hasNext()){
                Map.Entry<String,String> entry1 = it1.next();
                String key = entry1.getKey();
                String value = entry1.getValue();
                if(key.equals("Total Share Capital"))
                    balanceSheetDto.setTotalShareCapital(Double.parseDouble(value));
                if(key.equals("Equity Share Capital"))
                    balanceSheetDto.setEquityShareCapital(Double.parseDouble(value));
                if(key.equals("Reserves and Surplus"))
                    balanceSheetDto.setReserves(Double.parseDouble(value));
                if(key.equals("Long Term Borrowings")) {
                    double val = balanceSheetDto.getDebt();
                    val = val + Double.parseDouble(value);
                    balanceSheetDto.setDebt(val);
                }
                if(key.equals("Short Term Borrowings")){
                    double val = balanceSheetDto.getDebt();
                    val = val + Double.parseDouble(value);
                    balanceSheetDto.setDebt(val);
                }

            }
            logger.info("BalancesheetDto: "+balanceSheetDto);
            fundamentalInfoDto.addBalanceSheetDto(balanceSheetDto.build());
            fundamentalInfoDto.build();
        }


    }

}
