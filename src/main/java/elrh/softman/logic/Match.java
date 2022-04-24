package elrh.softman.logic;

import elrh.softman.logic.stats.*;
import javafx.scene.control.TextArea;

import java.util.List;

public class Match {
    
    private final Team awayTeam;
    private final Team homeTeam;
    
    private final BoxScore boxScore = new BoxScore();
    private List<Stats> stats;

    public Match(Team awayTeam, Team homeTeam) {
        this.awayTeam = awayTeam;
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public BoxScore getBoxScore() {
        return boxScore;
    }

    public void setStats(List<Stats> stats) {
        this.stats = stats;
    }
    
    public void simulate(TextArea target) {
        MatchSimulator.simulateMatch(this, target);
        
        boxScore.printBoxScore(target);
    }
    
}
