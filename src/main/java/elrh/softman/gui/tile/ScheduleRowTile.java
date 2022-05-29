package elrh.softman.gui.tile;

import com.j256.ormlite.stmt.query.In;
import elrh.softman.gui.tab.MatchTab;
import elrh.softman.logic.Match;
import elrh.softman.logic.MatchSimulator;
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

        var simButton = new Button("Simulate game");
        simButton.setMinWidth(120d);
        simButton.setMaxWidth(120d);
        buttonBar.getChildren().add(simButton);
        simButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> simulateMatch());

        var playButton = new Button("Play game");
        playButton.setMinWidth(120d);
        playButton.setMaxWidth(120d);
        buttonBar.getChildren().add(playButton);
        playButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> playMatch());

    }

    public void setMatch(Match match) {
        if (match != null) {

            sim = new MatchSimulator(match, MatchTab.getTarget());

            awayImage.setImage(new Image(getClass().getResourceAsStream(match.getAwayTeam().getLogo())));
            Tooltip.install(awayImage, new Tooltip(match.getAwayTeam().getName()));

            titleLabel.setText(match.getMatchInfo().getMatchDay().toString() + " @ " + "Ballpark");

            homeImage.setImage(new Image(getClass().getResourceAsStream(match.getHomeTeam().getLogo())));
            Tooltip.install(homeImage, new Tooltip(match.getHomeTeam().getName()));

        } else {
            sim = null;
        }
    }

    private void playMatch() {
        if (sim != null) {
            sim.playMatch();
        } else {
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Cannot play!");
            alert.showAndWait();
        }
    }

    private void simulateMatch() {
        if (sim != null) {
            sim.simulateMatch();
        } else {
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Cannot simulate!");
            alert.showAndWait();
        }
    }

}