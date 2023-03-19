package elrh.softman.logic.db.orm.player;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.Result;
import elrh.softman.logic.db.AbstractDBEntity;
import elrh.softman.logic.db.GameDBManager;
import elrh.softman.logic.enums.StatsType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@DatabaseTable(tableName = "softman_player_stats")
@Data @EqualsAndHashCode(callSuper=true) @NoArgsConstructor
public class PlayerStats extends AbstractDBEntity {

    @DatabaseField(generatedId = true)
    private long playerStatsId;

    @DatabaseField(canBeNull = false)
    @NonNull
    private long matchId = 0;

    @DatabaseField(canBeNull = false)
    @NonNull
    private String matchStr = "";

    @DatabaseField(canBeNull = false)
    @NonNull
    private long playerId = 0;

    @DatabaseField(canBeNull = false)
    @NonNull
    private String playerStr = "";

    // always "1" for single-game stats
    // sum of "n" for season and career stats
    @DatabaseField(canBeNull = false)
    private int games = 0;

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
    private int fPO = 0;
    @DatabaseField(canBeNull = false)
    private int fA = 0;
    @DatabaseField(canBeNull = false)
    private int fE = 0;
    @DatabaseField(canBeNull = false)
    private int fDP = 0;
    @DatabaseField(canBeNull = false)
    private int fIP = 0;

    // pitcher
    @DatabaseField(canBeNull = false)
    private int pW = 0;
    @DatabaseField(canBeNull = false)
    private int pL = 0;
    @DatabaseField(canBeNull = false)
    private int pS = 0;
    @DatabaseField(canBeNull = false)
    private int pBF = 0;
    @DatabaseField(canBeNull = false)
    private int pAB = 0;
    @DatabaseField(canBeNull = false)
    private int pR = 0;
    @DatabaseField(canBeNull = false)
    private int pER = 0;
    @DatabaseField(canBeNull = false)
    private int pH = 0;
    @DatabaseField(canBeNull = false)
    private int p2B = 0;
    @DatabaseField(canBeNull = false)
    private int p3B = 0;
    @DatabaseField(canBeNull = false)
    private int pHR = 0;
    @DatabaseField(canBeNull = false)
    private int pSH = 0;
    @DatabaseField(canBeNull = false)
    private int pSF = 0;
    @DatabaseField(canBeNull = false)
    private int pBB = 0;
    @DatabaseField(canBeNull = false)
    private int pHP = 0;
    @DatabaseField(canBeNull = false)
    private int pK = 0;
    @DatabaseField(canBeNull = false)
    private int pWP = 0;
    @DatabaseField(canBeNull = false)
    private int pNP = 0;
    @DatabaseField(canBeNull = false)
    private int pNS = 0;

    // catcher
    @DatabaseField(canBeNull = false)
    private int cPB = 0;
    @DatabaseField(canBeNull = false)
    private int cSB = 0;
    @DatabaseField(canBeNull = false)
    private int cCS = 0;

    
    // helpers
    
    public void init(long matchId, String matchStr, long playerId, String playerStr) {
        this.matchId = matchId;
        this.matchStr = matchStr;
        this.playerId = playerId;
        this.playerStr = playerStr;
    }

    public void initFrom(PlayerStats from) {
        this.matchId = from.getMatchId();
        this.matchStr = from.getMatchStr();
        this.playerId = from.getPlayerId();
        this.playerStr = from.getPlayerStr();
    }
    public void inc(StatsType stat) {
        switch (stat) {
            case G -> setGames(getGames() + 1);
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

    private void clear() {
        games = 0;
        bPA = 0;
        bAB = 0;
        bR = 0;
        bH = 0;
        b2B = 0;
        b3B = 0;
        bHR = 0;
        bSH = 0;
        bSF = 0;
        bBB = 0;
        bHP = 0;
        bSB = 0;
        bCS = 0;
        bK = 0;
        bRB = 0;

        fPO = 0;
        fA = 0;
        fE = 0;
        fDP = 0;
        fIP = 0;

        pW = 0;
        pL = 0;
        pS = 0;
        pBF = 0;
        pAB = 0;
        pR = 0;
        pER = 0;
        pH = 0;
        p2B = 0;
        p3B = 0;
        pHR = 0;
        pSH = 0;
        pSF = 0;
        pBB = 0;
        pHP = 0;
        pK = 0;
        pWP = 0;
        pNP = 0;
        pNS = 0;
        
        cPB = 0;
        cSB = 0;
        cCS = 0;
    }
    
    public void include(PlayerStats stats) {
        games += 1;
        bPA += stats.getBPA();
        bAB += stats.getBAB();
        bR += stats.getBR();
        bH += stats.getBH();
        b2B += stats.getB2B();
        b3B += stats.getB3B();
        bHR += stats.getBHR();
        bSH += stats.getBSH();
        bSF += stats.getBSF();
        bBB += stats.getBBB();
        bHP += stats.getBHP();
        bSB += stats.getBSB();
        bCS += stats.getBCS();
        bK += stats.getBK();
        bRB += stats.getBRB();

        fPO += stats.getFPO();
        fA += stats.getFA();
        fE += stats.getFE();
        fDP += stats.getFDP();
        fIP += stats.getFIP();

        pW += stats.getPW();
        pL += stats.getPL();
        pS += stats.getPS();
        pBF += stats.getPBF();
        pAB += stats.getPAB();
        pR += stats.getPR();
        pER += stats.getPER();
        pH += stats.getPH();
        p2B += stats.getP2B();
        p3B += stats.getP3B();
        pHR += stats.getPHR();
        pSH += stats.getPSH();
        pSF += stats.getPSF();
        pBB += stats.getPBB();
        pHP += stats.getPHP();
        pK += stats.getPK();
        pWP += stats.getPWP();
        pNP += stats.getPNP();
        pNS += stats.getPNS();

        cPB += stats.getCPB();
        cSB += stats.getCSB();
        cCS += stats.getCCS();
    }

    @Override
    public long getId() {
        return getPlayerStatsId();
    }

    @Override
    public Result persist() {
        return GameDBManager.getInstance().saveObject(PlayerStats.class, this);
    }

}
