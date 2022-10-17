package elrh.softman.logic.db.orm.match;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import elrh.softman.logic.Result;
import elrh.softman.logic.db.AbstractDBEntity;
import elrh.softman.logic.db.GameDBManager;
import elrh.softman.logic.enums.MatchStatus;
import elrh.softman.utils.ErrorUtils;
import lombok.*;

@DatabaseTable(tableName = "softman_match_info")
@Data @EqualsAndHashCode(callSuper=true) @NoArgsConstructor
public class MatchInfo extends AbstractDBEntity {

    @DatabaseField(generatedId = true)
    private long matchId;

    @DatabaseField(canBeNull = false)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Date matchDay;

    @DatabaseField(canBeNull = false)
    private long leagueId;
    @DatabaseField(canBeNull = false)
    private int leagueRound;

    @DatabaseField(canBeNull = false)
    private long awayTeamId;
    @DatabaseField(canBeNull = false)
    private long homeTeamId;

    @DatabaseField(canBeNull = false)
    private MatchStatus status;
    @DatabaseField(canBeNull = false)
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

    @Override
    public long getId() {
        return getMatchId();
    }

    @Override
    public Result persist() {
        return GameDBManager.getInstance().saveObject(MatchInfo.class, this);
    }

}
