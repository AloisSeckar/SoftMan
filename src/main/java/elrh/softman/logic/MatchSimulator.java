package elrh.softman.logic;

import elrh.softman.logic.core.Lineup;
import elrh.softman.logic.db.orm.player.PlayerRecord;
import elrh.softman.logic.db.orm.match.MatchPlayByPlay;
import elrh.softman.logic.core.Match;
import static elrh.softman.logic.enums.MatchStatus.*;
import static elrh.softman.logic.enums.PlayerPosition.*;
import static elrh.softman.logic.enums.StatsType.*;
import elrh.softman.logic.core.stats.BoxScore;
import elrh.softman.logic.db.orm.player.PlayerAttributes;
import elrh.softman.utils.*;
import java.util.Random;
import javafx.scene.control.TextArea;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class MatchSimulator {

    private static final Random random = new Random();

    @Setter
    private boolean visualMode = false;

    private final TextArea target;
    private final Match match;

    private final BoxScore boxScore;
    private final Lineup awayLineup;
    private final Lineup homeLineup;
    
    private boolean inningStart = true;
    private int inning = 1;
    private boolean top = true;
    private int outs = 0;
    private int awayBatter = 1;
    private int homeBatter = 1;
    private Lineup battingLineup;
    private Lineup fieldingLineup;

    private PlayerRecord batter;
    private PlayerAttributes batterAttr;
    private PlayerRecord pitcher;
    private PlayerAttributes pitcherAttr;

    private PlayerRecord base1;
    private PlayerRecord base2;
    private PlayerRecord base3;

    public MatchSimulator(Match match, TextArea target) {
        this.target = target;
        this.match = match;
        awayLineup = match.getAwayLineup();
        homeLineup = match.getHomeLineup();
        boxScore = match.getBoxScore();
    }

    // keep simulating until the end of the match
    public void simulateMatch() {
        while (!match.isFinished()) {
            simulateInning();
        }
    }

    // keep simulating until the end of current inning
    public void simulateInning() {
        int currentInning = inning;
        while (!match.isFinished() && inning == currentInning) {
            simulatePlay();
        }
    }

    // single new situation
    public void simulatePlay() {
        if (match.isScheduled()) {
            setUpMatch();
        }

        if (keepPlaying()) {
            setUpPlay();
            if (batter != null) {
                var pitchQuality = pitcherAttr.getPitchingSkill() + random.nextInt(100);
                var hitQuality = batterAttr.getBattingSkill() + random.nextInt(100);
                var qualityFactor = hitQuality - pitchQuality;
                var pitchOutcome = SimUtils.getPseudoRandomPitchOutcome(qualityFactor);

                // TODO random runner actions

                appendText(String.format("%d vs. %d => q: %d => %s\n", hitQuality, pitchQuality, qualityFactor, pitchOutcome));

                switch (pitchOutcome) {
                    case SimUtils.O_K -> handleK();
                    case SimUtils.O_W -> handleW();
                    default -> handleP(qualityFactor);
                }
            } else {
                handleNoBatter();
            }
            wrapUpPlay();
        }
    }

    ///////
    private void setUpMatch() {
        var matchId = match.getId();
        var matchStr = String.format("%s @ %s",
            awayLineup.getLinuepInfo().getTeamShortName(),
            homeLineup.getLinuepInfo().getTeamShortName());

        awayLineup.setUp(matchId, matchStr);
        homeLineup.setUp(matchId, matchStr);

        match.getMatchInfo().setStatus(ACTIVE);

        appendText(String.format("\n\nGAME BETWEEN %s AND %s\n",
            awayLineup.getLinuepInfo().getTeamName(),
            homeLineup.getLinuepInfo().getTeamName()));
    }

    private void setUpInning() {
        if (top) {
            appendText(String.format("\n\nINNING %d\n", inning));
            appendText("\n\nTOP\n");
            battingLineup = awayLineup;
            fieldingLineup = homeLineup;
            match.getMatchInfo().setHomeTeamFinishedBatting(false);
        } else {
            appendText("\n\nBOTTOM\n");
            battingLineup = homeLineup;
            fieldingLineup = awayLineup;
            match.getMatchInfo().setHomeTeamFinishedBatting(true);
        }
        inningStart = false;
    }

    private void setUpPlay() {
        if (inningStart) {
            setUpInning();
            evaluateRandomSubstitution(8);
        } else {
            evaluateRandomSubstitution(15);
        }

        pitcher = fieldingLineup.getCurrentPositionPlayer(PITCHER);
        pitcherAttr = pitcher.getPlayer().getAttributes();
        appendText(String.format("PITCHER: %s (%d)\n", pitcher, pitcherAttr.getPitchingSkill()));

        batter = battingLineup.getCurrentBatter(top ? awayBatter : homeBatter);
        if (batter != null) {
            batterAttr = batter.getPlayer().getAttributes();
            appendText(String.format("BATTER: %s (%d)\n", batter, batterAttr.getBattingSkill()));
        }
    }

    private void wrapUpPlay() {
        if (top) {
            awayBatter++;
            if (awayBatter > 9) {
                awayBatter = 1;
            }
        } else {
            homeBatter++;
            if (homeBatter > 9) {
                homeBatter = 1;
            }
        }

        if (outs == 3) {
            wrapUpInning();
        }

        if (!keepPlaying()) {
            wrapUpMatch();
        }
    }

    private void wrapUpInning() {
        getScore();
        swapLineups();
        base1 = null;
        base2 = null;
        base3 = null;
    }

    private void wrapUpMatch() {
        boxScore.trimIfNeeded(inning);
        appendText("\n\nGAME OVER\n\n");
        match.getMatchInfo().setStatus(FINISHED);

        appendText("\n\nSTATS\n");
        printStats(awayLineup);
        printStats(homeLineup);

        StatsUtils.saveStats(awayLineup);
        StatsUtils.saveStats(homeLineup);

        var leagueId = match.getMatchInfo().getLeagueId();
        var league = AssociationManager.getInstance().getLeagueById(leagueId);
        if (league != null) {
            league.saveMatch(match);
        } else {
            ErrorUtils.raise(String.format("Unknown leagueId %d", leagueId));
        }
    }

    private boolean keepPlaying() {
        boolean ret = true;

        int awayPoints = boxScore.getTotalPoints(true);
        int homePoints = boxScore.getTotalPoints(false);

        if (top) {
            // match can end only before next inning
            // after that away Lineup will always finnish their batting
            if (inningStart) {
                if (inning >= 4 && (Math.abs(awayPoints - homePoints) >= 15)) {
                    ret = false;
                } else if (inning >= 5 && (Math.abs(awayPoints - homePoints) >= 10)) {
                    ret = false;
                } else if (inning >= 6 && (Math.abs(awayPoints - homePoints) >= 7)) {
                    ret = false;
                } else if (inning >= 8 && (Math.abs(homePoints - awayPoints) > 0)) {
                    ret = false;
                }

                if (!ret) {
                    // revert values for correct box score (new inning didn't actually start)
                    inning--;
                    top = false;
                }
            }
        } else {
            // match can end anytime homeLineup gets in the lead or triggers mercy rule
            if (inning >= 3 && homePoints - awayPoints >= 15) {
                ret = false;
            } else if (inning >= 4 && homePoints - awayPoints >= 10) {
                ret = false;
            } else if (inning >= 5 && homePoints - awayPoints >= 7) {
                ret = false;
            } else if (inning >= 7 && homePoints - awayPoints > 0) {
                ret = false;
            }
        }

        return ret;
    }
    private void swapLineups() {
        outs = 0;
        top = !top;
        if (top) {
            inning++;
        }
        inningStart = true;
    }

    private void getScore() {
        appendText(String.format("\n\n%s: %d\n",
            awayLineup.getLinuepInfo().getTeamName(),
            boxScore.getTotalPoints(true)));
        appendText(String.format("%s: %d\n",
            homeLineup.getLinuepInfo().getTeamName(),
            boxScore.getTotalPoints(false)));
    }

    private void printStats(Lineup lineup) {
        appendText(String.format("\n\n%s\n", lineup.getLinuepInfo().getTeamName()));
        appendText("PLAYER                         | PA | AB |  H |  R | RBI |   AVG |  O |  IP | \n");

        for (int i = 0; i < Lineup.POSITION_PLAYERS; i++) {
            var players = lineup.getPositionPlayers()[i];
            if (Utils.listNotEmpty(players)) {
                players.forEach(playerRecord -> {
                    var nameWithPos = String.format("%s, %s",
                        playerRecord.getPlayer().getName(),
                        playerRecord.getPosition());
                    appendText(StringUtils.rightPad(nameWithPos, 30, " ") + " | ");
                    var record = playerRecord.getStats();
                    appendText(StringUtils.leftPad(String.valueOf(record.getBPA()), 2) + " | ");
                    appendText(StringUtils.leftPad(String.valueOf(record.getBAB()), 2) + " | ");
                    appendText(StringUtils.leftPad(String.valueOf(record.getBH()), 2) + " | ");
                    appendText(StringUtils.leftPad(String.valueOf(record.getBR()), 2) + " | ");
                    appendText(StringUtils.leftPad(String.valueOf(record.getBRB()), 3) + " | ");
                    appendText(StatsUtils.getAVG(record.getBAB(), record.getBH()) + " | ");
                    appendText(StringUtils.leftPad(String.valueOf(record.getFPO()), 2) + " | ");
                    appendText(StatsUtils.getIP(record.getFIP()) + " | \n");
                });
            }
        }

    }
    
    private void appendText(String text) {
        var pbp = new MatchPlayByPlay(match.getMatchInfo().getMatchId(), match.getPlayByPlay().size() + 1, text);
        match.getPlayByPlay().add(pbp);

        if (visualMode) {
            if (target != null) {
                target.appendText(text);
            }
            pbp.persist();
        }
    }

    private void handleNoBatter() {
        appendText("Batting spot not filled => OUT\n");
        StatsUtils.incFielding(fieldingLineup, CATCHER, FPO);
        StatsUtils.incIP(fieldingLineup);
        outs++;
    }

    private void handleK() {
        var stance = random.nextInt() % 3 == 0 ? "looking" : "swinging";
        appendText(String.format("%s STRUCK OUT %s TO %s\n", batter, stance, pitcher));

        StatsUtils.incK(batter);
        StatsUtils.handleFinishedBF(pitcher, true, 0, 3);
        pitcher.getStats().inc(PK);
        StatsUtils.incFielding(fieldingLineup, CATCHER, FPO);
        StatsUtils.incIP(fieldingLineup);

        // TODO even after strikeout runners may advance

        outs++;
    }

    private void handleW() {
        boolean walked  = random.nextInt() % 5 != 0;
        appendText(String.format("%s %s\n", batter, walked ? "WALKED" : "HIT BY PITCH"));

        batter.getStats().inc(BPA);
        if (walked) {
            batter.getStats().inc(BBB);
        } else {
            batter.getStats().inc(BHP);
        }
        StatsUtils.handleFinishedBF(pitcher, false, walked ? 4 : 1, 0);

        // TODO RBI with walk/hit
        //if (base1 != null && base2 != null && base3 != null) {
        //    batter.getStats().inc(BRB);
        //}

        handleAdvances(1, true);
        base1 = batter;
    }

    private void handleP(int qualityFactor) {
        var playOutcome = SimUtils.getPseudoRandomPlayOutcome(qualityFactor);
        appendText(String.format("in play: %s\n", playOutcome));

        if (SimUtils.P_H.equals(playOutcome)) {
            StatsUtils.incH(batter);
            StatsUtils.handleFinishedBF(pitcher, true, 0, 0);
            pitcher.getStats().inc(PH);

            String hitString;
            int bases = SimUtils.getPseudoRandomTotalBases(qualityFactor);
            switch (bases) {
                case 4 -> {
                    hitString = "HOMERUN";
                    batter.getStats().inc(BHR);
                    batter.getStats().inc(BRB);
                    pitcher.getStats().inc(PHR);
                    scoreRun(batter);
                    handleAdvances(4, false);
                }
                case 3 -> {
                    hitString = "TRIPLE";
                    batter.getStats().inc(B3B);
                    pitcher.getStats().inc(P3B);
                    handleAdvances(3, false);
                    base3 = batter;
                }
                case 2 -> {
                    hitString = "DOUBLE";
                    batter.getStats().inc(B2B);
                    pitcher.getStats().inc(P2B);
                    handleAdvances(2, false);
                    base2 = batter;
                }
                default -> {
                    hitString = "SINGLE";
                    handleAdvances(1, false);
                    base1 = batter;
                }
            }
            appendText(batter + " HITS " + hitString + "\n");
        } else {

            // TODO assists during fielding
            // TODO extra base errors

            var fielder = fieldingLineup.getCurrentPositionPlayer(SimUtils.getRandomPosition());
            var fielderAttr = fielder.getPlayer().getAttributes();
            appendText("FIELDER: " + fielder + " (" + fielderAttr.getFieldingSkill() + ")\n");
            int fieldingQuality = fielderAttr.getFieldingSkill() + random.nextInt(100);
            var fieldingFactor = fieldingQuality - qualityFactor;
            var fieldingOutcome = SimUtils.getPseudoRandomFieldingOutcome(fieldingFactor);

            appendText(fieldingQuality + " vs. " + qualityFactor + " => f: " + fieldingFactor + " => " + fieldingOutcome + "\n");

            if (SimUtils.F_O.equals(fieldingOutcome)) {
                StatsUtils.incAB(batter);
                StatsUtils.handleFinishedBF(pitcher, true, 0, 0);
                fielder.getStats().inc(FPO);
                StatsUtils.incIP(fieldingLineup);

                // TODO runners may advance even after out

                outs++;

            } else {
                StatsUtils.incAB(batter);
                StatsUtils.handleFinishedBF(pitcher, true, 0, 0);
                fielder.getStats().inc(FE);

                handleAdvances(1, false);
                base1 = batter;

                // TODO errors can be worse than 1B

            }
        }
    }

    private void handleAdvances(int guarantedAdvance, boolean deadBall) {

        // TODO add some random "more-than-guaranted" advances + runners may be out during them

        if (base3 != null && guarantedAdvance > 0) {
            scoreRun(base3);
            base3 = null;
        }

        if (base2 != null) {
            if (guarantedAdvance > 0) {
                if (guarantedAdvance > 1) {
                    scoreRun(base2);
                } else {
                    base3 = base2;
                }
                base2 = null;
            }
        }

        if (base1 != null) {
            if (guarantedAdvance > 0) {
                if (guarantedAdvance > 2) {
                    scoreRun(base1);
                } else if (guarantedAdvance > 1) {
                    base3 = base1;
                } else {
                    base2 = base1;
                }
                base1 = null;
            }
        }
    }

    private void scoreRun(PlayerRecord runner) {
        runner.getStats().inc(BR);
        appendText(runner + " SCORED\n");

        batter.getStats().inc(BRB); // TODO not all runs will be thanks to the batter
        pitcher.getStats().inc(PR); // TODO inherited runners
        pitcher.getStats().inc(PER); // TODO unearned runs
        boxScore.addHit(top);
        boxScore.addPoint(inning, top);
    }

    private void evaluateRandomSubstitution(int probability) {
        if (inning > 2) {
            boolean performSubstitution = random.nextInt() % probability == 0;
            if (performSubstitution) {
                var lineup = random.nextBoolean() ? awayLineup : homeLineup;
                var randomSubIndex = random.nextInt(Lineup.SUBSTITUTES);
                var randomSub = lineup.getSubstitutes()[randomSubIndex];
                if (randomSub != null) {
                    var randomPlrIndex = random.nextInt(Lineup.POSITION_PLAYERS);
                    var currentPlayer = lineup.getCurrentBatter(randomPlrIndex);
                    if (currentPlayer != null) {
                        var substitution = new PlayerRecord(randomSub.getPlayer(), currentPlayer.getPosition());
                        substitution.getStats().initFrom(currentPlayer.getStats());
                        appendText("SUBSTITUTION: " + substitution.getPlayer() + "(" + substitution.getPosition() + ") FOR " + currentPlayer.getPlayer() + "(" + currentPlayer.getPosition() + ")\n");
                        lineup.substitutePlayer(randomPlrIndex, substitution);
                    }
                }
            }
        }
    }

}
