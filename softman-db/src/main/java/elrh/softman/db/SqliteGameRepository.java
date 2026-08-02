package elrh.softman.db;

import com.j256.ormlite.dao.Dao;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;
import elrh.softman.logic.core.*;
import elrh.softman.logic.core.data.*;
import elrh.softman.logic.core.stats.Standing;
import elrh.softman.logic.interfaces.IGameRepository;
import elrh.softman.utils.Constants;
import elrh.softman.utils.ErrorUtils;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;

/**
 * Writes and reads the whole game world in a single transaction.
 * Nothing here is called during simulation - the world lives in memory and is only snapshotted on demand.
 */
@Slf4j
public class SqliteGameRepository implements IGameRepository {

    @Override
    public boolean saveExists(String gameId) {
        return GameDatabase.saveExists(gameId);
    }

    @Override
    public Result save(String gameId, AssociationManager world) {
        try (var db = GameDatabase.open(gameId, true)) {
            inTransaction(db, () -> {
                writeWorld(db, gameId, world);
                return null;
            });
            LOG.info("Game '{}' saved", gameId);
            return Constants.RESULT_OK;
        } catch (Exception ex) {
            return ErrorUtils.handleException("SqliteGameRepository.save", ex);
        }
    }

    @Override
    public Result load(String gameId, AssociationManager world) {
        try (var db = GameDatabase.open(gameId, false)) {
            var meta = first(db.dao(GameMeta.class).queryForAll());
            if (meta == null) {
                return new Result(false, "Save file for '" + gameId + "' holds no game");
            }
            if (meta.getSchemaVersion() != GameMeta.SCHEMA_VERSION) {
                return new Result(false, String.format(
                    "Save file schema version %d is not supported (expected %d)",
                    meta.getSchemaVersion(), GameMeta.SCHEMA_VERSION));
            }
            world.reset();
            readWorld(db, meta, world);
            LOG.info("Game '{}' loaded", gameId);
            return Constants.RESULT_OK;
        } catch (Exception ex) {
            return ErrorUtils.handleException("SqliteGameRepository.load", ex);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // save

    private void writeWorld(GameDatabase db, String gameId, AssociationManager world) throws Exception {
        var attributes = new LinkedHashMap<UUID, PlayerAttributes>();
        var playerInfos = new LinkedHashMap<UUID, PlayerInfo>();
        var stats = new LinkedHashMap<UUID, PlayerStats>();
        var records = new LinkedHashMap<UUID, PlayerRecord>();
        var lineups = new LinkedHashMap<UUID, Lineup>();

        for (var player : world.getPlayers(false)) {
            collectPlayer(player.getPlayerInfo(), playerInfos, attributes);
            player.getStats().forEach(s -> stats.put(s.getId(), s));
        }
        for (var team : world.getAllTeams()) {
            team.getPlayers().forEach(p -> collectPlayer(p, playerInfos, attributes));
            collectLineup(team.getDefaultLineup(), lineups, records, stats, playerInfos, attributes);
        }
        for (var match : world.getAllMatches()) {
            collectLineup(match.getAwayLineup(), lineups, records, stats, playerInfos, attributes);
            collectLineup(match.getHomeLineup(), lineups, records, stats, playerInfos, attributes);
        }

        // order matters: referenced rows must exist before the rows pointing at them
        writeAll(db, PlayerAttributes.class, attributes.values());
        writeAll(db, PlayerInfo.class, playerInfos.values());
        writeAll(db, PlayerStats.class, stats.values());
        writeAll(db, PlayerRecord.class, records.values());

        writeAll(db, ClubInfo.class, world.getClubs(false).stream().map(Club::getClubInfo).toList());
        writeAll(db, LeagueInfo.class, world.getAllLeagues().stream().map(League::getLeagueInfo).toList());
        writeAll(db, TeamInfo.class, world.getAllTeams().stream().map(Team::getTeamInfo).toList());
        writeAll(db, LineupInfo.class, lineups.values().stream().map(Lineup::getLineupInfo).toList());

        writeAll(db, TeamPlayerRow.class, teamPlayerRows(world));
        writeAll(db, LineupSpotRow.class, lineupSpotRows(lineups.values()));

        writeAll(db, MatchInfo.class, world.getAllMatches().stream().map(Match::getMatchInfo).toList());
        writeAll(db, MatchPlayByPlay.class, world.getAllMatches().stream().flatMap(m -> m.getPlayByPlay().stream()).toList());
        writeAll(db, BoxScoreRow.class, boxScoreRows(world));
        writeAll(db, Standing.class, world.getAllLeagues().stream().flatMap(l -> l.getStandings().stream()).toList());

        var meta = new GameMeta();
        meta.setGameId(gameId);
        meta.setSavedAt(LocalDate.now());
        meta.setCurrentDate(world.getClock().getCurrentDate());
        meta.setViewDate(world.getClock().getViewDate());
        meta.setActiveClubId(idOf(world.getUser().getActiveClub()));
        meta.setFocusedClubId(idOf(world.getUser().getFocusedClub()));
        meta.setFocusedTeamId(world.getUser().getFocusedTeam() != null ? world.getUser().getFocusedTeam().getId() : null);
        db.dao(GameMeta.class).create(meta);
    }

    private void collectPlayer(PlayerInfo info, Map<UUID, PlayerInfo> infos, Map<UUID, PlayerAttributes> attributes) {
        if (info == null) {
            return;
        }
        infos.put(info.getId(), info);
        if (info.getAttributes() != null) {
            attributes.put(info.getAttributes().getId(), info.getAttributes());
        }
    }

    private void collectLineup(Lineup lineup, Map<UUID, Lineup> lineups, Map<UUID, PlayerRecord> records,
                               Map<UUID, PlayerStats> stats, Map<UUID, PlayerInfo> infos,
                               Map<UUID, PlayerAttributes> attributes) {
        if (lineup == null || lineups.containsKey(lineup.getLineupInfo().getId())) {
            return;
        }
        lineups.put(lineup.getLineupInfo().getId(), lineup);
        forEachRecord(lineup, (kind, order, depth, record) -> {
            records.put(record.getId(), record);
            if (record.getStats() != null) {
                stats.put(record.getStats().getId(), record.getStats());
            }
            collectPlayer(record.getPlayer(), infos, attributes);
        });
    }

    private List<TeamPlayerRow> teamPlayerRows(AssociationManager world) {
        var rows = new ArrayList<TeamPlayerRow>();
        for (var team : world.getAllTeams()) {
            var players = team.getPlayers();
            for (int i = 0; i < players.size(); i++) {
                rows.add(new TeamPlayerRow(team.getId(), players.get(i).getId(), i));
            }
        }
        return rows;
    }

    private List<LineupSpotRow> lineupSpotRows(Collection<Lineup> lineups) {
        var rows = new ArrayList<LineupSpotRow>();
        lineups.forEach(lineup -> forEachRecord(lineup, (kind, order, depth, record) ->
            rows.add(new LineupSpotRow(lineup.getLineupInfo().getId(), kind, order, depth, record.getId()))));
        return rows;
    }

    private List<BoxScoreRow> boxScoreRows(AssociationManager world) {
        var rows = new ArrayList<BoxScoreRow>();
        for (var match : world.getAllMatches()) {
            var box = match.getBoxScore();
            var away = box.getPoints(true);
            var home = box.getPoints(false);
            for (int i = 0; i < box.getInnings(); i++) {
                var row = new BoxScoreRow(match.getId(), i + 1, away[i], home[i]);
                if (i == 0) {
                    row.setAwayHits(box.getHits(true));
                    row.setHomeHits(box.getHits(false));
                    row.setAwayErrors(box.getErrors(true));
                    row.setHomeErrors(box.getErrors(false));
                }
                rows.add(row);
            }
        }
        return rows;
    }

    ////////////////////////////////////////////////////////////////////////////
    // load

    private void readWorld(GameDatabase db, GameMeta meta, AssociationManager world) throws Exception {
        var attributes = byId(db.dao(PlayerAttributes.class).queryForAll());
        var playerInfos = byId(db.dao(PlayerInfo.class).queryForAll());
        var stats = byId(db.dao(PlayerStats.class).queryForAll());
        var records = byId(db.dao(PlayerRecord.class).queryForAll());
        var clubInfos = byId(db.dao(ClubInfo.class).queryForAll());
        var leagueInfos = byId(db.dao(LeagueInfo.class).queryForAll());
        var teamInfos = byId(db.dao(TeamInfo.class).queryForAll());
        var lineupInfos = byId(db.dao(LineupInfo.class).queryForAll());

        // OrmLite's auto-refresh hands back fresh instances per row; collapse them onto one object per id
        playerInfos.values().forEach(p -> p.setAttributes(canonical(attributes, p.getAttributes())));
        records.values().forEach(r -> {
            r.setPlayer(canonical(playerInfos, r.getPlayer()));
            r.setStats(canonical(stats, r.getStats()));
        });
        teamInfos.values().forEach(t -> {
            t.setClubInfo(canonical(clubInfos, t.getClubInfo()));
            t.setLeagueInfo(canonical(leagueInfos, t.getLeagueInfo()));
        });

        var clubs = new LinkedHashMap<UUID, Club>();
        clubInfos.values().forEach(info -> {
            var club = new Club(info);
            clubs.put(club.getId(), club);
            world.restoreClub(club);
        });

        var leagues = new LinkedHashMap<UUID, League>();
        leagueInfos.values().forEach(info -> {
            var league = new League(info);
            leagues.put(league.getId(), league);
            world.restoreLeague(league);
        });

        var players = new LinkedHashMap<UUID, Player>();
        playerInfos.values().forEach(info -> {
            var player = new Player();
            player.setPlayerInfo(info);
            players.put(player.getId(), player);
            world.restorePlayer(player);
        });
        // season totals are derived, not stored
        stats.values().stream()
            .filter(s -> s.getPlayerId() != null && s.getMatchId() != null)
            .forEach(s -> {
                var player = players.get(s.getPlayerId());
                if (player != null) {
                    player.getStats().add(s);
                    player.getSeasonTotal().include(s);
                }
            });

        var spotsByLineup = groupBy(db.dao(LineupSpotRow.class).queryForAll(), LineupSpotRow::getLineupId);
        var lineups = new LinkedHashMap<UUID, Lineup>();
        lineupInfos.values().forEach(info ->
            lineups.put(info.getId(), buildLineup(info, spotsByLineup.get(info.getId()), records)));

        var rosterByTeam = groupBy(db.dao(TeamPlayerRow.class).queryForAll(), TeamPlayerRow::getTeamId);
        teamInfos.values().forEach(info -> {
            var defaultLineup = lineups.values().stream()
                .filter(l -> info.getId().equals(l.getLineupInfo().getTeamId()))
                .findFirst().orElseGet(() -> new Lineup(info.getId(), info.getName(), "", ""));
            var team = new Team(info, defaultLineup);
            rosterByTeam.getOrDefault(info.getId(), List.of()).stream()
                .sorted(Comparator.comparingInt(TeamPlayerRow::getOrd))
                .forEach(row -> {
                    var player = players.get(row.getPlayerId());
                    if (player != null) {
                        team.getPlayers().add(player.getPlayerInfo());
                    }
                });
            world.addCurrentTeam(team);
            var club = clubs.get(info.getClubInfo().getId());
            if (club != null) {
                club.restoreTeam(team);
            }
            if (info.getLeagueInfo() != null) {
                var league = leagues.get(info.getLeagueInfo().getId());
                if (league != null) {
                    league.getTeams().add(team);
                }
            }
        });

        var pbpByMatch = groupBy(db.dao(MatchPlayByPlay.class).queryForAll(), MatchPlayByPlay::getMatchId);
        var boxByMatch = groupBy(db.dao(BoxScoreRow.class).queryForAll(), BoxScoreRow::getMatchId);
        for (var info : db.dao(MatchInfo.class).queryForAll()) {
            var status = info.getStatus();
            var away = lineups.get(info.getAwayLineupId());
            var home = lineups.get(info.getHomeLineupId());
            var match = new Match(info, away, home);
            info.setStatus(status); // the Match constructor resets it to SCHEDULED
            match.setPlayByPlay(sortedPlays(pbpByMatch.get(info.getId())));
            restoreBoxScore(match, boxByMatch.get(info.getId()));
            world.addCurrentMatch(match);
            var league = leagues.get(info.getLeagueId());
            if (league != null) {
                league.restoreMatch(match);
            }
        }

        db.dao(Standing.class).queryForAll().forEach(standing -> {
            var league = leagues.get(standing.getLeagueId());
            if (league != null) {
                league.getStandings().add(standing);
            }
        });

        world.getClock().restore(meta.getCurrentDate(), meta.getViewDate());
        world.getUser().setActiveClub(clubs.get(meta.getActiveClubId()));
        world.getUser().setFocusedClub(clubs.get(meta.getFocusedClubId()));
        if (meta.getFocusedTeamId() != null) {
            world.getUser().setFocusedTeam(world.getTeamById(meta.getFocusedTeamId()));
        }
    }

    private Lineup buildLineup(LineupInfo info, List<LineupSpotRow> spots, Map<UUID, PlayerRecord> records) {
        var lineup = new Lineup(info);
        if (spots == null) {
            return lineup;
        }
        spots.stream()
            .sorted(Comparator.comparingInt(LineupSpotRow::getSlotOrder).thenComparingInt(LineupSpotRow::getSlotDepth))
            .forEach(spot -> {
                var record = records.get(spot.getPlayerRecordId());
                if (record == null) {
                    return;
                }
                if (LineupSpotRow.KIND_SUBSTITUTE.equals(spot.getSlotKind())) {
                    lineup.initSubstitute(spot.getSlotOrder(), record);
                } else if (spot.getSlotDepth() == 0) {
                    lineup.initPositionPlayer(spot.getSlotOrder(), record);
                } else {
                    lineup.substitutePlayer(spot.getSlotOrder(), record);
                }
            });
        return lineup;
    }

    private void restoreBoxScore(Match match, List<BoxScoreRow> rows) {
        if (rows == null || rows.isEmpty()) {
            return;
        }
        var ordered = rows.stream().sorted(Comparator.comparingInt(BoxScoreRow::getInning)).toList();
        var away = new int[ordered.size()];
        var home = new int[ordered.size()];
        for (int i = 0; i < ordered.size(); i++) {
            away[i] = ordered.get(i).getAwayRuns();
            home[i] = ordered.get(i).getHomeRuns();
        }
        var totals = ordered.get(0);
        match.getBoxScore().restore(away, home,
            totals.getAwayHits(), totals.getHomeHits(), totals.getAwayErrors(), totals.getHomeErrors());
    }

    ////////////////////////////////////////////////////////////////////////////
    // helpers

    private interface RecordVisitor {
        void visit(String slotKind, int slotOrder, int slotDepth, PlayerRecord record);
    }

    private void forEachRecord(Lineup lineup, RecordVisitor visitor) {
        for (int i = 0; i < Lineup.POSITION_PLAYERS; i++) {
            var slot = lineup.getPositionPlayers()[i];
            if (slot != null) {
                for (int depth = 0; depth < slot.size(); depth++) {
                    visitor.visit(LineupSpotRow.KIND_POSITION, i + 1, depth, slot.get(depth));
                }
            }
        }
        for (int i = 0; i < Lineup.SUBSTITUTES; i++) {
            var substitute = lineup.getSubstitutes()[i];
            if (substitute != null) {
                visitor.visit(LineupSpotRow.KIND_SUBSTITUTE, i + 1, 0, substitute);
            }
        }
    }

    private <T> void writeAll(GameDatabase db, Class<T> type, Collection<T> rows) throws Exception {
        Dao<T, Object> dao = db.dao(type);
        for (T row : rows) {
            dao.create(row);
        }
    }

    private <T extends AbstractEntity> Map<UUID, T> byId(Collection<T> rows) {
        var map = new LinkedHashMap<UUID, T>();
        rows.forEach(row -> map.put(row.getId(), row));
        return map;
    }

    private <T extends AbstractEntity> T canonical(Map<UUID, T> known, T candidate) {
        return candidate == null ? null : known.getOrDefault(candidate.getId(), candidate);
    }

    private <T, K> Map<K, List<T>> groupBy(Collection<T> rows, java.util.function.Function<T, K> key) {
        var map = new LinkedHashMap<K, List<T>>();
        rows.forEach(row -> map.computeIfAbsent(key.apply(row), k -> new ArrayList<>()).add(row));
        return map;
    }

    private List<MatchPlayByPlay> sortedPlays(List<MatchPlayByPlay> plays) {
        if (plays == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(plays.stream().sorted(Comparator.comparingInt(MatchPlayByPlay::getOrd)).toList());
    }

    private <T> T first(List<T> rows) {
        return rows == null || rows.isEmpty() ? null : rows.get(0);
    }

    private UUID idOf(Club club) {
        return club != null ? club.getId() : null;
    }

    private <T> void inTransaction(GameDatabase db, Callable<T> work) throws Exception {
        com.j256.ormlite.misc.TransactionManager.callInTransaction(db.getConnectionSource(), work);
    }
}
