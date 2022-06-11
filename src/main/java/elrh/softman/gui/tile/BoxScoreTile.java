package elrh.softman.gui.tile;

import elrh.softman.logic.Match;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BoxScoreTile extends VBox {

    private final HBox innings = new HBox();

    private final Label awayTeam = new Label();
    private final HBox awayPoints = new HBox();
    private final Label awayRuns = new Label();
    private final Label awayHits = new Label();
    private final Label awayErrors = new Label();

    private final Label homeTeam = new Label();
    private final HBox homePoints = new HBox();
    private final Label homeRuns = new Label();
    private final Label homeHits = new Label();
    private final Label homeErrors = new Label();

    public BoxScoreTile() {
        var teamLabel = new Label("");
        teamLabel.getStyleClass().add("box-score");
        teamLabel.getStyleClass().add("box-score-team");
        var hitsLabel = new Label("R");
        hitsLabel.getStyleClass().add("box-score");
        var runsLabel = new Label("H");
        runsLabel.getStyleClass().add("box-score");
        var errorsLabel = new Label("E");
        errorsLabel.getStyleClass().add("box-score");
        var labelRow = new HBox(teamLabel, innings, hitsLabel, runsLabel, errorsLabel);
        super.getChildren().add(labelRow);

        awayTeam.getStyleClass().add("box-score");
        awayTeam.getStyleClass().add("box-score-team");
        awayRuns.getStyleClass().add("box-score");
        awayHits.getStyleClass().add("box-score");
        awayErrors.getStyleClass().add("box-score");
        var awayRow = new HBox(awayTeam, awayPoints, awayRuns, awayHits, awayErrors);
        super.getChildren().add(awayRow);

        homeTeam.getStyleClass().add("box-score");
        homeTeam.getStyleClass().add("box-score-team");
        homeRuns.getStyleClass().add("box-score");
        homeHits.getStyleClass().add("box-score");
        homeErrors.getStyleClass().add("box-score");
        var homeRow = new HBox(homeTeam, homePoints, homeRuns, homeHits, homeErrors);
        super.getChildren().add(homeRow);
    }

    public void loadBoxScore(Match match) {
        awayTeam.setText(match.getAwayTeam().getName());
        homeTeam.setText(match.getHomeTeam().getName());
        var boxScore = match.getBoxScore();
        if (boxScore != null) {
            innings.getChildren().clear();
            awayPoints.getChildren().clear();
            homePoints.getChildren().clear();
            int totalInnings = boxScore.getInnings();
            for (int i = 1; i <= totalInnings; i++) {
                var inningLabel = new Label(String.valueOf(i));
                inningLabel.getStyleClass().add("box-score");
                innings.getChildren().add(inningLabel);
                var awayScore = new Label(String.valueOf(boxScore.getPointsInInning(i, true)));
                awayScore.getStyleClass().add("box-score");
                awayPoints.getChildren().add(awayScore);
                var homeScore = new Label(String.valueOf(boxScore.getPointsInInning(i, false)));
                homeScore.getStyleClass().add("box-score");
                homePoints.getChildren().add(homeScore);
            }
            awayRuns.setText(String.valueOf(boxScore.getTotalPoints(true)));
            awayHits.setText(String.valueOf(boxScore.getHits(true)));
            awayErrors.setText(String.valueOf(boxScore.getErrors(true)));
            homeRuns.setText(String.valueOf(boxScore.getTotalPoints(false)));
            homeHits.setText(String.valueOf(boxScore.getHits(false)));
            homeErrors.setText(String.valueOf(boxScore.getErrors(false)));
        }
    }
}
