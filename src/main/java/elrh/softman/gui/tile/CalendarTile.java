package elrh.softman.gui.tile;

import elrh.softman.logic.AssociationManager;
import elrh.softman.utils.FormatUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;

public class CalendarTile extends VBox {

    private final Label titleLabel;
    private final VBox dailySchedule;
    private final ArrayList<ScheduleRowTile> dailyScheduleRows = new ArrayList<>();

    private int rows = 0;

    private long leagueId;

    public CalendarTile() {

        var topRow = new HBox();
        topRow.setSpacing(5d);
        topRow.setAlignment(Pos.CENTER_LEFT);

        var adjustButton = new Button("O");
        adjustButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> adjustDay());
        topRow.getChildren().add(adjustButton);

        var prevDayButton = new Button("<");
        prevDayButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> prevDay());
        topRow.getChildren().add(prevDayButton);

        titleLabel = new Label("Today");
        titleLabel.getStyleClass().setAll("schedule-date");
        topRow.getChildren().add(titleLabel);

        var prevNextButton = new Button(">");
        prevNextButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> nextDay());
        topRow.getChildren().add(prevNextButton);

        super.getChildren().add(topRow);

        dailySchedule = new VBox();
        super.getChildren().add(dailySchedule);
    }

    public void setDailySchedule(long leagueId) {
        this.leagueId = leagueId;
        var viewDate = AssociationManager.getInstance().getClock().getViewDate();
        titleLabel.setText(viewDate.format(FormatUtils.DF));

        dailySchedule.getChildren().clear();
        dailyScheduleRows.clear();
        var matches = AssociationManager.getInstance().getDailyMatchesForLeague(leagueId);
        if (matches.size() > 0) {
            rows = 0;
            matches.forEach(match -> {
                var row = new ScheduleRowTile(rows++ % 2 == 0);
                row.setMatch(match);
                dailySchedule.getChildren().add(row);
                dailyScheduleRows.add(row);
            });
        } else {
            dailySchedule.getChildren().add(new Label("No matches scheduled today"));
        }
    }

    public void refreshSchedule() {
        for (var row : dailyScheduleRows) {
            row.refreshMatch();
        }
    }

    private void adjustDay() {
        AssociationManager.getInstance().getClock().adjustViewDay();
        setDailySchedule(leagueId);
    }

    private void prevDay() {
        AssociationManager.getInstance().getClock().prevViewDay();
        setDailySchedule(leagueId);
    }

    private void nextDay() {
        AssociationManager.getInstance().getClock().nextViewDay();
        setDailySchedule(leagueId);
    }

}
