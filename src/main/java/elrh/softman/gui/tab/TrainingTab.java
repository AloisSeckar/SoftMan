package elrh.softman.gui.tab;

import elrh.softman.logic.core.Club;
import elrh.softman.logic.core.Team;
import elrh.softman.logic.interfaces.IFocusedClubListener;
import elrh.softman.logic.interfaces.IFocusedTeamListener;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.GridView;

public class TrainingTab extends AnchorPane implements IFocusedTeamListener, IFocusedClubListener {

    private static TrainingTab INSTANCE;

    public static TrainingTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TrainingTab();
        }
        return INSTANCE;
    }

    private TrainingTab() {
        var label = new Label("Manage yor player's training and progress");
        super.getChildren().add(label);
        AnchorPane.setLeftAnchor(label, 10d);
        AnchorPane.setTopAnchor(label, 5d);

        var days = new ArrayList<Integer>(30);
        for (int i = 1; i <= 30; i++) {
            days.add(i);
        }

        var calendar = new GridView<>(FXCollections.observableList(days));
        calendar.getStyleClass().add("framed");
        calendar.setPrefWidth(700);
        calendar.setCellWidth(100);
        calendar.setCellHeight(100);

        super.getChildren().add(calendar);
        AnchorPane.setLeftAnchor(calendar, 10d);
        AnchorPane.setTopAnchor(calendar, 25d);
    }

    @Override
    public void focusedClubChanged(Club newlyFocusedClub) {
    }

    @Override
    public void focusedTeamChanged(Team newlyFocusedTeam) {
    }
}
