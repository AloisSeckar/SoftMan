package elrh.softman.logic.interfaces;

import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Team;

public interface IFocusListener {

    void focusedClubChanged(Club newlyFocusedClub);
    void focusedTeamChanged(Team newlyFocusedTeam);

}
