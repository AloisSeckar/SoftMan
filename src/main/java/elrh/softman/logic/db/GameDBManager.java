package elrh.softman.logic.db;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import elrh.softman.logic.Result;
import elrh.softman.logic.db.orm.*;
import elrh.softman.logic.db.orm.lineup.*;
import elrh.softman.logic.db.orm.player.*;
import elrh.softman.logic.db.orm.match.*;
import elrh.softman.utils.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class GameDBManager {

    private final List<DaoManager<? extends AbstractDBEntity>> daoList = new ArrayList<>();

    private JdbcPooledConnectionSource conn;

    private static GameDBManager INSTANCE;

    public static GameDBManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameDBManager();
        }
        return INSTANCE;
    }

    private GameDBManager() {
        daoList.add(new DaoManager<>(LinuepInfo.class));
        daoList.add(new DaoManager<>(MatchInfo.class));
        daoList.add(new DaoManager<>(MatchPlayByPlay.class));
        daoList.add(new DaoManager<>(MatchResult.class));
        daoList.add(new DaoManager<>(PlayerAttributes.class));
        daoList.add(new DaoManager<>(PlayerRecord.class));
        daoList.add(new DaoManager<>(PlayerInfo.class));
        daoList.add(new DaoManager<>(PlayerStats.class));
        daoList.add(new DaoManager<>(ClubInfo.class));
        daoList.add(new DaoManager<>(LeagueInfo.class));
        daoList.add(new DaoManager<>(TeamInfo.class));
    }
    
    ////////////////////////////////////////////////////////////////////////////

    public Result setConnection(String gameId) {
        try {
            if (StringUtils.isNotBlank(gameId)) {
                // Create the directory if it doesn't exist
                java.nio.file.Files.createDirectories(java.nio.file.Paths.get("sav"));
                
                String url = Constants.GAME_DB.replace("$id$", gameId);
                conn = new JdbcPooledConnectionSource(url);
                LOG.info("DB connection to 'GAME' (ID = " + gameId + ") successful");

                var dropExisting = true; // TODO remove this to allow re-loading
                for (var dao : daoList) {
                    var result = dao.init(conn, dropExisting);
                    if (!result.ok()) {
                        LOG.error("Failed to initialize DAO for {}: {}", dao.getTypeParameterClass().getSimpleName(), result.message());
                        return result;
                    }
                }

                return Constants.RESULT_OK;
            } else {
                LOG.warn("DB connection attept ignored (ID not specified)");
                return new Result(false, "ID not specified");
            }
        } catch (Exception ex) {
            return ErrorUtils.handleException("setConnection", ex);
        }
    }

    public Result saveObject(Class<? extends AbstractDBEntity> objectClass, AbstractDBEntity object) {
        var dao = getDaoForClass(objectClass);
        if (dao.isPresent()) {
            return dao.get().saveObject(object);
        } else {
            return new Result(false, "DAO not found for " + objectClass);
        }
    }

    public Object getObjectById(Class<? extends AbstractDBEntity> objectClass, long objectId) {
        var dao = getDaoForClass(objectClass);
        if (dao.isPresent()) {
            return dao.get().getObjectById(objectId);
        } else {
            LOG.warn("DAO not found for " + objectClass);
            return null;
        }
    }

    public List<?> getObjectsByQuery(Class<? extends AbstractDBEntity> objectClass, String column, Object value) {
        var dao = getDaoForClass(objectClass);
        if (dao.isPresent()) {
            return dao.get().getObjectsByQuery(column, value);
        } else {
            LOG.warn("DAO not found for " + objectClass);
            return null;
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

    private Optional<DaoManager<? extends AbstractDBEntity>> getDaoForClass(Class<? extends AbstractDBEntity> objectClass) {
        return daoList.stream().filter(i -> i.getTypeParameterClass() == objectClass).findFirst();
    }
}
