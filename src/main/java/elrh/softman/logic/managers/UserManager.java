package elrh.softman.logic.managers;

import elrh.softman.logic.core.*;
import elrh.softman.logic.interfaces.*;
import java.util.HashSet;
import lombok.Getter;
import lombok.Setter;

public class UserManager {

    // TODO keep some user's info as well

    @Getter @Setter
    private Club activeClub; // = user's club

    @Getter
    private Club focusedClub; // = currently displayed in UI
    @Getter
    private Team focusedTeam; // = currently displayed in UI
    @Getter
    private League focusedLeague; // = useful reference in certain UI elements

    private final HashSet<IFocusedClubListener> focusedClubListeners = new HashSet<>();
    private final HashSet<IFocusedTeamListener> focusedTeamListeners = new HashSet<>();

    public void registerFocusedClubListener(IFocusedClubListener listener) {
        focusedClubListeners.add(listener);
    }

    public void clearFocusedClubListeners() {
        focusedClubListeners.clear();
    }

    public void registerFocusedTeamListener(IFocusedTeamListener listener) {
        focusedTeamListeners.add(listener);
    }

    public void clearFocusedTeamListeners() {
        focusedTeamListeners.clear();
    }

    public void setFocusedClub(Club focusedClub) {
        this.focusedClub = focusedClub;
        focusedClubListeners.forEach(l -> l.focusedClubChanged(focusedClub));
    }

    public void setFocusedTeam(Team focusedTeam) {
        if (focusedTeam != null) {
            this.focusedTeam = focusedTeam;
            this.focusedLeague = focusedTeam.getCurrentLeague();
            focusedTeamListeners.forEach(l -> l.focusedTeamChanged(focusedTeam));
        }
    }

    public void reset() {
        activeClub = null;
        focusedClub = null;
        focusedTeam = null;
        focusedClubListeners.forEach(l -> l.focusedClubChanged(null));
        focusedTeamListeners.forEach(l -> l.focusedTeamChanged(null));
    }

    public boolean userManagesTeam(long teamId) {
        if (activeClub != null) {
            return activeClub.getTeamIds().contains(teamId);
        } else {
            return false;
        }
    }

}
