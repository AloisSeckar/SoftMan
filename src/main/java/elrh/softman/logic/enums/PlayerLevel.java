package elrh.softman.logic.enums;

import static elrh.softman.logic.enums.PlayerGender.*;
import lombok.*;

@RequiredArgsConstructor
public enum PlayerLevel {

    MSEN(-1, M, "Men", 1000),
    MU18(18, M, "Boys U18", 3000),
    MU15(15, M, "Boys U15", 5000),
    MU12(12, M, "Boys U12", 7000),
    FSEN(-1, F, "Women", 2000),
    FU18(18, F, "Girls U18", 4000),
    FU15(15, F, "Girls U15", 6000),
    FU12(12, F, "Girls U12", 8000);

    @Getter
    private final int ageLimit;

    @Getter
    private final PlayerGender gender;

    @Getter
    private final String code;

    @Getter
    private final int matchNumber;

}
