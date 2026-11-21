package elrh.softman.gui.frame;

import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.utils.GUIUtils;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Team;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class FocusFrame extends HBox {

    private static FocusFrame INSTANCE;

    public static FocusFrame getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FocusFrame();
        }
        return INSTANCE;
    }

    private final Region clubSwatch = new Region();
    private final ComboBox<Club> focusedClubCB = new ComboBox<>();
    private final ComboBox<Team> focusedTeamCB = new ComboBox<>();
    private boolean refreshing;

    private FocusFrame() {
        super(Space.S.getPx());
        setAlignment(Pos.CENTER_RIGHT);

        clubSwatch.getStyleClass().add(Tokens.SWATCH);

        getChildren().addAll(clubSwatch, caption("Club"), focusedClubCB, caption("Team"), focusedTeamCB);

        focusedClubCB.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (refreshing) {
                return;
            }
            updateClubSwatch(newValue);
            AssociationManager.getInstance().getUser().setFocusedClub(newValue);
            var teams = newValue != null ? newValue.getTeams() : List.<Team>of();
            focusedTeamCB.setItems(FXCollections.observableArrayList(teams));
            focusedTeamCB.setValue(teams.isEmpty() ? null : teams.getFirst());
        });

        focusedTeamCB.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (refreshing) {
                return;
            }
            AssociationManager.getInstance().getUser().setFocusedTeam(newValue);
        });

        refresh();
    }

    public void refresh() {
        refreshing = true;
        try {
            var manager = AssociationManager.getInstance();
            var user = manager.getUser();
            var focusedClub = user.getFocusedClub();

            focusedClubCB.setItems(FXCollections.observableArrayList(manager.getClubs(true)));
            focusedClubCB.setValue(focusedClub);
            updateClubSwatch(focusedClub);

            var teams = focusedClub != null ? focusedClub.getTeams() : List.<Team>of();
            focusedTeamCB.setItems(FXCollections.observableArrayList(teams));
            focusedTeamCB.setValue(user.getFocusedTeam());
        } finally {
            refreshing = false;
        }
    }

    private void updateClubSwatch(Club club) {
        if (club != null) {
            GUIUtils.setBackgroundColor(clubSwatch, club.getColor());
        } else {
            clubSwatch.setBackground(null);
        }
    }

    private static Label caption(String text) {
        var label = new Label(text);
        label.getStyleClass().add(Tokens.CAPTION);
        return label;
    }
}
