package elrh.softman.logic;

import elrh.softman.constants.Constants;
import elrh.softman.db.GameDBManager;
import elrh.softman.gui.frame.ActionFrame;
import elrh.softman.gui.tab.IndexTab;
import elrh.softman.mock.MockTeamFactory;
import elrh.softman.utils.FormatUtils;
import java.time.LocalDate;
import java.util.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AssociationManager {

    @Getter
    private int year;
    @Getter
    private LocalDate currentDate = LocalDate.of(Constants.START_YEAR, 3, 31);
    @Getter
    private LocalDate viewDate = LocalDate.of(Constants.START_YEAR, 3, 31);

    private final List<League> activeLeagues = new ArrayList<>();
    private final List<League> archivedLeagues = new ArrayList<>();

    @Getter
    @Setter
    private League playerLeague;

    @Getter
    @Setter
    private Team playerTeam;

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

    public List<League> getLeagues(int year) {
        if (this.year == this.year) {
            return activeLeagues;
        } else {
            return archivedLeagues.stream().filter(l -> l.getYear() == year).toList();
        }
    }

    public void createNewLeague() {
        // TODO waiting for "league levels"
        activeLeagues.add(new League("Empty league", new ArrayList()));
    }
    
    public void nextSeason() {

        archivedLeagues.addAll(activeLeagues);
        activeLeagues.clear();

        year++;
    }

    public void prevViewDay() {
        viewDate = viewDate.minusDays(1);
    }

    public void nextViewDay() {
        viewDate = viewDate.plusDays(1);
    }

    public void adjustViewDay() {
        viewDate = currentDate;
    }

    public void nextDay() {
        if (isDayFinished() || confirmDayFinished()) {
            getTodayMatches().forEach(match -> {
                if (!match.isFinished()) {
                    match.simulate(new TextArea());
                }
            });

            IndexTab.getInstance().refreshSchedule();
            currentDate = currentDate.plusDays(1);
            adjustViewDay();
            LOG.info("NEW DAY. Today is " + currentDate.format(FormatUtils.DF));

            ActionFrame.getInstance().updateDateValue(currentDate);
            IndexTab.getInstance().setDailySchedule();
        }
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
        return playerLeague.getTodayMatches(viewDate);
    }

    public List<Match> getRoundMatches(int round) {
        return playerLeague.getRoundMatches(round);
    }

    public boolean isDayFinished() {
        return getTodayMatches().stream().allMatch(Match::isFinished);
    }

    public boolean isTodayMatch(Match match) {
        var matchDate = match.getMatchInfo().getMatchDay();
        return matchDate.compareTo(getCurrentDate()) == 0;
    }

    ////////////////////////////////////////////////////////////////////////////

    private boolean confirmDayFinished() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Simulate the rest of the day?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

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

        activeLeagues.add(testLeague);
        playerLeague = testLeague;
        playerTeam = testTeam;
        
        year = Constants.START_YEAR;
    }

}
