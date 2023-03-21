package elrh.softman.logic.sim;

import elrh.softman.gui.frame.ActionFrame;
import elrh.softman.gui.tab.ClubTab;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.MatchSimulator;
import elrh.softman.logic.Result;
import elrh.softman.logic.core.Match;
import elrh.softman.logic.managers.ClockManager;
import elrh.softman.utils.*;
import elrh.softman.utils.factory.AssociationFactory;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
            // in case of changes AssociationManager.plainAdvanceToNextDay (used during testing) should also be updated
            // however, since only few matches are being processed during tests, there was no need to adopt Fork/Join Framework yet
            var dailyMatches = new ArrayList<Match>();
            AssociationManager.getInstance().getDailyMatches().values().forEach(dailyMatches::addAll);

            var taskPool = ForkJoinPool.commonPool();
            var ret = taskPool.invoke(new RecursiveMatchSimulation(dailyMatches));

            var date = clock.getCurrentDate().format(FormatUtils.DF);
            if (ret) {
                LOG.info(date + " - DAILY MATCHES successfully simulated");
            } else {
                LOG.warn(date + " - Not all matches were properly simulated");
            }

            clock.plusDays(1);
            clock.adjustViewDay();
            LOG.info("NEW DAY. Today is " + clock.getCurrentDate().format(FormatUtils.DF));

            // TODO probably move elsewhere (maybe even separate UI action?)
            // advance to next year
            if (clock.getCurrentDate().equals(LocalDate.of(2024, 1, 1))) {
                AssociationFactory.createLeagues();
            }
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
    private static class RecursiveMatchSimulation extends RecursiveTask<Boolean> {

        private final List<Match> matchesToPlay;

        public RecursiveMatchSimulation(List<Match> matchesToPlay) {
            this.matchesToPlay = matchesToPlay;
        }

        @Override
        protected Boolean compute() {
            var ret = false;
            if (Utils.listNotEmpty(matchesToPlay)) {
                if (matchesToPlay.size() > 1) {
                    ret = ForkJoinTask.invokeAll(createSubtasks()).stream().allMatch(ForkJoinTask::join);
                } else {
                    ret = simulate(matchesToPlay.get(0));
                }
            } else {
                ret = true;
            }
            return ret;
        }

        private List<RecursiveMatchSimulation> createSubtasks() {
            var subtasks = new ArrayList<RecursiveMatchSimulation>();
            matchesToPlay.forEach(match -> subtasks.add(new RecursiveMatchSimulation(Collections.singletonList(match))));
            return subtasks;
        }

        private Boolean simulate(Match match) {
            try {
                if (!match.isFinished()) {
                    var sim = new MatchSimulator(match, null);
                    sim.simulateMatch();
                }
                return true;
            } catch (Exception ex) {
                ErrorUtils.handleException("RecursiveMatchSimulation.simulate", ex);
                return false;
            }
        }
    }

}
