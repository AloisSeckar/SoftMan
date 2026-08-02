package elrh.softman.logic;

import elrh.softman.logic.core.*;
import elrh.softman.logic.core.data.LeagueInfo;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.logic.managers.ClockManager;
import elrh.softman.logic.managers.UserManager;
import elrh.softman.logic.interfaces.IConfirmationPrompt;
import elrh.softman.logic.interfaces.ISimulationRunner;
import elrh.softman.utils.*;
import java.time.LocalDate;
import java.util.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AssociationManager {

    @Getter @Setter
    private boolean testMode = false; // TODO isn't there a better way to aviod GUI constructing during unit tests?

    @Getter
    private final ClockManager clock = new ClockManager();
    @Getter
    private final UserManager user = new UserManager();

    // LinkedHashMap: UUID keys have no natural order, insertion order must be kept stable
    private final Map<UUID, League> managedLeagues = new LinkedHashMap<>();
    private final Map<UUID, Club> registeredClubs = new LinkedHashMap<>();
    private final Map<UUID, Player> registeredPlayers = new LinkedHashMap<>();
    private final Map<UUID, Team> currentTeams = new LinkedHashMap<>();
    private final Map<UUID, Match> currentMatches = new LinkedHashMap<>();

    @Setter
    private ISimulationRunner simulationRunner;

    @Setter
    private IConfirmationPrompt confirmationPrompt;

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
        user.reset();
        managedLeagues.clear();
        registeredClubs.clear();
        currentTeams.clear();
        registeredPlayers.clear();
    }

    public List<League> getLeagues(int year) {
        return managedLeagues.values().stream().filter(l -> l.getLeagueInfo().getYear() == year).toList();
    }

    public List<League> getAllLeagues() {
        return List.copyOf(managedLeagues.values());
    }

    public List<Team> getAllTeams() {
        return List.copyOf(currentTeams.values());
    }

    public List<Match> getAllMatches() {
        return List.copyOf(currentMatches.values());
    }

    public League getLeagueById(UUID leagueId) {
        return managedLeagues.get(leagueId);
    }

    public Result createNewLeague(String name, PlayerLevel level, int tier) {
        try {
            var year = clock.getYear();
            var matchNumber = level.getMatchNumber() + ((tier - 1) * 200);
            LeagueInfo leagueInfo = new LeagueInfo(name, level, year, tier, matchNumber);
            League newLeague = new League(leagueInfo);
            managedLeagues.put(newLeague.getLeagueInfo().getLeagueId(), newLeague);
            LOG.info("New league {} created", newLeague);
            return Constants.RESULT_OK;
        } catch (Exception ex) {
            return ErrorUtils.handleException("AssociationManager.createNewLeague", ex);
        }
    }

    public Result registerTeamIntoLeague(UUID leagueId, Team team) {
        try {
            League league = managedLeagues.get(leagueId);
            if (league != null) {
                var res = league.registerTeam(team);
                if (res.ok()) {
                    addCurrentTeam(team);
                }
                return res;
            } else {
                return new Result(false, String.format("League %s not found", leagueId));
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

    public Club getClubById(UUID clubId) {
        return registeredClubs.get(clubId);
    }

    public Result registerClub(Club club) {
        try {
            var clubId = club.getId();
            int year = clock.getYear();
            Club existingClub = registeredClubs.get(clubId);
            if (existingClub != null) {
                existingClub.getClubInfo().setRegistered(year);
            } else {
                club.getClubInfo().setRegistered(year);
                registeredClubs.put(clubId, club);
            }
            LOG.info("Club {} was registered for {} season", clubId, year);
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

    public Player getPlayerById(UUID playerId) {
        return registeredPlayers.get(playerId);
    }

    public Result registerPlayer(Player player) {
        try {
            var playerId = player.getId();
            int year = clock.getYear();
            Player existingPlayer = registeredPlayers.get(playerId);
            if (existingPlayer != null) {
                existingPlayer.getPlayerInfo().setRegistered(year);
            } else {
                player.getPlayerInfo().setRegistered(year);
                registeredPlayers.put(playerId, player);
            }
            LOG.info("player {} was registered for {} season", playerId, year);
            return Constants.RESULT_OK;
        } catch (Exception ex) {
            return ErrorUtils.handleException("AssociationManager.registerPlayer", ex);
        }
    }

    public void addCurrentTeam(Team team) {
        currentTeams.put(team.getId(), team);
    }

    // used when reassembling a loaded world; unlike register*, these have no side effects
    public void restoreLeague(League league) {
        managedLeagues.put(league.getId(), league);
    }

    public void restoreClub(Club club) {
        registeredClubs.put(club.getId(), club);
    }

    public void restorePlayer(Player player) {
        registeredPlayers.put(player.getId(), player);
    }

    public Team getTeamById(UUID teamId) {
        return currentTeams.get(teamId);
    }

    public void addCurrentMatch(Match match) {
        currentMatches.put(match.getId(), match);
    }

    public Match getMatchById(UUID matchId) {
        return currentMatches.get(matchId);
    }

    public  Map<UUID, List<Match>> getDailyMatches() {
        var ret = new LinkedHashMap<UUID, List<Match>>();
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
            entry.getValue().forEach(match -> {
                if (user.userManagesTeam(match.getHomeLineup().getLineupInfo().getTeamId()) || user.userManagesTeam(match.getAwayLineup().getLineupInfo().getTeamId())) {
                    ret.add(match);
                }
            });
        }
        return ret;
    }

    public List<Match> getDailyMatchesForLeague(UUID leagueId) {
        return currentMatches.values().stream().filter(m -> m.belongsToLeagueAndDate(leagueId, clock.getViewDate())).toList();
    }

    public List<Match> getRoundMatchesForLeague(UUID leagueId, int round) {
        return currentMatches.values().stream().filter(m -> m.belongsToLeagueAndRound(leagueId, round)).toList();
    }

    public boolean isTodayMatch(Match match) {
        var matchDate = match.getMatchInfo().getMatchDay();
        return matchDate.compareTo(clock.getCurrentDate()) == 0;
    }

    public Result nextDay() {
        try {
            if (isDayFinished() || confirmDayFinished()) {
                if (testMode) {
                    // during unit test JavaFX Toolkit is not available
                    plainAdvanceToNextDay();
                    return Constants.RESULT_OK;
                } else {
                    // regular way with showing spinner
                    return simulationRunner.simulateUntil(clock.getCurrentDate().plusDays(1));
                }
            } else {
                return new Result(false, "Day not completed yet");
            }
        } catch (Exception ex) {
            return ErrorUtils.handleException("AssociationManager.nextDay", ex);
        }
    }

    public Result simulateUntil(LocalDate until) {
        try {
            if (testMode) {
                // during unit test JavaFX Toolkit is not available
                while (until.isAfter(clock.getCurrentDate())) {
                    plainAdvanceToNextDay();
                }
                return Constants.RESULT_OK;
            } else {
                // regular way with showing spinner
                return simulationRunner.simulateUntil(until);
            }
        } catch (Exception ex) {
            return ErrorUtils.handleException("AssociationManager.simulateUntil", ex);
        }
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
        if (testMode || confirmationPrompt == null) {
            return true;
        } else {
            return confirmationPrompt.confirm("Simulate the rest of the day?");
        }
    }

    private void plainAdvanceToNextDay() {
        // this is only used during unit tests
        // on runtime SimulationService is being called
        getDailyMatches().values().forEach(matches -> matches.forEach(match -> {
            if (!match.isFinished()) {
                var sim = new MatchSimulator(match, null);
                sim.simulateMatch();
            }
        }));
        clock.plusDays(1);
        clock.adjustViewDay();
    }
}


