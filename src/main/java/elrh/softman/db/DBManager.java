package elrh.softman.db;

import elrh.softman.constants.Constants;
import java.sql.*;
import org.slf4j.*;

public class DBManager {

    private static final Logger LOG = LoggerFactory.getLogger(DBManager.class);

    private static DBManager INSTANCE;

    private final Connection conn;

    private DBManager() {
        conn = connect();
    }

    public static DBManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DBManager();
        }
        return INSTANCE;
    }

    ////////////////////////////////////////////////////////////////////////////
    public String getRandomFirstName() {
        return getRandomName("softman_firstnames");
    }

    public String getRandomLastName() {
        return getRandomName("softman_lastnames");
    }
    
    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                LOG.error("DBManager.closeConnection", ex);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Connection connect() {
        Connection newConnection = null;

        try {
            newConnection = DriverManager.getConnection(Constants.SQLITE_DB);
            if (newConnection.isValid(5)) {
                LOG.info("DB connection successful");
            } else {
                LOG.warn("DB connection failed");
            }
        } catch (SQLException ex) {
            LOG.error("DBManager.connect", ex);
        }

        return newConnection;
    }

    private String getRandomName(String table) {
        String ret = "Player";

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT name FROM " + table + " ORDER BY RANDOM() LIMIT 1;")) {

            rs.next();
            ret = rs.getString("name");

        } catch (SQLException ex) {
            LOG.error("DBManager.getRandomName", ex);
        }

        return ret;
    }
}
