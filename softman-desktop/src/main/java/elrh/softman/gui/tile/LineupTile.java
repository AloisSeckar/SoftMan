package elrh.softman.gui.tile;

import static elrh.softman.logic.core.Lineup.*;

import atlantafx.base.controls.Card;
import elrh.softman.gui.kit.Cards;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Lineup;
import elrh.softman.logic.core.data.PlayerInfo;
import elrh.softman.logic.core.data.PlayerRecord;
import elrh.softman.logic.core.data.TeamInfo;
import elrh.softman.logic.enums.PlayerPosition;
import elrh.softman.utils.ErrorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;

public class LineupTile extends Card {

    private final LineupRowTile[] positionPlayersRows = new LineupRowTile[POSITION_PLAYERS];
    private final LineupRowTile[] substitutesRows = new LineupRowTile[SUBSTITUTES];
    private final Label titleLabel = Cards.title("Lineup");

    private TeamInfo team;

    public LineupTile(boolean readOnly) {
        setHeader(titleLabel);

        var body = Layouts.column(Space.XS, section("Position players"));
        for (int i = 0; i < POSITION_PLAYERS; i++) {
            var lineupRowTile = new LineupRowTile(this, i + 1, true);
            positionPlayersRows[i] = lineupRowTile;
            body.getChildren().add(lineupRowTile);
        }

        body.getChildren().addAll(new Separator(), section("Substitutes"));
        for (int i = 0; i < SUBSTITUTES; i++) {
            var lineupRowTile = new LineupRowTile(this, i + 1, false);
            substitutesRows[i] = lineupRowTile;
            body.getChildren().add(lineupRowTile);
        }

        setBody(body);
        setReadOnly(readOnly);
    }

    public void fillLineup(Lineup lineup) {
        if (lineup != null) {
            var lineupTeam = AssociationManager.getInstance().getTeamById(lineup.getLineupInfo().getTeamId());
            this.team = lineupTeam != null ? lineupTeam.getTeamInfo() : null;
            if (team != null) {
                titleLabel.setText(lineup.getLineupInfo().getTeamName());
                var players = lineupTeam.getPlayers();
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
                ErrorUtils.raise("Team " + lineup.getLineupInfo().getTeamId() + " not found");
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

    private static Label section(String text) {
        var label = new Label(text.toUpperCase());
        label.getStyleClass().add(Tokens.CAPTION);
        return label;
    }
}
