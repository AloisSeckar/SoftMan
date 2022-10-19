package elrh.softman.gui.tile;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Club;
import elrh.softman.utils.GUIUtils;
import elrh.softman.utils.Utils;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ClubInfoTile extends VBox {

    private final Label nameLabel = new Label();
    private final Label stadiumLabel = new Label();
    private final Label moneyLabel = new Label();
    private final Label registeredLabel = new Label();
    private final Label ownerLabel = new Label();
    private final ImageView logoView = new ImageView();

    public ClubInfoTile() {
        super.getStyleClass().add("club-info");
        super.getStyleClass().add("framed");

        nameLabel.getStyleClass().setAll("h3");

        logoView.setFitWidth(100);
        logoView.setFitHeight(100);
        logoView.getStyleClass().add("framed");

        var box = new VBox();
        box.getStyleClass().add("framed");
        box.setAlignment(Pos.CENTER);
        box.setMinWidth(300);
        box.setMaxWidth(300);
        Utils.setBackgroundColor(box, Color.CORNSILK);
        box.getChildren().addAll(nameLabel, logoView, stadiumLabel, moneyLabel, registeredLabel, ownerLabel);

        super.getChildren().add(box);
    }

    public void reload(Club club) {
        if (club != null) {
            nameLabel.setText(club.getClubInfo().getName());
            stadiumLabel.setText(club.getClubInfo().getStadium());
            moneyLabel.setText("$ " + club.getClubInfo().getMoney());
            registeredLabel.setText(club.isActive() ? "Active" : "Inactive");
            ownerLabel.setText(AssociationManager.getInstance().getUser().getActiveClub() == club ? "Player" : "PC");

            Image defaultImg = GUIUtils.getImageOrDefault(club.getClubInfo().getLogo());
            logoView.setImage(defaultImg);

            Utils.setBackgroundColor(ClubInfoTile.this, club.getColor());
        } else {
            nameLabel.setText("No club selected");
            stadiumLabel.setText("");
            moneyLabel.setText("");
            registeredLabel.setText("");
            ownerLabel.setText("");

            Image defaultImg = GUIUtils.getImageOrDefault("/img/ball.png");
            logoView.setImage(defaultImg);

            Utils.setBackgroundColor(ClubInfoTile.this, Color.GRAY);
        }
    }

}
