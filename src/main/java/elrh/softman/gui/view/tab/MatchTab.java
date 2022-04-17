package elrh.softman.gui.view.tab;

import elrh.softman.gui.tile.MatchPreviewTile;
import javafx.scene.layout.AnchorPane;

public class MatchTab extends AnchorPane {

    private static MatchTab INSTANCE;

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
        
    }
    
}
