package elrh.softman.utils.factory;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.*;
import elrh.softman.logic.enums.PlayerLevel;
import javafx.scene.paint.Color;

import java.util.*;

public class AssociationFactory {

    private static final Club CLUB01 = ClubFactory.getClub("PALADINS", "PAL", "paladins", Color.LIGHTBLUE);
    private static final Club CLUB02 = ClubFactory.getClub("SMOKIN' COWBOYS", "COW", "smokin-cowboys", Color.PURPLE);
    private static final Club CLUB03 = ClubFactory.getClub("GREEN MARES", "GMR", "green-mares", Color.FORESTGREEN);
    private static final Club CLUB04 = ClubFactory.getClub("KING COBRAS", "CBR", "king-cobras", Color.YELLOW);
    private static final Club CLUB05 = ClubFactory.getClub("MINOTAURS", "MIN", "minotaurs", Color.VIOLET);
    private static final Club CLUB06 = ClubFactory.getClub("PIRATES", "PIR", "pirates", Color.ORANGE);
    private static final Club CLUB07 = ClubFactory.getClub("SPARTANS", "SPA", "spartans", Color.DARKRED);
    private static final Club CLUB08 = ClubFactory.getClub("STEEL FALCONS", "FAL", "steel-falcons", Color.GOLD);
    private static final Club CLUB09 = ClubFactory.getClub("SHARX", "SHX", "sharx", Color.NAVY);
    private static final Club CLUB10 = ClubFactory.getClub("ASSASSINS", "ASA", "assassins", Color.DARKGREEN);
    private static final Club CLUB11 = ClubFactory.getClub("RAGING BULLS", "BUL", "raging-bulls", Color.LIGHTGRAY);
    private static final Club CLUB12 = ClubFactory.getClub("RAVENS", "RAV", "ravens", Color.DARKGRAY);
    private static final Club CLUB13 = ClubFactory.getClub("TOMCATS", "TOM", "tomcats", Color.DARKSLATEGRAY);
    private static final Club CLUB14 = ClubFactory.getClub("VIKINGS", "VIK", "vikings", Color.PALETURQUOISE);
    private static final Club CLUB15 = ClubFactory.getClub("WHITE RABBITS", "RAB", "white-rabbits", Color.TEAL);
    private static final Club CLUB16 = ClubFactory.getClub("WIZARDS", "WIZ", "wizards", Color.CORNSILK);

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
        manager.registerClub(CLUB11);
        manager.registerClub(CLUB12);
        manager.registerClub(CLUB13);
        manager.registerClub(CLUB14);
        manager.registerClub(CLUB15);
        manager.registerClub(CLUB16);

        manager.createNewLeague("1st League Men", PlayerLevel.MSEN, 1);
        manager.createNewLeague("2nd League Men", PlayerLevel.MSEN, 2);
        manager.createNewLeague("1st League Women", PlayerLevel.FSEN, 1);
        manager.createNewLeague("2nd League Women", PlayerLevel.FSEN, 2);
        manager.createNewLeague("League Junior Boys", PlayerLevel.MU18, 1);
        manager.createNewLeague("League Junior Girls", PlayerLevel.FU18, 1);

        var leagueMen1 = manager.getLeagues(year).get(0);
        var leagueMenTeams1 = createTeams(PlayerLevel.MSEN, Arrays.asList(CLUB01, CLUB02, CLUB03, CLUB04, CLUB05, CLUB06, CLUB07, CLUB08));
        leagueMenTeams1.forEach(team -> AssociationManager.getInstance().registerTeamIntoLeague(leagueMen1.getId(), team));
        leagueMen1.scheduleMatches();

        var leagueMen2 = manager.getLeagues(year).get(1);
        var leagueMenTeams2 = createTeams(PlayerLevel.MSEN, Arrays.asList(CLUB09, CLUB10, CLUB11, CLUB12, CLUB13, CLUB14, CLUB15, CLUB16));
        leagueMenTeams2.forEach(team -> AssociationManager.getInstance().registerTeamIntoLeague(leagueMen2.getId(), team));
        leagueMen2.scheduleMatches();

        var leagueWomen1 = manager.getLeagues(year).get(2);
        var leagueWomenTeams1 = createTeams(PlayerLevel.FSEN, Arrays.asList(CLUB01, CLUB02, CLUB03, CLUB04, CLUB05, CLUB06, CLUB09, CLUB10));
        leagueWomenTeams1.forEach(team -> AssociationManager.getInstance().registerTeamIntoLeague(leagueWomen1.getId(), team));
        leagueWomen1.scheduleMatches();

        var leagueWomen2 = manager.getLeagues(year).get(3);
        var leagueWomenTeams2 = createTeams(PlayerLevel.FSEN, Arrays.asList(CLUB07, CLUB08, CLUB11, CLUB12, CLUB13, CLUB14, CLUB15, CLUB16));
        leagueWomenTeams2.forEach(team -> AssociationManager.getInstance().registerTeamIntoLeague(leagueWomen2.getId(), team));
        leagueWomen2.scheduleMatches();

        var leagueJuniorBoys = manager.getLeagues(year).get(4);
        var leagueJuniorBoysTeams = createTeams(PlayerLevel.MU18, Arrays.asList(CLUB01, CLUB02, CLUB04, CLUB05, CLUB06, CLUB08));
        leagueJuniorBoysTeams.forEach(team -> AssociationManager.getInstance().registerTeamIntoLeague(leagueJuniorBoys.getId(), team));
        leagueJuniorBoys.scheduleMatches();

        var leagueJuniorGirls = manager.getLeagues(year).get(5);
        var leagueJuniorGirlsTeams = createTeams(PlayerLevel.FU18, Arrays.asList(CLUB01, CLUB04, CLUB05, CLUB08, CLUB09, CLUB10));
        leagueJuniorGirlsTeams.forEach(team -> AssociationManager.getInstance().registerTeamIntoLeague(leagueJuniorGirls.getId(), team));
        leagueJuniorGirls.scheduleMatches();

        CLUB01.formTeam(PlayerLevel.MSEN); // to test a "C" team not participating in any league

        manager.getUser().setActiveClub(CLUB01);
        manager.getUser().setFocusedClub(CLUB01);
        manager.getUser().setFocusedTeam(CLUB01.getTeams().get(0));
    }

    private static ArrayList<Team> createTeams(PlayerLevel level, List<Club> participants) {
        ArrayList<Team> ret = new ArrayList<>();
        participants.forEach(club -> {
            club.formTeam(level);
            var newTeam = club.getTeams().get(club.getTeams().size() - 1);
            ret.add(newTeam);
        });
        return ret;
    }

}
