package elrh.softman.gui.tile;

import elrh.softman.utils.GUIUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ClubHeaderTile extends VBox {

    public ClubHeaderTile() {
        super.getStyleClass().add("framed");

        ImageView stadiumView = new ImageView();
        stadiumView.setFitWidth(600);
        stadiumView.setFitHeight(300);
        stadiumView.getStyleClass().add("framed");

        Image stadiumImg = GUIUtils.getImageOrDefault("/img/stadium.jpg");
        stadiumView.setImage(stadiumImg);

        super.getChildren().add(stadiumView);
    }

}
