package elrh.softman.gui.tile;

import elrh.softman.gui.tab.MatchTab;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.League;
import elrh.softman.logic.Match;
import elrh.softman.logic.MatchSimulator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class MatchPreviewTile extends BorderPane {

    private static final int LOGO_WIDTH = 150;

    private MatchSimulator sim;

    public MatchPreviewTile() {

        super.getStyleClass().add("framed");
        super.setPadding(new Insets(5));

        var titleLabel = new Label("Upcomming match");
        titleLabel.getStyleClass().setAll("h3");
        titleLabel.getStyleClass().add("padding-5");
        super.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        var awayImage = new ImageView();
        awayImage.setFitWidth(LOGO_WIDTH);
        awayImage.setFitHeight(LOGO_WIDTH);
        awayImage.setImage(new Image(getClass().getResourceAsStream("/img/teams/reds.png")));
        super.setLeft(awayImage);

        var homeImage = new ImageView();
        homeImage.setFitWidth(LOGO_WIDTH);
        homeImage.setFitHeight(LOGO_WIDTH);
        homeImage.setImage(new Image(getClass().getResourceAsStream("/img/teams/blues.png")));
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

    private void playMatch() {
        if (sim == null) {
            League testLeague = AssociationManager.getInstance().getPlayerLeague();
            Match testMatch = testLeague.mockGetMatch();
            sim = new MatchSimulator(testMatch, MatchTab.getTarget());
        }

        sim.playMatch();
    }

    private void simulateMatch() {
        League testLeague = AssociationManager.getInstance().getPlayerLeague();
        testLeague.mockPlayMatch(MatchTab.getTarget());
    }

}
