package elrh.softman.logic.core.data;

import java.util.Random;
import java.util.UUID;
import lombok.*;

@Data @EqualsAndHashCode(callSuper=true)
public class PlayerAttributes extends AbstractEntity {

    private UUID playerAttributesId = UUID.randomUUID();

    private int battingPower;

    private int swingControl;

    private int pitchEvaluation;

    private int pitchingSpeed;

    private int ballControl;

    private int pitchVariety;

    private int fieldingReach;

    private int gloveControl;

    private int throwControl;

    private int strength;

    private int speed;

    private int endurance;

    private int recovery;

    private int talent;

    private int dedication;

    private int luck;

    private int fatigue;

    public PlayerAttributes() {
        Random rand = new Random();
        this.battingPower = rand.nextInt(100) + 1;
        this.swingControl = rand.nextInt(100) + 1;
        this.pitchEvaluation = rand.nextInt(100) + 1;
        this.pitchingSpeed = rand.nextInt(100) + 1;
        this.ballControl = rand.nextInt(100) + 1;
        this.pitchVariety = rand.nextInt(100) + 1;
        this.fieldingReach = rand.nextInt(100) + 1;
        this.gloveControl = rand.nextInt(100) + 1;
        this.throwControl = rand.nextInt(100) + 1;
        this.strength = rand.nextInt(100) + 1;
        this.speed = rand.nextInt(100) + 1;
        this.endurance = rand.nextInt(100) + 1;
        this.recovery = rand.nextInt(100) + 1;
        this.talent = rand.nextInt(100) + 1;
        this.dedication = rand.nextInt(100) + 1;
        this.luck = rand.nextInt(100) + 1;
        this.fatigue = 0;
    }

    public int getBattingSkill() {
        return (battingPower + swingControl + pitchEvaluation) / 3;
    }

    public int getPitchingSkill() {
        return (pitchingSpeed + ballControl + throwControl) / 3;
    }

    public int getFieldingSkill() {
        return (fieldingReach + gloveControl + pitchVariety) / 3;
    }

    public int getPhysicalSkill() {
        return (strength + speed + endurance + recovery) / 4;
    }

    public int getTotal() {
        return (getBattingSkill() + getPitchingSkill() + getFieldingSkill() + getPhysicalSkill()) / 4;
    }

    @Override
    public UUID getId() {
        return getPlayerAttributesId();
    }

}
