package elrh.softman.gui.tile;

import elrh.softman.logic.db.orm.player.PlayerInfo;
import elrh.softman.utils.GUIUtils;
import eu.hansolo.medusa.Gauge;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PlayerInfoTile extends VBox {

    private final Label nameLabel;
    private final Label ageLabel;

    private final ImageView imgView;

    private final Gauge overallGauge;
    private final Gauge battingGauge;
    private final Gauge pitchingGauge;
    private final Gauge fieldingGauge;
    private final Gauge physicalGauge;

    private final boolean full;

    public PlayerInfoTile(boolean full) {
        this.full = full;

        super.setAlignment(Pos.BASELINE_CENTER);
        super.getStyleClass().add(full ? "info-full" : "info-brief");
        super.setSpacing(15);

        nameLabel = new Label();
        nameLabel.getStyleClass().add("player-name");
        super.getChildren().add(nameLabel);

        imgView = new ImageView();
        imgView.setFitWidth(150);
        imgView.setFitHeight(150);
        imgView.getStyleClass().add("player-img");
        super.getChildren().add(imgView);

        Image defaultImg = GUIUtils.getImageOrDefault("/img/ball.png");
        imgView.setImage(defaultImg);

        ageLabel = new Label();
        ageLabel.getStyleClass().add("player-age");
        super.getChildren().add(ageLabel);

        var size = full ? 200 : 100;
        overallGauge = GUIUtils.getGauge(size, Color.BLUEVIOLET,"Overall");
        super.getChildren().add(overallGauge);

        if (full) {
            var statsRow = new HBox(10);
            statsRow.setPadding(Insets.EMPTY);
            statsRow.setAlignment(Pos.BASELINE_CENTER);
            statsRow.getStyleClass().add("player-stats");
            super.getChildren().add(statsRow);

            battingGauge = GUIUtils.getGauge(80, Color.CRIMSON,"BAT");
            statsRow.getChildren().add(battingGauge);

            pitchingGauge = GUIUtils.getGauge(80, Color.CRIMSON,"PIT");
            statsRow.getChildren().add(pitchingGauge);

            fieldingGauge = GUIUtils.getGauge(80, Color.CRIMSON,"FLD");
            statsRow.getChildren().add(fieldingGauge);

            physicalGauge = GUIUtils.getGauge(80, Color.CRIMSON,"PHY");
            statsRow.getChildren().add(physicalGauge);
        } else {
            battingGauge = null;
            pitchingGauge = null;
            fieldingGauge = null;
            physicalGauge = null;
        }
    }

    public void reload(PlayerInfo player) {
        if (player != null) {
            nameLabel.setText(player.getName());
            ageLabel.setText(player.getAge() + " yrs");

            imgView.setImage(GUIUtils.getImageOrDefault("/img/" + player.getImg()));

            overallGauge.setValue(player.getAttributes().getTotal());
            if (full) {
                battingGauge.setValue(player.getAttributes().getBattingSkill());
                pitchingGauge.setValue(player.getAttributes().getPitchingSkill());
                fieldingGauge.setValue(player.getAttributes().getFieldingSkill());
                physicalGauge.setValue(player.getAttributes().getPhysicalSkill());
            }
        }
    }
}
