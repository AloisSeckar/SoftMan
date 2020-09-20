package elrh.softman.db;

import elrh.softman.constants.Constants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;

public class GameDBManager {

    private static final Logger LOG = LoggerFactory.getLogger(GameDBManager.class);

    private static GameDBManager INSTANCE;

    private Connection conn;

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
                conn = DriverManager.getConnection(url);
                if (conn.isValid(5)) {
                    LOG.info("DB connection to 'GAME' (ID = " + gameId + ") successful");
                } else {
                    LOG.warn("DB connection to 'GAME' (ID = " + gameId + ") failed");
                }
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
            } catch (SQLException ex) {
                LOG.error("GameDBManager.closeConnection", ex);
            }
        }
    }

}
