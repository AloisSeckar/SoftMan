package elrh.softman.utils;

import elrh.softman.logic.enums.PlayerPosition;

import java.util.Random;

public class SimUtils {

    public static final String O_P = "ball in play";
    public static final String O_W = "walk/hp";
    public static final String O_K = "strike out";

    public static final String P_H = "hit";
    public static final String P_F = "field action";

    public static final String F_O = "out";
    public static final String F_E = "error";

    private static final Random random = new Random();

    public static String getPseudoRandomPitchOutcome(int qualityFactor) {
        var rand = random.nextInt();
        if (qualityFactor < -100) {
            return O_K;
        } else if (qualityFactor < -50) {
            if (rand % 10 == 0) {
                return O_W;
            } else if (rand % 5 == 0) {
                return O_P;
            } else {
                return O_K;
            }
        } else if (qualityFactor < -20) {
            if (rand % 5 == 0) {
                return O_W;
            } else if (rand % 3 == 0) {
                return O_K;
            } else {
                return O_P;
            }
        } else if (qualityFactor < 20) {
            if (rand % 4 == 0) {
                return O_K;
            } else if (rand % 4 == 1) {
                return O_W;
            } else {
                return O_P;
            }
        } else if (qualityFactor < 50) {
            if (rand % 5 == 0) {
                return O_K;
            } else if (rand % 3 == 0) {
                return O_W;
            } else {
                return O_P;
            }
        } else if (qualityFactor < 100) {
            if (rand % 10 == 0) {
                return O_K;
            } else if (rand % 3 == 0) {
                return O_W;
            } else {
                return O_P;
            }
        } else {
            return rand % 3 == 0 ? O_W : O_P;
        }
    }

    public static String getPseudoRandomPlayOutcome(int qualityFactor) {
        var rand = random.nextInt();
        if (qualityFactor < -50) {
            return rand % 5 == 0 ? P_H : P_F;
        } else if (qualityFactor < -20) {
            return rand % 3 == 0 ? P_H : P_F;
        } else if (qualityFactor < 20) {
            return rand % 2 == 0 ? P_H : P_F;
        } else if (qualityFactor < 50) {
            return rand % 3 == 0 ? P_F : P_H;
        } else if (qualityFactor < 100) {
            return rand % 5 == 0 ? P_F : P_H;
        } else {
            return P_H;
        }
    }

    public static int getPseudoRandomTotalBases(int qualityFactor) {
        var rand = random.nextInt();
        if (qualityFactor < -50) {
            return -1;
        } else if (qualityFactor < -20) {
            return rand % 3 == 0 ? 2 : 1;
        } else if (qualityFactor < 20) {
            if (rand % 15 == 0) {
                return 4;
            } else if (rand % 12 == 0) {
                return 3;
            } else if (rand % 5 == 0) {
                return 2;
            } else {
                return 1;
            }
        } else if (qualityFactor < 50) {
            if (rand % 10 == 0) {
                return 4;
            } else if (rand % 8 == 0) {
                return 3;
            } else if (rand % 4 == 0) {
                return 2;
            } else {
                return 1;
            }
        } else if (qualityFactor < 100) {
            if (rand % 7 == 0) {
                return 4;
            } else if (rand % 5 == 0) {
                return 3;
            } else if (rand % 2 == 0) {
                return 2;
            } else {
                return 1;
            }
        } else {
            if (rand % 4 == 0) {
                return 4;
            } else if (rand % 3 == 0) {
                return 3;
            } else {
                return 2;
            }
        }
    }

    public static String getPseudoRandomFieldingOutcome(int qualityFactor) {
        var rand = random.nextInt();
        if (qualityFactor < -50) {
            return rand % 4 == 0 ? F_O : F_E;
        } else if (qualityFactor < -20) {
            return rand % 2 == 0 ? F_O : F_E;
        } else if (qualityFactor < 20) {
            return rand % 3 == 0 ? F_E : F_O;
        } else if (qualityFactor < 50) {
            return rand % 5 == 0 ? F_E : F_O;
        } else if (qualityFactor < 100) {
            return rand % 8 == 0 ? F_E : F_O;
        } else {
            return F_O;
        }
    }

    public static PlayerPosition getRandomPosition() {
        return switch (random.nextInt(9) + 1) {
            case 2 -> PlayerPosition.CATCHER;
            case 3 -> PlayerPosition.FIRST_BASE;
            case 4 -> PlayerPosition.SECOND_BASE;
            case 5 -> PlayerPosition.THIRD_BASE;
            case 6 -> PlayerPosition.SHORT_STOP;
            case 7 -> PlayerPosition.LEFT_FIELD;
            case 8 -> PlayerPosition.CENTER_FIELD;
            case 9 -> PlayerPosition.RIGHT_FIELD;
            default -> PlayerPosition.PITCHER;
        };
    }
}
