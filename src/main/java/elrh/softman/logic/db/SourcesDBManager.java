package elrh.softman.logic.db;

import elrh.softman.utils.Constants;
import java.sql.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SourcesDBManager {

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
    public String getRandomFirstName(String gender) {
        return getRandomName(gender, "softman_firstnames");
    }

    public String getRandomLastName() {
        return getRandomName("x", "softman_lastnames");
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

    private String getRandomName(String gender, String table) {
        String ret = "Player";

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT name FROM " + table + " WHERE gender = '" + gender + "' ORDER BY RANDOM() LIMIT 1;")) {
            rs.next();
            ret = rs.getString("name");
        } catch (SQLException ex) {
            LOG.error("SourcesDBManager.getRandomName", ex);
        }

        return ret;
    }
}
