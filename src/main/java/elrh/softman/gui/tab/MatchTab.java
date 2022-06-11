package elrh.softman.gui.tab;

import elrh.softman.gui.tile.BoxScoreTile;
import elrh.softman.gui.tile.LineupTile;
import elrh.softman.gui.tile.MatchHeaderTile;
import elrh.softman.logic.Match;
import elrh.softman.utils.FormatUtils;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class MatchTab extends BorderPane {

    private static MatchTab INSTANCE;


    private final LineupTile awayLineup;
    private final LineupTile homeLineup;

    private final MatchHeaderTile matchHeaderTile;

    private final BoxScoreTile boxScore;
    private final TextArea matchOverview;

    public static MatchTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MatchTab();
        }
        return INSTANCE;
    }

    private MatchTab() {

        awayLineup = new LineupTile(true);
        awayLineup.setPadding(FormatUtils.PADDING_10);
        super.setLeft(awayLineup);

        homeLineup = new LineupTile(true);
        homeLineup.setPadding(FormatUtils.PADDING_10);
        super.setRight(homeLineup);

        matchHeaderTile = new MatchHeaderTile();
        super.setTop(matchHeaderTile);

        boxScore = new BoxScoreTile();
        boxScore.getStyleClass().add("framed");

        matchOverview = new TextArea();
        matchOverview.getStyleClass().add("output-window");
        matchOverview.setPadding(FormatUtils.PADDING_10);

        var centerLayout = new BorderPane();
        BorderPane.setAlignment(boxScore, Pos.CENTER);
        centerLayout.setTop(boxScore);
        centerLayout.setCenter(matchOverview);
        super.setCenter(centerLayout);

    }

    public static TextArea getTarget() {
        return INSTANCE.matchOverview;
    }

    public void setMatch(Match match) {
        matchHeaderTile.setMatch(match);
        awayLineup.fillLineup(match.getAwayTeam());
        homeLineup.fillLineup(match.getHomeTeam());
        boxScore.loadBoxScore(match);
        match.printPlayByPlay(matchOverview);
    }
}
