package elrh.softman.gui.tab;

import elrh.softman.gui.tile.ScheduleRowTile;
import elrh.softman.logic.AssociationManager;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class IndexTab extends VBox {

    private static IndexTab INSTANCE;

    private final VBox dailySchedule;
    private final ArrayList<ScheduleRowTile> dailyScheduleRows = new ArrayList<>();

    public static IndexTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IndexTab();
        }
        return INSTANCE;
    }

    private IndexTab() {

        var titleLabel = new Label("Daily schedule");
        titleLabel.getStyleClass().setAll("h3");
        titleLabel.getStyleClass().add("padding-5");
        super.getChildren().add(titleLabel);

        dailySchedule = new VBox();
        super.getChildren().add(dailySchedule);

    }

    public void setDailySchedule() {
        dailySchedule.getChildren().clear();
        dailyScheduleRows.clear();
        var matches = AssociationManager.getInstance().getTodayMatches();
        int i = 0;
        for (var match : matches) {
            var row = new ScheduleRowTile(i++ % 2 == 0);
            row.setMatch(match);
            dailySchedule.getChildren().add(row);
            dailyScheduleRows.add(row);
        }
    }

    public void refreshSchedule() {
        for (var row : dailyScheduleRows) {
            row.refreshMatch();
        }
    }
}
