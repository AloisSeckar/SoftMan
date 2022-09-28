package elrh.softman.utils;

import elrh.softman.logic.core.lineup.Lineup;
import elrh.softman.logic.core.lineup.PlayerRecord;
import static elrh.softman.logic.enums.StatsType.*;

public class StatsUtils {

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

    public static void incIP(Lineup lineup) {
        if (lineup != null) {
            var fielders = lineup.getCurrentFielders();
            if (fielders != null) {
                for (int i = 0; i < fielders.length; i++) {
                    fielders[i].getStats().inc(FIP);
                }
            }
        }
    }

}
