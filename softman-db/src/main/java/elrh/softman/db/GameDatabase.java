package elrh.softman.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import elrh.softman.logic.core.data.*;
import elrh.softman.logic.core.stats.Standing;
import elrh.softman.utils.ErrorUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

// owns the save-file connection and one DAO per table; not touched during simulation, only on save/load
@Slf4j
class GameDatabase implements AutoCloseable {

    static final String SAVE_DIR = "sav";

    private final JdbcPooledConnectionSource conn;

    final Dao<ClubInfo, UUID> clubInfos;
    final Dao<LeagueInfo, UUID> leagueInfos;
    final Dao<TeamInfo, UUID> teamInfos;
    final Dao<LineupInfo, UUID> lineupInfos;
    final Dao<MatchInfo, UUID> matchInfos;
    final Dao<MatchPlayByPlay, UUID> matchPlayByPlays;
    final Dao<PlayerInfo, UUID> playerInfos;
    final Dao<PlayerAttributes, UUID> playerAttributes;
    final Dao<PlayerRecord, UUID> playerRecords;
    final Dao<PlayerStats, UUID> playerStats;
    final Dao<Standing, UUID> standings;
    final Dao<TeamPlayerRow, UUID> teamPlayerRows;
    final Dao<LineupSpotRow, UUID> lineupSpotRows;
    final Dao<BoxScoreRow, UUID> boxScoreRows;
    final Dao<GameMeta, UUID> gameMetas;

    private GameDatabase(JdbcPooledConnectionSource conn, boolean recreateTables) throws Exception {
        this.conn = conn;
        this.clubInfos = initTable(TableConfigs.CLUB_INFO, recreateTables);
        this.leagueInfos = initTable(TableConfigs.LEAGUE_INFO, recreateTables);
        this.teamInfos = initTable(TableConfigs.TEAM_INFO, recreateTables);
        this.lineupInfos = initTable(TableConfigs.LINEUP_INFO, recreateTables);
        this.matchInfos = initTable(TableConfigs.MATCH_INFO, recreateTables);
        this.matchPlayByPlays = initTable(TableConfigs.MATCH_PLAY_BY_PLAY, recreateTables);
        this.playerInfos = initTable(TableConfigs.PLAYER_INFO, recreateTables);
        this.playerAttributes = initTable(TableConfigs.PLAYER_ATTRIBUTES, recreateTables);
        this.playerRecords = initTable(TableConfigs.PLAYER_RECORD, recreateTables);
        this.playerStats = initTable(TableConfigs.PLAYER_STATS, recreateTables);
        this.standings = initTable(TableConfigs.STANDING, recreateTables);
        this.teamPlayerRows = initTable(TableConfigs.TEAM_PLAYER_ROW, recreateTables);
        this.lineupSpotRows = initTable(TableConfigs.LINEUP_SPOT_ROW, recreateTables);
        this.boxScoreRows = initTable(TableConfigs.BOX_SCORE_ROW, recreateTables);
        this.gameMetas = initTable(TableConfigs.GAME_META, recreateTables);
    }

    static GameDatabase open(String gameId, boolean recreateTables) throws Exception {
        if (StringUtils.isBlank(gameId)) {
            throw new IllegalArgumentException("Game ID not specified");
        }
        Files.createDirectories(Path.of(SAVE_DIR));

        var url = "jdbc:sqlite:" + saveFile(gameId);
        var conn = new JdbcPooledConnectionSource(url);
        try {
            var database = new GameDatabase(conn, recreateTables);
            LOG.info("Save file for game '{}' opened", gameId);
            return database;
        } catch (Exception ex) {
            conn.close();
            throw ex;
        }
    }

    static boolean saveExists(String gameId) {
        return Files.exists(saveFile(gameId));
    }

    static Path saveFile(String gameId) {
        return Path.of(SAVE_DIR, "game-" + gameId + ".db");
    }

    ConnectionSource getConnectionSource() {
        return conn;
    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (Exception ex) {
            ErrorUtils.handleException("GameDatabase.close", ex);
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    private <T> Dao<T, UUID> initTable(DatabaseTableConfig<T> config, boolean recreateTables) throws Exception {
        if (recreateTables) {
            TableUtils.dropTable(conn, config, true);
        }
        TableUtils.createTableIfNotExists(conn, config);
        return DaoManager.createDao(conn, config);
    }
}
