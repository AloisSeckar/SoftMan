package elrh.softman.gui.tile;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Club;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ClubInfoTile extends VBox {

    private final Label nameLabel = new Label();
    private final Label stadiumLabel = new Label();
    private final Label moneyLabel = new Label();
    private final Label registeredLabel = new Label();
    private final Label ownerLabel = new Label();
    private final ImageView logoView = new ImageView();

    public ClubInfoTile() {
        super.getStyleClass().add("club-info");

        nameLabel.getStyleClass().setAll("h3");

        logoView.setFitWidth(100);
        logoView.setFitHeight(100);
        logoView.getStyleClass().add("framed");

        super.getChildren().addAll(nameLabel, logoView, stadiumLabel, moneyLabel, registeredLabel, ownerLabel);
    }

    public void reload(Club club) {
        if (club != null) {
            nameLabel.setText(club.getClubInfo().getName());
            stadiumLabel.setText(club.getClubInfo().getStadium());
            moneyLabel.setText("$ " + club.getClubInfo().getMoney());
            registeredLabel.setText(club.isActive() ? "Active" : "Inactive");
            ownerLabel.setText(AssociationManager.getInstance().getUser().getActiveClub() == club ? "Player" : "PC");

            Image defaultImg = new Image(getClass().getResourceAsStream(club.getClubInfo().getLogo()));
            logoView.setImage(defaultImg);
        }
    }

}
