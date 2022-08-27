package elrh.softman.logic.enums;

import static elrh.softman.logic.enums.PlayerGender.*;
import lombok.*;

@RequiredArgsConstructor
public enum PlayerLevel {

    MSEN(-1, M, 1000),
    MU18(18, M, 3000),
    MU15(15, M, 5000),
    MU12(12, M, 7000),
    FSEN(-1, F, 2000),
    FU18(18, F, 4000),
    FU15(15, F, 6000),
    FU12(12, F, 8000);

    @Getter
    private final int ageLimit;

    @Getter
    private final PlayerGender gender;

    @Getter
    private final int matchId;

}
