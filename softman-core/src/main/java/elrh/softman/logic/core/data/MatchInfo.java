package elrh.softman.logic.core.data;

import elrh.softman.logic.enums.MatchStatus;
import elrh.softman.utils.ErrorUtils;
import java.time.LocalDate;
import java.util.UUID;
import lombok.*;

@Data @EqualsAndHashCode(callSuper=true) @NoArgsConstructor
public class MatchInfo extends AbstractEntity {

    private UUID matchId = UUID.randomUUID();

    private int matchNumber;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private LocalDate matchDay;

    private String stadium;

    private UUID leagueId;
    private int leagueRound;

    private UUID awayTeamId;
    private UUID homeTeamId;

    private UUID awayLineupId;
    private UUID homeLineupId;

    private MatchStatus status;
    private boolean homeTeamFinishedBatting;

    public void setMatchDay(LocalDate matchDay) {
        if (matchDay != null) {
            this.matchDay = matchDay;
        } else {
            ErrorUtils.raise("Illegal attempt to set NULL 'matchDay'");
        }
    }

    public LocalDate getMatchDay() {
        if (matchDay == null) {
            ErrorUtils.raise("Illegal attempt to read 'matchDay' before it was set-up");
        }
        return matchDay;
    }

    @Override
    public UUID getId() {
        return getMatchId();
    }

}
