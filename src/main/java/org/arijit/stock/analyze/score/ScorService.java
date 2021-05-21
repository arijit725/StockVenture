package org.arijit.stock.analyze.score;

public class ScorService {

    private static ScorService instance = new ScorService();
    private BalancesheetScore balancesheetScore;
    private ProfitAndLossScore profitAndLossScore;
    private ScorService(){
        balancesheetScore = new BalancesheetScore();
        profitAndLossScore = new ProfitAndLossScore();
    }

    public BalancesheetScore getBalancesheetScore() {
        return balancesheetScore;
    }

    public ProfitAndLossScore getProfitAndLossScore() {
        return profitAndLossScore;
    }

    public static ScorService getInstance() {
        return instance;
    }
}
