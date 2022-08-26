package elrh.softman.logic.managers;

import elrh.softman.logic.core.Team;
import lombok.Getter;
import lombok.Setter;

public class UserManager {

    // TODO this is just temporary mock
    @Getter @Setter
    private Team activeTeam;

    public void reset() {
        // TODO impl
    }

    public boolean managesTeam(Team team) {
        // TODO impl
        return true;
    }

}
