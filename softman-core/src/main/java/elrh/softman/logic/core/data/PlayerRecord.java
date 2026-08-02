package elrh.softman.logic.core.data;

import elrh.softman.logic.enums.PlayerPosition;
import java.util.UUID;
import lombok.*;

@Data @EqualsAndHashCode(callSuper=true) @NoArgsConstructor
public class PlayerRecord extends AbstractEntity {

    private UUID playerRecordId = UUID.randomUUID();

    private PlayerInfo player;

    private PlayerPosition position;

    private PlayerStats stats = new PlayerStats();

    public PlayerRecord(PlayerInfo player, PlayerPosition position) {
        this.player = player;
        this.position = position;
    }

    @Override
    public String toString() {
        return player.toString();
    }

    @Override
    public UUID getId() {
        return playerRecordId;
    }
}
