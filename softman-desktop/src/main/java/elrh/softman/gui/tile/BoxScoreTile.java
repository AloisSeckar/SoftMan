package elrh.softman.gui.tile;

import atlantafx.base.controls.Card;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.logic.core.Match;
import elrh.softman.utils.Constants;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class BoxScoreTile extends Card {

    private final GridPane grid = new GridPane();

    public BoxScoreTile() {
        grid.getStyleClass().add(Tokens.BOX_SCORE);
        setBody(grid);
    }

    public void loadBoxScore(Match match) {
        grid.getChildren().clear();

        grid.add(cell("", Tokens.BOX_TEAM, Tokens.BOX_HEAD), 0, 0);
        grid.add(cell(match.getAwayLineup().getLineupInfo().getTeamName(), Tokens.BOX_TEAM), 0, 1);
        grid.add(cell(match.getHomeLineup().getLineupInfo().getTeamName(), Tokens.BOX_TEAM), 0, 2);

        var boxScore = match.getBoxScore();
        int inningsPlayed = boxScore != null ? boxScore.getInnings() : 0;
        int innings = Math.max(inningsPlayed, Constants.INNINGS);

        for (int i = 1; i <= innings; i++) {
            String awayScoreValue = "";
            String homeScoreValue = "";
            if (boxScore != null) {
                if (match.isFinished() && i > inningsPlayed) {
                    awayScoreValue = "X";
                    homeScoreValue = "X";
                } else {
                    awayScoreValue = String.valueOf(boxScore.getPointsInInning(i, true));
                    if (match.isFinished() && i == inningsPlayed && !match.getMatchInfo().isHomeTeamFinishedBatting()) {
                        homeScoreValue = "X";
                    } else {
                        homeScoreValue = String.valueOf(boxScore.getPointsInInning(i, false));
                    }
                }
            }
            grid.add(cell(String.valueOf(i), Tokens.BOX_HEAD), i, 0);
            grid.add(cell(awayScoreValue), i, 1);
            grid.add(cell(homeScoreValue), i, 2);
        }

        int col = innings + 1;
        grid.add(cell("R", Tokens.BOX_HEAD, Tokens.BOX_TOTAL), col, 0);
        grid.add(cell("H", Tokens.BOX_HEAD, Tokens.BOX_TOTAL), col + 1, 0);
        grid.add(cell("E", Tokens.BOX_HEAD, Tokens.BOX_TOTAL), col + 2, 0);
        if (boxScore != null) {
            grid.add(cell(String.valueOf(boxScore.getTotalPoints(true)), Tokens.BOX_TOTAL), col, 1);
            grid.add(cell(String.valueOf(boxScore.getHits(true)), Tokens.BOX_TOTAL), col + 1, 1);
            grid.add(cell(String.valueOf(boxScore.getErrors(true)), Tokens.BOX_TOTAL), col + 2, 1);
            grid.add(cell(String.valueOf(boxScore.getTotalPoints(false)), Tokens.BOX_TOTAL), col, 2);
            grid.add(cell(String.valueOf(boxScore.getHits(false)), Tokens.BOX_TOTAL), col + 1, 2);
            grid.add(cell(String.valueOf(boxScore.getErrors(false)), Tokens.BOX_TOTAL), col + 2, 2);
        }
    }

    private static Label cell(String text, String... styleClasses) {
        var label = new Label(text);
        label.getStyleClass().add(Tokens.BOX_CELL);
        label.getStyleClass().addAll(styleClasses);
        label.setMaxWidth(Double.MAX_VALUE);
        if (label.getStyleClass().contains(Tokens.BOX_TEAM)) {
            GridPane.setHgrow(label, Priority.ALWAYS);
        }
        return label;
    }
}
