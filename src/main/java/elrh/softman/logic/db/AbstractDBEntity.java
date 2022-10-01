package elrh.softman.logic.db;

import elrh.softman.logic.Result;

public abstract class AbstractDBEntity {

    // get the artificial ID generated in DB
    public abstract long getId();

    // store current state into DB
    public abstract Result persist();

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        var other = (AbstractDBEntity) obj;
        return other != null ? this.getId() == other.getId() : false;
    }
}
