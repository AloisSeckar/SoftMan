package elrh.softman.logic.core.data;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.enums.PlayerGender;
import java.util.UUID;
import lombok.*;

@Data @EqualsAndHashCode(callSuper=true) @NoArgsConstructor
public class PlayerInfo extends AbstractEntity implements Comparable<PlayerInfo> {

    private UUID playerId = UUID.randomUUID();

    private String name;

    private PlayerGender gender;

    private String img;

    private int birth;

    private int registered;

    private int number;

    private PlayerAttributes attributes;

    public PlayerInfo(String name, PlayerGender gender, int birth, int number) {
        this.name = name;
        this.gender = gender;

        if (gender == PlayerGender.M) {
            this.img = "vecteezy/avatar-boy.jpg";
        } else {
            this.img = "vecteezy/avatar-girl.jpg";
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
    public UUID getId() {
        return getPlayerId();
    }

}
