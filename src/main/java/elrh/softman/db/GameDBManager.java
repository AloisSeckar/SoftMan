package elrh.softman.db;

import com.j256.ormlite.dao.*;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import elrh.softman.constants.Constants;
import elrh.softman.db.orm.*;
import elrh.softman.logic.League;
import elrh.softman.logic.Match;
import elrh.softman.logic.Team;
import java.sql.SQLException;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class GameDBManager {

    private static GameDBManager INSTANCE;

    private JdbcPooledConnectionSource conn;
    
    private Dao<Result, Long> matchDao;
    private Dao<LeagueInfo, Long> leagueDao;
    private Dao<TeamInfo, Long> teamDao;

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
            leagueDao.create(league.getLeagueInfo());
            LOG.info("LEAGUE SAVED");
        } catch (Exception ex) {
            LOG.error("GameDBManager.saveLeague", ex);
        }
    }
    
    public void saveMatch(Match match) {
        try {
            Result dbObject = new Result(match);
            matchDao.create(dbObject);
            LOG.info("MATCH SAVED");
        } catch (Exception ex) {
            LOG.error("GameDBManager.saveMatch", ex);
        }
    }

    public void saveTeams(ArrayList<Team> teams) {
        try {
            for (Team team : teams) {
                teamDao.create(team.getTeamInfo());
            }
            LOG.info("TEAMS SAVED");
        } catch (Exception ex) {
            LOG.error("GameDBManager.saveTeams", ex);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private void setUpDatabase() throws SQLException {
        // TODO remove this to allow re-loading
        TableUtils.dropTable(conn, LeagueInfo.class, true);
        TableUtils.dropTable(conn, TeamInfo.class, true);
        TableUtils.dropTable(conn, Result.class, true);
        // TODO remove this to allow re-loading
        
        TableUtils.createTableIfNotExists(conn, LeagueInfo.class);
        TableUtils.createTableIfNotExists(conn, TeamInfo.class);
        TableUtils.createTableIfNotExists(conn, Result.class);
        
        matchDao = DaoManager.createDao(conn, Result.class);
        leagueDao = DaoManager.createDao(conn, LeagueInfo.class);
        teamDao = DaoManager.createDao(conn, TeamInfo.class);
    }

}
