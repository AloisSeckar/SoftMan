package elrh.softman.logic.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import elrh.softman.logic.Result;
import elrh.softman.utils.Constants;
import elrh.softman.utils.ErrorUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DaoManager<U extends AbstractDBEntity> {

    private Dao<U, Long> dao;

    @NonNull
    private final Class<U> typeParameterClass;

    public Class<U> getTypeParameterClass() {
        return typeParameterClass;
    }

    public Result init(JdbcPooledConnectionSource conn, boolean dropExisting) {
        try {
            if (dropExisting) {
                TableUtils.dropTable(conn, typeParameterClass, true);
            }
            TableUtils.createTableIfNotExists(conn, typeParameterClass);
            dao = com.j256.ormlite.dao.DaoManager.createDao(conn, typeParameterClass);
            LOG.info("Dao object initialized");
            return Constants.RESULT_OK;
        } catch (Exception ex) {
            return ErrorUtils.handleException("init", ex);
        }
    }

    public Result saveObject(AbstractDBEntity object) {
        try {
            if (object.getId() > 0) {
                dao.update((U) object);
            } else {
                dao.create((U) object);
            }
            LOG.info("OBJECT saved into DB");
            return Constants.RESULT_OK;
        } catch (Exception ex) {
            return ErrorUtils.handleException("saveObject", ex);
        }
    }

    public U getObjectById(long objectId) {
        try {
            return dao.queryForId(objectId);
        } catch (Exception ex) {
            ErrorUtils.handleException("getObjectById", ex);
            return null;
        }
    }

}
