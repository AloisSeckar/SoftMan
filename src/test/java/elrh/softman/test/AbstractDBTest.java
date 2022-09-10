package elrh.softman.test;

import elrh.softman.logic.db.GameDBManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractDBTest {
    @BeforeAll
    static void openDB() {
        GameDBManager.getInstance().setConnection("softmanTest");
    }
    @AfterAll
    static void closeDB() {
        GameDBManager.getInstance().closeConnection();
    }
}
