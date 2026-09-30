package elrh.softman.logic.core;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;
import elrh.softman.logic.core.data.LineupInfo;
import elrh.softman.logic.core.data.PlayerInfo;
import elrh.softman.logic.core.data.PlayerRecord;
import elrh.softman.logic.core.data.PlayerStats;
import elrh.softman.logic.enums.PlayerPosition;
import elrh.softman.utils.Constants;
import elrh.softman.utils.Utils;
import java.util.*;
import lombok.Getter;

// TODO better validations (player may not be in lineup twice, all positions must be set, etc.)
// TODO method for retrieving current player at given position in defense

public class Lineup {

    public static final int POSITION_PLAYERS = 10;
    public static final int SUBSTITUTES = 8;

    @Getter
    private final LineupInfo lineupInfo;

    @SuppressWarnings("unchecked") // TODO try to solve
    @Getter
    private final ArrayList<PlayerRecord>[] positionPlayers = new ArrayList[POSITION_PLAYERS];

    @Getter
    private final PlayerRecord[] substitutes = new PlayerRecord[SUBSTITUTES]; // TODO change type to PlayerInfo

    public Lineup(UUID teamId, String teamName, String teamShortName, String teamLogo) {
        this.lineupInfo = new LineupInfo(teamId, teamName, teamShortName, teamLogo);
        reset();
    }

    // used when reassembling a loaded world, so the stored lineup identity is kept
    public Lineup(LineupInfo lineupInfo) {
        this.lineupInfo = lineupInfo;
        reset();
    }

    public void reset() {
        for (int i = 0; i < POSITION_PLAYERS; i++) {
            positionPlayers[i] = new ArrayList<>();
        }
        Arrays.fill(substitutes, null);
    }

    public Result initPositionPlayer(int batOrder, PlayerRecord player) {
        if (batOrder > 0 && batOrder <= POSITION_PLAYERS) {
            var newList = new ArrayList<PlayerRecord>();
            newList.add(player);
            positionPlayers[batOrder - 1] = newList;
            return Constants.RESULT_OK;
        } else {
            return new Result(false, String.format("Batting order %d out of bounds (1 - %d)", batOrder, POSITION_PLAYERS));
        }
    }

    public Result initSubstitute(int subOrder, PlayerRecord player) {
        if (subOrder > 0 && subOrder <= SUBSTITUTES) {
            substitutes[subOrder - 1] = player;
            return Constants.RESULT_OK;
        } else {
            return new Result(false, String.format("Substitute position %d out of bounds (1 - %d)", subOrder, SUBSTITUTES));
        }
    }

    public Result substitutePlayer(int batOrder, PlayerRecord player) {
        if (batOrder > 0 && batOrder <= POSITION_PLAYERS) {
            var records = positionPlayers[batOrder - 1];
            if (Utils.listNotEmpty(records)) {
                records.add(player);
                return Constants.RESULT_OK;
            } else {
                return new Result(false, String.format("Batting order %d not initialized", batOrder));
            }
        } else {
            return new Result(false, String.format("Batting order %d out of bounds (1 - %d)", batOrder, POSITION_PLAYERS));
        }
    }

    // fastpitch rules: a starter may re-enter once into his own spot, a substitute never re-enters
    public List<PlayerInfo> getAvailableReplacements(int batOrder) {
        var ret = new ArrayList<PlayerInfo>();
        var spot = getSpot(batOrder);
        if (spot == null) {
            return ret;
        }
        var starter = spot.getFirst().getPlayer();
        if (!samePlayer(spot.getLast().getPlayer(), starter) && !hasReEntered(spot)) {
            ret.add(starter);
        }
        for (var substitute : substitutes) {
            if (substitute != null && !hasAppeared(substitute.getPlayer())) {
                ret.add(substitute.getPlayer());
            }
        }
        return ret;
    }

    // DP/FLEX moves are not supported, so the DP spot stays offense-only
    public List<PlayerPosition> getDefensivePositions(int batOrder) {
        var spot = getSpot(batOrder);
        if (spot != null && spot.getFirst().getPosition() == PlayerPosition.DESIGNATED_PLAYER) {
            return List.of(PlayerPosition.DESIGNATED_PLAYER);
        }
        return PlayerPosition.getAvailablePositions(false, false);
    }

    public Result replacePlayer(int batOrder, PlayerInfo player, PlayerPosition position) {
        var spot = getSpot(batOrder);
        if (spot == null) {
            return new Result(false, String.format("Batting order %d not initialized", batOrder));
        }
        if (player == null || getAvailableReplacements(batOrder).stream().noneMatch(p -> samePlayer(p, player))) {
            return new Result(false, String.format("%s cannot enter the game at batting order %d", player, batOrder));
        }
        if (position == null || !isAllowedPosition(batOrder, position)) {
            return new Result(false, String.format("Position %s is not allowed at batting order %d", position, batOrder));
        }
        appendRecord(spot, player, position);
        return Constants.RESULT_OK;
    }

    public Result changePosition(int batOrder, PlayerPosition position) {
        var spot = getSpot(batOrder);
        if (spot == null) {
            return new Result(false, String.format("Batting order %d not initialized", batOrder));
        }
        if (position == null || !getDefensivePositions(batOrder).contains(position)) {
            return new Result(false, String.format("Position %s is not allowed at batting order %d", position, batOrder));
        }
        var current = spot.getLast();
        if (current.getPosition() == position) {
            return new Result(false, String.format("%s already plays %s", current, position));
        }
        appendRecord(spot, current.getPlayer(), position);
        return Constants.RESULT_OK;
    }

    public Result checkDefense() {
        var counts = new EnumMap<PlayerPosition, Integer>(PlayerPosition.class);
        for (int i = 1; i <= POSITION_PLAYERS; i++) {
            var current = getCurrentBatter(i);
            if (current != null) {
                if (!getDefensivePositions(i).contains(current.getPosition())) {
                    return new Result(false, String.format("%s (%s) needs a defensive position", current, current.getPosition()));
                }
                counts.merge(current.getPosition(), 1, Integer::sum);
            }
        }
        for (var position : PlayerPosition.getAvailablePositions(false, false)) {
            int count = counts.getOrDefault(position, 0);
            if (count == 0) {
                return new Result(false, String.format("Nobody plays %s", position));
            } else if (count > 1) {
                return new Result(false, String.format("%s is assigned more than once", position));
            }
        }
        return Constants.RESULT_OK;
    }

    // fallback for AI and full simulation: unsettled or duplicated players take the uncovered positions
    public List<PlayerRecord> settleDefense() {
        var changed = new ArrayList<PlayerRecord>();
        var missing = new ArrayList<>(PlayerPosition.getAvailablePositions(false, false));
        var unsettled = new ArrayList<Integer>();
        for (int i = 1; i <= POSITION_PLAYERS; i++) {
            var current = getCurrentBatter(i);
            if (current != null) {
                if (getDefensivePositions(i).contains(current.getPosition())
                    && (current.getPosition() == PlayerPosition.DESIGNATED_PLAYER || missing.remove(current.getPosition()))) {
                    continue;
                }
                unsettled.add(i);
            }
        }
        for (int batOrder : unsettled) {
            var allowed = getDefensivePositions(batOrder);
            var position = allowed.contains(PlayerPosition.DESIGNATED_PLAYER) ? PlayerPosition.DESIGNATED_PLAYER
                : !missing.isEmpty() ? missing.removeFirst() : null;
            if (position != null) {
                changed.add(appendRecord(positionPlayers[batOrder - 1], getCurrentBatter(batOrder).getPlayer(), position));
            }
        }
        return changed;
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

    public void setUp(UUID matchId, String matchStr) {
        var teamId = lineupInfo.getTeamId();
        var team = AssociationManager.getInstance().getTeamById(teamId);
        var lineup = team.getDefaultLineup();

        System.arraycopy(lineup.getPositionPlayers(), 0, positionPlayers, 0, POSITION_PLAYERS);
        System.arraycopy(lineup.getSubstitutes(), 0, substitutes, 0, SUBSTITUTES);

        for (int i = 0; i < POSITION_PLAYERS; i++) {
            var lineupSpot = positionPlayers[i];
            if (Utils.listNotEmpty(lineupSpot)) {
                var first = lineupSpot.get(0);
                var stats = new PlayerStats();
                var playerString = String.format("%s, %s", first, first.getPosition());
                stats.init(matchId, matchStr, first.getPlayer().getPlayerId(), playerString);
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

    private ArrayList<PlayerRecord> getSpot(int batOrder) {
        if (batOrder > 0 && batOrder <= POSITION_PLAYERS && Utils.listNotEmpty(positionPlayers[batOrder - 1])) {
            return positionPlayers[batOrder - 1];
        }
        return null;
    }

    private boolean isAllowedPosition(int batOrder, PlayerPosition position) {
        return position == PlayerPosition.PINCH_HITTER || position == PlayerPosition.PINCH_RUNNER
            || getDefensivePositions(batOrder).contains(position);
    }

    // new record per change keeps the starter record intact; the same player keeps one stats line
    private PlayerRecord appendRecord(List<PlayerRecord> spot, PlayerInfo player, PlayerPosition position) {
        var record = new PlayerRecord(player, position);
        var previous = spot.stream().filter(r -> samePlayer(r.getPlayer(), player)).findFirst();
        if (previous.isPresent()) {
            record.setStats(previous.get().getStats());
        } else {
            var matchStats = spot.getFirst().getStats();
            record.getStats().init(matchStats.getMatchId(), matchStats.getMatchStr(), player.getPlayerId(),
                String.format("%s, %s", player, position));
        }
        spot.add(record);
        return record;
    }

    private boolean hasReEntered(List<PlayerRecord> spot) {
        var starter = spot.getFirst().getPlayer();
        boolean left = false;
        for (var record : spot) {
            boolean isStarter = samePlayer(record.getPlayer(), starter);
            if (!isStarter) {
                left = true;
            } else if (left) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAppeared(PlayerInfo player) {
        for (var spot : positionPlayers) {
            if (spot != null && spot.stream().anyMatch(r -> samePlayer(r.getPlayer(), player))) {
                return true;
            }
        }
        return false;
    }

    private static boolean samePlayer(PlayerInfo a, PlayerInfo b) {
        return a != null && b != null && a.getPlayerId().equals(b.getPlayerId());
    }

}
