package org.arijit.stock.analyze.util;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

public class StockUtil {

    public static String generateID(){
        String uuid = UUID.randomUUID().toString();
        String id = uuid+"-"+System.nanoTime();
        return id;
    }
}
