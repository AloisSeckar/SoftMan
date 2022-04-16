package elrh.softman.gui.tile;

import elrh.softman.db.orm.PlayerInfo;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PlayerInfoTile extends VBox {

    private final Label nameLabel;
    private final Label ageLabel;

    public PlayerInfoTile() {

        super.setAlignment(Pos.BASELINE_CENTER);
        super.getStyleClass().add("info");
        super.setSpacing(15);

        nameLabel = new Label();
        nameLabel.getStyleClass().add("player-name");
        super.getChildren().add(nameLabel);

        var imgView = new ImageView();
        imgView.setFitWidth(150);
        imgView.setFitHeight(150);
        imgView.getStyleClass().add("player-img");
        super.getChildren().add(imgView);

        Image defaultImg = new Image(getClass().getResourceAsStream("/img/player.png"));
        imgView.setImage(defaultImg);

        ageLabel = new Label();
        ageLabel.getStyleClass().add("player-age");
        super.getChildren().add(ageLabel);

    }

    public void reload(PlayerInfo player) {
        if (player != null) {
            nameLabel.setText(player.getName());
            ageLabel.setText(player.getAge() + " yrs");
        }
    }
}
