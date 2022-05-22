package elrh.softman.gui.tab;

import elrh.softman.gui.tile.CalendarTile;
import elrh.softman.gui.tile.MatchPreviewTile;
import elrh.softman.logic.AssociationManager;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class MatchTab extends AnchorPane {

    private static MatchTab INSTANCE;

    private final MatchPreviewTile matchPreviewTile;
    private final TextArea matchOverview;

    public static MatchTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MatchTab();
        }
        return INSTANCE;
    }

    private MatchTab() {

        matchPreviewTile = new MatchPreviewTile();
        super.getChildren().add(matchPreviewTile);
        AnchorPane.setLeftAnchor(matchPreviewTile, 10d);
        AnchorPane.setTopAnchor(matchPreviewTile, 10d);

        matchOverview = new TextArea();
        matchOverview.getStyleClass().setAll("output-window");
        matchOverview.setPrefWidth(500);
        matchOverview.setPrefRowCount(40);
        super.getChildren().add(matchOverview);
        AnchorPane.setRightAnchor(matchOverview, 10d);
        AnchorPane.setTopAnchor(matchOverview, 10d);


        var calendar = new CalendarTile();
        super.getChildren().add(calendar);
        AnchorPane.setLeftAnchor(calendar, 10d);
        AnchorPane.setBottomAnchor(calendar, 10d);
        
    }

    public static TextArea getTarget() {
        return INSTANCE.matchOverview;
    }

    public static void setMatch() {
        INSTANCE.matchPreviewTile.setMatch(AssociationManager.getInstance().getTodayMatchForPlayer());
    }
}
