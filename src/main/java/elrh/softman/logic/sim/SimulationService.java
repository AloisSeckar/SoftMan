package elrh.softman.logic.sim;

import elrh.softman.gui.frame.ActionFrame;
import elrh.softman.gui.tab.ClubTab;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;
import elrh.softman.logic.managers.ClockManager;
import elrh.softman.utils.*;
import java.time.LocalDate;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class SimulationService extends Service<Result> {

    @NonNull
    private LocalDate until;

    @Override
    protected Task<Result> createTask() {
        return new SimulationTask(until);
    }

    private static class SimulationTask extends Task<Result> {

        private final LocalDate until;
        private final AssociationManager manager;
        private final ClockManager clock;

        public SimulationTask(LocalDate until) {
            this.until = until;
            this.manager = AssociationManager.getInstance();
            this.clock = manager.getClock();
        }

        @Override
        protected Result call() {
            try {
                while (until.isAfter(clock.getCurrentDate())) {
                    advanceToNextDay();
                }
                refreshUIComponents();
                return Constants.RESULT_OK;
            } catch (Exception ex) {
                return ErrorUtils.handleException("SimulationTask", ex);
            }
        }

        private void advanceToNextDay() {
            // in case of changes AssociationManager.plainAdvanceToNextDay also needs to be updated
            AssociationManager.getInstance().getDailyMatches().values().forEach(matches -> matches.forEach(match -> {
                if (!match.isFinished()) {
                    match.simulate(manager.isTestMode() ? null : new TextArea()); // TODO rendering actions shouldn't be part of simulating
                }
            }));

            clock.plusDays(1);
            clock.adjustViewDay();
            LOG.info("NEW DAY. Today is " + clock.getCurrentDate().format(FormatUtils.DF));
        }

        private void refreshUIComponents() {
            if (!manager.isTestMode()) {
                Platform.runLater(() -> {
                    ActionFrame.getInstance().updateDateValue(clock.getCurrentDate());
                    ClubTab.getInstance().setDailySchedule();
                });
            }
        }
    }
}
