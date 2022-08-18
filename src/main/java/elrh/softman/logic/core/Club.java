package elrh.softman.logic.core;

import elrh.softman.logic.db.GameDBManager;
import elrh.softman.logic.db.orm.ClubInfo;
import elrh.softman.logic.db.orm.PlayerInfo;
import java.util.HashMap;
import java.util.List;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.interfaces.IDatabaseEntity;
import lombok.Getter;

public class Club implements IDatabaseEntity {

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

    @Override
    public long getId() {
        return clubInfo.getClubId();
    }

    @Override
    public void persist() {
        GameDBManager.getInstance().saveClub(this);
    }

   public List<PlayerInfo> getPlayers() {
        return players.values().stream().toList();
    }

    public List<Team> getTeams() {
        return teams.values().stream().toList();
    }

    public boolean isActive() {
        return getClubInfo().getRegistered() == AssociationManager.getInstance().getClock().getYear();
    }

    public void register(int year) {
        getClubInfo().setRegistered(year);
        persist();
    }

}
