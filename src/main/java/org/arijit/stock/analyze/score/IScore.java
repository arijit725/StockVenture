package org.arijit.stock.analyze.score;

import org.arijit.stock.analyze.analysisdto.AnalyzedInfoDto;
import org.arijit.stock.analyze.dto.FundamentalInfoDto;

public interface IScore {

    public void score(FundamentalInfoDto fundamentalInfoDto, AnalyzedInfoDto analyzedInfoDto,int years);
}
