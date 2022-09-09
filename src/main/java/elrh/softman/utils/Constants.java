package elrh.softman.utils;

import elrh.softman.logic.Result;

import java.time.LocalDate;

public class Constants {
    
    private Constants() {
    }

    public static final Result RESULT_OK = new Result(true, null);

    public static final int START_YEAR = 2023;

    // TODO change it to 01.01.
    public static final LocalDate START_DATE = LocalDate.of(Constants.START_YEAR, 3, 31);

    public static final int START_FUNDS = 100000;
    
    public static final String SOURCES_DB = "jdbc:sqlite:softman.db";
    public static final String GAME_DB = "jdbc:sqlite:sav\\game-$id$.db";
    
    public static final int INNINGS = 7;

    public static final int LINEUP_SIZE = 17;
    
}
