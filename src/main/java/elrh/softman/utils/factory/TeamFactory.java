package elrh.softman.utils.factory;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Player;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.db.GameDBManager;
import elrh.softman.logic.enums.PlayerLevel;
import elrh.softman.utils.Constants;

import java.util.ArrayList;
import java.util.Random;

public class TeamFactory {

    public static Team getTeam(PlayerLevel level, String name, Club club) {

        var team = new Team(level, name, club);
        team.persist();

        var rand = new Random();
        var usedNumbers = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            int number;
            do {
                number = rand.nextInt(99) + 1;
            } while (usedNumbers.contains(number));
            usedNumbers.add(number);
            team.addPlayer(PlayerFactory.getRandomPlayerInfo(level.getGender(), getRandomBirth(level), number));
        }

        GameDBManager.getInstance().savePlayers(team.getPlayers());

        // TODO get rid of this and revamp whole Player's logic
        team.getPlayers().forEach(info -> {
            var player = new Player();
            player.setPlayerInfo(info);
            AssociationManager.getInstance().registerPlayer(player);
        });

        team.randomizeLineup();

        return team;
    }

    private static int getRandomBirth(PlayerLevel level) {
        if (level == PlayerLevel.MSEN || level == PlayerLevel.FSEN) {
            return Constants.START_YEAR - 16 - new Random().nextInt(25);
        } else {
            return Constants.START_YEAR - 12 - new Random().nextInt(6);
        }
    }

}
