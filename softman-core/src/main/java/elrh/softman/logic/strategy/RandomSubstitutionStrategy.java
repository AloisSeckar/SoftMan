package elrh.softman.logic.strategy;

import elrh.softman.logic.MatchSimulator;
import elrh.softman.logic.core.Lineup;
import elrh.softman.logic.enums.PlayerPosition;
import elrh.softman.logic.interfaces.ISubstitutionStrategy;
import java.util.Random;

public class RandomSubstitutionStrategy implements ISubstitutionStrategy {

    private static final Random random = new Random();

    private static final int FIRST_INNING = 3;

    // "1 in N" chances
    private static final int PINCH_HITTER_ODDS = 30;
    private static final int PINCH_RUNNER_ODDS = 20;
    private static final int DEFENSE_HALF_START_ODDS = 6;
    private static final int DEFENSE_ODDS = 40;
    private static final int REPLACE_UNSETTLED_ODDS = 2;

    @Override
    public void evaluate(MatchSimulator sim, Lineup lineup) {
        if (sim.getInning() < FIRST_INNING) {
            return;
        }
        if (sim.isBatting(lineup)) {
            evaluateOffense(sim, lineup);
        } else {
            evaluateDefense(sim, lineup);
        }
    }

    private void evaluateOffense(MatchSimulator sim, Lineup lineup) {
        if (chance(PINCH_HITTER_ODDS)) {
            trySubstitute(sim, lineup, sim.getUpcomingBatOrder(lineup), null);
        }
        for (int batOrder = 1; batOrder < Lineup.POSITION_PLAYERS; batOrder++) {
            if (sim.getOffensiveRole(lineup, batOrder) == PlayerPosition.PINCH_RUNNER && chance(PINCH_RUNNER_ODDS)) {
                trySubstitute(sim, lineup, batOrder, null);
            }
        }
    }

    private void evaluateDefense(MatchSimulator sim, Lineup lineup) {
        if (sim.isHalfInningStart()) {
            // PH/PR left unreplaced here get a position from the simulator's fallback
            for (int batOrder = 1; batOrder <= Lineup.POSITION_PLAYERS; batOrder++) {
                var current = lineup.getCurrentBatter(batOrder);
                if (current != null && !lineup.getDefensivePositions(batOrder).contains(current.getPosition())
                    && chance(REPLACE_UNSETTLED_ODDS)) {
                    trySubstitute(sim, lineup, batOrder, lastDefensivePosition(lineup, batOrder));
                }
            }
        }
        if (chance(sim.isHalfInningStart() ? DEFENSE_HALF_START_ODDS : DEFENSE_ODDS)) {
            var batOrder = 1 + random.nextInt(Lineup.POSITION_PLAYERS);
            var current = lineup.getCurrentBatter(batOrder);
            if (current != null && lineup.getDefensivePositions(batOrder).contains(current.getPosition())) {
                trySubstitute(sim, lineup, batOrder, current.getPosition());
            }
        }
    }

    private void trySubstitute(MatchSimulator sim, Lineup lineup, int batOrder, PlayerPosition defensivePosition) {
        var candidates = lineup.getAvailableReplacements(batOrder);
        if (lineup.getCurrentBatter(batOrder) != null && !candidates.isEmpty()) {
            var player = candidates.get(random.nextInt(candidates.size()));
            sim.substitute(lineup, batOrder, player, defensivePosition);
        }
    }

    // the position the spot held before PH/PR came in
    private PlayerPosition lastDefensivePosition(Lineup lineup, int batOrder) {
        var allowed = lineup.getDefensivePositions(batOrder);
        var spot = lineup.getPositionPlayers()[batOrder - 1];
        for (int i = spot.size() - 1; i >= 0; i--) {
            var position = spot.get(i).getPosition();
            if (allowed.contains(position)) {
                return position;
            }
        }
        return allowed.getFirst();
    }

    private static boolean chance(int odds) {
        return random.nextInt(odds) == 0;
    }

}
