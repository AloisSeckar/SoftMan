package elrh.softman.db;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseFieldConfig;
import com.j256.ormlite.table.DatabaseTableConfig;
import elrh.softman.logic.core.data.*;
import elrh.softman.logic.core.stats.Standing;
import java.util.ArrayList;
import java.util.List;

// the schema lives here, not on the domain classes, so softman-core stays free of any persistence dependency
final class TableConfigs {

    private TableConfigs() {
    }

    // declared in dependency order so foreign() can wire up the nested config without annotation scanning
    static final DatabaseTableConfig<ClubInfo> CLUB_INFO = config(ClubInfo.class, "softman_club_info",
        id("clubId"),
        col("name"), col("shortName"), col("logo"), col("city"), col("stadium"),
        col("color"), col("registered"), col("money"));

    static final DatabaseTableConfig<LeagueInfo> LEAGUE_INFO = config(LeagueInfo.class, "softman_league_info",
        id("leagueId"),
        uuid("leagueAbove"), uuid("leagueBelow"),
        col("leagueName"), col("level"), col("year"), col("tier"), col("matchNumber"));

    static final DatabaseTableConfig<PlayerAttributes> PLAYER_ATTRIBUTES = config(PlayerAttributes.class, "softman_player_attributes",
        id("playerAttributesId"),
        col("battingPower"), col("swingControl"), col("pitchEvaluation"),
        col("pitchingSpeed"), col("ballControl"), col("pitchVariety"),
        col("fieldingReach"), col("gloveControl"), col("throwControl"),
        col("strength"), col("speed"), col("endurance"), col("recovery"),
        col("talent"), col("dedication"), col("luck"), col("fatigue"));

    static final DatabaseTableConfig<PlayerInfo> PLAYER_INFO = config(PlayerInfo.class, "softman_player_info",
        id("playerId"),
        col("name"), col("gender"), col("img"), col("birth"), col("registered"), col("number"),
        foreign("attributes", PLAYER_ATTRIBUTES));

    static final DatabaseTableConfig<PlayerStats> PLAYER_STATS = config(PlayerStats.class, "softman_player_stats",
        id("playerStatsId"),
        uuid("matchId"), col("matchStr"), uuid("playerId"), col("playerStr"), col("games"),
        // batter
        col("bPA"), col("bAB"), col("bR"), col("bH"), col("b2B"), col("b3B"), col("bHR"),
        col("bSH"), col("bSF"), col("bBB"), col("bHP"), col("bSB"), col("bCS"), col("bK"), col("bRB"),
        // fielder
        col("fPO"), col("fA"), col("fE"), col("fDP"), col("fIP"),
        // pitcher
        col("pW"), col("pL"), col("pS"), col("pBF"), col("pAB"), col("pR"), col("pER"),
        col("pH"), col("p2B"), col("p3B"), col("pHR"), col("pSH"), col("pSF"), col("pBB"),
        col("pHP"), col("pK"), col("pWP"), col("pNP"), col("pNS"),
        // catcher
        col("cPB"), col("cSB"), col("cCS"));

    static final DatabaseTableConfig<TeamInfo> TEAM_INFO = config(TeamInfo.class, "softman_team_info",
        id("teamId"),
        col("level"), col("name"),
        foreign("clubInfo", CLUB_INFO), foreign("leagueInfo", LEAGUE_INFO));

    static final DatabaseTableConfig<LineupInfo> LINEUP_INFO = config(LineupInfo.class, "softman_lineup_info",
        id("lineupId"),
        uuid("teamId"),
        col("teamName"), col("teamShortName"), col("teamLogo"));

    static final DatabaseTableConfig<MatchInfo> MATCH_INFO = config(MatchInfo.class, "softman_match_info",
        id("matchId"),
        col("matchNumber"),
        localDate("matchDay"),
        col("stadium"),
        uuid("leagueId"), col("leagueRound"),
        uuid("awayTeamId"), uuid("homeTeamId"),
        uuid("awayLineupId"), uuid("homeLineupId"),
        col("status"), col("homeTeamFinishedBatting"));

    static final DatabaseTableConfig<MatchPlayByPlay> MATCH_PLAY_BY_PLAY = config(MatchPlayByPlay.class, "softman_match_pbp",
        id("matchPlayByPlayId"),
        uuid("matchId"), col("ord"), col("play"));

    static final DatabaseTableConfig<PlayerRecord> PLAYER_RECORD = config(PlayerRecord.class, "softman_player_record",
        id("playerRecordId"),
        foreign("player", PLAYER_INFO), col("position"), foreign("stats", PLAYER_STATS));

    static final DatabaseTableConfig<Standing> STANDING = config(Standing.class, "softman_standing",
        id("standingId"),
        uuid("leagueId"), uuid("teamId"), col("team"),
        col("games"), col("wins"), col("loses"), col("runsFor"), col("runsAgainst"));

    static final DatabaseTableConfig<TeamPlayerRow> TEAM_PLAYER_ROW = config(TeamPlayerRow.class, "softman_team_player",
        id("teamPlayerId"),
        uuid("teamId"), uuid("playerId"), col("ord"));

    static final DatabaseTableConfig<LineupSpotRow> LINEUP_SPOT_ROW = config(LineupSpotRow.class, "softman_lineup_spot",
        id("lineupSpotId"),
        uuid("lineupId"), col("slotKind"), col("slotOrder"), col("slotDepth"),
        uuid("playerRecordId"));

    static final DatabaseTableConfig<BoxScoreRow> BOX_SCORE_ROW = config(BoxScoreRow.class, "softman_box_score",
        id("boxScoreId"),
        uuid("matchId"), col("inning"), col("awayRuns"), col("homeRuns"),
        col("awayHits"), col("homeHits"), col("awayErrors"), col("homeErrors"));

    static final DatabaseTableConfig<GameMeta> GAME_META = config(GameMeta.class, "softman_game_meta",
        id("gameMetaId"),
        col("schemaVersion"), col("gameId"),
        localDate("savedAt"), localDate("currentDate"), localDate("viewDate"),
        uuid("activeClubId"), uuid("focusedClubId"), uuid("focusedTeamId"));

    private static <T> DatabaseTableConfig<T> config(Class<T> type, String tableName, DatabaseFieldConfig... fields) {
        return new DatabaseTableConfig<>(type, tableName, new ArrayList<>(List.of(fields)));
    }

    private static DatabaseFieldConfig id(String fieldName) {
        var config = uuid(fieldName);
        config.setId(true);
        return config;
    }

    private static DatabaseFieldConfig uuid(String fieldName) {
        return col(fieldName, DataType.UUID);
    }

    private static DatabaseFieldConfig col(String fieldName) {
        return new DatabaseFieldConfig(fieldName);
    }

    private static DatabaseFieldConfig col(String fieldName, DataType dataType) {
        var config = new DatabaseFieldConfig(fieldName);
        config.setDataType(dataType);
        return config;
    }

    private static DatabaseFieldConfig localDate(String fieldName) {
        var config = new DatabaseFieldConfig(fieldName);
        config.setPersisterClass(LocalDatePersister.class);
        return config;
    }

    private static DatabaseFieldConfig foreign(String fieldName, DatabaseTableConfig<?> foreignConfig) {
        var config = new DatabaseFieldConfig(fieldName);
        config.setForeign(true);
        config.setForeignAutoRefresh(true);
        config.setMaxForeignAutoRefreshLevel(2);
        config.setForeignTableConfig(foreignConfig);
        return config;
    }
}
