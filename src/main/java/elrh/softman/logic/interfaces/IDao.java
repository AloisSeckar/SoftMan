package elrh.softman.logic.interfaces;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import elrh.softman.logic.Result;
import elrh.softman.logic.db.AbstractDBEntity;

public interface IDao<T extends AbstractDBEntity> {

    Class<T> getTypePameterClass();

    Result init(JdbcPooledConnectionSource conn, boolean dropExisting);

    Result saveObject(T object);

    T getObjectById(long objectId);

}
