package elrh.softman.gui.tile;

import atlantafx.base.controls.Card;
import elrh.softman.gui.kit.Images;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Ratings;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.utils.GUIUtils;
import elrh.softman.logic.core.data.PlayerInfo;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;

public class PlayerInfoTile extends Card {

    private final Label nameLabel = new Label();
    private final Label ageLabel = new Label();

    private final ImageView imgView;

    private final ProgressBar fatigueBar = new ProgressBar(0);
    private final Label fatigueLabel = new Label();

    private final Ratings.Bar overallBar = Ratings.bar("Overall");
    private final Ratings.Bar battingBar;
    private final Ratings.Bar pitchingBar;
    private final Ratings.Bar fieldingBar;
    private final Ratings.Bar physicalBar;

    private final boolean full;

    public PlayerInfoTile(boolean full) {
        this.full = full;

        imgView = Images.rounded(full ? 6 : 4);
        imgView.setImage(GUIUtils.getImageOrDefault("/img/ball.png"));
        nameLabel.getStyleClass().add(full ? Tokens.DISPLAY : Tokens.TITLE);
        nameLabel.setWrapText(true);
        ageLabel.getStyleClass().add(Tokens.CAPTION);
        setHeader(Layouts.row(Space.M, imgView, Layouts.column(Space.XS, nameLabel, ageLabel)));

        // same shape as a rating bar, but untiered: high fatigue is not "elite"
        var fatigueName = new Label("Fatigue");
        fatigueName.setMinWidth(Layouts.em(8));
        fatigueName.setPrefWidth(Layouts.em(8));
        fatigueLabel.getStyleClass().add(Tokens.RATING_VALUE);
        fatigueLabel.setMinWidth(Layouts.em(2.5));
        var fatigueRow = Layouts.row(Space.S, fatigueName, Layouts.grow(fatigueBar), fatigueLabel);
        fatigueRow.getStyleClass().add(Tokens.RATING);

        var body = Layouts.column(Space.S, fatigueRow, overallBar);

        if (full) {
            battingBar = Ratings.bar("Batting");
            pitchingBar = Ratings.bar("Pitching");
            fieldingBar = Ratings.bar("Fielding");
            physicalBar = Ratings.bar("Physical");
            body.getChildren().addAll(new Separator(), battingBar, pitchingBar, fieldingBar, physicalBar);
        } else {
            battingBar = null;
            pitchingBar = null;
            fieldingBar = null;
            physicalBar = null;
        }

        setBody(body);
    }

    public void reload(PlayerInfo player) {
        if (player != null) {
            nameLabel.setText(player.getName());
            ageLabel.setText("#" + player.getNumber() + ", " + player.getAge() + " yrs");

            imgView.setImage(GUIUtils.getImageOrDefault("/img/" + player.getImg()));

            var attributes = player.getAttributes();
            fatigueBar.setProgress(attributes.getFatigue() / 100d);
            fatigueLabel.setText(String.valueOf(attributes.getFatigue()));

            overallBar.setValue(attributes.getTotal());
            if (full) {
                battingBar.setValue(attributes.getBattingSkill());
                pitchingBar.setValue(attributes.getPitchingSkill());
                fieldingBar.setValue(attributes.getFieldingSkill());
                physicalBar.setValue(attributes.getPhysicalSkill());
            }
        }
    }
}
