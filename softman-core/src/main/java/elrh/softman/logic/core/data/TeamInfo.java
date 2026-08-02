package elrh.softman.logic.core.data;

import elrh.softman.logic.enums.PlayerLevel;
import java.util.UUID;
import lombok.*;

@Data @EqualsAndHashCode(callSuper=true) @NoArgsConstructor
@RequiredArgsConstructor // TODO why RequiredArgsConstructor not working out of the box with Data?
public class TeamInfo extends AbstractEntity {

    private UUID teamId = UUID.randomUUID();

    @NonNull
    private PlayerLevel level;

    @NonNull
    private String name;

    @NonNull
    private ClubInfo clubInfo;

    private LeagueInfo leagueInfo;

    @Override
    public UUID getId() {
        return getTeamId();
    }

}
