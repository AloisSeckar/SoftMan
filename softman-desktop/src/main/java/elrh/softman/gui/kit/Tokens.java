package elrh.softman.gui.kit;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

// Style classes defined in softman.css; keep both in sync
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Tokens {

    public static final String CAPTION = "sm-caption";
    public static final String BODY = "sm-body";
    public static final String TITLE = "sm-title";
    public static final String DISPLAY = "sm-display";
    public static final String MONO = "sm-mono";

    public static final String CHIP = "sm-chip";
    public static final String RESULT_WIN = "sm-result-win";
    public static final String RESULT_LOSS = "sm-result-loss";
    public static final String RESULT_DRAW = "sm-result-draw";

    public static final String CARD_TITLE = "sm-card-title";

    public static final String RATING = "sm-rating";
    public static final String RATING_VALUE = "sm-rating-value";
    public static final String RATING_LOW = "sm-rating-low";
    public static final String RATING_MID = "sm-rating-mid";
    public static final String RATING_GOOD = "sm-rating-good";
    public static final String RATING_ELITE = "sm-rating-elite";
    public static final List<String> RATING_TIERS = List.of(RATING_LOW, RATING_MID, RATING_GOOD, RATING_ELITE);

    public static final String CLUB_BANNER = "sm-club-banner";
    public static final String CLUB_STRIPE = "sm-club-stripe";
    public static final String CLUB_FACTS = "sm-club-facts";
}
