package elrh.softman.logic;

import elrh.softman.logic.stats.*;
import javafx.scene.control.TextArea;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class Match {
    @Getter
    private final LocalDate matchDay;

    @Getter
    private final Team awayTeam;
    @Getter
    private final Team homeTeam;

    @Getter
    private final BoxScore boxScore = new BoxScore();
    private List<Stats> stats;

    public Match(LocalDate matchDay, Team awayTeam, Team homeTeam) {
        this.matchDay = matchDay;
        this.awayTeam = awayTeam;
        this.homeTeam = homeTeam;
    }

    public void setStats(List<Stats> stats) {
        this.stats = stats;
    }
    
    public void simulate(TextArea target) {
        MatchSimulator sim = new MatchSimulator(this, target);
        sim.simulateMatch();
        
        boxScore.printBoxScore(target);
    }
    
}
