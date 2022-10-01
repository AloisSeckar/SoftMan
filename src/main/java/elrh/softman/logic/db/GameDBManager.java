package elrh.softman.logic.db;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import elrh.softman.logic.Result;
import elrh.softman.logic.db.orm.*;
import elrh.softman.logic.db.orm.player.*;
import elrh.softman.logic.db.orm.match.*;
import elrh.softman.logic.interfaces.IDao;
import elrh.softman.utils.*;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class GameDBManager {

    private final List<IDao> daoList = new ArrayList<>();

    private JdbcPooledConnectionSource conn;

    private static GameDBManager INSTANCE;

    public static GameDBManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameDBManager();
        }
        return INSTANCE;
    }

    private GameDBManager() {
        daoList.add(new DaoManager<>(MatchInfo.class));
        daoList.add(new DaoManager<>(MatchPlayByPlay.class));
        daoList.add(new DaoManager<>(MatchResult.class));
        daoList.add(new DaoManager<>(PlayerAttributes.class));
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
                String url = Constants.GAME_DB.replace("$id$", gameId);
                conn = new JdbcPooledConnectionSource(url);
                LOG.info("DB connection to 'GAME' (ID = " + gameId + ") successful");

                var dropExisting = true; // TODO remove this to allow re-loading
                daoList.forEach(dao -> dao.init(conn, dropExisting));

                return Constants.RESULT_OK;
            } else {
                LOG.warn("DB connection attept ignored (ID not specified)");
                return new Result(false, "ID not specified");
            }
        } catch (Exception ex) {
            return ErrorUtils.handleException("setConnection", ex);
        }
    }

    public Result saveObject(Class objectClass, AbstractDBEntity object) {
        var dao = daoList.stream().filter(i -> i.getTypePameterClass() == objectClass).findFirst();
        if (dao.isPresent()) {
            return dao.get().saveObject(object);
        } else {
            return new Result(false, "DAO not found for " + objectClass);
        }
    }

    public Object getObjectById(Class objectClass, long objectId) {
        var dao = daoList.stream().filter(i -> i.getTypePameterClass() == objectClass).findFirst();
        if (dao.isPresent()) {
            return dao.get().getObjectById(objectId);
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
}
