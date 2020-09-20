package elrh.softman.db;

import elrh.softman.constants.Constants;
import java.sql.*;
import org.slf4j.*;

public class SourcesDBManager {

    private static final Logger LOG = LoggerFactory.getLogger(SourcesDBManager.class);

    private static SourcesDBManager INSTANCE;

    private final Connection conn;

    private SourcesDBManager() {
        conn = connect();
    }

    public static SourcesDBManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SourcesDBManager();
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
                LOG.error("SourcesDBManager.closeConnection", ex);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Connection connect() {
        Connection newConnection = null;

        try {
            newConnection = DriverManager.getConnection(Constants.SOURCES_DB);
            if (newConnection.isValid(5)) {
                LOG.info("DB connection to 'SOURCES' successful");
            } else {
                LOG.warn("DB connection to 'SOURCES' failed");
            }
        } catch (SQLException ex) {
            LOG.error("SourcesDBManager.connect", ex);
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
            LOG.error("SourcesDBManager.getRandomName", ex);
        }

        return ret;
    }
}
