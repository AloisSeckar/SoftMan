package elrh.softman.utils;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Lineup;
import elrh.softman.logic.db.orm.player.PlayerRecord;
import elrh.softman.logic.enums.PlayerPosition;
import elrh.softman.logic.enums.StatsType;
import static elrh.softman.logic.enums.StatsType.*;
import java.util.Random;

public class StatsUtils {

    public static void saveStats(Lineup lineup) {
        for (int i = 0; i < Lineup.POSITION_PLAYERS; i++) {
            var current = lineup.getPositionPlayers()[i];
            if (Utils.listNotEmpty(current)) {
                current.forEach(data -> {
                    var stats = data.getStats();
                    stats.persist();
                    // TODO this looks quite inefficient...
                    var player = AssociationManager.getInstance().getPlayerById(data.getPlayer().getPlayerId());
                    player.getStats().add(stats);
                    player.getSeasonTotal().include(stats);
                });
            }
        }
    }

    public static String getAVG(int bAB, int bH) {
        return bAB > 0 ? String.format("%.3f", (float) bH / bAB) : "0,000";
    }

    public static String getSLG(int bAB, int bH, int b2B, int b3B, int bHR) {
        if (bAB > 0) {
            var totalBases = bH + b2B + b3B + bHR;
            return String.format("%.3f", (float) totalBases / bAB);
        } else {
            return "0,000";
        }
    }

    public static String getERA(int pER) {
        return String.format("%.3f", (float) pER / Constants.INNINGS);
    }

    public static String getIP(int fIP) {
        return (fIP / 3) + "." + (fIP % 3);
    }

    public static String getFLD(int fPO, int fA, int fE) {
        var chances = fPO + fA + fE;
        return  chances > 0 ? String.format("%.3f", 1 - ((float) fE / chances)) : "0,000";
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
