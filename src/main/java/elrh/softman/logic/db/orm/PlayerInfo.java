package elrh.softman.logic.db.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.enums.PlayerGender;
import lombok.*;

@DatabaseTable(tableName = "softman_players")
@Data @NoArgsConstructor
public class PlayerInfo implements Comparable<PlayerInfo> {
    
    @DatabaseField(generatedId = true)
    private long playerId;
   
    @DatabaseField(canBeNull = false)
    private String name;
    
    @DatabaseField(canBeNull = false)
    private PlayerGender gender;
    
    @DatabaseField(canBeNull = false)
    private int birth;

    @DatabaseField(canBeNull = false)
    private int registered;
    
    @DatabaseField(canBeNull = false)
    private int number;
    
    @DatabaseField(canBeNull = false, foreign = true)
    private PlayerAttributes attributes;
    
    public PlayerInfo(String player, int number) {
        this.name = player;
        this.gender = PlayerGender.M;
        this.birth = 2000;
        this.number = number;
        this.attributes = new PlayerAttributes();
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
        return AssociationManager.getInstance().getClock().getYear() - birth;
    }
    
    public PlayerAttributes getAttributes() {
        return attributes;
    }
    
}
