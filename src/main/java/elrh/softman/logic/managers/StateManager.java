package elrh.softman.logic.managers;

import elrh.softman.logic.core.*;
import elrh.softman.logic.interfaces.IFocusListener;
import java.util.HashSet;
import lombok.Getter;
import lombok.Setter;

public class StateManager {

    // TODO keep some user's info as well

    @Getter @Setter
    private Club activeClub; // = user's club

    @Getter
    private Club focusedClub; // = currently displayed in UI
    @Getter
    private Team focusedTeam; // = currently displayed in UI
    @Getter
    private League focusedLeague; // = useful reference in certain UI elements

    private final HashSet<IFocusListener> focusListeners = new HashSet<>();

    public void setFocusedClub(Club focusedClub) {
        this.focusedClub = focusedClub;
        focusListeners.forEach(l -> l.focusedClubChanged(focusedClub));
    }

    public void setFocusedTeam(Team focusedTeam) {
        if (focusedTeam != null) {
            this.focusedTeam = focusedTeam;
            this.focusedLeague = focusedTeam.getCurrentLeague();
            focusListeners.forEach(l -> l.focusedTeamChanged(focusedTeam));
        }
    }

    public void reset() {
        activeClub = null;
        focusedClub = null;
        focusedTeam = null;
    }

    public boolean userManagesTeam(long teamId) {
        if (activeClub != null) {
            return activeClub.getTeamIds().contains(teamId);
        } else {
            return false;
        }
    }

    public void registerFocusListener(IFocusListener listener) {
        focusListeners.add(listener);
    }

}
