package elrh.softman.gui.tile;

import atlantafx.base.controls.Card;
import elrh.softman.gui.kit.Cards;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Ratings;
import elrh.softman.logic.core.data.PlayerAttributes;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class PlayerAttributesTile extends Card {

    private final Ratings.Bar battingBar = Ratings.bar("Overall");
    private final Ratings.Bar battingPowerBar = Ratings.bar("Power");
    private final Ratings.Bar swingControlBar = Ratings.bar("Swing control");
    private final Ratings.Bar pitchEvaluationBar = Ratings.bar("Pitch evaluation");

    private final Ratings.Bar pitchingBar = Ratings.bar("Overall");
    private final Ratings.Bar pitchingSpeedBar = Ratings.bar("Speed");
    private final Ratings.Bar ballControlBar = Ratings.bar("Ball control");
    private final Ratings.Bar pitchVarietyBar = Ratings.bar("Variety");

    private final Ratings.Bar fieldingBar = Ratings.bar("Overall");
    private final Ratings.Bar fieldingReachBar = Ratings.bar("Reach");
    private final Ratings.Bar gloveControlBar = Ratings.bar("Glove control");
    private final Ratings.Bar throwControlBar = Ratings.bar("Throw control");

    private final Ratings.Bar physicalBar = Ratings.bar("Overall");
    private final Ratings.Bar strengthBar = Ratings.bar("Strength");
    private final Ratings.Bar speedBar = Ratings.bar("Speed");
    private final Ratings.Bar enduranceBar = Ratings.bar("Endurance");

    public PlayerAttributesTile() {
        var grid = new GridPane(Space.XL.getPx(), Space.L.getPx());
        var half = new ColumnConstraints();
        half.setPercentWidth(50);
        grid.getColumnConstraints().addAll(half, half);

        grid.add(Ratings.group("Batting", battingBar, battingPowerBar, swingControlBar, pitchEvaluationBar), 0, 0);
        grid.add(Ratings.group("Pitching", pitchingBar, pitchingSpeedBar, ballControlBar, pitchVarietyBar), 1, 0);
        grid.add(Ratings.group("Fielding", fieldingBar, fieldingReachBar, gloveControlBar, throwControlBar), 0, 1);
        grid.add(Ratings.group("Physical", physicalBar, strengthBar, speedBar, enduranceBar), 1, 1);

        setHeader(Cards.title("Attributes"));
        setBody(grid);
    }

    public void reload(PlayerAttributes attributes) {
        battingBar.setValue(attributes.getBattingSkill());
        battingPowerBar.setValue(attributes.getBattingPower());
        swingControlBar.setValue(attributes.getSwingControl());
        pitchEvaluationBar.setValue(attributes.getPitchEvaluation());

        pitchingBar.setValue(attributes.getPitchingSkill());
        pitchingSpeedBar.setValue(attributes.getPitchingSpeed());
        ballControlBar.setValue(attributes.getBallControl());
        pitchVarietyBar.setValue(attributes.getPitchVariety());

        fieldingBar.setValue(attributes.getFieldingSkill());
        fieldingReachBar.setValue(attributes.getFieldingReach());
        gloveControlBar.setValue(attributes.getGloveControl());
        throwControlBar.setValue(attributes.getThrowControl());

        physicalBar.setValue(attributes.getPhysicalSkill());
        strengthBar.setValue(attributes.getStrength());
        speedBar.setValue(attributes.getSpeed());
        enduranceBar.setValue(attributes.getEndurance());
    }

}
