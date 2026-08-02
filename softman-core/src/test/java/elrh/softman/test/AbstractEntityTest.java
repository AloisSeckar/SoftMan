package elrh.softman.test;

import elrh.softman.logic.core.data.AbstractEntity;
import java.util.UUID;
import org.junit.jupiter.api.*;

public class AbstractEntityTest {

    @Test
    @DisplayName("abstractEntityTest")
    void abstractEntityTest() {
        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();

        var a1 = new A(id1);
        var a2 = new A(id1);
        var a3 = new A(id2);
        var b1 = new B(id1);
        var b2 = new B(id1);
        var b3 = new B(id2);
        Assertions.assertEquals(id1.hashCode(), a1.hashCode());
        Assertions.assertEquals(id2.hashCode(), a3.hashCode());
        Assertions.assertEquals(a1, a2);
        Assertions.assertNotEquals(a1, a3);
        Assertions.assertNotEquals(a1, b1); // need this to check equals implementation
        Assertions.assertEquals(b1, b2);
        Assertions.assertNotEquals(b1, b3);
        Assertions.assertNotEquals(a3, b3); // need this to check equals implementation
    }

    private static class A extends AbstractEntity {
        private final UUID id;
        public A(UUID id) { this.id = id; }
        @Override
        public UUID getId() { return id; }
    }

    private static class B extends AbstractEntity {
        private final UUID id;
        public B(UUID id) { this.id = id; }
        @Override
        public UUID getId() { return id; }
    }
}
