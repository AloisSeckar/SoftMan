package elrh.softman.logic;

import elrh.softman.db.orm.*;
import static elrh.softman.logic.Position.*;
import java.util.*;
import lombok.*;

public class Team {
    
    @Getter
    private final TeamInfo teamInfo;
    
    @Getter
    private final List<PlayerInfo> players = new ArrayList<>();
    
    private final LineupPosition[] battingOrder = new LineupPosition[10];
    private final PlayerInfo[] substitutes = new PlayerInfo[8];

    public Team(String name) {
        this.teamInfo = new TeamInfo(name);
    }

    public long getId() {
        return teamInfo.getTeamId();
    }

    public String getName() {
        return teamInfo.getTeamName();
    }
    
    public void addPlayer(PlayerInfo player) {
        players.add(player);
    }
    
    public LineupPosition getBatter(int order) {
        return battingOrder[order];
    }
    
    public PlayerInfo getFielder(int order) {
        PlayerInfo ret = null;
        
        LineupPosition pos = battingOrder[order];
        if (pos != null) {
            ret = pos.getPlayer();
        }
        
        return ret;
    }
    
    public PlayerInfo getFielder(Position position) {
        PlayerInfo ret = null;
        
        for (int i = 0; i < 10; i++) {
            LineupPosition pos = battingOrder[i];
            if (pos != null && pos.getPosition() == position) {
                ret = pos.getPlayer();
                break;
            }
        }
        
        return ret;
    }
    
    public void fillPosition(PlayerInfo player, Position position, int order) {
        LineupPosition newPosition = new LineupPosition(player, position);
        battingOrder[order] = newPosition;
    }
    
    public void addSubtitute(PlayerInfo player) {
        int maxSubstitutes = battingOrder[9] == null ? 8 : 7;
        for (int i = 0; i < maxSubstitutes; i++) {
            if (substitutes[i] == null) {
                substitutes[i] = player;
                break;
            }
        }
    }

    public void randomizeLineup() {
        Random rand = new Random();
        boolean useDP = rand.nextBoolean();
        
        List<PlayerInfo> availablePlayers = new ArrayList<>(players);

        var availablePositions = new ArrayList<Position>();
        availablePositions.addAll(Arrays.asList(PITCHER, CATCHER, FIRST_BASE, SECOND_BASE, THIRD_BASE, SHORT_STOP, LEFT_FIELD, CENTER_FIELD, RIGHT_FIELD));
        if (useDP) {
            availablePositions.add(DESIGNATED_PLAYER);
        }
        
        int lineup = useDP ? 10 : 9;
        for (int i = 0; i < lineup; i++) {
            PlayerInfo player = availablePlayers.remove(rand.nextInt(availablePlayers.size()));
            Position position = availablePositions.remove(rand.nextInt(availablePositions.size()));
            fillPosition(player, position, i);
        }
        availablePlayers.forEach(this::addSubtitute);
    }

    public void setLineup(List<LineupPosition> lineup) {
        if (lineup != null) {
            int ord = 0;
            for (LineupPosition row : lineup) {
                PlayerInfo player = row.getPlayer();
                Position position = row.getPosition();
                if (position != null) {
                    fillPosition(player, position, ord++);
                } else {
                    addSubtitute(player);
                }
            }
        }
    }
}
