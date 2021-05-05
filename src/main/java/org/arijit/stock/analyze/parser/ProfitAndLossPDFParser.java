package org.arijit.stock.analyze.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.dto.BalanceSheetDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.ProfitAndLossDto;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class ProfitAndLossPDFParser extends AbstractPDFProcessor {
    private static final Logger logger = LogManager.getLogger(ProfitAndLossPDFParser.class);

    private String[] dataPoints = {"Net Sales","Raw Materials","Employee Cost","Interest","Reported Net Profit"};

    public ProfitAndLossPDFParser(File file){
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
            ProfitAndLossDto profitAndLossDto = ProfitAndLossDto
                    .builder()
                    .setDate(date);
            Iterator<Map.Entry<String,String>> it1 = entry.getValue().entrySet().iterator();
            while(it1.hasNext()){
                Map.Entry<String,String> entry1 = it1.next();
                String key = entry1.getKey();
                String value = entry1.getValue();
                if(key.equals("Net Sales"))
                    profitAndLossDto.setNetSales(Double.parseDouble(value));
                if(key.equals("Raw Materials"))
                    profitAndLossDto.setConsumptionRawMaterial(Double.valueOf(value));
                if(key.equals("Employee Cost"))
                    profitAndLossDto.setEmployeeCost(Double.valueOf(value));
                if(key.equals("Interest"))
                    profitAndLossDto.setInterest(Double.valueOf(value));
                if(key.equals("Reported Net Profit"))
                    profitAndLossDto.setNetProfit(Double.parseDouble(value));
            }
            logger.info("profitAndLossDto: "+profitAndLossDto);
            fundamentalInfoDto.addProfitAndLossDto(profitAndLossDto.build());
            fundamentalInfoDto.build();
        }


    }

}
