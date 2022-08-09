package elrh.softman.constants;

public class Constants {
    
    private Constants() {
    }

    public static final int START_YEAR = 2023;
    
    public static final String SOURCES_DB = "jdbc:sqlite:softman.db";
    public static final String GAME_DB = "jdbc:sqlite:sav\\game-$id$.db";
    
    public static final int INNINGS = 7;

    public static final int LINEUP_SIZE = 17;
    
    public static final String GENDER_MALE = "m";
    public static final String GENDER_FEMALE = "f";
    
}
