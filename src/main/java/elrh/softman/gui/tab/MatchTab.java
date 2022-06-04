package elrh.softman.gui.tab;

import elrh.softman.gui.tile.LineupTile;
import elrh.softman.gui.tile.MatchHeaderTile;
import elrh.softman.logic.Match;
import elrh.softman.utils.FormatUtils;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class MatchTab extends BorderPane {

    private static MatchTab INSTANCE;


    private final LineupTile awayLineup;
    private final LineupTile homeLineup;

    private final MatchHeaderTile matchHeaderTile;
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

        matchOverview = new TextArea();
        matchOverview.getStyleClass().add("output-window");
        matchOverview.setPadding(FormatUtils.PADDING_10);
        super.setCenter(matchOverview);
        
    }

    public static TextArea getTarget() {
        return INSTANCE.matchOverview;
    }

    public void setMatch(Match match) {
        matchHeaderTile.setMatch(match);
        awayLineup.fillLineup(match.getAwayTeam());
        homeLineup.fillLineup(match.getHomeTeam());
    }
}
