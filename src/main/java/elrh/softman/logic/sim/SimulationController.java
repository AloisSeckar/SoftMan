package elrh.softman.logic.sim;

import elrh.softman.logic.Result;
import java.time.LocalDate;

import elrh.softman.utils.ErrorUtils;
import javafx.scene.control.ProgressIndicator;
import lombok.*;

@RequiredArgsConstructor
public class SimulationController {

    @NonNull
    private final ProgressIndicator progressIndicator;

    @Getter
    private Result serviceResult;

    public void initialize(LocalDate until) {
        var simulationService = new SimulationService(until);
        progressIndicator.visibleProperty().bind(simulationService.runningProperty());
        simulationService.setOnSucceeded(workerStateEvent -> serviceResult = simulationService.getValue());
        simulationService.setOnFailed(workerStateEvent -> {
            var ex = new Exception("SimulationService task failed");
            serviceResult = ErrorUtils.handleException("SimulationController", ex);
        });
        simulationService.restart();
    }
}
