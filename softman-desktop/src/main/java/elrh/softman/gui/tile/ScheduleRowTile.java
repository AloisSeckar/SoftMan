package elrh.softman.gui.tile;

import atlantafx.base.theme.Styles;
import elrh.softman.gui.frame.ContentFrame;
import elrh.softman.gui.frame.ContentFrame.Screen;
import elrh.softman.gui.kit.Icons;
import elrh.softman.gui.kit.Images;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.tab.ClubTab;
import elrh.softman.gui.tab.MatchTab;
import elrh.softman.gui.utils.GUIUtils;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.MatchSimulator;
import elrh.softman.logic.core.Match;
import elrh.softman.logic.core.stats.BoxScore;
import elrh.softman.utils.ErrorUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.feather.Feather;

public class ScheduleRowTile extends HBox {

    private final ImageView awayImage = Images.rounded(2.5);
    private final ImageView homeImage = Images.rounded(2.5);
    private final Label awayLabel = new Label();
    private final Label homeLabel = new Label();
    private final Label titleLabel = new Label("Match");
    private final Label detailLabel = new Label();

    private final Button simButton = Icons.button(Feather.ZAP, "Simulate match");
    private final Button playButton = Icons.button(Feather.PLAY, "Play match");

    private Match match;
    private MatchSimulator sim;

    public ScheduleRowTile() {
        super(Space.M.getPx());
        getStyleClass().add(Tokens.LIST_ROW);
        setAlignment(Pos.CENTER_LEFT);

        awayLabel.getStyleClass().add(Styles.TEXT_BOLD);
        homeLabel.getStyleClass().add(Styles.TEXT_BOLD);
        titleLabel.getStyleClass().add(Tokens.TITLE);
        detailLabel.getStyleClass().add(Tokens.CAPTION);

        var away = Layouts.row(Space.S, awayImage, awayLabel);
        var home = Layouts.row(Space.S, homeLabel, homeImage);
        home.setAlignment(Pos.CENTER_RIGHT);
        // zero pref width + grow on both sides keeps the score centred
        for (var side : new HBox[] {away, home}) {
            Layouts.grow(side);
            side.setPrefWidth(0);
        }

        var center = Layouts.column(Space.XS, titleLabel, detailLabel);
        center.setAlignment(Pos.CENTER);
        center.setMinWidth(Layouts.em(8));

        simButton.setOnAction(e -> simulateMatch());
        playButton.setOnAction(e -> viewMatch());
        var viewButton = Icons.button(Feather.EYE, "View match");
        viewButton.setOnAction(e -> viewMatch());

        getChildren().addAll(away, center, home, Layouts.row(Space.XS, simButton, playButton, viewButton));
    }

    public void setMatch(Match match) {
        this.match = match;
        refreshMatch();
    }

    public void refreshMatch() {
        if (match != null) {
            boolean todayMatch = AssociationManager.getInstance().isTodayMatch(match);
            simButton.setDisable(!todayMatch);
            playButton.setDisable(!todayMatch);

            sim = new MatchSimulator(match, MatchTab.getMatchReporter());

            var awayInfo = match.getAwayLineup().getLineupInfo();
            awayImage.setImage(GUIUtils.getImageOrDefault(awayInfo.getTeamLogo()));
            awayLabel.setText(awayInfo.getTeamName());

            var homeInfo = match.getHomeLineup().getLineupInfo();
            homeImage.setImage(GUIUtils.getImageOrDefault(homeInfo.getTeamLogo()));
            homeLabel.setText(homeInfo.getTeamName());

            switch (match.getMatchInfo().getStatus()) {
                case SCHEDULED -> {
                    titleLabel.setText("@");
                    detailLabel.setText(match.getMatchInfo().getStadium());
                }
                case ACTIVE -> {
                    titleLabel.setText("LIVE");
                    detailLabel.setText(match.getMatchInfo().getStadium());
                }
                case FINISHED -> {
                    BoxScore score = match.getBoxScore();
                    titleLabel.setText(score.getTotalPoints(true) + " : " + score.getTotalPoints(false));
                    detailLabel.setText("Final, " + score.getInnings() + " inn");
                    simButton.setDisable(true);
                    playButton.setDisable(true);
                }
            }

        } else {
            sim = null;
        }
    }

    private void simulateMatch() {
        if (sim != null) {
            sim.simulateMatch();
            ClubTab.getInstance().refreshSchedule();
        } else {
            ErrorUtils.raise("Match simulator cannot be NULL");
        }
    }

    private void viewMatch() {
        MatchTab.getInstance().setMatch(match);
        ContentFrame.getInstance().switchTo(Screen.MATCH);
    }

}
