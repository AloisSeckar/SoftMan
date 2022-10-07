package elrh.softman.gui.tile;

import elrh.softman.logic.db.orm.player.PlayerAttributes;
import elrh.softman.utils.FormatUtils;
import elrh.softman.utils.GUIUtils;
import eu.hansolo.medusa.Gauge;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class PlayerAttributesTile extends VBox {
    
    public static final int BIG = 120;
    public static final int SMALL = 85;

    private final Gauge battingGauge;
    private final Gauge battingPowerGauge;
    private final Gauge swingControlGauge;
    private final Gauge pitchEvaluationGauge;

    private final Gauge pitchingGauge;
    private final Gauge pitchingSpeedGauge;
    private final Gauge ballControlGauge;
    private final Gauge pitchVarietyGauge;

    private final Gauge fieldingGauge;
    private final Gauge fieldingReachGauge;
    private final Gauge gloveControlGauge;
    private final Gauge throwControlGauge;

    private final Gauge physicalGauge;
    private final Gauge strengthGauge;
    private final Gauge speedGauge;
    private final Gauge enduranceGauge;

    public PlayerAttributesTile() {

        super.setPadding(FormatUtils.PADDING_10);

        battingGauge = GUIUtils.getGauge(BIG, Color.CRIMSON,"Batting");
        battingPowerGauge = GUIUtils.getGauge(SMALL, Color.DARKRED,"Power");
        swingControlGauge = GUIUtils.getGauge(SMALL, Color.DARKRED,"Control");
        pitchEvaluationGauge = GUIUtils.getGauge(SMALL, Color.DARKRED,"Evaluation");
        addGaugeRow(battingGauge, battingPowerGauge, swingControlGauge, pitchEvaluationGauge);

        pitchingGauge = GUIUtils.getGauge(BIG, Color.GREEN,"Pitching");
        pitchingSpeedGauge = GUIUtils.getGauge(SMALL, Color.SEAGREEN,"Speed");
        ballControlGauge = GUIUtils.getGauge(SMALL, Color.SEAGREEN,"Control");
        pitchVarietyGauge = GUIUtils.getGauge(SMALL, Color.SEAGREEN,"Variety");
        addGaugeRow(pitchingGauge, pitchingSpeedGauge, ballControlGauge, pitchVarietyGauge);

        fieldingGauge = GUIUtils.getGauge(BIG, Color.GOLD,"Fielding");
        fieldingReachGauge = GUIUtils.getGauge(SMALL, Color.YELLOW,"Reach");
        gloveControlGauge = GUIUtils.getGauge(SMALL, Color.YELLOW,"Glove");
        throwControlGauge = GUIUtils.getGauge(SMALL, Color.YELLOW,"Throw");
        addGaugeRow(fieldingGauge, fieldingReachGauge, gloveControlGauge, throwControlGauge);

        physicalGauge = GUIUtils.getGauge(BIG, Color.VIOLET,"Physical");
        strengthGauge = GUIUtils.getGauge(SMALL, Color.PURPLE,"Strength");
        speedGauge = GUIUtils.getGauge(SMALL, Color.PURPLE,"Speed");
        enduranceGauge = GUIUtils.getGauge(SMALL, Color.PURPLE,"Endurance");
        addGaugeRow(physicalGauge, strengthGauge, speedGauge, enduranceGauge);
    }

    private void addGaugeRow(Gauge... gauges) {
        var row = new HBox(10, gauges);
        row.setPadding(FormatUtils.PADDING_10);
        super.getChildren().add(row);
    }

    public void reload(PlayerAttributes attributes) {
        battingGauge.setValue(attributes.getBattingSkill());
        battingPowerGauge.setValue(attributes.getBattingPower());
        swingControlGauge.setValue(attributes.getSwingControl());
        pitchEvaluationGauge.setValue(attributes.getPitchEvaluation());

        pitchingGauge.setValue(attributes.getPitchingSkill());
        pitchingSpeedGauge.setValue(attributes.getPitchingSpeed());
        ballControlGauge.setValue(attributes.getBallControl());
        pitchVarietyGauge.setValue(attributes.getPitchVariety());

        fieldingGauge.setValue(attributes.getFieldingSkill());
        fieldingReachGauge.setValue(attributes.getFieldingReach());
        gloveControlGauge.setValue(attributes.getGloveControl());
        throwControlGauge.setValue(attributes.getThrowControl());

        physicalGauge.setValue(attributes.getPhysicalSkill());
        strengthGauge.setValue(attributes.getStrength());
        speedGauge.setValue(attributes.getSpeed());
        enduranceGauge.setValue(attributes.getEndurance());
    }

}
