package elrh.softman.logic;

import elrh.softman.db.orm.ClubInfo;
import elrh.softman.db.orm.PlayerInfo;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;

public class Club {

    @Getter
    private final ClubInfo clubInfo;

    private HashMap<Long, PlayerInfo> players = new HashMap<>();
    private HashMap<Long, Team> teams = new HashMap<>();

    public Club(String name, String city, String stadium) {
        clubInfo = new ClubInfo();
        clubInfo.setName(name);
        clubInfo.setCity(city);
        clubInfo.setStadium(stadium);
    }

    public long getClubId() {
        return clubInfo.getClubId();
    }

    public List<PlayerInfo> getPlayers() {
        return players.values().stream().toList();
    }

    public List<Team> getTeams() {
        return teams.values().stream().toList();
    }

    public boolean isActive() {
        // TODO  impl
        return true;
    }
}
