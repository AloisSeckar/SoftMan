package elrh.softman.gui.tile;

import elrh.softman.logic.AssociationManager;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class CalendarTile extends HBox {

    public CalendarTile() {

        final var tempCalendar = new TextArea();
        tempCalendar.setPrefRowCount(20);
        tempCalendar.setPrefColumnCount(50);
        this.getChildren().add(tempCalendar);

        var loadGames = new Button("Display");
        this.getChildren().add(loadGames);
        loadGames.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            tempCalendar.appendText("TODAY GAMES\n\n");
            var todayMatches = AssociationManager.getInstance().getTodayMatches();
            todayMatches.forEach(match -> tempCalendar.appendText(match.getAwayTeam().getName() + "@" + match.getHomeTeam().getName() + "\n"));
            tempCalendar.appendText("\n\n");

            tempCalendar.appendText("ROUND 3 GAMES\n\n");
            var roundGames = AssociationManager.getInstance().getRoundMatches(3);
            roundGames.forEach(match -> tempCalendar.appendText(match.getAwayTeam().getName() + "@" + match.getHomeTeam().getName() + "\n"));
            tempCalendar.appendText("\n\n");
        });

    }

}
