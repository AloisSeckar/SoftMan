package elrh.softman.gui.tab;

import elrh.softman.gui.kit.Cards;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.interfaces.IFocusedClubListener;
import elrh.softman.logic.interfaces.IFocusedTeamListener;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.GridView;

public class TrainingTab extends BorderPane implements IFocusedTeamListener, IFocusedClubListener {

    private static TrainingTab INSTANCE;

    public static TrainingTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TrainingTab();
        }
        return INSTANCE;
    }

    private TrainingTab() {
        setPadding(Space.S.insets());

        var label = new Label("Manage your players' training and progress");
        label.getStyleClass().add(Tokens.CAPTION);

        var days = new ArrayList<Integer>(30);
        for (int i = 1; i <= 30; i++) {
            days.add(i);
        }

        var calendar = new GridView<>(FXCollections.observableList(days));
        calendar.setCellWidth(Layouts.em(6));
        calendar.setCellHeight(Layouts.em(6));

        setCenter(Cards.titled("Training", Layouts.column(Space.M, label, Layouts.grow(calendar))));
    }

    @Override
    public void focusedClubChanged(Club newlyFocusedClub) {
    }

    @Override
    public void focusedTeamChanged(Team newlyFocusedTeam) {
    }
}
