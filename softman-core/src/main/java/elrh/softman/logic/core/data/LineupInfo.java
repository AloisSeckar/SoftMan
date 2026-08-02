package elrh.softman.logic.core.data;

import java.util.UUID;
import lombok.*;

@Data @EqualsAndHashCode(callSuper=true) @NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class LineupInfo extends AbstractEntity {

    private UUID lineupId = UUID.randomUUID();

    private final UUID teamId;

    @NonNull
    private String teamName;

    @NonNull
    private String teamShortName;

    @NonNull
    private String teamLogo;

    @Override
    public UUID getId() {
        return lineupId;
    }

}
