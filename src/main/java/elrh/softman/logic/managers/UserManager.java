package elrh.softman.logic.managers;

import elrh.softman.logic.core.Team;
import lombok.Getter;
import lombok.Setter;

public class UserManager {

    // TODO this is just temporary mock
    @Getter @Setter
    private Team activeTeam;

    public boolean managesTeam(Team team) {
        // todo impl
        return true;
    }

}
