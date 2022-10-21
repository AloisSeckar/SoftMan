package elrh.softman.logic.core;

import elrh.softman.logic.Result;
import elrh.softman.logic.db.orm.lineup.LinuepInfo;
import elrh.softman.logic.db.orm.player.PlayerRecord;
import elrh.softman.logic.db.orm.player.PlayerStats;
import elrh.softman.logic.enums.PlayerPosition;
import elrh.softman.utils.Constants;
import elrh.softman.utils.Utils;
import java.util.*;
import lombok.*;

// TODO better validations (player may not be in lineup twice, all positions must be set, etc.)
// TODO method for retrieving current player at given position in defense

public class Lineup {

    public static final int POSITION_PLAYERS = 10;
    public static final int SUBSTITUTES = 8;

    @Getter
    private final LinuepInfo linuepInfo;

    @Getter
    private final ArrayList<PlayerRecord>[] positionPlayers = new ArrayList[POSITION_PLAYERS];

    @Getter
    private final PlayerRecord[] substitutes = new PlayerRecord[SUBSTITUTES]; // TODO change type to PlayerInfo

    public Lineup(long teamId, String teamName, String teamShortName, String teamLogo) {
        this.linuepInfo = new LinuepInfo(teamId, teamName, teamShortName, teamLogo);

        for (int i = 0; i < POSITION_PLAYERS; i++) {
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

    public PlayerRecord getCurrentBatter(int batOrder) {
        if (batOrder > 0 && batOrder <= POSITION_PLAYERS) {
            var lineupSpot = positionPlayers[batOrder - 1];
            if (Utils.listNotEmpty(lineupSpot)) {
                return lineupSpot.get(lineupSpot.size() - 1);
            }
        }
        return null;
    }

    public PlayerRecord getCurrentPositionPlayer(PlayerPosition position) {
        PlayerRecord ret = null;
        if (position != null) {
            for (int i = 0; i < POSITION_PLAYERS; i++) {
                var lineupSpot = positionPlayers[i];
                if (Utils.listNotEmpty(lineupSpot)) {
                    var player = lineupSpot.get(lineupSpot.size() - 1);
                    if (position == player.getPosition()) {
                        ret = player;
                        break;
                    }
                }
            }
        }
        return ret;
    }

    public PlayerRecord[] getCurrentFielders() {
        var ret = new PlayerRecord[9];
        ret[0] = getCurrentPositionPlayer(PlayerPosition.PITCHER);
        ret[1] = getCurrentPositionPlayer(PlayerPosition.CATCHER);
        ret[2] = getCurrentPositionPlayer(PlayerPosition.FIRST_BASE);
        ret[3] = getCurrentPositionPlayer(PlayerPosition.SECOND_BASE);
        ret[4] = getCurrentPositionPlayer(PlayerPosition.THIRD_BASE);
        ret[5] = getCurrentPositionPlayer(PlayerPosition.SHORT_STOP);
        ret[6] = getCurrentPositionPlayer(PlayerPosition.LEFT_FIELD);
        ret[7] = getCurrentPositionPlayer(PlayerPosition.CENTER_FIELD);
        ret[8] = getCurrentPositionPlayer(PlayerPosition.RIGHT_FIELD);
        return ret;
    }

    public boolean useDP() {
        return Utils.listNotEmpty(positionPlayers[POSITION_PLAYERS - 1]);
    }

    public void setUp(Match match) {
        for (int i = 0; i < POSITION_PLAYERS; i++) {
            var lineupSpot = positionPlayers[i];
            if (Utils.listNotEmpty(lineupSpot)) {
                var first = lineupSpot.get(0);
                var stats = new PlayerStats();
                var matchString = match.getAwayLineup().getLinuepInfo().getTeamShortName() + " @ " + match.getHomeLineup().getLinuepInfo().getTeamShortName();
                var playerString = first.toString() + ", " + first.getPosition().toString();
                stats.init(match.getMatchInfo().getMatchId(), matchString, first.getPlayer().getPlayerId(), playerString);
                first.setStats(stats);
                positionPlayers[i] = new ArrayList<>();
                positionPlayers[i].add(first);
            }
        }
        for (int i = 0; i < SUBSTITUTES; i++) {
            var substitute = substitutes[i];
            if (substitute != null) {
                substitute.setStats(new PlayerStats());
            }
        }
    }

}
