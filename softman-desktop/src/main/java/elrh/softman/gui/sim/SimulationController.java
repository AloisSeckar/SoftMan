package elrh.softman.gui.sim;

import elrh.softman.logic.Result;
import java.time.LocalDate;

import elrh.softman.logic.interfaces.ISimulationRunner;
import elrh.softman.utils.ErrorUtils;
import javafx.scene.control.ProgressIndicator;
import lombok.*;

@RequiredArgsConstructor
public class SimulationController implements ISimulationRunner {

    @NonNull
    private final ProgressIndicator progressIndicator;

    @Getter
    private Result serviceResult;

    @Override
    public Result simulateUntil(LocalDate until) {
        var simulationService = new SimulationService(until);
        progressIndicator.visibleProperty().bind(simulationService.runningProperty());
        simulationService.setOnSucceeded(workerStateEvent -> serviceResult = simulationService.getValue());
        simulationService.setOnFailed(workerStateEvent -> {
            var ex = new Exception("SimulationService task failed");
            serviceResult = ErrorUtils.handleException("SimulationController", ex);
        });
        simulationService.restart();
        return serviceResult;
    }
}
