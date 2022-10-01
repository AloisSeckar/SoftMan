package elrh.softman.logic.db.orm.player;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.Result;
import elrh.softman.logic.db.AbstractDBEntity;
import elrh.softman.logic.db.GameDBManager;
import java.util.Random;
import lombok.*;

@DatabaseTable(tableName = "softman_player_attributes")
@Data @EqualsAndHashCode(callSuper=true)
public class PlayerAttributes extends AbstractDBEntity {
    
    @DatabaseField(generatedId = true)
    private long playerAttributesId;
    
    @DatabaseField(canBeNull = false)
    private int battingPower;

    @DatabaseField(canBeNull = false)
    private int swingControl;
    
    @DatabaseField(canBeNull = false)
    private int pitchEvaluation;
    
    @DatabaseField(canBeNull = false)
    private int pitchingSpeed;
    
    @DatabaseField(canBeNull = false)
    private int ballControl;
    
    @DatabaseField(canBeNull = false)
    private int pitchVariety;
    
    @DatabaseField(canBeNull = false)
    private int fieldingReach;
    
    @DatabaseField(canBeNull = false)
    private int gloveControl;
    
    @DatabaseField(canBeNull = false)
    private int throwControl;
    
    @DatabaseField(canBeNull = false)
    private int strength;
    
    @DatabaseField(canBeNull = false)
    private int speed;
    
    @DatabaseField(canBeNull = false)
    private int endurance;
    
    @DatabaseField(canBeNull = false)
    private int recovery;
    
    @DatabaseField(canBeNull = false)
    private int talent;
    
    @DatabaseField(canBeNull = false)
    private int dedication;
    
    @DatabaseField(canBeNull = false)
    private int luck;
    
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
    public long getId() {
        return getPlayerAttributesId();
    }

    @Override
    public Result persist() {
        return GameDBManager.getInstance().saveObject(PlayerAttributes.class, this);
    }

}
