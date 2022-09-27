package elrh.softman.logic.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StatsType {

    // batter
    BPA("bPA"),
    BAB("bAB"),
    BR("bR"),
    BH("bH"),
    B2B("b2B"),
    B3B("b3B"),
    BHR("bHR"),
    BSH("bSH"),
    BSF("bSF"),
    BBB("bBB"),
    BHP("bHP"),
    BSB("bSB"),
    BCS("bCS"),
    BK("bK"),
    BRB("bRB"),

    // fielder
    FPO("fPO"),
    FA("fA"),
    FE("fE"),
    FDP("fDP"),
    FIP("fIP"),

    // pitcher
    PW("pW"),
    PL("pL"),
    PS("pS"),
    PBF("pBF"),
    PAB("pAB"),
    PR("pR"),
    PER("pER"),
    PH("pH"),
    P2B("p2B"),
    P3B("p3B"),
    PHR("pHR"),
    PSH("pSH"),
    PSF("pSF"),
    PBB("pBB"),
    PHP("pHP"),
    PK("pK"),
    PWP("pWP"),
    PNP("pNP"),
    PNS("pNS"),

    // catcher
    CPB("cPB"),
    CSB("cSB"),
    CCS("cCS");

    // possible future additions
    // B: GDP, IBB, IO, RISP; P: IBB, IO, IP, GS, CG, Hold, BS; etc.?

    private final String code;

    @Override
    public String toString() {
        return code;
    }
    
}
