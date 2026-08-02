package elrh.softman.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import elrh.softman.utils.Constants;
import elrh.softman.utils.ErrorUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

// owns the save-file connection and one DAO per table; not touched during simulation, only on save/load
@Slf4j
class GameDatabase implements AutoCloseable {

    static final String SAVE_DIR = "sav";

    private final JdbcPooledConnectionSource conn;
    private final Map<Class<?>, Dao<?, ?>> daos = new LinkedHashMap<>();

    private GameDatabase(JdbcPooledConnectionSource conn) {
        this.conn = conn;
    }

    static GameDatabase open(String gameId, boolean recreateTables) throws Exception {
        if (StringUtils.isBlank(gameId)) {
            throw new IllegalArgumentException("Game ID not specified");
        }
        Files.createDirectories(Path.of(SAVE_DIR));

        var url = Constants.GAME_DB.replace("$id$", gameId);
        var database = new GameDatabase(new JdbcPooledConnectionSource(url));
        database.initTables(recreateTables);
        LOG.info("Save file for game '{}' opened", gameId);
        return database;
    }

    static boolean saveExists(String gameId) {
        return Files.exists(saveFile(gameId));
    }

    static Path saveFile(String gameId) {
        return Path.of(SAVE_DIR, "game-" + gameId + ".db");
    }

    @SuppressWarnings("unchecked")
    <T> Dao<T, Object> dao(Class<T> type) {
        var dao = daos.get(type);
        if (dao == null) {
            throw new IllegalStateException("No DAO registered for " + type.getSimpleName());
        }
        return (Dao<T, Object>) dao;
    }

    ConnectionSource getConnectionSource() {
        return conn;
    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (Exception ex) {
            ErrorUtils.handleException("GameDatabase.close", ex);
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    private void initTables(boolean recreateTables) throws Exception {
        for (DatabaseTableConfig<?> config : TableConfigs.all()) {
            if (recreateTables) {
                TableUtils.dropTable(conn, config, true);
            }
            TableUtils.createTableIfNotExists(conn, config);
            daos.put(config.getDataClass(), com.j256.ormlite.dao.DaoManager.createDao(conn, config));
        }
    }
}
