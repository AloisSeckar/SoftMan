package elrh.softman.gui.tile;

import atlantafx.base.controls.Card;
import elrh.softman.gui.kit.Cards;
import elrh.softman.gui.kit.Icons;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.utils.FormatUtils;
import elrh.softman.logic.AssociationManager;
import java.util.ArrayList;
import java.util.UUID;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;

public class CalendarTile extends Card {

    private final Label titleLabel = new Label("Today");
    private final VBox dailySchedule = new VBox();
    private final ArrayList<ScheduleRowTile> dailyScheduleRows = new ArrayList<>();

    private UUID leagueId;

    public CalendarTile() {
        titleLabel.getStyleClass().add(Tokens.CARD_TITLE);

        var adjustButton = Icons.button(Feather.CALENDAR, "Back to current day");
        adjustButton.setOnAction(e -> adjustDay());
        var prevDayButton = Icons.button(Feather.CHEVRON_LEFT, "Previous day");
        prevDayButton.setOnAction(e -> prevDay());
        var nextDayButton = Icons.button(Feather.CHEVRON_RIGHT, "Next day");
        nextDayButton.setOnAction(e -> nextDay());

        setHeader(Layouts.row(Space.S, Cards.title("Schedule"), Layouts.spacer(),
            adjustButton, prevDayButton, titleLabel, nextDayButton));
        setBody(dailySchedule);
    }

    public void setDailySchedule(UUID leagueId) {
        this.leagueId = leagueId;
        var viewDate = AssociationManager.getInstance().getClock().getViewDate();
        titleLabel.setText(viewDate.format(FormatUtils.DF));

        dailySchedule.getChildren().clear();
        dailyScheduleRows.clear();
        var matches = AssociationManager.getInstance().getDailyMatchesForLeague(leagueId);
        if (matches.size() > 0) {
            matches.forEach(match -> {
                var row = new ScheduleRowTile();
                row.setMatch(match);
                dailySchedule.getChildren().add(row);
                dailyScheduleRows.add(row);
            });
        } else {
            var empty = new Label("No matches scheduled on this day");
            empty.getStyleClass().add(Tokens.CAPTION);
            dailySchedule.getChildren().add(empty);
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
