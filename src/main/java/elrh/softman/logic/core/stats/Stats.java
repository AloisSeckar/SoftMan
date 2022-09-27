package elrh.softman.logic.core.stats;

import elrh.softman.logic.db.orm.records.StatsRecord;
import elrh.softman.logic.enums.PlayerPosition;
import elrh.softman.logic.enums.StatsType;
import elrh.softman.utils.Constants;
import lombok.Data;

@Data
public class Stats {

    private StatsRecord statsRecord;

    public Stats() {
        // TODO need to init matchId, playerId and position at stats object properly to store into DB
        statsRecord = new StatsRecord(0, 0, "");
    }

    public void inc(StatsType stat) {
        switch (stat) {
            // batter
            case BPA -> statsRecord.setBPA(statsRecord.getBPA() + 1);
            case BAB -> statsRecord.setBAB(statsRecord.getBAB() + 1);
            case BR  -> statsRecord.setBR(statsRecord.getBR() + 1);
            case BH  -> statsRecord.setBH(statsRecord.getBH() + 1);
            case B2B -> statsRecord.setB2B(statsRecord.getB2B() + 1);
            case B3B -> statsRecord.setB3B(statsRecord.getB3B() + 1);
            case BHR -> statsRecord.setBHR(statsRecord.getBHR() + 1);
            case BSH -> statsRecord.setBSH(statsRecord.getBSH() + 1);
            case BSF -> statsRecord.setBSF(statsRecord.getBSF() + 1);
            case BBB -> statsRecord.setBBB(statsRecord.getBBB() + 1);
            case BHP -> statsRecord.setBHP(statsRecord.getBHP() + 1);
            case BSB -> statsRecord.setBSB(statsRecord.getBSB() + 1);
            case BCS -> statsRecord.setBCS(statsRecord.getBCS() + 1);
            case BK  -> statsRecord.setBK(statsRecord.getBK() + 1);
            case BRB -> statsRecord.setBRB(statsRecord.getBRB() + 1);
            // fielder
            case FPO -> statsRecord.setFPO(statsRecord.getFPO() + 1);
            case FA  -> statsRecord.setFA(statsRecord.getFA() + 1);
            case FE  -> statsRecord.setFE(statsRecord.getFE() + 1);
            case FDP -> statsRecord.setFDP(statsRecord.getFDP() + 1);
            case FIP -> statsRecord.setFIP(statsRecord.getFIP() + 1);
            // pitcher
            case PW  -> statsRecord.setPW(statsRecord.getPW() + 1);
            case PL  -> statsRecord.setPL(statsRecord.getPL() + 1);
            case PS  -> statsRecord.setPS(statsRecord.getPS() + 1);
            case PBF -> statsRecord.setPBF(statsRecord.getPBF() + 1);
            case PAB -> statsRecord.setPAB(statsRecord.getPAB() + 1);
            case PR  -> statsRecord.setPR(statsRecord.getPR() + 1);
            case PER -> statsRecord.setPER(statsRecord.getPER() + 1);
            case PH  -> statsRecord.setPH(statsRecord.getPH() + 1);
            case P2B -> statsRecord.setP2B(statsRecord.getP2B() + 1);
            case P3B -> statsRecord.setP3B(statsRecord.getP3B() + 1);
            case PHR -> statsRecord.setPHR(statsRecord.getPHR() + 1);
            case PSH -> statsRecord.setPSH(statsRecord.getPSH() + 1);
            case PSF -> statsRecord.setPSF(statsRecord.getPSF() + 1);
            case PBB -> statsRecord.setPBB(statsRecord.getPBB() + 1);
            case PHP -> statsRecord.setPHP(statsRecord.getPHP() + 1);
            case PK  -> statsRecord.setPK(statsRecord.getPK() + 1);
            case PWP -> statsRecord.setPWP(statsRecord.getPWP() + 1);
            case PNP -> statsRecord.setPNP(statsRecord.getPNP() + 1);
            case PNS -> statsRecord.setPNS(statsRecord.getPNS() + 1);
            // catcher
            case CPB -> statsRecord.setCPB(statsRecord.getCPB() + 1);
            case CSB -> statsRecord.setCSB(statsRecord.getCSB() + 1);
            case CCS -> statsRecord.setCCS(statsRecord.getCCS() + 1);
        }
    }

    // computed stats

    public String getAVG() {
        return statsRecord.getBAB() > 0 ? String.format("%.3f", (float) statsRecord.getBH() / statsRecord.getBAB()) : "0,000";
    }

    public String getSLG() {
        if (statsRecord.getBAB() > 0) {
            var totalBases = statsRecord.getBH() + statsRecord.getB2B() + statsRecord.getB3B() + statsRecord.getBHR();
            String.format("%.3f", (float) totalBases / statsRecord.getBAB());
        } else {
           return "0,000";
        }
        return statsRecord.getBAB() > 0 ? String.format("%.3f", (float) statsRecord.getBH() / statsRecord.getBAB()) : "0,000";
    }

    public String getERA() {
        return String.format("%.3f", (float) statsRecord.getPER() / Constants.INNINGS);
    }

    public String getIP() {
        return (statsRecord.getFIP() / 3) + "." + (statsRecord.getFIP() % 3);
    }

    public String getFLD() {
        var chances = statsRecord.getFPO() + statsRecord.getFA() + statsRecord.getFE();
        return  chances > 0 ? String.format("%.3f", 1 - ((float) statsRecord.getFE() / chances)) : "0,000";
    }
    
}
