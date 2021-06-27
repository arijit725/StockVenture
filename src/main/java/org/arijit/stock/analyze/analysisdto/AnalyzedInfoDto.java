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
    private final CashFlowAnalysisInfo cashFlowAnalysisInfo;
    private final MisleneousAnalysisInfo misleneousAnalysisInfo;
    private final EconomicGrowthDCFDto economicGrowthDCFDto;
    private final EVEBITDAValuationModelDto evebitdaValuationModelDto;
    private final PEValuationModelDto peValuationModelDto;
    private final EPSMutlipliedValuationModelDto epsMutlipliedValuationModelDto;
    private final NetProfitValuationModelDto netProfitValuationModelDto;
    private final DCFTwoPhaseValuationModelDto dcfTwoPhaseValuationModelDto;
    private final BenjaminGrahamValuationModelDto benjaminGrahamValuationModelDto;
    private final WaacDto waacDto;
    private final AverageGrowthDto averageGrowthDto;

    private AnalyzedInfoDto(){
        balanceSheetAnalysisInfo = new BalanceSheetAnalysisInfo();
        yearlyReportAnalysisInfo = new YearlyReportAnalysisInfo();
        quarterlyReportAnalysisInfo = new QuarterlyReportAnalysisInfo();
        targetPriceEstimationDto = new TargetPriceEstimationDto();
        ratioAnalysisInfo = new RatioAnalysisInfo();
        profitAndLossAnalysisInfo = new ProfitAndLossAnalysisInfo();
        cashFlowAnalysisInfo = new CashFlowAnalysisInfo();
        misleneousAnalysisInfo = new MisleneousAnalysisInfo();
        economicGrowthDCFDto = new EconomicGrowthDCFDto();
        evebitdaValuationModelDto = new EVEBITDAValuationModelDto();
        peValuationModelDto = new PEValuationModelDto();
        epsMutlipliedValuationModelDto = new EPSMutlipliedValuationModelDto();
        netProfitValuationModelDto = new NetProfitValuationModelDto();
        dcfTwoPhaseValuationModelDto = new DCFTwoPhaseValuationModelDto();
        waacDto = new WaacDto();
        averageGrowthDto = new AverageGrowthDto();
        benjaminGrahamValuationModelDto = new BenjaminGrahamValuationModelDto();
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

    public CashFlowAnalysisInfo getCashFlowAnalysisInfo() {
        return cashFlowAnalysisInfo;
    }

    public MisleneousAnalysisInfo getMisleneousAnalysisInfo() {
        return misleneousAnalysisInfo;
    }

    public EconomicGrowthDCFDto getEconomicGrowthDCFDto() {
        return economicGrowthDCFDto;
    }

    public EVEBITDAValuationModelDto getEvebitdaValuationModelDto() {
        return evebitdaValuationModelDto;
    }

    public PEValuationModelDto getPeValuationModelDto() {
        return peValuationModelDto;
    }

    public EPSMutlipliedValuationModelDto getEpsMutlipliedValuationModelDto() {
        return epsMutlipliedValuationModelDto;
    }

    public NetProfitValuationModelDto getNetProfitValuationModelDto() {
        return netProfitValuationModelDto;
    }

    public DCFTwoPhaseValuationModelDto getDcfTwoPhaseValuationModelDto() {
        return dcfTwoPhaseValuationModelDto;
    }

    public WaacDto getWaacDto() {
        return waacDto;
    }

    public AverageGrowthDto getAverageGrowthDto() {
        return averageGrowthDto;
    }

    public BenjaminGrahamValuationModelDto getBenjaminGrahamValuationModelDto() {
        return benjaminGrahamValuationModelDto;
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
                ", cashFlowAnalysisInfo="+cashFlowAnalysisInfo+
                ", misleneousAnalysisInfo="+misleneousAnalysisInfo+
                '}';
    }
}
