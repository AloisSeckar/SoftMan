package elrh.softman.test;

import elrh.softman.logic.managers.ClockManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import elrh.softman.utils.Constants;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

public class ClockManagerTest {

    private static final int YEAR = Constants.START_YEAR;
    private static final LocalDate YESTERDAY = LocalDate.of(YEAR, 3, 30);
    private static final LocalDate TODAY = LocalDate.of(YEAR, 3, 31);
    private static final LocalDate TOMORROW = LocalDate.of(YEAR, 4, 1);
    private static final LocalDate NEW_YEAR = LocalDate.of(YEAR + 1, 1, 1);

    private ClockManager clock;

    @BeforeEach
    void setUp() {
        // initial date is "LocalDate.of(Constants.START_YEAR, 3, 31)"
        clock = new ClockManager();
    }

    @Test
    @DisplayName("viewDateTest")
    void viewDateTest() {
        clock.prevViewDay();
        assertEquals(clock.getViewDate(), YESTERDAY, "prevViewDay - result doesn't match YESTERDAY");
        clock.nextViewDay();
        clock.nextViewDay();
        assertEquals(clock.getViewDate(), TOMORROW, "nextViewDay (x2) - result doesn't match TOMORROW");
        clock.adjustViewDay();
        assertEquals(clock.getViewDate(), TODAY, "adjustViewDay - result doesn't match TODAY");
    }

    @Test
    @DisplayName("plusDaysTest")
    void plusDaysTest() {
        clock.plusDays(1);
        assertEquals(clock.getCurrentDate(), TOMORROW, "plusDays(1) - result doesn't match TOMORROW");
        clock.plusDays(-2);
        assertEquals(clock.getCurrentDate(), YESTERDAY, "plusDays(-2) - result doesn't match YESTERDAY");
    }

    @Test
    @DisplayName("getYearTest")
    void getYearTest() {
        int year1 = clock.getYear();
        assertEquals(year1, YEAR, "getYear() 1 - result doesn't match YEAR");
        clock.plusDays(365);
        int year2 = clock.getYear();
        assertEquals(year2, YEAR + 1, "getYear() 2 - result doesn't match YEAR+1");
    }

    @Test
    @DisplayName("nextYearTest")
    void nextYearTest() {
        clock.nextYear();
        assertEquals(clock.getYear(), YEAR + 1, "nextYear() - result doesn't match YEAR+1");
        assertEquals(clock.getCurrentDate(), NEW_YEAR, "nextYear() - result doesn't match NEW_YEAR");
    }

}