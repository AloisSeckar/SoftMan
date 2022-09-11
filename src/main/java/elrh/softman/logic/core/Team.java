package elrh.softman.logic.core;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;
import elrh.softman.logic.db.GameDBManager;
import static elrh.softman.logic.enums.PlayerPosition.*;
import java.util.*;
import elrh.softman.logic.core.lineup.LineupPosition;
import elrh.softman.logic.db.orm.PlayerInfo;
import elrh.softman.logic.db.orm.TeamInfo;
import elrh.softman.logic.enums.PlayerGender;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.logic.enums.PlayerPosition;
import elrh.softman.logic.interfaces.IDatabaseEntity;
import elrh.softman.utils.Constants;
import elrh.softman.utils.ErrorUtils;
import lombok.*;

public class Team implements IDatabaseEntity {
    
    @Getter
    private final TeamInfo teamInfo;
    
    @Getter
    private final List<PlayerInfo> players = new ArrayList<>();

    // TODO this should be changed to "default lineup for (next) game"
    private final LineupPosition[] battingOrder = new LineupPosition[10];
    private final PlayerInfo[] substitutes = new PlayerInfo[8];

    public Team(PlayerLevel level, String name, Club club) {
        this.teamInfo = new TeamInfo(level, name, club.getClubInfo());
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public long getId() {
        return teamInfo.getTeamId();
    }

    @Override
    public void persist() {
        GameDBManager.getInstance().saveTeam(this);
    }

    public String getName() {
        return teamInfo.getName();
    }

    public String getLogo() {
        return teamInfo.getClubInfo().getLogo();
    }

    public League getLeague() {
        if (teamInfo.getLeagueInfo() != null) {
            return AssociationManager.getInstance().getLeagueById(teamInfo.getLeagueInfo().getLeagueId());
        } else {
            return null;
        }
    }
    
    public Result addPlayer(PlayerInfo player) {
        try {
            if (eligibleByGender(player.getGender()) && eligibleByAge(player.getAge())) {
                players.add(player);
                return Constants.RESULT_OK;
            } else {
                return new Result(false, "Player is not eligible");
            }
        } catch (Exception ex) {
            return ErrorUtils.handleException("League.saveMatch", ex);
        }
    }

    private boolean eligibleByAge(int age) {
        int ageLimit = teamInfo.getLevel().getAgeLimit();
        if (ageLimit > 0) {
            return age <= ageLimit;
        } else {
            return age >= 16;
        }
    }

    private boolean eligibleByGender(PlayerGender gender) {
        return gender == teamInfo.getLevel().getGender();
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
    
    public PlayerInfo getFielder(PlayerPosition position) {
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
    
    public void fillPosition(PlayerInfo player, PlayerPosition position, int order) {
        LineupPosition newPosition = new LineupPosition(order, player, position);
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

        var availablePositions = new ArrayList<PlayerPosition>();
        availablePositions.addAll(Arrays.asList(PITCHER, CATCHER, FIRST_BASE, SECOND_BASE, THIRD_BASE, SHORT_STOP, LEFT_FIELD, CENTER_FIELD, RIGHT_FIELD));
        if (useDP) {
            availablePositions.add(DESIGNATED_PLAYER);
        }
        
        int lineup = useDP ? 10 : 9;
        for (int i = 0; i < lineup; i++) {
            PlayerInfo player = availablePlayers.remove(rand.nextInt(availablePlayers.size()));
            PlayerPosition position = availablePositions.remove(rand.nextInt(availablePositions.size()));
            fillPosition(player, position, i);
        }
        availablePlayers.forEach(this::addSubtitute);
    }

    public void setLineup(List<LineupPosition> lineup) {
        if (lineup != null) {
            int ord = 0;
            for (LineupPosition row : lineup) {
                PlayerInfo player = row.getPlayer();
                PlayerPosition position = row.getPosition();
                if (position != null) {
                    fillPosition(player, position, ord++);
                } else {
                    addSubtitute(player);
                }
            }
        }
    }
}
