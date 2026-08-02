package elrh.softman.db;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// team roster membership; the domain models this as Team.players, which has no table of its own
@Data @NoArgsConstructor @AllArgsConstructor
public class TeamPlayerRow {

    private UUID teamPlayerId = UUID.randomUUID();

    private UUID teamId;

    private UUID playerId;

    private int ord;

    public TeamPlayerRow(UUID teamId, UUID playerId, int ord) {
        this.teamPlayerId = UUID.randomUUID();
        this.teamId = teamId;
        this.playerId = playerId;
        this.ord = ord;
    }
}
