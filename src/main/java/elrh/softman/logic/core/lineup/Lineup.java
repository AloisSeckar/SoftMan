package elrh.softman.logic.core.lineup;

import java.util.ArrayList;

import elrh.softman.constants.Constants;
import elrh.softman.logic.core.Team;
import lombok.Data;

@Data
public class Lineup {

    private final Team team;
    private final ArrayList<LineupPosition> lineup = new ArrayList<>(Constants.LINEUP_SIZE);
    private boolean useDP;

}
