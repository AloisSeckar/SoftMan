package elrh.softman.utils;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.lineup.Lineup;
import elrh.softman.logic.core.lineup.PlayerRecord;
import elrh.softman.logic.enums.PlayerPosition;
import elrh.softman.logic.enums.StatsType;
import static elrh.softman.logic.enums.StatsType.*;
import java.util.Random;

public class StatsUtils {

    public static void saveStatsToPlayers(Lineup lineup) {
        for (int i = 0; i < Lineup.POSITION_PLAYERS; i++) {
            var current = lineup.getPositionPlayers()[i];
            if (Utils.listNotEmpty(current)) {
                current.forEach(data -> {
                    // TODO this looks quite inefficient...
                    var player = AssociationManager.getInstance().getPlayerById(data.getPlayer().getPlayerId());
                    player.getStats().add(data.getStats().getStatsRecord());
                });
            }
        }
    }

    public static void incAB(PlayerRecord batter) {
        if (batter != null) {
            batter.getStats().inc(BPA);
            batter.getStats().inc(BAB);
        }
    }

    public static void incH(PlayerRecord batter) {
        if (batter != null) {
            incAB(batter);
            batter.getStats().inc(BH);
        }
    }

    public static void incK(PlayerRecord batter) {
        if (batter != null) {
            incAB(batter);
            batter.getStats().inc(BK);
        }
    }

    public static void incIP(Lineup lineup) {
        if (lineup != null) {
            var fielders = lineup.getCurrentFielders();
            if (fielders != null) {
                for (PlayerRecord fielder : fielders) {
                    fielder.getStats().inc(FIP);
                }
            }
        }
    }

    public static void incFielding(Lineup fieldingLineup, PlayerPosition pos, StatsType stat) {
        var fielder = fieldingLineup.getCurrentPositionPlayer(pos);
        if (fielder != null) {
            fielder.getStats().inc(stat);
        }
    }

    public static void handleFinishedBF(PlayerRecord pitcher, boolean isAB, int minBalls, int minStrikes) {
        if (pitcher != null) {
            pitcher.getStats().inc(PBF);
            if (isAB) {
                pitcher.getStats().inc(PAB);
            }
            var random = new Random();
            var pitches = Math.min(minBalls + minStrikes + random.nextInt(8), 1);
            var balls = minBalls + random.nextInt(Math.max(4 - minBalls, 1));
            incNP(pitcher, pitches, pitches - balls);
        }
    }

    private static void incNP(PlayerRecord pitcher, int pitches, int strikes) {
        for (int i = 0; i < pitches; i++) {
            pitcher.getStats().inc(PNP);
        }
        for (int i = 0; i < strikes; i++) {
            pitcher.getStats().inc(PNS);
        }
    }
}
