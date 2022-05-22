package elrh.softman.gui.tile;

import elrh.softman.gui.tab.MatchTab;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Match;
import elrh.softman.logic.MatchSimulator;
import elrh.softman.logic.Team;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class MatchPreviewTile extends BorderPane {

    private static final int LOGO_WIDTH = 150;

    private final ImageView awayImage;
    private final ImageView homeImage;

    public MatchPreviewTile() {

        super.getStyleClass().add("framed");
        super.setPadding(new Insets(5));

        var titleLabel = new Label("Upcomming match");
        titleLabel.getStyleClass().setAll("h3");
        titleLabel.getStyleClass().add("padding-5");
        super.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        awayImage = new ImageView();
        awayImage.setFitWidth(LOGO_WIDTH);
        awayImage.setFitHeight(LOGO_WIDTH);
        super.setLeft(awayImage);

        homeImage = new ImageView();
        homeImage.setFitWidth(LOGO_WIDTH);
        homeImage.setFitHeight(LOGO_WIDTH);
        super.setRight(homeImage);

        var label = new Label(" @ ");
        label.getStyleClass().setAll("h3");
        super.setCenter(label);

        var buttonBar = new HBox(10);
        buttonBar.getStyleClass().add("padding-5");
        super.setBottom(buttonBar);
        BorderPane.setAlignment(buttonBar, Pos.CENTER);
        BorderPane.setMargin(buttonBar, new Insets(5));
        buttonBar.setAlignment(Pos.CENTER);

        var simButton = new Button("Simulate game");
        buttonBar.getChildren().add(simButton);
        simButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> simulateMatch());

        var playButton = new Button("Play game");
        buttonBar.getChildren().add(playButton);
        playButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> playMatch());

    }

    public void setMatch(Match match) {
        if (match != null) {
            awayImage.setImage(new Image(getClass().getResourceAsStream(match.getAwayTeam().getLogo())));
            homeImage.setImage(new Image(getClass().getResourceAsStream(match.getHomeTeam().getLogo())));
        }
    }

    private void playMatch() {
        var sim = getMatchSimulator();
        if (sim != null) {
            sim.playMatch();
        } else {
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("No match today");
            alert.showAndWait();
        }
    }

    private void simulateMatch() {
        var sim = getMatchSimulator();
        if (sim != null) {
            sim.simulateMatch();
        } else {
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("No match today");
            alert.showAndWait();
        }
    }

    private MatchSimulator getMatchSimulator() {
        MatchSimulator ret = null;

        var testMatch = AssociationManager.getInstance().getTodayMatchForPlayer();
        if (testMatch != null) {
            ret = new MatchSimulator(testMatch, MatchTab.getTarget());
        }

        return ret;
    }

}
