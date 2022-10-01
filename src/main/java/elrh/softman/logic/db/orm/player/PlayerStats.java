package elrh.softman.logic.db.orm.player;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.db.GameDBManager;
import elrh.softman.logic.enums.StatsType;
import elrh.softman.logic.interfaces.IDatabaseEntity;
import lombok.Data;
import lombok.NonNull;

@DatabaseTable(tableName = "softman_player_stats")
@Data
public class PlayerStats implements IDatabaseEntity {

    @DatabaseField(generatedId = true)
    private long statsId;

    @DatabaseField(canBeNull = false)
    @NonNull
    private long matchId = 0;

    @DatabaseField(canBeNull = false)
    @NonNull
    private long playerId = 0;

    @DatabaseField(canBeNull = false)
    @NonNull
    private String playerStr = "";

    // batter
    @DatabaseField(canBeNull = false)
    private int bPA = 0;
    @DatabaseField(canBeNull = false)
    private int bAB = 0;
    @DatabaseField(canBeNull = false)
    private int bR = 0;
    @DatabaseField(canBeNull = false)
    private int bH = 0;
    @DatabaseField(canBeNull = false)
    private int b2B = 0;
    @DatabaseField(canBeNull = false)
    private int b3B = 0;
    @DatabaseField(canBeNull = false)
    private int bHR = 0;
    @DatabaseField(canBeNull = false)
    private int bSH = 0;
    @DatabaseField(canBeNull = false)
    private int bSF = 0;
    @DatabaseField(canBeNull = false)
    private int bBB = 0;
    @DatabaseField(canBeNull = false)
    private int bHP = 0;
    @DatabaseField(canBeNull = false)
    private int bSB = 0;
    @DatabaseField(canBeNull = false)
    private int bCS = 0;
    @DatabaseField(canBeNull = false)
    private int bK = 0;
    @DatabaseField(canBeNull = false)
    private int bRB = 0;

    // fielder
    @DatabaseField(canBeNull = false)
    private int fPO;
    @DatabaseField(canBeNull = false)
    private int fA;
    @DatabaseField(canBeNull = false)
    private int fE;
    @DatabaseField(canBeNull = false)
    private int fDP;
    @DatabaseField(canBeNull = false)
    private int fIP;

    // pitcher
    @DatabaseField(canBeNull = false)
    private int pW;
    @DatabaseField(canBeNull = false)
    private int pL;
    @DatabaseField(canBeNull = false)
    private int pS;
    @DatabaseField(canBeNull = false)
    private int pBF;
    @DatabaseField(canBeNull = false)
    private int pAB;
    @DatabaseField(canBeNull = false)
    private int pR;
    @DatabaseField(canBeNull = false)
    private int pER;
    @DatabaseField(canBeNull = false)
    private int pH;
    @DatabaseField(canBeNull = false)
    private int p2B;
    @DatabaseField(canBeNull = false)
    private int p3B;
    @DatabaseField(canBeNull = false)
    private int pHR;
    @DatabaseField(canBeNull = false)
    private int pSH;
    @DatabaseField(canBeNull = false)
    private int pSF;
    @DatabaseField(canBeNull = false)
    private int pBB;
    @DatabaseField(canBeNull = false)
    private int pHP;
    @DatabaseField(canBeNull = false)
    private int pK;
    @DatabaseField(canBeNull = false)
    private int pWP;
    @DatabaseField(canBeNull = false)
    private int pNP;
    @DatabaseField(canBeNull = false)
    private int pNS;

    // catcher
    @DatabaseField(canBeNull = false)
    private int cPB;
    @DatabaseField(canBeNull = false)
    private int cSB;
    @DatabaseField(canBeNull = false)
    private int cCS;

    
    // helpers
    
    public void init(long matchId, long playerId, String playerStr) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.playerStr = playerStr;
    }
    public void inc(StatsType stat) {
        switch (stat) {
            // batter
            case BPA -> setBPA(getBPA() + 1);
            case BAB -> setBAB(getBAB() + 1);
            case BR  -> setBR(getBR() + 1);
            case BH  -> setBH(getBH() + 1);
            case B2B -> setB2B(getB2B() + 1);
            case B3B -> setB3B(getB3B() + 1);
            case BHR -> setBHR(getBHR() + 1);
            case BSH -> setBSH(getBSH() + 1);
            case BSF -> setBSF(getBSF() + 1);
            case BBB -> setBBB(getBBB() + 1);
            case BHP -> setBHP(getBHP() + 1);
            case BSB -> setBSB(getBSB() + 1);
            case BCS -> setBCS(getBCS() + 1);
            case BK  -> setBK(getBK() + 1);
            case BRB -> setBRB(getBRB() + 1);
            // fielder
            case FPO -> setFPO(getFPO() + 1);
            case FA  -> setFA(getFA() + 1);
            case FE  -> setFE(getFE() + 1);
            case FDP -> setFDP(getFDP() + 1);
            case FIP -> setFIP(getFIP() + 1);
            // pitcher
            case PW  -> setPW(getPW() + 1);
            case PL  -> setPL(getPL() + 1);
            case PS  -> setPS(getPS() + 1);
            case PBF -> setPBF(getPBF() + 1);
            case PAB -> setPAB(getPAB() + 1);
            case PR  -> setPR(getPR() + 1);
            case PER -> setPER(getPER() + 1);
            case PH  -> setPH(getPH() + 1);
            case P2B -> setP2B(getP2B() + 1);
            case P3B -> setP3B(getP3B() + 1);
            case PHR -> setPHR(getPHR() + 1);
            case PSH -> setPSH(getPSH() + 1);
            case PSF -> setPSF(getPSF() + 1);
            case PBB -> setPBB(getPBB() + 1);
            case PHP -> setPHP(getPHP() + 1);
            case PK  -> setPK(getPK() + 1);
            case PWP -> setPWP(getPWP() + 1);
            case PNP -> setPNP(getPNP() + 1);
            case PNS -> setPNS(getPNS() + 1);
            // catcher
            case CPB -> setCPB(getCPB() + 1);
            case CSB -> setCSB(getCSB() + 1);
            case CCS -> setCCS(getCCS() + 1);
        }
    }

    @Override
    public long getId() {
        return getStatsId();
    }

    @Override
    public void persist() {
        GameDBManager.getInstance().saveStatsRecord(this);
    }
}
