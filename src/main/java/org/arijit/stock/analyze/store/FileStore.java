package org.arijit.stock.analyze.store;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;
import org.arijit.stock.analyze.util.StockUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FileStore implements IStore {
    private static final Logger logger = LogManager.getLogger(FileStore.class);
    private String storePath;
    @Override
    public void createStore(String storeEndPoint) {
        this.storePath = storeEndPoint;
        File file = new File(storeEndPoint);
        if(file.exists() && file.isDirectory()){
            logger.info("FileStore at : "+storeEndPoint+" already present");
            return;
        }
        file.mkdirs();
        logger.info("FileStore at : "+storeEndPoint+" is created");
    }

    @Override
    public void insertStock(FundamentalInfoDto fundamentalInfoDto) throws IOException {
        String jsonString = StockUtil.generateJsonString(fundamentalInfoDto);
        String industry = fundamentalInfoDto.getCompanyDto().getIndustry().trim().replaceAll(" ","_");
        industry = industry.replaceAll("&","");
        String companyName = fundamentalInfoDto.getCompanyDto().getCompanyName().replaceAll(" ","_")+".json";
        companyName = companyName.replaceAll("&","");
        String dir = storePath+File.separator+industry;
        File file = new File(dir);
        if(!file.exists())
            file.mkdirs();
        Path path = Paths.get(dir+File.separator+companyName);

        try {
            Files.write(path, jsonString.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        }catch (NoSuchFileException e){
            logger.info("Creating "+path.toAbsolutePath().toString());
            Files.write(path, jsonString.getBytes(), StandardOpenOption.CREATE);
        }
        logger.info("Company Fundamentalinfo written at Path: "+path.toAbsolutePath().toString());
    }

    @Override
    public FundamentalInfoDto retriveStock(String industry, String companyName) throws FileNotFoundException {
        String fileName = storePath+File.separator+industry+File.separator+companyName+".json";
        logger.info("Reading fundamental info from: "+fileName);
        Gson gson = new Gson();
        FileReader reader = new FileReader(fileName);
        FundamentalInfoDto fundamentalInfoDto = gson.fromJson(reader,FundamentalInfoDto.class);
        try {
            reader.close();
        } catch (IOException e) {
            logger.warn("Unable to close reader for file: "+fileName);
        }
        return fundamentalInfoDto;
    }


    @Override
    public List<String> listStock(String industry) throws IOException {
        Map<String,String> stockFileMap = new HashMap<>();
        String industryPath = storePath+File.separator+industry;
        Path path = Paths.get(industryPath);
        List<String> stockList = Files.list(path).map(path1->{String p = path1.toString().replaceAll(industryPath,"").replaceAll(File.separator,"");
                                        return p.substring(0, p.indexOf("."));}).collect(Collectors.toList());
        return stockList;
    }

    @Override
    public List<String> listIndustry() throws IOException {
        Path path = Paths.get(storePath);
        List<String> industyList = Files.list(path).map(path1->{return path1.toString().replace(storePath,"").replaceAll(File.separator,"");}).collect(Collectors.toList());
        return industyList;
    }


}
