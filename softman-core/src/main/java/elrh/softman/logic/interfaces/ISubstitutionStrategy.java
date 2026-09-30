package elrh.softman.logic.interfaces;

import elrh.softman.logic.MatchSimulator;
import elrh.softman.logic.core.Lineup;

public interface ISubstitutionStrategy {

    // called before every play for each lineup the computer manages; acts via sim.substitute / sim.changePosition
    void evaluate(MatchSimulator sim, Lineup lineup);

}
