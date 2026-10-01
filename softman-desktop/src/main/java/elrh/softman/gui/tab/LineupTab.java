package elrh.softman.gui.tab;

import atlantafx.base.theme.Styles;
import elrh.softman.gui.kit.Icons;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.tile.DefenseTile;
import elrh.softman.gui.tile.LineupTile;
import elrh.softman.gui.utils.GUIUtils;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Lineup;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.interfaces.IFocusedClubListener;
import elrh.softman.logic.interfaces.IFocusedTeamListener;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.feather.Feather;

public class LineupTab extends GridPane implements IFocusedTeamListener, IFocusedClubListener {

    private final LineupTile lineupTile;

    private final DefenseTile defenseTile;

    private final Button saveButton;

    private final Region clubStripe = new Region();

    private static LineupTab INSTANCE;

    public static LineupTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LineupTab();
        }
        return INSTANCE;
    }

    private LineupTab() {
        setPadding(Space.S.insets());
        setHgap(Space.S.getPx());
        setVgap(Space.S.getPx());

        var column1 = new ColumnConstraints();
        var column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);
        getColumnConstraints().addAll(column1, column2);

        clubStripe.getStyleClass().add(Tokens.CLUB_STRIPE);
        add(clubStripe, 0, 0, 2, 1);

        saveButton = new Button("Save lineup", Icons.of(Feather.SAVE));
        saveButton.getStyleClass().add(Styles.ACCENT);
        saveButton.setOnAction(e -> saveLineup());

        lineupTile = new LineupTile(false);
        lineupTile.setFooter(saveButton);
        lineupTile.setMaxHeight(Region.USE_PREF_SIZE);
        GridPane.setValignment(lineupTile, VPos.TOP);
        add(lineupTile, 0, 1);

        defenseTile = new DefenseTile();
        add(Layouts.grow(defenseTile), 1, 1);

        var user = AssociationManager.getInstance().getUser();

        focusedClubChanged(user.getFocusedClub());
        focusedTeamChanged(user.getFocusedTeam());

        user.registerFocusedClubListener(this);
        user.registerFocusedTeamListener(this);
    }

    @Override
    public void focusedClubChanged(Club newlyFocusedClub) {
        if (newlyFocusedClub != null) {
            GUIUtils.setBackgroundColor(clubStripe, newlyFocusedClub.getColor());
        } else {
            clubStripe.setBackground(null);
        }
    }

    @Override
    public void focusedTeamChanged(Team newlyFocusedTeam) {
        lineupTile.fillLineup(newlyFocusedTeam.getDefaultLineup());
        saveLineup();

        var readOnly = !AssociationManager.getInstance().getUser().userManagesTeam(newlyFocusedTeam.getId());
        lineupTile.setReadOnly(readOnly);
        saveButton.setDisable(readOnly);
    }

    private void saveLineup() {
        String check = lineupTile.checkLineup();
        if (StringUtils.isBlank(check)) {
            Lineup lineup = lineupTile.getLineup();
            AssociationManager.getInstance().getUser().getActiveClub().getTeams().get(0).setLineup(lineup);
            for (int i = 1; i <= Lineup.POSITION_PLAYERS; i++) {
                var current = lineup.getCurrentBatter(i);
                if (current != null) {
                    defenseTile.setPosition(current);
                }
            }
        } else {
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(check);
            alert.showAndWait();
        }
    }
}
