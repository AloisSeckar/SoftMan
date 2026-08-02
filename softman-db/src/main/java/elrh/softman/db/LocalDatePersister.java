package elrh.softman.db;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.time.LocalDate;

// OrmLite 6.1 has no built-in java.time support; stored as ISO-8601 so the save file stays readable
public class LocalDatePersister extends BaseDataType {

    private static final LocalDatePersister SINGLETON = new LocalDatePersister();

    private LocalDatePersister() {
        super(SqlType.STRING, new Class<?>[] { LocalDate.class });
    }

    public static LocalDatePersister getSingleton() {
        return SINGLETON;
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return defaultStr;
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getString(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return sqlArg == null ? null : LocalDate.parse((String) sqlArg);
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        return javaObject == null ? null : javaObject.toString();
    }

    @Override
    public boolean isValidForField(Field field) {
        return LocalDate.class == field.getType();
    }
}
