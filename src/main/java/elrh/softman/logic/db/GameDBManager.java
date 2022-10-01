package elrh.softman.logic.db;

import com.j256.ormlite.dao.*;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import elrh.softman.logic.db.orm.*;
import elrh.softman.logic.db.orm.player.*;
import elrh.softman.logic.db.orm.match.*;
import elrh.softman.logic.core.*;
import elrh.softman.utils.*;
import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class GameDBManager {

    private static GameDBManager INSTANCE;

    private JdbcPooledConnectionSource conn;
    
    private Dao<MatchResult, Long> matchDao;
    private Dao<LeagueInfo, Long> leagueDao;
    private Dao<ClubInfo, Long> clubDao;
    private Dao<TeamInfo, Long> teamDao;
    private Dao<PlayerInfo, Long> playerDao;
    private Dao<PlayerStats, Long> statsRecordDao;

    private GameDBManager() {
    }

    public static GameDBManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameDBManager();
        }
        return INSTANCE;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    public void setConnection(String gameId) {
        if (StringUtils.isNotBlank(gameId)) {
            try {
                String url = Constants.GAME_DB.replace("$id$", gameId);
                conn = new JdbcPooledConnectionSource(url);
                LOG.info("DB connection to 'GAME' (ID = " + gameId + ") successful");
                setUpDatabase();
            } catch (SQLException ex) {
                LOG.error("GameDBManager.connect", ex);
            }
        } else {
            LOG.warn("DB connection attept ignored (ID not specified)");
        }
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception ex) {
                LOG.error("GameDBManager.closeConnection", ex);
            }
        }
    }
    
    public void saveLeague(League league) {
        try {
            if (league.getId() > 0) {
                leagueDao.update(league.getLeagueInfo());
            } else {
                leagueDao.create(league.getLeagueInfo());
            }
            LOG.info("LEAGUE SAVED");
        } catch (Exception ex) {
            LOG.error("GameDBManager.saveLeague", ex);
        }
    }

    public void saveClub(Club club) {
        try {
            if (club.getId() > 0) {
                clubDao.update(club.getClubInfo());
            } else {
                clubDao.create(club.getClubInfo());
            }
            LOG.info("CLUB SAVED");
        } catch (Exception ex) {
            LOG.error("GameDBManager.saveClub", ex);
        }
    }
    
    public void saveMatch(Match match) {
        try {
            // TODO better logically connect Match and ResultRecord
            MatchResult dbObject = new MatchResult(match);
            matchDao.create(dbObject);
            LOG.info("MATCH SAVED");
        } catch (Exception ex) {
            LOG.error("GameDBManager.saveMatch", ex);
        }
    }

    public void savePlayer(Player player) {
        try {
            if (player.getId() > 0) {
                playerDao.update(player.getPlayerInfo());
            } else {
                playerDao.create(player.getPlayerInfo());
            }
            LOG.info("PLAYER SAVED");
        } catch (Exception ex) {
            LOG.error("GameDBManager.savePlayer", ex);
        }
    }

    public void savePlayers(List<PlayerInfo> players) {
        try {
            for (var player : players) {
                if (player.getPlayerId() > 0) {
                    playerDao.update(player);
                } else {
                    playerDao.create(player);
                }
            }
            LOG.info("ALL PLAYERS SAVED");
        } catch (Exception ex) {
            LOG.error("GameDBManager.savePlayers", ex);
        }
    }

    public void saveTeam(Team team) {
        try {
            if (team.getId() > 0) {
                teamDao.update(team.getTeamInfo());
            } else {
                teamDao.create(team.getTeamInfo());
            }
            LOG.info("TEAM SAVED");
        } catch (Exception ex) {
            LOG.error("GameDBManager.saveTeam", ex);
        }
    }

    public void saveTeams(List<Team> teams) {
        try {
            for (Team team : teams) {
                saveTeam(team);
                savePlayers(team.getPlayers());
            }
            LOG.info("TEAMS SAVED");
        } catch (Exception ex) {
            LOG.error("GameDBManager.saveTeams", ex);
        }
    }

    public TeamInfo getTeam(long teamId) {
        try {
            return teamDao.queryForId(teamId);
        } catch (Exception ex) {
            ErrorUtils.handleException("getTeam", ex);
            return null;
        }
    }

    public elrh.softman.logic.Result saveStatsRecord(PlayerStats record) {
        try {
            if (record.getId() > 0) {
                statsRecordDao.update(record);
            } else {
                statsRecordDao.create(record);
            }
            LOG.info("TEAM SAVED");
            return Constants.RESULT_OK;
        } catch (Exception ex) {
            return ErrorUtils.handleException("saveStatsRecord", ex);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private void setUpDatabase() throws SQLException {
        // TODO remove this to allow re-loading
        TableUtils.dropTable(conn, LeagueInfo.class, true);
        TableUtils.dropTable(conn, ClubInfo.class, true);
        TableUtils.dropTable(conn, PlayerInfo.class, true);
        TableUtils.dropTable(conn, PlayerAttributes.class, true);
        TableUtils.dropTable(conn, TeamInfo.class, true);
        TableUtils.dropTable(conn, MatchResult.class, true);
        TableUtils.dropTable(conn, PlayerStats.class, true);
        // TODO remove this to allow re-loading

        TableUtils.createTableIfNotExists(conn, LeagueInfo.class);
        TableUtils.createTableIfNotExists(conn, ClubInfo.class);
        TableUtils.createTableIfNotExists(conn, PlayerInfo.class);
        TableUtils.createTableIfNotExists(conn, PlayerAttributes.class);
        TableUtils.createTableIfNotExists(conn, TeamInfo.class);
        TableUtils.createTableIfNotExists(conn, MatchResult.class);
        TableUtils.createTableIfNotExists(conn, PlayerStats.class);

        leagueDao = DaoManager.createDao(conn, LeagueInfo.class);
        clubDao = DaoManager.createDao(conn, ClubInfo.class);
        playerDao = DaoManager.createDao(conn, PlayerInfo.class);
        teamDao = DaoManager.createDao(conn, TeamInfo.class);
        matchDao = DaoManager.createDao(conn, MatchResult.class);

        statsRecordDao = DaoManager.createDao(conn, PlayerStats.class);
    }

}
