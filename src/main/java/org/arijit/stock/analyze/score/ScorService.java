package org.arijit.stock.analyze.score;

public class ScorService {

    private static ScorService instance = new ScorService();
    private BalancesheetScore balancesheetScore;
    private ScorService(){
        balancesheetScore = new BalancesheetScore();
    }

    public BalancesheetScore getBalancesheetScore() {
        return balancesheetScore;
    }

    public static ScorService getInstance() {
        return instance;
    }
}
