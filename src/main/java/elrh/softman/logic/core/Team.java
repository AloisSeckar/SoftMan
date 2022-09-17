package elrh.softman.logic.core;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;
import elrh.softman.logic.core.lineup.Lineup;
import elrh.softman.logic.db.GameDBManager;
import static elrh.softman.logic.enums.PlayerPosition.*;
import java.util.*;
import elrh.softman.logic.core.lineup.PlayerRecord;
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

    @Getter
    private Lineup defaultLineup;

    public Team(PlayerLevel level, String name, Club club) {
        this.teamInfo = new TeamInfo(level, name, club.getClubInfo());
        persist();
        defaultLineup = new Lineup(getId(), getName(), getLogo());
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

    public League getCurrentLeague() {
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

    public PlayerRecord getBatter(int order) {
        return defaultLineup.getCurrentBatter(order);
    }
    
    public PlayerInfo getFielder(int order) {
        PlayerInfo ret = null;
        
        var pos = getBatter(order);
        if (pos != null) {
            ret = pos.getPlayer();
        }
        
        return ret;
    }
    
    public PlayerInfo getFielder(PlayerPosition position) {
        PlayerInfo ret = null;

        // TODO create a method to ask Lineup directly
        for (int i = 0; i < 10; i++) {
            var pos = getBatter(i);
            if (pos != null && pos.getPosition() == position) {
                ret = pos.getPlayer();
                break;
            }
        }
        
        return ret;
    }

    public void randomizeLineup() {
        var rand = new Random();
        var useDP = rand.nextBoolean();
        
        var availablePlayers = new ArrayList<>(players);

        var availablePositions = new ArrayList<PlayerPosition>();
        availablePositions.addAll(Arrays.asList(PITCHER, CATCHER, FIRST_BASE, SECOND_BASE, THIRD_BASE, SHORT_STOP, LEFT_FIELD, CENTER_FIELD, RIGHT_FIELD));
        if (useDP) {
            availablePositions.add(DESIGNATED_PLAYER);
        }
        
        int maxBatters = useDP ? Lineup.POSITION_PLAYERS : Lineup.POSITION_PLAYERS - 1;
        for (int i = 1; i <= maxBatters; i++) {
            var player = availablePlayers.remove(rand.nextInt(availablePlayers.size()));
            var position = availablePositions.remove(rand.nextInt(availablePositions.size()));
            defaultLineup.initPositionPlayer(i, new PlayerRecord(player, position));
        }

        for (int i = 1; i <= Lineup.SUBSTITUTES || i < availablePlayers.size(); i++) {
            if (i < Lineup.SUBSTITUTES || !useDP) {
                var player = availablePlayers.remove(rand.nextInt(availablePlayers.size()));
                defaultLineup.initSubstitute(i, new PlayerRecord(player, null));
            }
        }
    }

    public void setLineup(Lineup lineup) {
        if (lineup != null) {
            this.defaultLineup = lineup;
        } else {
            ErrorUtils.raise("Default team lineup can't be NULL");
        }
    }
}
