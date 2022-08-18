package elrh.softman.logic.managers;

import elrh.softman.utils.Constants;
import java.time.LocalDate;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import lombok.Getter;

public class ClockManager {

    @Getter
    private LocalDate currentDate = LocalDate.of(Constants.START_YEAR, 3, 31);
    @Getter
    private LocalDate viewDate = LocalDate.of(Constants.START_YEAR, 3, 31);

    public void prevViewDay() {
        viewDate = viewDate.minusDays(1);
    }

    public void nextViewDay() {
        viewDate = viewDate.plusDays(1);
    }

    public void adjustViewDay() {
        viewDate = currentDate;
    }

    public void plusDays(long days) {
        currentDate = currentDate.plusDays(days);
    }

    public int getYear() {
        return currentDate.getYear();
    }

    public void nextYear() {
        currentDate = currentDate.with(lastDayOfYear());
        currentDate = currentDate.plusDays(1);
    }
}
