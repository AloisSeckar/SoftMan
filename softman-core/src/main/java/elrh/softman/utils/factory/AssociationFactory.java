package elrh.softman.utils.factory;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.*;
import elrh.softman.logic.enums.PlayerLevel;

import java.util.*;

public class AssociationFactory {

    private record ClubSpec(String name, String shortName, String city, String logo, String stadium, String color) {
        Club create() {
            return ClubFactory.getClub(name, shortName, city, logo, stadium, color);
        }
    }

    private static final List<ClubSpec> CLUB_SPECS = List.of(
        new ClubSpec("PALADINS", "PAL", "Dalton", "paladins", "The Arena", "#ADD8E6"),
        new ClubSpec("SMOKIN' COWBOYS", "COW", "Oxnard", "smokin-cowboys", "Smokin' Range", "#800080"),
        new ClubSpec("GREEN MARES", "GMR", "Willburn", "green-mares", "Horse pen", "#228B22"),
        new ClubSpec("KING COBRAS", "CBR", "Turlock", "king-cobras", "The Nest", "#FFFF00"),
        new ClubSpec("MINOTAURS", "MIN", "Cape Horn", "minotaurs", "Labyrinth", "#EE82EE"),
        new ClubSpec("PIRATES", "PIR", "Blackwell", "pirates", "Black Pearl Cove", "#FFA500"),
        new ClubSpec("SPARTANS", "SPA", "Cliff Rose", "spartans", "Sparta Field", "#8B0000"),
        new ClubSpec("STEEL FALCONS", "FAL", "Nampa", "steel-falcons", "Steelwings Stadium", "#FFD700"),
        new ClubSpec("SHARX", "SHX", "Lawton", "sharx", "Sharx Bay", "#000080"),
        new ClubSpec("ASSASSINS", "ASA", "Melstone", "assassins", "Dark House", "#006400"),
        new ClubSpec("RAGING BULLS", "BUL", "Barling", "raging-bulls", "Bull Ridge", "#D3D3D3"),
        new ClubSpec("RAVENS", "RAV", "Andrews", "ravens", "Black Feather Field", "#A9A9A9"),
        new ClubSpec("TOMCATS", "TOM", "Carrizo", "tomcats", "Cat Palace", "#2F4F4F"),
        new ClubSpec("VIKINGS", "VIK", "Gillroy", "vikings", "Valhalla", "#AFEEEE"),
        new ClubSpec("WHITE RABBITS", "RAB", "Drummond", "white-rabbits", "Rabbit Hole", "#008080"),
        new ClubSpec("WIZARDS", "WIZ", "Gallup", "wizards", "The Tower", "#FFF8DC")
    );

    public static void populateAssociation() {
        AssociationManager manager = AssociationManager.getInstance();
        var clubs = CLUB_SPECS.stream().map(ClubSpec::create).toList();
        clubs.forEach(manager::registerClub);

        // create leagues for the initial year
        createLeagues(clubs);

        var userClub = clubs.getFirst();

        // TODO remove
        // to test a "B" team not participating in any league
        userClub.formTeam(PlayerLevel.MSEN);

        // TODO allow players to pick club
        manager.getUser().setActiveClub(userClub);
        manager.getUser().setFocusedClub(userClub);
        manager.getUser().setFocusedTeam(userClub.getTeams().getFirst());
    }

    // this is being called when the association is initialized for the first time
    public static void createLeagues(List<Club> clubs) {
        AssociationManager manager = AssociationManager.getInstance();
        int year = AssociationManager.getInstance().getClock().getYear();

        manager.createNewLeague("1st League Men", PlayerLevel.MSEN, 1);
        // TODO more than one league
        /*
        manager.createNewLeague("2nd League Men", PlayerLevel.MSEN, 2);
        manager.createNewLeague("1st League Women", PlayerLevel.FSEN, 1);
        manager.createNewLeague("2nd League Women", PlayerLevel.FSEN, 2);
        manager.createNewLeague("League Junior Boys", PlayerLevel.MU18, 1);
        manager.createNewLeague("League Junior Girls", PlayerLevel.FU18, 1);
         */

        var leagueMen1 = manager.getLeagues(year).get(0);
        var leagueMenTeams1 = createTeams(PlayerLevel.MSEN, clubs.subList(0, 8));
        leagueMenTeams1.forEach(team -> AssociationManager.getInstance().registerTeamIntoLeague(leagueMen1.getId(), team));
        leagueMen1.scheduleMatches();

        // TODO more than one league
        /*
        var leagueMen2 = manager.getLeagues(year).get(1);
        var leagueMenTeams2 = createTeams(PlayerLevel.MSEN, clubs.subList(8, 16));
        leagueMenTeams2.forEach(team -> AssociationManager.getInstance().registerTeamIntoLeague(leagueMen2.getId(), team));
        leagueMen2.scheduleMatches();

        leagueMen1.getLeagueInfo().setLeagueBelow(leagueMen2.getId());
        leagueMen1.getLeagueInfo().persist();
        leagueMen2.getLeagueInfo().setLeagueAbove(leagueMen1.getId());
        leagueMen2.getLeagueInfo().persist();

        var leagueWomen1 = manager.getLeagues(year).get(2);
        var leagueWomenTeams1 = createTeams(PlayerLevel.FSEN, List.of(clubs.get(0), clubs.get(1), clubs.get(2), clubs.get(3), clubs.get(4), clubs.get(5), clubs.get(8), clubs.get(9)));
        leagueWomenTeams1.forEach(team -> AssociationManager.getInstance().registerTeamIntoLeague(leagueWomen1.getId(), team));
        leagueWomen1.scheduleMatches();

        var leagueWomen2 = manager.getLeagues(year).get(3);
        var leagueWomenTeams2 = createTeams(PlayerLevel.FSEN, List.of(clubs.get(6), clubs.get(7), clubs.get(10), clubs.get(11), clubs.get(12), clubs.get(13), clubs.get(14), clubs.get(15)));
        leagueWomenTeams2.forEach(team -> AssociationManager.getInstance().registerTeamIntoLeague(leagueWomen2.getId(), team));
        leagueWomen2.scheduleMatches();

        leagueWomen1.getLeagueInfo().setLeagueBelow(leagueWomen2.getId());
        leagueWomen1.getLeagueInfo().persist();
        leagueWomen2.getLeagueInfo().setLeagueAbove(leagueWomen1.getId());
        leagueWomen2.getLeagueInfo().persist();

        var leagueJuniorBoys = manager.getLeagues(year).get(4);
        var leagueJuniorBoysTeams = createTeams(PlayerLevel.MU18, List.of(clubs.get(0), clubs.get(1), clubs.get(3), clubs.get(4), clubs.get(5), clubs.get(7)));
        leagueJuniorBoysTeams.forEach(team -> AssociationManager.getInstance().registerTeamIntoLeague(leagueJuniorBoys.getId(), team));
        leagueJuniorBoys.scheduleMatches();

        var leagueJuniorGirls = manager.getLeagues(year).get(5);
        var leagueJuniorGirlsTeams = createTeams(PlayerLevel.FU18, List.of(clubs.get(0), clubs.get(3), clubs.get(4), clubs.get(7), clubs.get(8), clubs.get(9)));
        leagueJuniorGirlsTeams.forEach(team -> AssociationManager.getInstance().registerTeamIntoLeague(leagueJuniorGirls.getId(), team));
        leagueJuniorGirls.scheduleMatches();
         */
    }

    // this is being called at the beginning of every year
    public static void recreateLeagues() {
        AssociationManager manager = AssociationManager.getInstance();
        var year = AssociationManager.getInstance().getClock().getYear();
        var leagues = manager.getLeagues(year - 1);
        leagues.forEach(league -> {
            var info = league.getLeagueInfo();
            var teams = league.getTeams().size();
            var standings = league.getStandings();
            Collections.sort(standings);

            // setup new league
            manager.createNewLeague(info.getLeagueName(), info.getLevel(), info.getTier());
            var newLeague = manager.getLeagues(year).get(manager.getLeagues(year).size() - 1); // TODO direct acess by ID

            // copy teams from previous season
            var newLeagueTeams = new ArrayList<>(league.getTeams());

            // transfer teams to/from league above
            if (info.getLeagueAbove() != null) {
                var advancingTeam1 = manager.getTeamById(standings.get(0).getTeamId());
                newLeagueTeams.remove(advancingTeam1);
                var advancingTeam2 = manager.getTeamById(standings.get(1).getTeamId());
                newLeagueTeams.remove(advancingTeam2);
                var teamsFromAbove = manager.getLeagueById(info.getLeagueAbove()).getRelegatedTeams();
                for (var teamId : teamsFromAbove) {
                    var teamFromAbove = manager.getTeamById(teamId);
                    newLeagueTeams.add(teamFromAbove);
                }
            }
            // transfer teams to/from league below
            if (info.getLeagueBelow() != null) {
                var relegatedTeam1 = manager.getTeamById(standings.get(teams - 1).getTeamId());
                newLeagueTeams.remove(relegatedTeam1);
                var relegatedTeam2 = manager.getTeamById(standings.get(teams - 2).getTeamId());
                newLeagueTeams.remove(relegatedTeam2);
                var teamsFromBelow = manager.getLeagueById(info.getLeagueBelow()).getAdvancingTeams();
                for (var teamId : teamsFromBelow) {
                    var teamFromBelow = manager.getTeamById(teamId);
                    newLeagueTeams.add(teamFromBelow);
                }
            }

            // register teams and create new schedule
            newLeagueTeams.forEach(team -> AssociationManager.getInstance().registerTeamIntoLeague(newLeague.getId(), team));
            newLeague.scheduleMatches();
        });
    }

    private static ArrayList<Team> createTeams(PlayerLevel level, List<Club> participants) {
        ArrayList<Team> ret = new ArrayList<>();
        participants.forEach(club -> {
            var result = club.formTeam(level);
            if (result.ok()) {
                var newTeam = club.getTeamById(UUID.fromString(result.message()));
                ret.add(newTeam);
            } else {
                throw new RuntimeException("Failed to create a team");
            }
        });
        return ret;
    }

}
