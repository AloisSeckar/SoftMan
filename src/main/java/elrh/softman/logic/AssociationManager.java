package elrh.softman.logic;

import elrh.softman.gui.frame.ActionFrame;
import elrh.softman.gui.tab.ClubTab;
import elrh.softman.logic.core.*;
import elrh.softman.logic.db.orm.LeagueInfo;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.logic.managers.ClockManager;
import elrh.softman.logic.managers.StateManager;
import elrh.softman.utils.Constants;
import elrh.softman.utils.ErrorUtils;
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
    private final StateManager state = new StateManager();

    private final HashMap<Long, League> managedLeagues = new HashMap<>();
    private final HashMap<Long, Club> registeredClubs = new HashMap<>();
    private final HashMap<Long, Player> registeredPlayers = new HashMap<>();

    private static AssociationManager INSTANCE;

    private AssociationManager() {
    }

    public static AssociationManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AssociationManager();
        }
        return INSTANCE;
    }

    public void reset() {
        clock.reset();
        state.reset();
        managedLeagues.clear();
        registeredClubs.clear();
        registeredPlayers.clear();
    }

    public List<League> getLeagues(int year) {
        return managedLeagues.values().stream().filter(l -> l.getLeagueInfo().getYear() == year).toList();
    }

    public League getLeagueById(long leagueId) {
        return managedLeagues.get(leagueId);
    }

    public Result createNewLeague(String name, PlayerLevel level) {
        try {
            LeagueInfo leagueInfo = new LeagueInfo(name, level, clock.getYear(), level.getMatchId());
            League newLeague = new League(leagueInfo);
            newLeague.persist();
            managedLeagues.put(newLeague.getLeagueInfo().getLeagueId(), newLeague);
            LOG.info("New league ID " + newLeague + " created");
            return Constants.RESULT_OK;
        } catch (Exception ex) {
            return ErrorUtils.handleException("AssociationManager.createNewLeague", ex);
        }
    }

    public Result registerTeamIntoLeague(long leagueId, Team team) {
        try {
            League league = managedLeagues.get(leagueId);
            if (league != null) {
                return league.registerTeam(team);
            } else {
                return new Result(false, "League " + leagueId + " not found");
            }
        } catch (Exception ex) {
            return ErrorUtils.handleException("AssociationManager.registerTeamIntoLeague", ex);
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

    public Result registerClub(Club club) {
        try {
            long clubId = club.getId();
            int year = clock.getYear();
            Club existingClub = registeredClubs.get(clubId);
            if (existingClub != null) {
                existingClub.getClubInfo().setRegistered(year);
                existingClub.persist();
            } else {
                club.getClubInfo().setRegistered(year);
                club.persist();
                registeredClubs.put(clubId, club);
            }
            LOG.info("Club " + clubId + " was registered for " + year + " season");
            return Constants.RESULT_OK;
        } catch (Exception ex) {
            return ErrorUtils.handleException("AssociationManager.registerClub", ex);
        }
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

    public Result registerPlayer(Player player) {
        try {
            long playerId = player.getId();
            int year = clock.getYear();
            Player existingPlayer = registeredPlayers.get(playerId);
            if (existingPlayer != null) {
                existingPlayer.getPlayerInfo().setRegistered(year);
                existingPlayer.persist();
            } else {
                player.getPlayerInfo().setRegistered(year);
                player.persist();
                registeredPlayers.put(playerId, player);
            }
            LOG.info("player " + playerId + " was registered for " + year + " season");
            return Constants.RESULT_OK;
        } catch (Exception ex) {
            return ErrorUtils.handleException("AssociationManager.registerPlayer", ex);
        }
    }

    public  HashMap<Long, List<Match>> getDailyMatches() {
        var ret = new HashMap<Long, List<Match>>();
        getLeagues(clock.getYear()).forEach(league -> {
            var leagueId = league.getLeagueInfo().getLeagueId();
            var leagueMatches = new ArrayList<>(league.getMatchesForDay(clock.getViewDate()));
            ret.put(leagueId, leagueMatches);
        });
        return ret;
    }

    public List<Match> getDailyMatchesForUser() {
        var ret = new ArrayList<Match>();
        for (var entry : getDailyMatches().entrySet()) {
            var leagueId = entry.getKey();
            entry.getValue().forEach(match -> {
                if (state.userManagesTeam(match.getHomeTeam()) || state.userManagesTeam(match.getAwayTeam())) {
                    ret.add(match);
                }
            });
        }
        return ret;
    }

    public List<Match> getDailyMatchesForLeague(long leagueId) {
        var ret = new ArrayList<Match>();
        var league = managedLeagues.get(leagueId);
        if (league != null) {
            ret.addAll(league.getMatchesForDay(clock.getViewDate()));
        }
        return ret;
    }

    public List<Match> getRoundMatchesForLeague(long leagueId, int round) {
        var ret = new ArrayList<Match>();
        var league = managedLeagues.get(leagueId);
        if (league != null) {
            ret.addAll(league.getMatchesForRound(round));
        }
        return ret;
    }

    public Result nextDay() {
        try {
            if (isDayFinished() || confirmDayFinished()) {
                getDailyMatches().values().forEach(matches -> matches.forEach(match -> {
                    if (!match.isFinished()) {
                        match.simulate(new TextArea());
                    }
                }));

                ClubTab.getInstance().refreshSchedule();
                clock.plusDays(1);
                clock.adjustViewDay();
                LOG.info("NEW DAY. Today is " + clock.getCurrentDate().format(FormatUtils.DF));

                ActionFrame.getInstance().updateDateValue(clock.getCurrentDate());
                ClubTab.getInstance().setDailySchedule();
                return Constants.RESULT_OK;
            } else {
                return new Result(false, "Day not completed yet");
            }
        } catch (Exception ex) {
            return ErrorUtils.handleException("AssociationManager.registerPlayer", ex);
        }
    }

    public boolean isTodayMatch(Match match) {
        var matchDate = match.getMatchInfo().getMatchDay();
        return matchDate.compareTo(clock.getCurrentDate()) == 0;
    }

    ////////////////////////////////////////////////////////////////////////////

    private boolean isDayFinished() {
        boolean finished = true;
        var todayMatches = getDailyMatches();
        for (var matches : todayMatches.values()) {
            for (var match : matches) {
                finished = match.isFinished();
                if (!finished) {
                    break;
                }
            }
            if (!finished) {
                break;
            }
        }
        return finished;
    }

    private boolean confirmDayFinished() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Simulate the rest of the day?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

}
