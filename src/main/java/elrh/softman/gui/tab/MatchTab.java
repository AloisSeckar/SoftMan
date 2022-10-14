package elrh.softman.gui.tab;

import elrh.softman.gui.tile.BoxScoreTile;
import elrh.softman.gui.tile.LineupTile;
import elrh.softman.gui.tile.MatchHeaderTile;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Match;
import elrh.softman.logic.MatchSimulator;
import elrh.softman.utils.ErrorUtils;
import elrh.softman.utils.FormatUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class MatchTab extends BorderPane {

    private static MatchTab INSTANCE;

    private final MatchHeaderTile matchHeaderTile;
    private final LineupTile awayLineup;
    private final LineupTile homeLineup;
    private final BoxScoreTile boxScore;
    private final TextArea matchOverview;

    private final Button simButton;
    private final Button playButton;

    private Match match;
    private MatchSimulator sim;

    public static MatchTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MatchTab();
        }
        return INSTANCE;
    }

    private MatchTab() {

        awayLineup = new LineupTile(true);
        awayLineup.setPadding(FormatUtils.PADDING_10);
        super.setLeft(awayLineup);

        homeLineup = new LineupTile(true);
        homeLineup.setPadding(FormatUtils.PADDING_10);
        super.setRight(homeLineup);

        matchHeaderTile = new MatchHeaderTile();
        super.setTop(matchHeaderTile);

        boxScore = new BoxScoreTile();
        boxScore.getStyleClass().add("framed");

        matchOverview = new TextArea();
        matchOverview.getStyleClass().add("output-window");
        matchOverview.setPadding(FormatUtils.PADDING_10);

        simButton = new Button("Simulate game");
        simButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> simulateMatch());

        playButton = new Button("Play game");
        playButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> playMatch());

        Button refreshButton = new Button("Refresh");
        refreshButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> refreshMatch());

        var buttonBar = new HBox(10, simButton, playButton, refreshButton);
        buttonBar.getStyleClass().add("padding-5");

        var centerLayout = new BorderPane();

        BorderPane.setAlignment(boxScore, Pos.CENTER);
        centerLayout.setTop(boxScore);

        centerLayout.setCenter(matchOverview);

        BorderPane.setAlignment(buttonBar, Pos.CENTER);
        BorderPane.setMargin(buttonBar, new Insets(5));
        buttonBar.setAlignment(Pos.CENTER);
        centerLayout.setBottom(buttonBar);

        super.setCenter(centerLayout);

    }

    public static TextArea getTarget() {
        return INSTANCE.matchOverview;
    }

    public void setMatch(Match match) {
        this.match = match;
        this.sim = new MatchSimulator(match, matchOverview);
        matchHeaderTile.setMatch(match);
        awayLineup.fillLineup(match.getAwayLineup());
        awayLineup.setReadOnly(true); // TODO without that, 8th sub spot is occasionally active, but this solution doesn't seem correct
        homeLineup.fillLineup(match.getHomeLineup());
        homeLineup.setReadOnly(true); // TODO see above

        refreshMatch();
    }

    private void playMatch() {
        if (sim != null) {
            sim.simulatePlay();
            refreshMatch();
        } else {
            ErrorUtils.raise("Match simulator cannot be NULL");
        }
    }

    private void simulateMatch() {
        if (sim != null) {
            sim.simulateMatch();
            refreshMatch();
        } else {
            ErrorUtils.raise("Match simulator cannot be NULL");
        }
    }

    private void refreshMatch() {
        matchOverview.clear();
        match.printPlayByPlay(matchOverview);

        boxScore.loadBoxScore(match);

        boolean todayMatch = AssociationManager.getInstance().isTodayMatch(match); // TODO maybe not asking every time?
        boolean finishedMatch = match.isFinished();
        boolean disableControls = !todayMatch || finishedMatch;
        simButton.setDisable(disableControls);
        playButton.setDisable(disableControls);

        ClubTab.getInstance().refreshSchedule(); // TODO maybe not refreshing every single play?
    }
}
