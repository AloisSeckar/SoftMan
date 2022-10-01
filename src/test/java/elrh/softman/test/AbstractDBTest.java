package elrh.softman.test;

import elrh.softman.logic.Result;
import elrh.softman.logic.db.AbstractDBEntity;
import elrh.softman.logic.db.GameDBManager;
import org.junit.jupiter.api.*;

public abstract class AbstractDBTest {
    @BeforeAll
    static void openDB() {
        GameDBManager.getInstance().setConnection("softmanTest");
    }
    @AfterAll
    static void closeDB() {
        GameDBManager.getInstance().closeConnection();
    }

    @Test
    @DisplayName("abstractDBEntityTest")
    void abstractDBEntityTest() {
        var a1 = new A(1L);
        var a2 = new A(1L);
        var a3 = new A(2L);
        var b1 = new B(1L);
        var b2 = new B(1L);
        var b3 = new B(2L);
        Assertions.assertEquals(Long.valueOf(1L).hashCode(), a1.hashCode());
        Assertions.assertEquals(Long.valueOf(2L).hashCode(), a3.hashCode());
        Assertions.assertEquals(a1, a2);
        Assertions.assertNotEquals(a1, a3);
        Assertions.assertNotEquals(a1, b1); // need this to check equals implementation
        Assertions.assertEquals(b1, b2);
        Assertions.assertNotEquals(b1, b3);
        Assertions.assertNotEquals(a3, b3); // need this to check equals implementation
    }

    private class A extends AbstractDBEntity {
        public long id;
        public A(long id) { this.id = id; }
        @Override
        public long getId() { return id; }
        @Override
        public Result persist() { return null; }
    }

    private class B extends AbstractDBEntity {
        public long id;
        public B(long id) { this.id = id; }
        @Override
        public long getId() { return id; }
        @Override
        public Result persist() { return null; }
    }
}
