package org.arijit.stock.analyze.util;

import com.google.gson.Gson;
import org.arijit.stock.analyze.enums.AnalysisEnums;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;

public class StockUtil {

    public static String generateID(){
        String uuid = UUID.randomUUID().toString();
        String id = uuid+"-"+System.nanoTime();
        return id;
    }

    public static String generateJsonString(Object obj){
        String jsonString = null;
        Gson gson = new Gson();
        jsonString = gson.toJson(obj);
        return jsonString;
    }

    public static String convertDoubleToPrecision(double value, int precision){
        if(value==0)
            return Double.toString(value);
        BigDecimal tempBig = new BigDecimal(Double.toString(value));
        tempBig = tempBig.setScale(precision, BigDecimal.ROUND_HALF_EVEN);
        String strValue = tempBig.stripTrailingZeros().toPlainString();
        return strValue;

    }

    public static String createAnalysisStatement(String statementStr, AnalysisEnums analysisEnums){
        Statement statement = new Statement();
        statement.statement = statementStr;
        statement.style = analysisEnums.getValue();
        return generateJsonString(statement);
    }


    private static class Statement{
        private String statement;
        private String style;
    }
}
