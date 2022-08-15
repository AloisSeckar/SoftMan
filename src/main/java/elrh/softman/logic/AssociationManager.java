package elrh.softman.logic;

import elrh.softman.constants.Constants;
import elrh.softman.db.GameDBManager;
import elrh.softman.gui.frame.ActionFrame;
import elrh.softman.gui.tab.IndexTab;
import elrh.softman.logic.enums.LeagueLevelEnum;
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

    private final HashMap<Long, League> managedLeagues = new HashMap<>();
    private final HashMap<Long, Club> registeredClubs = new HashMap<>();
    private final HashMap<Long, Player> registeredPlayers = new HashMap<>();

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
        return managedLeagues.values().stream().filter(l -> l.getYear() == year).toList();
    }

    public void createNewLeague(String name, LeagueLevelEnum level, ArrayList<Team> teams) {
        League newLeague = new League(name, level, teams);
        GameDBManager.getInstance().saveLeague(newLeague);
        managedLeagues.put(newLeague.getLeagueInfo().getLeagueId(), newLeague);
    }

    public List<Club> getClubs(boolean active) {
        if (active) {
            return registeredClubs.values().stream().filter(Club::isActive).toList();
        } else {
            return registeredClubs.values().stream().toList();
        }
    }

    public Club getClubById(long clubId) {
        return registeredClubs.get(clubId);
    }

    public void registerClub(Club newClub) {
        long clubId = newClub.getId();
        registeredClubs.put(clubId, newClub);
        LOG.info("Club " + clubId + " was registered");
    }

    public List<Player> getPlayers(boolean active) {
        if (active) {
            return registeredPlayers.values().stream().filter(Player::isActive).toList();
        } else {
            return registeredPlayers.values().stream().toList();
        }
    }

    public Player getPlayerById(long playerId) {
        return registeredPlayers.get(playerId);
    }

    public void registerPlayer(Player newPlayer) {
        long PlayerId = newPlayer.getId();
        registeredPlayers.put(PlayerId, newPlayer);
        LOG.info("Player " + PlayerId + " was registered");
    }
    
    public void nextSeason() {

        // TODO expire all club/player registrations

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

    public List<Match> getTodayMatchesForPlayer() {
        var playersMatches = new ArrayList<Match>();
        for (Match match : getTodayMatches()) {
            if (match.getHomeTeam().equals(playerTeam) || match.getAwayTeam().equals(playerTeam)) {
                playersMatches.add(match);
            }
        }
        return playersMatches;
    }

    public List<Match> getTodayMatches() {
        var ret = new ArrayList<Match>();
        getLeagues(year).forEach(league -> ret.addAll(league.getTodayMatches(viewDate)));
        return ret;
    }

    public List<Match> getRoundMatches(int round) {
        var ret = new ArrayList<Match>();
        getLeagues(year).forEach(league -> ret.addAll(league.getRoundMatches(round)));
        return ret;
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

        League testLeague = new League("Test league", LeagueLevelEnum.MSEN, teams);
        GameDBManager.getInstance().saveLeague(testLeague);

        managedLeagues.put(testLeague.getLeagueInfo().getLeagueId(), testLeague);
        playerLeague = testLeague;
        playerTeam = testTeam;
        
        year = Constants.START_YEAR;
    }

}
