package elrh.softman.logic.managers;

import elrh.softman.logic.core.*;
import lombok.Data;

@Data
public class StateManager {

    // TODO keep some user's info as well

    private Club activeClub; // = user's club

    private Club focusedClub; // = currently displayed in UI
    private Team focusedTeam; // = currently displayed in UI

    public void reset() {
        activeClub = null;
        focusedClub = null;
        focusedTeam = null;
    }

    public boolean userManagesTeam(Team team) {
        if (activeClub != null) {
            return activeClub.getTeams().contains(team);
        } else {
            return false;
        }
    }
}
