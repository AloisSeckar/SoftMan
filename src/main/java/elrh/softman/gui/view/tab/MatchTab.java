package elrh.softman.gui.view.tab;

import elrh.softman.gui.tile.MatchPreviewTile;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class MatchTab extends AnchorPane {

    private static MatchTab INSTANCE;

    private final TextArea matchOverview;

    public static MatchTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MatchTab();
        }
        return INSTANCE;
    }

    private MatchTab() {

        var matchPreviewTile = new MatchPreviewTile();
        super.getChildren().add(matchPreviewTile);
        AnchorPane.setLeftAnchor(matchPreviewTile, 10d);
        AnchorPane.setTopAnchor(matchPreviewTile, 10d);

        matchOverview = new TextArea();
        matchOverview.getStyleClass().setAll("output-window");
        matchOverview.setPrefWidth(500);
        matchOverview.setPrefRowCount(50);
        super.getChildren().add(matchOverview);
        AnchorPane.setRightAnchor(matchOverview, 10d);
        AnchorPane.setTopAnchor(matchOverview, 10d);
        
    }

    public static TextArea getTarget() {
        return INSTANCE.matchOverview;
    }
    
}
