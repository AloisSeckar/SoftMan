package elrh.softman.logic;

import java.util.ArrayList;

import elrh.softman.constants.Constants;
import lombok.Data;

@Data
public class Lineup {

    private final Team team;
    private final ArrayList<LineupPosition> lineup = new ArrayList<>(Constants.LINEUP_SIZE);
    private boolean useDP;

}
