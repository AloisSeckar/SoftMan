package elrh.softman.logic.interfaces;

// text sink for play-by-play output; keeps the simulation free of any UI type
@FunctionalInterface
public interface IMatchReporter {

    void report(String text);

}
