package elrh.softman.gui.frame;

import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Team;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class FocusFrame extends HBox {

    private final ComboBox<Club> focusedClubCB;
    private final ComboBox<Team> focusedTeamCB;

    private static FocusFrame INSTANCE;

    public static FocusFrame getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FocusFrame();
        }
        return INSTANCE;
    }

    private FocusFrame() {
        super.setAlignment(Pos.CENTER_RIGHT);

        var manager = AssociationManager.getInstance();
        var state = manager.getState();

        this.getChildren().add(new Label("Focused club: "));
        focusedClubCB = new ComboBox<>(FXCollections.observableList(manager.getClubs(true)));
        focusedClubCB.setValue(state.getFocusedClub());
        this.getChildren().add(focusedClubCB);

        this.getChildren().add(new Label(" Focused team: "));
        focusedTeamCB = new ComboBox<>(FXCollections.observableList(state.getFocusedClub().getTeams()));
        focusedTeamCB.setValue(state.getFocusedTeam());
        this.getChildren().add(focusedTeamCB);
    }
}
