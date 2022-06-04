package elrh.softman.gui.tile;

import elrh.softman.db.orm.PlayerInfo;
import elrh.softman.logic.LineupPosition;
import elrh.softman.logic.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import elrh.softman.logic.Team;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

public class LineupTile extends VBox {

    private static final int MAX_PLAYERS = 17;

    private final List<LineupRowTile> lineupRows = new ArrayList<>(MAX_PLAYERS);

    public LineupTile(boolean readOnly) {
        super.setSpacing(5);

        for (int i = 0; i < MAX_PLAYERS; i++) {
            var lineupRowTile = new LineupRowTile(i);
            lineupRows.add(lineupRowTile);
            super.getChildren().add(lineupRowTile);

            if (i == 8) {
                super.getChildren().add(new Separator());
            }
        }

        setReadOnly(readOnly);
    }

    public void fillLineup(Team team) {
        List<PlayerInfo> players = team.getPlayers();
        for (int i = 0; i < MAX_PLAYERS; i++) {
            lineupRows.get(i).setUp(players, i < 9 ? team.getBatter(i) : null);
        }
    }

    public void setReadOnly(boolean readOnly) {
        lineupRows.forEach(row -> row.setReadOnly(readOnly));
    }

    public String checkLineup() {
        List<LineupPosition> checkedLineup = new ArrayList<>(MAX_PLAYERS);
        for (int i = 0; i < MAX_PLAYERS; i++) {

            var currentSelection = lineupRows.get(i).getCurrentSelection();

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

                Position positionToCheck = rowToCheck.getPosition();
                if (positionToCheck != null && positionToCheck.equals(currentSelection.getPosition())) {
                    return positionToCheck.name() + " is already filled";
                }
            }

            checkedLineup.add(currentSelection);
        }

        return null;
    }

    public List<LineupPosition> getLineup() {
        List<LineupPosition> ret = new ArrayList<>();
        lineupRows.stream().filter(LineupRowTile::isFilled).forEach(row -> ret.add(row.getCurrentSelection()));
        return Collections.unmodifiableList(ret);
    }

}
