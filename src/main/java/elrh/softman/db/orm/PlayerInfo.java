package elrh.softman.db.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.constants.Constants;
import elrh.softman.logic.AssociationManager;
import java.util.Random;
import lombok.*;

@DatabaseTable(tableName = "softman_players")
@Data @NoArgsConstructor
public class PlayerInfo implements Comparable<PlayerInfo> {
    
    @DatabaseField(generatedId = true)
    private long playerId;
   
    @DatabaseField(canBeNull = false)
    private String name;
    
    @DatabaseField(canBeNull = false)
    private String gender;
    
    @DatabaseField(canBeNull = false)
    private int birth;
    
    @DatabaseField(canBeNull = false)
    private int number;
    
    @DatabaseField(canBeNull = false)
    private int battingSkill;
    
    @DatabaseField(canBeNull = false)
    private int pitchingSkill;
    
    @DatabaseField(canBeNull = false)
    private int fieldingSkill;
    
    public PlayerInfo(String player, int number) {
        this.name = player;
        this.gender = Constants.GENDER_MALE;
        this.birth = 2000;
        this.number = number;
        
        Random rand = new Random();
        this.battingSkill = rand.nextInt(100) + 1;
        this.pitchingSkill = rand.nextInt(100) + 1;
        this.fieldingSkill = rand.nextInt(100) + 1;
    }

    @Override
    public String toString() {
        return "#" + number + " " + name;
    }

    @Override
    public int compareTo(PlayerInfo other) {
        int ret;
        
        if (other != null) {
            ret = Integer.compare(this.getNumber(), other.getNumber());
        } else {
            ret = 1;
        }
        
        return ret;
    }
    
    public int getAge() {
        return AssociationManager.getInstance().getSeason() - birth;
    }
    
}
