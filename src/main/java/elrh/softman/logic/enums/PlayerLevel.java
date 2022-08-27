package elrh.softman.logic.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PlayerLevel {

    MSEN(1000),
    MU18(3000),
    MU15(5000),
    MU12(7000),
    FSEN(2000),
    FU18(4000),
    FU15(6000),
    FU12(8000);

    @Getter
    private final int matchId;

}
