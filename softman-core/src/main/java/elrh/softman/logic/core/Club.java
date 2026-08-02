package elrh.softman.logic.core;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;
import elrh.softman.logic.core.data.ClubInfo;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.utils.Constants;
import elrh.softman.utils.ErrorUtils;
import elrh.softman.utils.factory.TeamFactory;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Club {

    @Getter
    private final ClubInfo clubInfo;

    // LinkedHashMap: UUID keys have no natural order, insertion order must be kept stable
    private final Map<UUID, Team> teams = new LinkedHashMap<>();

    public Club(String name, String shortName, String city, String stadium) {
        clubInfo = new ClubInfo();
        clubInfo.setName(name);
        clubInfo.setShortName(shortName);
        clubInfo.setCity(city);
        clubInfo.setStadium(stadium);
        clubInfo.setMoney(Constants.START_FUNDS);
        clubInfo.setLogo("softman.jpg");
    }

    public Club(ClubInfo clubInfo) {
        this.clubInfo = clubInfo;
    }

    public String getColor() {
        return clubInfo.getColor();
    }

    public void setColor(String color) {
        clubInfo.setColor(color);
    }

    @Override
    public String toString() {
        return clubInfo.getName();
    }

    public UUID getId() {
        return clubInfo.getClubId();
    }

    public List<Team> getTeams() {
        return teams.values().stream().toList();
    }
    public List<UUID> getTeamIds() {
        return teams.values().stream().map(Team::getId).toList();
    }

    public Team getTeamById(UUID teamId) {
        return teams.get(teamId);
    }

    // used when reassembling a loaded world
    public void restoreTeam(Team team) {
        teams.put(team.getId(), team);
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
            return new Result(true, String.valueOf(newTeam.getId()));
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
