package elrh.softman.logic.stats;

import elrh.softman.db.orm.PlayerInfo;

public class Stats {
    
    private final PlayerInfo player;
    
    private int PA;
    private int AB;
    private int H;
    private int R;
    private int RBI;
    
    private int IP;

    public Stats(PlayerInfo player) {
        this.player = player;
    }

    public PlayerInfo getPlayer() {
        return player;
    }

    public int getPA() {
        return PA;
    }

    public void setPA(int PA) {
        this.PA = PA;
    }

    public int getAB() {
        return AB;
    }

    public void setAB(int AB) {
        this.AB = AB;
    }

    public int getH() {
        return H;
    }

    public void setH(int H) {
        this.H = H;
    }

    public int getR() {
        return R;
    }

    public void setR(int R) {
        this.R = R;
    }

    public int getRBI() {
        return RBI;
    }

    public void setRBI(int RBI) {
        this.RBI = RBI;
    }

    public int getIP() {
        return IP;
    }

    public void setIP(int IP) {
        this.IP = IP;
    }
    
}
