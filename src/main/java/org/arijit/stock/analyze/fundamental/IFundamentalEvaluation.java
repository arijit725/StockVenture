package org.arijit.stock.analyze.fundamental;

import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;

public interface IFundamentalEvaluation{

    public boolean isEvaluated();

    public void evaluate(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto, int year) throws Exception;
}
