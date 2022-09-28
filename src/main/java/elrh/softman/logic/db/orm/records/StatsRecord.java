package elrh.softman.logic.db.orm.records;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.NonNull;

@DatabaseTable(tableName = "softman_stats")
@Data
public class StatsRecord {

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
    private String playerPos = "";

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

}
