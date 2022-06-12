package elrh.softman.gui.tile;

import elrh.softman.gui.frame.ContentFrame;
import elrh.softman.gui.tab.IndexTab;
import elrh.softman.gui.tab.MatchTab;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Match;
import elrh.softman.logic.MatchSimulator;
import elrh.softman.logic.stats.BoxScore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ScheduleRowTile extends BorderPane {

    private static final int LOGO_SIZE = 60;

    private final Label titleLabel;
    private final ImageView awayImage;
    private final ImageView homeImage;

    private final Button simButton;
    private final Button playButton;

    private Match match;
    private MatchSimulator sim;


    public ScheduleRowTile(boolean oddRow) {

        super.setMaxWidth(600d);
        super.getStyleClass().add(oddRow ? "odd-row" : "even-row");
        super.setPadding(new Insets(5));

        var infoBox = new BorderPane();

        awayImage = new ImageView();
        awayImage.setFitWidth(LOGO_SIZE);
        awayImage.setFitHeight(LOGO_SIZE);
        infoBox.setLeft(awayImage);
        BorderPane.setAlignment(awayImage, Pos.CENTER);

        titleLabel = new Label("Match");
        titleLabel.getStyleClass().setAll("h4");
        titleLabel.getStyleClass().add("padding-5");
        infoBox.setCenter(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        homeImage = new ImageView();
        homeImage.setFitWidth(LOGO_SIZE);
        homeImage.setFitHeight(LOGO_SIZE);
        infoBox.setRight(homeImage);
        BorderPane.setAlignment(homeImage, Pos.CENTER);

        super.setCenter(infoBox);

        var buttonBar = new VBox(10);
        buttonBar.setPadding(new Insets(2,5,2,25));
        super.setRight(buttonBar);
        BorderPane.setAlignment(buttonBar, Pos.CENTER);
        BorderPane.setMargin(buttonBar, new Insets(5));
        buttonBar.setAlignment(Pos.CENTER);

        simButton = new Button("Simulate game");
        simButton.setMinWidth(120d);
        simButton.setMaxWidth(120d);
        buttonBar.getChildren().add(simButton);
        simButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> simulateMatch());

        playButton = new Button("Play game");
        playButton.setMinWidth(120d);
        playButton.setMaxWidth(120d);
        buttonBar.getChildren().add(playButton);
        playButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> playMatch());

        Button viewButton = new Button("View detail");
        viewButton.setMinWidth(120d);
        viewButton.setMaxWidth(120d);
        buttonBar.getChildren().add(viewButton);
        viewButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> viewMatch());

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

            sim = new MatchSimulator(match, MatchTab.getTarget());

            awayImage.setImage(new Image(getClass().getResourceAsStream(match.getAwayTeam().getLogo())));
            Tooltip.install(awayImage, new Tooltip(match.getAwayTeam().getName()));

            switch (match.getMatchInfo().getStatus()) {
                case SCHEDULED -> titleLabel.setText(match.getMatchInfo().getMatchDay().toString() + " @ " + "Ballpark");
                case IN_PROGRESS -> titleLabel.setText("LIVE");
                case PLAYED -> {
                    BoxScore score = match.getBoxScore();
                    titleLabel.setText(score.getTotalPoints(true) + " : " + score.getTotalPoints(false) + " (" + score.getInnings() + " INN)");
                    simButton.setDisable(true);
                    playButton.setDisable(true);
                }
            }

            homeImage.setImage(new Image(getClass().getResourceAsStream(match.getHomeTeam().getLogo())));
            Tooltip.install(homeImage, new Tooltip(match.getHomeTeam().getName()));

        } else {
            sim = null;
        }

    }

    private void playMatch() {
        if (sim != null) {
            sim.playMatch();
            IndexTab.getInstance().refreshSchedule();
        } else {
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Cannot play!");
            alert.showAndWait();
        }
    }

    private void simulateMatch() {
        if (sim != null) {
            sim.simulateMatch();
            IndexTab.getInstance().refreshSchedule();
        } else {
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Cannot simulate!");
            alert.showAndWait();
        }
    }

    private void viewMatch() {
        MatchTab.getInstance().setMatch(match);
        ContentFrame.getInstance().switchTo("Match");
    }

}
