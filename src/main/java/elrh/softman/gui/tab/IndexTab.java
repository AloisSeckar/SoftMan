package elrh.softman.gui.tab;

import elrh.softman.gui.tile.CalendarTile;
import elrh.softman.gui.tile.MatchPreviewTile;
import elrh.softman.gui.tile.ScheduleRowTile;
import elrh.softman.logic.AssociationManager;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class IndexTab extends VBox {

    private static IndexTab INSTANCE;

    private final VBox dailySchedule;

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
        var matches = AssociationManager.getInstance().getTodayMatches();
        for (var match : matches) {
            var row = new ScheduleRowTile();
            row.setMatch(match);
            dailySchedule.getChildren().add(row);
        }
    }
}
