package elrh.softman.logic.core.data;

import elrh.softman.logic.enums.PlayerLevel;
import java.util.Objects;
import java.util.UUID;
import lombok.*;

@Data @NoArgsConstructor(force = true) @RequiredArgsConstructor
public class LeagueInfo extends AbstractEntity {

    private UUID leagueId = UUID.randomUUID();

    private UUID leagueAbove;
    private UUID leagueBelow;

    @NonNull
    private String leagueName;

    @NonNull
    private PlayerLevel level;

    private final int year;

    private final int tier;

    private final int matchNumber;

    @Override
    public int hashCode() {
        return leagueName != null ? leagueName.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LeagueInfo other = (LeagueInfo) obj;
        return (Objects.equals(this.leagueName, other.leagueName));
    }

    @Override
    public UUID getId() {
        return getLeagueId();
    }

}
