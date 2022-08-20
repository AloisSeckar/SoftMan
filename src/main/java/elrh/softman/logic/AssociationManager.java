package elrh.softman.logic;

import elrh.softman.gui.frame.ActionFrame;
import elrh.softman.gui.tab.IndexTab;
import elrh.softman.logic.core.*;
import elrh.softman.logic.db.orm.LeagueInfo;
import elrh.softman.logic.enums.LeagueLevel;
import elrh.softman.logic.managers.ClockManager;
import elrh.softman.logic.managers.UserManager;
import elrh.softman.utils.mock.MockTeamFactory;
import elrh.softman.utils.FormatUtils;
import java.util.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AssociationManager {

    @Getter
    private final ClockManager clock = new ClockManager();
    @Getter
    private final UserManager user = new UserManager();

    private final HashMap<Long, League> managedLeagues = new HashMap<>();
    private final HashMap<Long, Club> registeredClubs = new HashMap<>();
    private final HashMap<Long, Player> registeredPlayers = new HashMap<>();

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
        return managedLeagues.values().stream().filter(l -> l.getLeagueInfo().getYear() == year).toList();
    }

    public void createNewLeague(String name, LeagueLevel level) {
        LeagueInfo leagueInfo = new LeagueInfo(name, level, clock.getYear(), level.getMatchId());
        League newLeague = new League(leagueInfo);
        newLeague.persist();
        managedLeagues.put(newLeague.getLeagueInfo().getLeagueId(), newLeague);
    }

    public void registerTeamIntoLeague(long leagueId, Team team) {
        League league = managedLeagues.get(leagueId);
        if (league != null) {
            league.registerTeam(team);
        } else {
            LOG.error("League " + leagueId + " not found");
        }
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

    public void registerClub(Club club) {
        long clubId = club.getId();
        int year = clock.getYear();
        Club existingClub = registeredClubs.get(clubId);
        if (existingClub != null) {
            existingClub.register(year);
        } else {
            club.register(year);
            registeredClubs.put(clubId, club);
        }
        LOG.info("Club " + clubId + " was registered for " + year + " season");
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

    public void registerPlayer(Player player) {
        long playerId = player.getId();
        int year = clock.getYear();
        Player existingPlayer = registeredPlayers.get(playerId);
        if (existingPlayer != null) {
            existingPlayer.register(year);
        } else {
            player.register(year);
            registeredPlayers.put(playerId, player);
        }
        LOG.info("player " + playerId + " was registered for " + year + " season");
    }

    public HashMap<Long, Match> getTodayMatchesForUser() {
        var playersMatches = new HashMap<Long, Match>();
        for (var entry : getTodayMatches().entrySet()) {
            Long leagueId = entry.getKey();
            Match match = entry.getValue();
            if (user.managesTeam(match.getHomeTeam()) || user.managesTeam(match.getAwayTeam())) {
                playersMatches.put(leagueId, match);
            }
        }
        return playersMatches;
    }

    public  HashMap<Long, Match> getTodayMatches() {
        var ret = new HashMap<Long, Match>();
        getLeagues(clock.getYear()).forEach(league -> {
            Long leagueId = league.getLeagueInfo().getLeagueId();
            league.getTodayMatches(clock.getViewDate()).forEach(match -> ret.put(leagueId, match));
        });
        return ret;
    }

    public List<Match> getRoundMatches(int leagueId, int round) {
        var ret = new ArrayList<Match>();
        var league = managedLeagues.get(leagueId);
        if (league != null) {
            ret.addAll(league.getRoundMatches(round));
        }
        return ret;
    }

    public void nextDay() {
        if (isDayFinished() || confirmDayFinished()) {
            getTodayMatches().values().forEach(match -> {
                if (!match.isFinished()) {
                    match.simulate(new TextArea());
                }
            });

            IndexTab.getInstance().refreshSchedule();
            clock.plusDays(1);
            clock.adjustViewDay();
            LOG.info("NEW DAY. Today is " + clock.getCurrentDate().format(FormatUtils.DF));

            ActionFrame.getInstance().updateDateValue(clock.getCurrentDate());
            IndexTab.getInstance().setDailySchedule();
        }
    }

    public boolean isTodayMatch(Match match) {
        var matchDate = match.getMatchInfo().getMatchDay();
        return matchDate.compareTo(clock.getCurrentDate()) == 0;
    }

    ////////////////////////////////////////////////////////////////////////////

    private boolean isDayFinished() {
        return getTodayMatches().values().stream().allMatch(Match::isFinished);
    }

    private boolean confirmDayFinished() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Simulate the rest of the day?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

    private void prepareLeagues() {
        Team testTeam = MockTeamFactory.getMockTeam("REDS");
        user.setActiveTeam(testTeam);

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

        LeagueInfo leagueInfo = new LeagueInfo("Test league", LeagueLevel.MSEN, clock.getYear(), LeagueLevel.MSEN.getMatchId());
        League testLeague = new League(leagueInfo);
        testLeague.persist();

        teams.forEach(testLeague::registerTeam);
        testLeague.scheduleMatches();

        managedLeagues.put(testLeague.getLeagueInfo().getLeagueId(), testLeague);

        Club testPlayerClub = new Club("REDS", "Unknown", "The Field");
        testPlayerClub.persist();
        registerClub(testPlayerClub);
        // TODO associate club with User
    }

}
