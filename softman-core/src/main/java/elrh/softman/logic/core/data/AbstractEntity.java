package elrh.softman.logic.core.data;

import java.util.UUID;

public abstract class AbstractEntity {

    public abstract UUID getId();

    @Override
    public int hashCode() {
        var id = getId();
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !this.getClass().equals(obj.getClass())) {
            return false;
        }
        var other = (AbstractEntity) obj;
        return this.getId() != null && this.getId().equals(other.getId());
    }
}
