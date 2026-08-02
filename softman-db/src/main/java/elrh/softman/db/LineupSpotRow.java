package elrh.softman.db;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// one row per occupied lineup slot; a batting slot may hold several records once substitutions happen
@Data @NoArgsConstructor @AllArgsConstructor
public class LineupSpotRow {

    static final String KIND_POSITION = "POSITION";
    static final String KIND_SUBSTITUTE = "SUBSTITUTE";

    private UUID lineupSpotId = UUID.randomUUID();

    private UUID lineupId;

    private String slotKind;

    private int slotOrder;

    // order within a batting slot: 0 is the starter, higher values are substitutions in sequence
    private int slotDepth;

    private UUID playerRecordId;

    public LineupSpotRow(UUID lineupId, String slotKind, int slotOrder, int slotDepth, UUID playerRecordId) {
        this.lineupSpotId = UUID.randomUUID();
        this.lineupId = lineupId;
        this.slotKind = slotKind;
        this.slotOrder = slotOrder;
        this.slotDepth = slotDepth;
        this.playerRecordId = playerRecordId;
    }
}
