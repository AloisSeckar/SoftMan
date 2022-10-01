package elrh.softman.logic.db.orm.player;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.Result;
import elrh.softman.logic.db.AbstractDBEntity;
import elrh.softman.logic.db.GameDBManager;
import elrh.softman.logic.enums.PlayerGender;
import lombok.*;

@DatabaseTable(tableName = "softman_player_info")
@Data @EqualsAndHashCode(callSuper=true) @NoArgsConstructor
public class PlayerInfo extends AbstractDBEntity implements Comparable<PlayerInfo> {
    
    @DatabaseField(generatedId = true)
    private long playerId;
   
    @DatabaseField(canBeNull = false)
    private String name;
    
    @DatabaseField(canBeNull = false)
    private PlayerGender gender;

    @DatabaseField(canBeNull = false)
    private String img;
    
    @DatabaseField(canBeNull = false)
    private int birth;

    @DatabaseField(canBeNull = false)
    private int registered;
    
    @DatabaseField(canBeNull = false)
    private int number;
    
    @DatabaseField(canBeNull = false, foreign = true)
    private PlayerAttributes attributes;

    public PlayerInfo(String name, PlayerGender gender, int birth, int number) {
        this.name = name;
        this.gender = gender;

        // TODO default img should be different and stored in "faces" directory
        if (gender == PlayerGender.M) {
            this.img = "player-m.png";
        } else {
            this.img = "player-f.png";
        }

        this.birth = birth;
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

    @Override
    public long getId() {
        return getPlayerId();
    }

    @Override
    public Result persist() {
        return GameDBManager.getInstance().saveObject(PlayerInfo.class, this);
    }
    
}
