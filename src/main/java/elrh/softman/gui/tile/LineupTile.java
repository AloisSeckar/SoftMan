package elrh.softman.gui.tile;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Lineup;
import static elrh.softman.logic.core.Lineup.*;
import elrh.softman.logic.db.GameDBManager;
import elrh.softman.logic.db.orm.player.PlayerInfo;
import elrh.softman.logic.db.orm.player.PlayerRecord;
import elrh.softman.logic.db.orm.TeamInfo;
import elrh.softman.logic.enums.PlayerPosition;
import java.util.ArrayList;
import java.util.Arrays;

import elrh.softman.utils.ErrorUtils;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

public class LineupTile extends VBox {

    private final LineupRowTile[] positionPlayersRows = new LineupRowTile[POSITION_PLAYERS];
    private final LineupRowTile[] substitutesRows = new LineupRowTile[SUBSTITUTES];

    private TeamInfo team;

    public LineupTile(boolean readOnly) {
        super.setSpacing(5);

        super.getChildren().add(new Label("POSITION PLAYERS"));

        for (int i = 0; i < POSITION_PLAYERS; i++) {
            var lineupRowTile = new LineupRowTile(this, i + 1, true);
            positionPlayersRows[i] = lineupRowTile;
            super.getChildren().add(lineupRowTile);
        }

        super.getChildren().add(new Separator());
        super.getChildren().add(new Label("SUBSTITUTES"));

        for (int i = 0; i < SUBSTITUTES; i++) {
            var lineupRowTile = new LineupRowTile(this, i + 1, false);
            substitutesRows[i] = lineupRowTile;
            super.getChildren().add(lineupRowTile);
            lineupRowTile.setAlignment(Pos.CENTER_LEFT);
        }

        super.getChildren().add(new Separator());

        setReadOnly(readOnly);
    }

    public void fillLineup(Lineup lineup) {
        if (lineup != null) {
            // TODO is this effective way to get necessary data?
            this.team = (TeamInfo) GameDBManager.getInstance().getObjectById(TeamInfo.class, lineup.getLinuepInfo().getTeamId());
            if (team != null) {
                // TODO get to player list more directly and correctly
                var club = AssociationManager.getInstance().getClubById(team.getClubInfo().getClubId());
                var players = club.getTeamById(team.getTeamId()).getPlayers();
                var playerList = FXCollections.observableArrayList(players);
                playerList.add(0, null);
                // TODO get to player list more directly and correctly
                for (int i = 0; i < POSITION_PLAYERS; i++) {
                    positionPlayersRows[i].setUp(playerList, lineup.getCurrentBatter(i + 1));
                }
                for (int i = 0; i < SUBSTITUTES; i++) {
                    substitutesRows[i].setUp(playerList, lineup.getSubstitutes()[i]);
                }
            } else {
                ErrorUtils.raise("Team " + lineup.getLinuepInfo().getTeamId() + " not found");
            }
        } else {
            ErrorUtils.raise("Illega passing of NULL Lineup object");
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
                    return String.format("%s is already filled", playerToCheck.getName());
                }

                PlayerPosition positionToCheck = rowToCheck.getPosition();
                if (positionToCheck != null && positionToCheck.equals(currentSelection.getPosition())) {
                    return String.format("%s is already filled", positionToCheck.name());
                }
            }

            checkedLineup.add(currentSelection);
        }

        return null;
    }

    public Lineup getLineup() {
        var ret = new Lineup(team.getTeamId(), team.getName(), team.getClubInfo().getShortName(), team.getClubInfo().getLogo());

        Arrays.stream(positionPlayersRows).filter(LineupRowTile::isFilled).forEach(row -> ret.initPositionPlayer(row.getRow(), row.getCurrentSelection()));
        Arrays.stream(substitutesRows).filter(LineupRowTile::isFilled).forEach(row -> ret.initSubstitute(row.getRow(), row.getCurrentSelection()));

        return ret;
    }

    public void handleFlexChange(PlayerInfo newValue) {
        if (newValue != null) {
            substitutesRows[SUBSTITUTES - 1].clear();
            substitutesRows[SUBSTITUTES - 1].setReadOnly(true);
        } else {
            substitutesRows[SUBSTITUTES - 1].setReadOnly(false);
        }
    }
}
