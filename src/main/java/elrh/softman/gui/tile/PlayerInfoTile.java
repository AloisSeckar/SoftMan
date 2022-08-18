package elrh.softman.gui.tile;

import elrh.softman.db.orm.PlayerInfo;
import elrh.softman.logic.enums.Gender;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
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

    public PlayerInfoTile() {

        super.setAlignment(Pos.BASELINE_CENTER);
        super.getStyleClass().add("info");
        super.setSpacing(15);

        nameLabel = new Label();
        nameLabel.getStyleClass().add("player-name");
        super.getChildren().add(nameLabel);

        imgView = new ImageView();
        imgView.setFitWidth(150);
        imgView.setFitHeight(150);
        imgView.getStyleClass().add("player-img");
        super.getChildren().add(imgView);

        Image defaultImg = new Image(getClass().getResourceAsStream("/img/player-f.png"));
        imgView.setImage(defaultImg);

        ageLabel = new Label();
        ageLabel.getStyleClass().add("player-age");
        super.getChildren().add(ageLabel);

        overallGauge = GaugeBuilder.create()
                .skinType(Gauge.SkinType.FLAT)
                .minValue(0).maxValue(100).decimals(0)
                .minWidth(200).maxWidth(200)
                .barColor(Color.BLUEVIOLET)
                .title("Overall")
                .build();
        super.getChildren().add(overallGauge);

        var statsRow = new HBox(10);
        statsRow.setPadding(Insets.EMPTY);
        statsRow.setAlignment(Pos.BASELINE_CENTER);
        statsRow.getStyleClass().add("player-stats");
        super.getChildren().add(statsRow);

        battingGauge = GaugeBuilder.create()
                .skinType(Gauge.SkinType.FLAT)
                .minValue(0).maxValue(100).decimals(0)
                .minWidth(80).maxWidth(80)
                .barColor(Color.CRIMSON)
                .title("BAT")
                .build();
        statsRow.getChildren().add(battingGauge);

        pitchingGauge = GaugeBuilder.create()
                .skinType(Gauge.SkinType.FLAT)
                .minValue(0).maxValue(100).decimals(0)
                .minWidth(80).maxWidth(80)
                .barColor(Color.CRIMSON)
                .title("PIT")
                .build();
        statsRow.getChildren().add(pitchingGauge);

        fieldingGauge = GaugeBuilder.create()
                .skinType(Gauge.SkinType.FLAT)
                .minValue(0).maxValue(100).decimals(0)
                .minWidth(80).maxWidth(80)
                .barColor(Color.CRIMSON)
                .title("FLD")
                .build();
        statsRow.getChildren().add(fieldingGauge);

        physicalGauge = GaugeBuilder.create()
                .skinType(Gauge.SkinType.FLAT)
                .minValue(0).maxValue(100).decimals(0)
                .minWidth(80).maxWidth(80)
                .barColor(Color.CRIMSON)
                .title("PHY")
                .build();
        statsRow.getChildren().add(physicalGauge);

    }

    public void reload(PlayerInfo player) {
        if (player != null) {
            nameLabel.setText(player.getName());
            ageLabel.setText(player.getAge() + " yrs");

            if (Gender.F.equals(player.getGender())) {
                imgView.setImage(new Image(getClass().getResourceAsStream("/img/player-f.png")));
            } else {
                imgView.setImage(new Image(getClass().getResourceAsStream("/img/player-m.png")));
            }

            overallGauge.setValue(player.getAttributes().getTotal());
            battingGauge.setValue(player.getAttributes().getBattingSkill());
            pitchingGauge.setValue(player.getAttributes().getPitchingSkill());
            fieldingGauge.setValue(player.getAttributes().getFieldingSkill());
            physicalGauge.setValue(player.getAttributes().getPhysicalSkill());
        }
    }
}
