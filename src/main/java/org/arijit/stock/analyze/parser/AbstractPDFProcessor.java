package org.arijit.stock.analyze.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.util.DateUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AbstractPDFProcessor implements IMoneyControlPDFParser {
    private static final Logger logger = LogManager.getLogger(AbstractPDFProcessor.class);

    private File pdfFile;
    private List<String> dataPoints;

    private List<String> dates;

//    private  parsedContent;

    public AbstractPDFProcessor(File file){
        this.pdfFile = file;

    }
    protected void setDataPoints(String[] dataPoints){
        this.dataPoints = Arrays.asList(dataPoints);
    }

    protected Map<String,Map<String, String>> getContents(){
        try {
            String text = PDFParserUtil.parsePDF(pdfFile);
            List<String> lines = PDFParserUtil.processPDFContents(text);
            if(lines==null || lines.isEmpty()){
                throw new Exception(pdfFile.getAbsolutePath()+" content is empty");
            }

            parseDates(lines);
            logger.info("Dates: "+dates);
            List<String> filteredContent = filterByDataPoints(lines);
            return generateFilteredDatapointMap(filteredContent);
        } catch (IOException e) {
            logger.error("Unable to parse PDF file :"+pdfFile.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Unable to parse PDF file :"+pdfFile.getAbsolutePath());
        }
        return Collections.emptyMap();
    }

    private Map<String,Map<String, String>> generateFilteredDatapointMap(List<String> filteredContent){
        Map<String,Map<String, String>> parsedContent = new HashMap<>();
        if(dates==null || dates.isEmpty()|| dataPoints==null || dataPoints.isEmpty())
            return Collections.emptyMap();
//        for(String date:dates){
//            parsedContent.put(date, new HashMap<>());
//        }
        List<List<String>> datapointvsContentList = new ArrayList<>();
        Iterator<String> itf = filteredContent.iterator();
        while(itf.hasNext()){
            String content = itf.next();
            List<String> contentList = new ArrayList<>();
            for(String datapoint:dataPoints){
                if(content.startsWith(datapoint)){
                    contentList.add(datapoint);
                    content = content.replace(datapoint,"").trim();
                    String[] tmp = content.split(" ");
                    contentList.addAll(Arrays.asList(tmp).stream().map(t->t.replaceAll(",","")).collect(Collectors.toList()));
                    logger.info("Parsed list: "+contentList);
                    datapointvsContentList.add(contentList);
                }
            }
        }

        for(int i=0;i<dates.size();i++){
            HashMap<String, String> contentMap = new HashMap<>();
            parsedContent.put(dates.get(i), contentMap);
            for(List<String> l:datapointvsContentList){
                contentMap.put(l.get(0),l.get(i+1));
            }
        }

        return parsedContent;
    }

    private List<String> filterByDataPoints(List<String> lines) throws Exception {
        if(lines==null|| lines.isEmpty())
            throw new Exception(pdfFile.getAbsolutePath()+" content is empty");
       List<String> filteredLines =  lines.stream().filter(line->{
            for(String datpoint:dataPoints){
                if(line.startsWith(datpoint))
                    return true;
            }
            return false;
        }).collect(Collectors.toList());
        return filteredLines;
    }

    private void parseDates(List<String> lines){
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
