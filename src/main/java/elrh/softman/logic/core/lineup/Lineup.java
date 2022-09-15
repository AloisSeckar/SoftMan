package elrh.softman.logic.core.lineup;

import elrh.softman.logic.Result;
import elrh.softman.utils.Constants;
import elrh.softman.utils.Utils;
import java.util.*;
import lombok.*;

// TODO better validations (player may not be in lineup twice, all positions must be set, etc.)
// TODO method for retrieving current player at given position in defense

public class Lineup {

    public static final int POSITION_PLAYERS = 10;
    public static final int SUBSTITUTES = 7;

    @Getter
    private final long teamId;
    @Getter
    private final String teamName;

    @Getter
    private final List<PlayerRecord>[] positionPlayers = new ArrayList[POSITION_PLAYERS];

    @Getter
    private final PlayerRecord[] substitutes = new PlayerRecord[SUBSTITUTES]; // TODO change type to PlayerInfo

    public Lineup(long teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;

        for (int i = 0; i < 9; i++) {
            positionPlayers[i] = new ArrayList<>();
        }
    }

    public Result initPositionPlayer(int batOrder, PlayerRecord player) {
        if (batOrder > 0 && batOrder <= POSITION_PLAYERS) {
            var newList = new ArrayList<PlayerRecord>();
            newList.add(player);
            positionPlayers[batOrder - 1] = newList;
            return Constants.RESULT_OK;
        } else {
            return new Result(false, "Batting order " + batOrder + " out of bounds (1 - " + POSITION_PLAYERS + ")");
        }
    }

    public Result initSubstitute(int subOrder, PlayerRecord player) {
        if (subOrder > 0 && subOrder <= SUBSTITUTES) {
            substitutes[subOrder - 1] = player;
            return Constants.RESULT_OK;
        } else {
            return new Result(false, "Substitute position " + subOrder + " out of bounds (1 - " + SUBSTITUTES + ")");
        }
    }

    public Result substitutePlayer(int batOrder, PlayerRecord player) {
        if (batOrder > 0 && batOrder <= POSITION_PLAYERS) {
            var records = positionPlayers[batOrder - 1];
            if (Utils.listNotEmpty(records)) {
                records.add(player);
                return Constants.RESULT_OK;
            } else {
                return new Result(false, "Batting order " + batOrder + " not initialized");
            }
        } else {
            return new Result(false, "Batting order " + batOrder + " out of bounds (1 - " + POSITION_PLAYERS + ")");
        }
    }

    public PlayerRecord getCurrentPositionPlayer(int batOrder) {
        if (batOrder > 0 && batOrder <= POSITION_PLAYERS) {
            var lineupSpot = positionPlayers[batOrder - 1];
            if (lineupSpot != null && lineupSpot.size() > 0) {
                return lineupSpot.get(lineupSpot.size() - 1);
            }
        }
        return null;
    }

    public boolean useDP() {
        return Utils.listNotEmpty(positionPlayers[POSITION_PLAYERS - 1]);
    }

}
