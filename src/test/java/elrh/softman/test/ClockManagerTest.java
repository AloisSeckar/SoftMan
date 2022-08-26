package elrh.softman.test;

import elrh.softman.logic.managers.ClockManager;
import elrh.softman.utils.Constants;
import java.time.LocalDate;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClockManagerTest {

    private static final int YEAR = Constants.START_YEAR;
    private static final LocalDate TODAY = Constants.START_DATE;
    private static final LocalDate YESTERDAY = LocalDate.of(YEAR, 3, 30);
    private static final LocalDate TOMORROW = LocalDate.of(YEAR, 4, 1);
    private static final LocalDate NEW_YEAR = LocalDate.of(YEAR + 1, 1, 1);

    private ClockManager clock;

    @BeforeEach
    void setUp() {
        // initial date is Constants.START_DATE ("LocalDate.of(Constants.START_YEAR, 3, 31)")
        clock = new ClockManager();
    }

    @Test
    @DisplayName("viewDateTest")
    void viewDateTest() {
        clock.prevViewDay();
        assertEquals(YESTERDAY, clock.getViewDate(), "prevViewDay() doesn't match YESTERDAY");
        clock.nextViewDay();
        clock.nextViewDay();
        assertEquals(TOMORROW, clock.getViewDate(), "nextViewDay() (x2) doesn't match TOMORROW");
        clock.adjustViewDay();
        assertEquals(TODAY, clock.getViewDate(), "adjustViewDay() doesn't match TODAY");
    }

    @Test
    @DisplayName("plusDaysTest")
    void plusDaysTest() {
        clock.plusDays(1);
        assertEquals(TOMORROW, clock.getCurrentDate(), "plusDays(1) doesn't match TOMORROW");
        clock.plusDays(-2);
        assertEquals(YESTERDAY, clock.getCurrentDate(), "plusDays(-2) doesn't match YESTERDAY");
    }

    @Test
    @DisplayName("getYearTest")
    void getYearTest() {
        int year1 = clock.getYear();
        assertEquals(YEAR, year1, "getYear() 1 doesn't match YEAR");
        clock.plusDays(365);
        int year2 = clock.getYear();
        assertEquals(YEAR + 1, year2, "getYear() 2 doesn't match YEAR+1");
    }

    @Test
    @DisplayName("nextYearTest")
    void nextYearTest() {
        clock.nextYear();
        assertEquals(YEAR + 1, clock.getYear(), "getYear() doesn't match YEAR+1");
        assertEquals(NEW_YEAR, clock.getCurrentDate(), "getCurrentDate() doesn't match NEW_YEAR");
    }

    @Test
    @DisplayName("resetTest")
    void resetTest() {
        clock.prevViewDay();
        clock.plusDays(1);
        clock.nextYear();
        clock.adjustViewDay();
        clock.reset();
        assertEquals(YEAR, clock.getYear(), "getYear() doesn't match YEAR");
        assertEquals(TODAY, clock.getCurrentDate(), "getCurrentDate() doesn't match TODAY");
        assertEquals(TODAY, clock.getViewDate(), "getViewDate() doesn't match TODAY");
    }

}