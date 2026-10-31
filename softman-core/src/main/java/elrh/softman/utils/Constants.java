package elrh.softman.utils;

import elrh.softman.logic.Result;

import java.time.LocalDate;

public class Constants {
    
    private Constants() {
    }

    public static final Result RESULT_OK = new Result(true, null);

    public static final int START_YEAR = 2025;

    // TODO change it to 01.01.
    public static final LocalDate START_DATE = LocalDate.of(Constants.START_YEAR, 3, 31);

    public static final int START_FUNDS = 100000;

    // single fixed save slot for now; revisit if/when multiple save games are supported
    public static final String DEFAULT_GAME_ID = "career";

    public static final int INNINGS = 7;
    
}
