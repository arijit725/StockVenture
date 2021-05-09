package org.arijit.stock.analyze.analysisdto;

/**
 * This class will contain outcomes after analze
 */
public class AnalyzedInfoDto {

    private final BalanceSheetAnalysisInfo balanceSheetAnalysisInfo;
    private final YearlyReportAnalysisInfo yearlyReportAnalysisInfo;
    private final QuarterlyReportAnalysisInfo quarterlyReportAnalysisInfo;
    private final TargetPriceEstimationDto targetPriceEstimationDto;
    private final RatioAnalysisInfo ratioAnalysisInfo;
    private final ProfitAndLossAnalysisInfo profitAndLossAnalysisInfo;
    private final MisleneousAnalysisInfo misleneousAnalysisInfo;

    private AnalyzedInfoDto(){
        balanceSheetAnalysisInfo = new BalanceSheetAnalysisInfo();
        yearlyReportAnalysisInfo = new YearlyReportAnalysisInfo();
        quarterlyReportAnalysisInfo = new QuarterlyReportAnalysisInfo();
        targetPriceEstimationDto = new TargetPriceEstimationDto();
        ratioAnalysisInfo = new RatioAnalysisInfo();
        profitAndLossAnalysisInfo = new ProfitAndLossAnalysisInfo();
        misleneousAnalysisInfo = new MisleneousAnalysisInfo();
    }

    public static AnalyzedInfoDto create(){
        AnalyzedInfoDto analyzedInfoDto = new AnalyzedInfoDto();
        return analyzedInfoDto;
    }
    public BalanceSheetAnalysisInfo getBalanceSheetAnalysisInfo() {
        return balanceSheetAnalysisInfo;
    }

    public TargetPriceEstimationDto getTargetPriceEstimationDto() {
        return targetPriceEstimationDto;
    }

    public YearlyReportAnalysisInfo getYearlyReportAnalysisInfo() {
        return yearlyReportAnalysisInfo;
    }

    public ProfitAndLossAnalysisInfo getProfitAndLossAnalysisInfo() {
        return profitAndLossAnalysisInfo;
    }

    public RatioAnalysisInfo getRatioAnalysisInfo() {
        return ratioAnalysisInfo;
    }

    public QuarterlyReportAnalysisInfo getQuarterlyReportAnalysisInfo() {
        return quarterlyReportAnalysisInfo;
    }

    public MisleneousAnalysisInfo getMisleneousAnalysisInfo() {
        return misleneousAnalysisInfo;
    }


    @Override
    public String toString() {
        return "AnalyzedInfoDto{" +
                "balanceSheetAnalysisInfo=" + balanceSheetAnalysisInfo +
                ", yearlyReportAnalysisInfo=" + yearlyReportAnalysisInfo +
                ", quarterlyReportAnalysisInfo=" + quarterlyReportAnalysisInfo +
                ", targetPriceEstimationDto=" + targetPriceEstimationDto +
                ", ratioAnalysisInfo=" + ratioAnalysisInfo +
                ", profitAndLossAnalysisInfo="+profitAndLossAnalysisInfo+
                ", misleneousAnalysisInfo="+misleneousAnalysisInfo+
                '}';
    }
}
