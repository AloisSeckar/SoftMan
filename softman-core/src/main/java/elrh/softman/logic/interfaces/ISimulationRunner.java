package elrh.softman.logic.interfaces;

import elrh.softman.logic.Result;
import java.time.LocalDate;

// lets the core hand time advancement to a driver (JavaFX Service today, scheduler later)
public interface ISimulationRunner {

    Result simulateUntil(LocalDate until);

}
