package elrh.softman.gui.frame;

import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.utils.GUIUtils;
import elrh.softman.logic.AssociationManager;
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

    private FocusFrame() {
        super(Space.S.getPx());
        setAlignment(Pos.CENTER_RIGHT);

        var manager = AssociationManager.getInstance();
        var user = manager.getUser();

        clubSwatch.getStyleClass().add(Tokens.SWATCH);
        GUIUtils.setBackgroundColor(clubSwatch, user.getFocusedClub().getColor());

        final var focusedClubCB = new ComboBox<>(FXCollections.observableList(manager.getClubs(true)));
        focusedClubCB.setValue(user.getFocusedClub());

        final var focusedTeamCB = new ComboBox<>(FXCollections.observableList(user.getFocusedClub().getTeams()));
        focusedTeamCB.setValue(user.getFocusedTeam());

        getChildren().addAll(clubSwatch, caption("Club"), focusedClubCB, caption("Team"), focusedTeamCB);

        focusedClubCB.valueProperty().addListener((ov, oldValue, newValue) -> {
            GUIUtils.setBackgroundColor(clubSwatch, newValue.getColor());
            AssociationManager.getInstance().getUser().setFocusedClub(newValue);
            focusedTeamCB.setItems(FXCollections.observableList(newValue.getTeams()));
            focusedTeamCB.setValue(newValue.getTeams().get(0));
        });

        focusedTeamCB.valueProperty().addListener((ov, oldValue, newValue) -> {
            AssociationManager.getInstance().getUser().setFocusedTeam(newValue);
        });

    }

    private static Label caption(String text) {
        var label = new Label(text);
        label.getStyleClass().add(Tokens.CAPTION);
        return label;
    }
}
