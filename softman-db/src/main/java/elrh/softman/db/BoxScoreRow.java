package elrh.softman.db;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

// per-inning runs are an int[] in BoxScore; stored as one row per inning to keep the save file readable
@Data @NoArgsConstructor
public class BoxScoreRow {

    private UUID boxScoreId = UUID.randomUUID();

    private UUID matchId;

    private int inning;

    private int awayRuns;

    private int homeRuns;

    // totals repeated on every row would be wasteful; only the first inning row carries them
    private int awayHits;

    private int homeHits;

    private int awayErrors;

    private int homeErrors;

    public BoxScoreRow(UUID matchId, int inning, int awayRuns, int homeRuns) {
        this.boxScoreId = UUID.randomUUID();
        this.matchId = matchId;
        this.inning = inning;
        this.awayRuns = awayRuns;
        this.homeRuns = homeRuns;
    }
}
