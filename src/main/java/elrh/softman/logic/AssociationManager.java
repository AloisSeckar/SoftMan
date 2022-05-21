package elrh.softman.logic;

import elrh.softman.db.GameDBManager;
import elrh.softman.gui.frame.ActionFrame;
import elrh.softman.mock.MockTeamFactory;
import java.time.LocalDate;
import java.util.*;

import elrh.softman.utils.FormatUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AssociationManager {

    private final List<League> managedLeagues = new ArrayList<>();

    @Getter
    @Setter
    private League playerLeague;

    @Getter
    @Setter
    private Team playerTeam;

    @Getter
    private LocalDate currentDate = LocalDate.of(2023, 03, 31);
    @Getter
    private int season;

    private static AssociationManager INSTANCE;

    private AssociationManager() {
        prepareLeagues();
    }

    public static AssociationManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AssociationManager();
        }
        return INSTANCE;
    }
    
    public void nextSeason() {
        season++;
    }

    public void nextDay() {
        currentDate = currentDate.plusDays(1);
        LOG.info("NEW DAY. Today is " + currentDate.format(FormatUtils.DF));
        ActionFrame.getInstance().updateDateValue(currentDate);
    }

    public Match getTodayMatchForPlayer() {
        Match playersMatch = null;
        for (Match match : getTodayMatches()) {
            if (match.getHomeTeam().equals(playerTeam) || match.getAwayTeam().equals(playerTeam)) {
                playersMatch = match;
                break;
            }
        }
        return playersMatch;
    }

    public List<Match> getTodayMatches() {
        return playerLeague.getTodayMatches(currentDate);
    }

    public List<Match> getRoundMatches(int round) {
        return playerLeague.getRoundMatches(round);
    }

    ////////////////////////////////////////////////////////////////////////////
    private void prepareLeagues() {
        Team testTeam = MockTeamFactory.getMockTeam("REDS");

        ArrayList<Team> teams = new ArrayList<>();
        teams.add(testTeam);
        teams.add(MockTeamFactory.getMockTeam("BLUES"));
        teams.add(MockTeamFactory.getMockTeam("GREENS"));
        teams.add(MockTeamFactory.getMockTeam("YELLOWS"));
        teams.add(MockTeamFactory.getMockTeam("BLACKS"));
        teams.add(MockTeamFactory.getMockTeam("WHITES"));
        teams.add(MockTeamFactory.getMockTeam("SILVERS"));
        teams.add(MockTeamFactory.getMockTeam("VIOLETS"));
        teams.add(MockTeamFactory.getMockTeam("BROWNS"));
        teams.add(MockTeamFactory.getMockTeam("GOLDS"));

        League testLeague = new League("Test league", teams);
        GameDBManager.getInstance().saveLeague(testLeague);

        managedLeagues.add(testLeague);
        playerLeague = testLeague;
        playerTeam = testTeam;
        
        season = 2021;
    }

}
