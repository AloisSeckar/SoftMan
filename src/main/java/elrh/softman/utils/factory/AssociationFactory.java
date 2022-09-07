package elrh.softman.utils.factory;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.*;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.utils.mock.MockTeamFactory;
import java.util.*;

public class AssociationFactory {

    private static final Club CLUB01 = ClubFactory.getClub("REDS");
    private static final Club CLUB02 = ClubFactory.getClub("BLUES");
    private static final Club CLUB03 = ClubFactory.getClub("GREENS");
    private static final Club CLUB04 = ClubFactory.getClub("YELLOWS");
    private static final Club CLUB05 = ClubFactory.getClub("BLACKS");
    private static final Club CLUB06 = ClubFactory.getClub("WHITES");
    private static final Club CLUB07 = ClubFactory.getClub("SILVERS");
    private static final Club CLUB08 = ClubFactory.getClub("VIOLETS");
    private static final Club CLUB09 = ClubFactory.getClub("BROWNS");
    private static final Club CLUB10 = ClubFactory.getClub("GOLDS");

    public static void populateAssociation() {
        AssociationManager manager = AssociationManager.getInstance();
        int year = AssociationManager.getInstance().getClock().getYear();

        manager.registerClub(CLUB01);
        manager.registerClub(CLUB02);
        manager.registerClub(CLUB03);
        manager.registerClub(CLUB04);
        manager.registerClub(CLUB05);
        manager.registerClub(CLUB06);
        manager.registerClub(CLUB07);
        manager.registerClub(CLUB08);
        manager.registerClub(CLUB09);
        manager.registerClub(CLUB10);

        manager.createNewLeague("1st League Men", PlayerLevel.MSEN);
        manager.createNewLeague("1st League Women", PlayerLevel.FSEN);
        manager.createNewLeague("League Junior Boys", PlayerLevel.MU18);
        manager.createNewLeague("League Junior Girls", PlayerLevel.FU18);

        League leagueMen = manager.getLeagues(year).get(0);
        ArrayList<Team> leagueMenTeams = createTeams(PlayerLevel.MSEN, Arrays.asList(CLUB01, CLUB02, CLUB03, CLUB04, CLUB05, CLUB06, CLUB07, CLUB08));
        leagueMenTeams.forEach(leagueMen::registerTeam);
        leagueMen.scheduleMatches();

        League leagueWomen = manager.getLeagues(year).get(1);
        ArrayList<Team> leagueWomenTeams = createTeams(PlayerLevel.FSEN, Arrays.asList(CLUB01, CLUB02, CLUB03, CLUB04, CLUB05, CLUB06, CLUB09, CLUB10));
        leagueWomenTeams.forEach(leagueWomen::registerTeam);
        leagueWomen.scheduleMatches();

        League leagueJuniorBoys = manager.getLeagues(year).get(2);
        ArrayList<Team> leagueJuniorBoysTeams = createTeams(PlayerLevel.MU18, Arrays.asList(CLUB01, CLUB02, CLUB04, CLUB05, CLUB06, CLUB08));
        leagueJuniorBoysTeams.forEach(leagueJuniorBoys::registerTeam);
        leagueJuniorBoys.scheduleMatches();

        League leagueJuniorGirls = manager.getLeagues(year).get(3);
        ArrayList<Team> leagueJuniorGirlsTeams = createTeams(PlayerLevel.FU18, Arrays.asList(CLUB03, CLUB04, CLUB05, CLUB08, CLUB09, CLUB10));
        leagueJuniorGirlsTeams.forEach(leagueJuniorGirls::registerTeam);
        leagueJuniorGirls.scheduleMatches();

        manager.getUser().setActiveTeam(leagueMen.getTeams().get(0));
    }

    private static ArrayList<Team> createTeams(PlayerLevel level, List<Club> participants) {
        ArrayList<Team> ret = new ArrayList<>();
        participants.forEach(club -> ret.add(MockTeamFactory.getMockTeam(level, club)));
        return ret;
    }

}
