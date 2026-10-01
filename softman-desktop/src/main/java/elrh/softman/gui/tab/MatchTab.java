package elrh.softman.gui.tab;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import elrh.softman.gui.kit.Cards;
import elrh.softman.gui.kit.Icons;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.tile.BoxScoreTile;
import elrh.softman.gui.tile.LineupTile;
import elrh.softman.gui.tile.MatchHeaderTile;
import elrh.softman.gui.utils.InfoUtils;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;
import elrh.softman.logic.core.Lineup;
import elrh.softman.logic.core.Match;
import elrh.softman.logic.MatchSimulator;
import elrh.softman.logic.core.data.PlayerInfo;
import elrh.softman.logic.core.data.PlayerRecord;
import elrh.softman.logic.enums.PlayerPosition;
import elrh.softman.logic.interfaces.IMatchReporter;
import elrh.softman.utils.ErrorUtils;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.kordamp.ikonli.feather.Feather;

public class MatchTab extends BorderPane {

    private static MatchTab INSTANCE;

    private final MatchHeaderTile matchHeaderTile;
    private final LineupTile awayLineup;
    private final LineupTile homeLineup;
    private final BoxScoreTile boxScore;
    private final TextArea matchOverview;

    private final Button simButton;
    private final Button simInningButton;
    private final Button playButton;

    private final HBox substitutionBar;
    private final ComboBox<SpotItem> spotCB = new ComboBox<>();
    private final ComboBox<PlayerInfo> replacementCB = new ComboBox<>();
    private final ComboBox<PlayerPosition> positionCB = new ComboBox<>();
    private final Button substituteButton = new Button("Substitute");
    private final Button changePositionButton = new Button("Change position");
    private final Label substitutionInfo = new Label();

    private Match match;
    private MatchSimulator sim;
    private Lineup managedLineup;
    private boolean controlsDisabled;

    private record SpotItem(int batOrder, PlayerRecord current) {
        @Override
        public String toString() {
            return String.format("%d. %s (%s)", batOrder, current, current.getPosition());
        }
    }

    public static MatchTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MatchTab();
        }
        return INSTANCE;
    }

    private MatchTab() {
        setPadding(Space.S.insets());
        double gap = Space.S.getPx();

        matchHeaderTile = new MatchHeaderTile();
        BorderPane.setMargin(matchHeaderTile, new Insets(0, 0, gap, 0));
        super.setTop(matchHeaderTile);

        awayLineup = new LineupTile(true);
        awayLineup.setMaxHeight(Region.USE_PREF_SIZE);
        BorderPane.setMargin(awayLineup, new Insets(0, gap, 0, 0));
        super.setLeft(awayLineup);

        homeLineup = new LineupTile(true);
        homeLineup.setMaxHeight(Region.USE_PREF_SIZE);
        BorderPane.setMargin(homeLineup, new Insets(0, 0, 0, gap));
        super.setRight(homeLineup);

        boxScore = new BoxScoreTile();

        matchOverview = new TextArea();
        matchOverview.getStyleClass().add(Tokens.MONO);
        matchOverview.setEditable(false);

        playButton = new Button("Play game", Icons.of(Feather.PLAY));
        playButton.getStyleClass().add(Styles.ACCENT);
        playButton.setOnAction(e -> playMatch());

        simInningButton = new Button("Simulate inning", Icons.of(Feather.SKIP_FORWARD));
        simInningButton.setOnAction(e -> simulateInning());

        simButton = new Button("Simulate game", Icons.of(Feather.FAST_FORWARD));
        simButton.setOnAction(e -> simulateMatch());

        var refreshButton = Icons.button(Feather.REFRESH_CW, "Refresh");
        refreshButton.setOnAction(e -> refreshMatch());

        var buttonBar = Layouts.row(Space.S, new InputGroup(playButton, simInningButton, simButton), Layouts.spacer(), refreshButton);

        spotCB.setPromptText("Lineup spot");
        replacementCB.setPromptText("Substitute");
        positionCB.setPromptText("Position");
        spotCB.valueProperty().addListener((ov, oldValue, newValue) -> updateSubstitutionOptions());
        replacementCB.valueProperty().addListener((ov, oldValue, newValue) -> updateSubstitutionButtons());
        positionCB.valueProperty().addListener((ov, oldValue, newValue) -> updateSubstitutionButtons());
        substituteButton.setGraphic(Icons.of(Feather.USER_PLUS));
        substituteButton.setOnAction(e -> substitute());
        changePositionButton.setGraphic(Icons.of(Feather.MOVE));
        changePositionButton.setOnAction(e -> changePosition());

        substitutionBar = Layouts.row(Space.S, spotCB, replacementCB, positionCB, substituteButton, changePositionButton);
        substitutionInfo.getStyleClass().add(Tokens.CAPTION);

        var controls = Cards.plain(Layouts.column(Space.S, buttonBar, new Separator(), substitutionBar, substitutionInfo));

        var playByPlay = Cards.titled("Play-by-play", matchOverview);

        super.setCenter(Layouts.column(Space.S, boxScore, Layouts.grow(playByPlay), controls));

    }

    public static IMatchReporter getMatchReporter() {
        return INSTANCE.matchOverview::appendText;
    }

    public void setMatch(Match match) {
        // keep the simulator of the displayed match, it holds the game state (inning, outs, runners)
        if (this.match != match || sim == null) {
            this.match = match;
            this.sim = new MatchSimulator(match, matchOverview::appendText);
            sim.setVisualMode(true);
            managedLineup = findManagedLineup();
            sim.setManagedTeamId(managedLineup != null ? managedLineup.getLineupInfo().getTeamId() : null);
        }
        matchHeaderTile.setMatch(match);

        refreshMatch();
    }

    private void playMatch() {
        if (sim != null) {
            if (defenseReady()) {
                sim.simulatePlay();
                refreshMatch();
            }
        } else {
            ErrorUtils.raise("Match simulator cannot be NULL");
        }
    }

    // during auto-simulation the substitution strategy settles the defense
    private void simulateMatch() {
        if (sim != null) {
            sim.simulateMatch();
            refreshMatch();
        } else {
            ErrorUtils.raise("Match simulator cannot be NULL");
        }
    }

    private void simulateInning() {
        if (sim != null) {
            sim.simulateInning();
            refreshMatch();
        } else {
            ErrorUtils.raise("Match simulator cannot be NULL");
        }
    }

    private void refreshMatch() {
        matchOverview.clear();
        match.printPlayByPlay(matchOverview::appendText);

        boxScore.loadBoxScore(match);

        awayLineup.fillLineup(match.getAwayLineup());
        awayLineup.setReadOnly(true); // TODO without that, 8th sub spot is occasionally active, but this solution doesn't seem correct
        homeLineup.fillLineup(match.getHomeLineup());
        homeLineup.setReadOnly(true); // TODO see above

        boolean todayMatch = AssociationManager.getInstance().isTodayMatch(match); // TODO maybe not asking every time?
        boolean finishedMatch = match.isFinished();
        controlsDisabled = !todayMatch || finishedMatch;
        simButton.setDisable(controlsDisabled);
        simInningButton.setDisable(controlsDisabled);
        playButton.setDisable(controlsDisabled);

        refreshSubstitutions();

        ClubTab.getInstance().refreshSchedule(); // TODO maybe not refreshing every single play?
    }

    private Lineup findManagedLineup() {
        var user = AssociationManager.getInstance().getUser();
        if (user.userManagesTeam(match.getAwayLineup().getLineupInfo().getTeamId())) {
            return match.getAwayLineup();
        } else if (user.userManagesTeam(match.getHomeLineup().getLineupInfo().getTeamId())) {
            return match.getHomeLineup();
        }
        return null;
    }

    // PH/PR must be settled before the team takes the field; unsettled ones at game end don't matter
    private boolean defenseReady() {
        if (managedLineup != null) {
            var check = sim.checkDefense(managedLineup);
            if (!check.ok()) {
                InfoUtils.showMessage("Set up your defense first: " + check.message());
                return false;
            }
        }
        return true;
    }

    private void refreshSubstitutions() {
        boolean enabled = managedLineup != null && match.isActive() && !controlsDisabled;
        substitutionBar.setDisable(!enabled);
        if (!enabled) {
            spotCB.getItems().clear();
            substitutionInfo.setText(managedLineup != null && match.isScheduled() ? "Substitutions are available once the game starts" : "");
            return;
        }

        var selectedOrder = spotCB.getValue() != null ? spotCB.getValue().batOrder() : 0;
        var items = new ArrayList<SpotItem>();
        for (int i = 1; i <= Lineup.POSITION_PLAYERS; i++) {
            var current = managedLineup.getCurrentBatter(i);
            if (current != null) {
                items.add(new SpotItem(i, current));
            }
        }
        spotCB.getItems().setAll(items);
        spotCB.setValue(items.stream().filter(item -> item.batOrder() == selectedOrder).findFirst().orElse(null));
        updateSubstitutionOptions();

        if (sim.isBatting(managedLineup)) {
            substitutionInfo.setText("On offense: replace the upcoming batter (PH) or a runner (PR)");
        } else {
            var check = sim.checkDefense(managedLineup);
            substitutionInfo.setText(check.ok()
                ? "On defense: substitute players or change their positions"
                : "Defense not ready: " + check.message());
        }
    }

    private void updateSubstitutionOptions() {
        var item = spotCB.getValue();
        if (item == null || managedLineup == null) {
            replacementCB.getItems().clear();
            positionCB.getItems().clear();
            updateSubstitutionButtons();
            return;
        }

        replacementCB.getItems().setAll(managedLineup.getAvailableReplacements(item.batOrder()));
        if (sim.isBatting(managedLineup)) {
            var role = sim.getOffensiveRole(managedLineup, item.batOrder());
            positionCB.getItems().setAll(role != null ? List.of(role) : List.of());
            positionCB.setValue(role);
            positionCB.setDisable(true);
        } else {
            var allowed = managedLineup.getDefensivePositions(item.batOrder());
            positionCB.getItems().setAll(allowed);
            positionCB.setValue(allowed.contains(item.current().getPosition()) ? item.current().getPosition() : null);
            positionCB.setDisable(false);
        }
        updateSubstitutionButtons();
    }

    private void updateSubstitutionButtons() {
        var item = spotCB.getValue();
        var position = positionCB.getValue();
        boolean batting = managedLineup != null && sim.isBatting(managedLineup);
        substituteButton.setDisable(item == null || replacementCB.getValue() == null || position == null);
        changePositionButton.setDisable(item == null || batting || position == null || position == item.current().getPosition());
    }

    private void substitute() {
        var item = spotCB.getValue();
        if (item != null) {
            handleResult(sim.substitute(managedLineup, item.batOrder(), replacementCB.getValue(), positionCB.getValue()));
        }
    }

    private void changePosition() {
        var item = spotCB.getValue();
        if (item != null) {
            handleResult(sim.changePosition(managedLineup, item.batOrder(), positionCB.getValue()));
        }
    }

    private void handleResult(Result result) {
        if (!result.ok()) {
            InfoUtils.showMessage(result.message());
        }
        refreshMatch();
    }
}
