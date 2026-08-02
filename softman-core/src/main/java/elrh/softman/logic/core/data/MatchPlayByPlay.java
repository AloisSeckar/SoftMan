package elrh.softman.logic.core.data;

import java.util.UUID;
import lombok.*;

@Data @EqualsAndHashCode(callSuper=true) @NoArgsConstructor
public class MatchPlayByPlay extends AbstractEntity {

    private UUID matchPlayByPlayId = UUID.randomUUID();

    private UUID matchId;

    private int ord;

    private String play;

    public MatchPlayByPlay(UUID matchId, int ord, String play) {
        this.matchId = matchId;
        this.ord = ord;
        this.play = play;
    }

    @Override
    public UUID getId() {
        return getMatchPlayByPlayId();
    }

}
