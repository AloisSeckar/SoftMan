package elrh.softman.db;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

// save-file header; purely a persistence concept, so it lives here rather than in softman-core
@Data @NoArgsConstructor
public class GameMeta {

    static final int SCHEMA_VERSION = 1;

    private UUID gameMetaId = UUID.randomUUID();

    private int schemaVersion = SCHEMA_VERSION;

    private String gameId;

    private LocalDate savedAt;

    private LocalDate currentDate;

    private LocalDate viewDate;

    private UUID activeClubId;

    private UUID focusedClubId;

    private UUID focusedTeamId;

}
