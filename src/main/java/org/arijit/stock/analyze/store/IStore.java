package org.arijit.stock.analyze.store;

import org.arijit.stock.analyze.dto.FundamentalInfoDto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IStore {

    public void createStore(String storeEndPoint);
    public void insertStock(FundamentalInfoDto fundamentalInfoDto) throws IOException;
    public FundamentalInfoDto retriveStock(String industry, String companyName) throws FileNotFoundException;
    public List<String> listStock(String industry) throws IOException;
    public List<String> listIndustry()throws IOException;
}
