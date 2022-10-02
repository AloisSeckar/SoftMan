package elrh.softman.logic.core;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;
import elrh.softman.logic.db.orm.ClubInfo;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.utils.Constants;
import elrh.softman.utils.ErrorUtils;
import elrh.softman.utils.factory.TeamFactory;
import java.util.HashMap;
import java.util.List;
import javafx.scene.paint.Color;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Club {

    @Getter
    private final ClubInfo clubInfo;

    @Getter @Setter
    private Color color; // TODO how to persist and represent in DB?

    private final HashMap<Long, Team> teams = new HashMap<>();

    public Club(String name, String shortName,String city, String stadium) {
        clubInfo = new ClubInfo();
        clubInfo.setName(name);
        clubInfo.setShortName(shortName);
        clubInfo.setCity(city);
        clubInfo.setStadium(stadium);
        clubInfo.setMoney(Constants.START_FUNDS);
        clubInfo.setLogo("softman.jpg");
    }

    @Override
    public String toString() {
        return clubInfo.getName();
    }

    public long getId() {
        return clubInfo.getClubId();
    }

    public List<Team> getTeams() {
        return teams.values().stream().toList();
    }
    public List<Long> getTeamIds() {
        return teams.values().stream().map(Team::getId).toList();
    }

    public Team getTeamById(long teamId) {
        return teams.get(teamId);
    }

    public boolean isActive() {
        return getClubInfo().getRegistered() == AssociationManager.getInstance().getClock().getYear();
    }

    public Result formTeam(PlayerLevel level) {
        try {
            var existingTeams = teams.values().stream().filter(t -> t.getTeamInfo().getLevel() == level).count();
            var squad = getSquadCode(existingTeams);
            var name = clubInfo.getName() + " " + level.getCode() + " " + squad;

            var newTeam = TeamFactory.getTeam(level, name, this);
            teams.put(newTeam.getId(), newTeam);

            LOG.info("New team " + newTeam.getId() + "('" + name + "') was formed");
            return Constants.RESULT_OK;
        } catch (Exception ex) {
            return ErrorUtils.handleException("Club.formTeam", ex);
        }
    }

    private String getSquadCode(long existingTeams) throws ErrorUtils.ReportedException {
        String squad;
        if (existingTeams == 0) {
            squad = "A";
        } else if (existingTeams == 1) {
            squad = "B";
        } else if (existingTeams == 2) {
            squad = "C";
        } else {
            throw new ErrorUtils.ReportedException("Max 3 teams allowed for each level");
        }
        return squad;
    }
}
