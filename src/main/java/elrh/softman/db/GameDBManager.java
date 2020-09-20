package elrh.softman.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import elrh.softman.constants.Constants;
import elrh.softman.db.orm.DBMatch;
import elrh.softman.logic.Match;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;

public class GameDBManager {

    private static final Logger LOG = LoggerFactory.getLogger(GameDBManager.class);

    private static GameDBManager INSTANCE;

    private JdbcPooledConnectionSource conn;
    
    private Dao<DBMatch, Long> matchDao;

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
    
    public void saveMatch(Match match) {
        try {
            DBMatch dbObject = new DBMatch(match);
            matchDao.create(dbObject);
            LOG.info("SAVED");
        } catch (Exception ex) {
            LOG.error("GameDBManager.saveMatch", ex);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private void setUpDatabase() throws SQLException {
        TableUtils.createTableIfNotExists(conn, DBMatch.class);
        TableUtils.clearTable(conn, DBMatch.class); // TODO remove this to allow re-loading
        matchDao = DaoManager.createDao(conn, DBMatch.class);
    }

}
