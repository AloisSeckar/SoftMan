package elrh.softman.logic.db.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import elrh.softman.logic.enums.MatchStatus;
import elrh.softman.utils.ErrorUtils;
import lombok.*;

@DatabaseTable(tableName = "softman_matches")
@Data @NoArgsConstructor
public class MatchInfo {

    @DatabaseField(generatedId = true)
    private long matchId;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Date matchDay;

    private int leagueRound;

    private long awayTeamId;
    private long homeTeamId;

    private MatchStatus status;
    private boolean homeTeamFinishedBatting;

    public void setMatchDay(LocalDate matchDayLocal) {
        if (matchDayLocal != null) {
            // due to OrmLite data types
            // conversion via https://stackoverflow.com/a/40143687/3204544
            ZonedDateTime zdt = matchDayLocal.atStartOfDay(ZoneId.systemDefault());
            this.matchDay = Date.from(zdt.toInstant());
        } else {
            ErrorUtils.raise("Illegal attempt to set NULL 'matchDay'");
        }
    }

    public LocalDate getMatchDay() {
        // due to OrmLite data types
        // conversion via https://stackoverflow.com/a/40143687/3204544
        if (matchDay != null) {
            Instant instant = matchDay.toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            return zdt.toLocalDate();
        } else {
            ErrorUtils.raise("Illegal attempt to read 'matchDay' before it was set-up");
            return null;
        }
    }

}
