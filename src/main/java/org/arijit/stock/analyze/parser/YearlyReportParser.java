package org.arijit.stock.analyze.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.dto.ProfitAndLossDto;
import org.arijit.stock.analyze.dto.YearlyReportDto;
import org.arijit.stock.analyze.util.DateUtil;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class YearlyReportParser extends AbstractPDFProcessor {
    private static final Logger logger = LogManager.getLogger(YearlyReportParser.class);

    private String[] dataPoints = {"P/L Before Other Inc., Int., Excpt. Items & Tax","Basic EPS"};

    public YearlyReportParser(File file){
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
            YearlyReportDto yearlyReportDto = YearlyReportDto
                    .builder()
                    .setDate(date);
            ProfitAndLossDto profitAndLossDto = getProfitAndLossDto(fundamentalInfoDto,date);
            Iterator<Map.Entry<String,String>> it1 = entry.getValue().entrySet().iterator();
            while(it1.hasNext()){
                Map.Entry<String,String> entry1 = it1.next();
                String key = entry1.getKey();
                String value = entry1.getValue();
                if(key.equals("P/L Before Other Inc., Int., Excpt. Items & Tax")) {
                    yearlyReportDto.setPbit(Double.parseDouble(value));
                    if(profitAndLossDto!=null)
                        profitAndLossDto.setPbit(Double.parseDouble(value));
                }
                if(key.equals("Basic EPS"))
                    yearlyReportDto.setBasicEPS(Double.parseDouble(value));
            }
            logger.info("profitAndLossDto: "+yearlyReportDto);
            fundamentalInfoDto.addYearlyReportDto(yearlyReportDto.build());
            fundamentalInfoDto.build();
        }
    }

    private ProfitAndLossDto getProfitAndLossDto(FundamentalInfoDto fundamentalInfoDto, String date){
        Iterator<ProfitAndLossDto> it = fundamentalInfoDto.getProfitAndLossDtoList().iterator();

        while(it.hasNext()){
            ProfitAndLossDto profitAndLossDto = it.next();
            if(profitAndLossDto.getDate().equals(date))
                return profitAndLossDto;
        }
        return null;
    }
    protected List<String> filterByDataPoints(List<String> lines) throws Exception {
        if(lines==null|| lines.isEmpty())
            throw new Exception(pdfFile.getAbsolutePath()+" content is empty");
        String prevLine = null;
        List<String> filteredLines = new ArrayList<>();
        ListIterator<String> it = lines.listIterator();
        while(it.hasNext()){
            String line1 = it.next();
            if(line1.startsWith("Tax")) {
                if (prevLine.equals("P/L Before Other Inc., Int., Excpt. Items &")) {
                    String tmpLine = "P/L Before Other Inc., Int., Excpt. Items & "+line1;
                    filteredLines.add(tmpLine);
                    logger.info("Added: "+line1);
                }
            }
            else if(line1.startsWith("Basic EPS")){
                if(prevLine.equals("EPS Before Extra Ordinary")){
                    filteredLines.add(line1);
                }
            }
            prevLine = line1;
        }
        return filteredLines;
    }

    protected void parseDates(List<String> lines){
        String patStr = "^((Mar(\\s)+'[0-9]{2})(\\s)*)*$";
        String patgroup="(Mar\\s+'[0-9]{2})";
        Pattern pattern = Pattern.compile(patStr);
        for(String line:lines){
            line = line.trim();
            Matcher matcher = pattern.matcher(line);
            if(matcher.matches() && dates==null){
                dates = new ArrayList<>();
                Pattern pattern1 = Pattern.compile(patgroup);
                Matcher matcher1 = pattern1.matcher(line);
                while(matcher1.find()){
                    String date = matcher1.group();
                    dates.add(DateUtil.dateFomratConverter(date));
                }

            }
        }
    }

}
