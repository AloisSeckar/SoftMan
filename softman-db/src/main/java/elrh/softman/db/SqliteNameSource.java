package elrh.softman.db;

import elrh.softman.logic.enums.PlayerGender;
import elrh.softman.logic.interfaces.INameSource;
import java.sql.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqliteNameSource implements INameSource, AutoCloseable {

    private static final String SOURCES_DB = "jdbc:sqlite:softman.db";
    private static final String SQL_FIRST_NAME =
            "SELECT name FROM softman_firstnames WHERE gender = ? ORDER BY RANDOM() LIMIT 1";
    private static final String SQL_LAST_NAME =
            "SELECT name FROM softman_lastnames WHERE gender = ? ORDER BY RANDOM() LIMIT 1";

    private final Connection conn;

    public SqliteNameSource() {
        conn = connect();
    }

    @Override
    public String getRandomFirstName(PlayerGender gender) {
        return getRandomName(gender.toString(), SQL_FIRST_NAME);
    }

    @Override
    public String getRandomLastName() {
        return getRandomName("x", SQL_LAST_NAME);
    }

    @Override
    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                LOG.error("SqliteNameSource.close", ex);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private Connection connect() {
        Connection newConnection = null;

        try {
            newConnection = DriverManager.getConnection(SOURCES_DB);
            if (newConnection.isValid(5)) {
                LOG.info("DB connection to 'SOURCES' successful");
            } else {
                LOG.warn("DB connection to 'SOURCES' failed");
            }
        } catch (SQLException ex) {
            LOG.error("SqliteNameSource.connect", ex);
        }

        return newConnection;
    }

    private String getRandomName(String gender, String sql) {
        String ret = "Player";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, gender);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ret = rs.getString("name");
                }
            }
        } catch (SQLException ex) {
            LOG.error("SqliteNameSource.getRandomName", ex);
        }

        return ret;
    }
}
