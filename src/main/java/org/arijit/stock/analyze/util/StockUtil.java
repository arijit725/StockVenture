package org.arijit.stock.analyze.util;

import com.google.gson.Gson;

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
}
