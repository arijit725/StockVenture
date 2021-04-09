package org.arijit.stock.analyze.analysisdto;

/**
 * This class will contain outcomes after analze
 */
public class AnalyzedInfoDto {

    private final BalanceSheetAnalysisInfo balanceSheetAnalysisInfo;
    private final YearlyReportAnalysisInfo yearlyReportAnalysisInfo;
    private final EVEBITDAValuationModelDto evebitdaValuationModelDto;
    private final RatioAnalysisInfo ratioAnalysisInfo;

    private AnalyzedInfoDto(){
        balanceSheetAnalysisInfo = new BalanceSheetAnalysisInfo();
        yearlyReportAnalysisInfo = new YearlyReportAnalysisInfo();
        evebitdaValuationModelDto = new EVEBITDAValuationModelDto();
        ratioAnalysisInfo = new RatioAnalysisInfo();
    }

    public static AnalyzedInfoDto create(){
        AnalyzedInfoDto analyzedInfoDto = new AnalyzedInfoDto();
        return analyzedInfoDto;
    }
    public BalanceSheetAnalysisInfo getBalanceSheetAnalysisInfo() {
        return balanceSheetAnalysisInfo;
    }

    public EVEBITDAValuationModelDto getEvebitdaValuationModelDto() {
        return evebitdaValuationModelDto;
    }

    public YearlyReportAnalysisInfo getYearlyReportAnalysisInfo() {
        return yearlyReportAnalysisInfo;
    }

    public RatioAnalysisInfo getRatioAnalysisInfo() {
        return ratioAnalysisInfo;
    }

    @Override
    public String toString() {
        return "AnalyzedInfoDto{" +
                "balanceSheetAnalysisInfo=" + balanceSheetAnalysisInfo +
                ", yearlyReportAnalysisInfo=" + yearlyReportAnalysisInfo +
                ", evebitdaValuationModelDto=" + evebitdaValuationModelDto +
                ", ratioAnalysisInfo=" + ratioAnalysisInfo +
                '}';
    }
}
