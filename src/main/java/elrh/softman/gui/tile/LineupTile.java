package elrh.softman.gui.tile;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.lineup.Lineup;
import static elrh.softman.logic.core.lineup.Lineup.*;
import elrh.softman.logic.db.GameDBManager;
import elrh.softman.logic.db.orm.PlayerInfo;
import elrh.softman.logic.core.lineup.PlayerRecord;
import elrh.softman.logic.db.orm.TeamInfo;
import elrh.softman.logic.enums.PlayerPosition;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

public class LineupTile extends VBox {

    private final LineupRowTile[] positionPlayersRows = new LineupRowTile[POSITION_PLAYERS];
    private final LineupRowTile[] substitutesRows = new LineupRowTile[SUBSTITUTES];

    private TeamInfo team;

    public LineupTile(boolean readOnly) {
        super.setSpacing(5);

        for (int i = 0; i < POSITION_PLAYERS; i++) {
            var lineupRowTile = new LineupRowTile(i);
            positionPlayersRows[i] = lineupRowTile;
            super.getChildren().add(lineupRowTile);
        }

        super.getChildren().add(new Separator());

        for (int i = 0; i < SUBSTITUTES; i++) {
            var lineupRowTile = new LineupRowTile(i);
            substitutesRows[i] = lineupRowTile;
            super.getChildren().add(lineupRowTile);
        }

        setReadOnly(readOnly);
    }

    public void fillLineup(Lineup lineup) {
        this.team = GameDBManager.getInstance().getTeam(lineup.getTeamId());
        // TODO get to player list more directly and correctly
        var club = AssociationManager.getInstance().getClubById(team.getClubInfo().getClubId());
        var players = club.getTeams().get(0).getPlayers();
        // TODO get to player list more directly and correctly
        for (int i = 0; i < POSITION_PLAYERS; i++) {
            positionPlayersRows[i].setUp(players, i < 9 ? lineup.getCurrentBatter(i + 1) : null);
        }
    }

    public void setReadOnly(boolean readOnly) {
        Arrays.stream(positionPlayersRows).forEach(row -> row.setReadOnly(readOnly));
        Arrays.stream(substitutesRows).forEach(row -> row.setReadOnly(readOnly));
    }

    public String checkLineup() {
        var checkedLineup = new ArrayList<PlayerRecord>(POSITION_PLAYERS);
        for (int i = 0; i < POSITION_PLAYERS; i++) {

            var currentSelection = positionPlayersRows[i].getCurrentSelection();

            if (i < 9) {
                if (currentSelection.getPlayer() == null || currentSelection.getPosition() == null) {
                    return "Positions 1-9 must be filled";
                }
            }

            for (var rowToCheck : checkedLineup) {
                PlayerInfo playerToCheck = rowToCheck.getPlayer();
                if (playerToCheck != null && playerToCheck.equals(currentSelection.getPlayer())) {
                    return playerToCheck.getName() + " is already filled";
                }

                PlayerPosition positionToCheck = rowToCheck.getPosition();
                if (positionToCheck != null && positionToCheck.equals(currentSelection.getPosition())) {
                    return positionToCheck.name() + " is already filled";
                }
            }

            checkedLineup.add(currentSelection);
        }

        return null;
    }

    public Lineup getLineup() {
        var ret = new Lineup(team.getTeamId(), team.getName(), team.getClubInfo().getLogo());

        Arrays.stream(positionPlayersRows).filter(LineupRowTile::isFilled).forEach(row -> ret.initPositionPlayer(row.getRow(), row.getCurrentSelection()));
        Arrays.stream(substitutesRows).filter(LineupRowTile::isFilled).forEach(row -> ret.initSubstitute(row.getRow(), row.getCurrentSelection()));

        return ret;
    }

}
