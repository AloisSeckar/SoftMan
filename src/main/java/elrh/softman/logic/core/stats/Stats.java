package elrh.softman.logic.core.stats;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class Stats {
    
    private int PA;
    private int AB;
    private int H;
    private int R;
    private int RBI;

    private int O;
    @Getter(AccessLevel.NONE)
    private int IP;

    public void incPA() {
        PA++;
    }

    public void incAB() {
        AB++;
    }

    public void incH() {
        H++;
    }

    public void incR() {
        R++;
    }

    public void incRBI() {
        RBI++;
    }

    public void incO() {
        O++;
    }

    public void incIP() {
        IP++;
    }

    public String getAVG() {
        return AB > 0 ? String.format("%.3f", (float) H / AB) : "0,000";
    }

    public String getIP() {
        return (IP / 3) + "." + (IP % 3);
    }
    
}
