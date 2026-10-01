package elrh.softman.gui;

import elrh.softman.gui.frame.ActionFrame;
import elrh.softman.gui.frame.ContentFrame;
import elrh.softman.gui.frame.FocusFrame;
import elrh.softman.gui.frame.MenuFrame;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import javafx.scene.layout.BorderPane;

public class MainLayout extends BorderPane {
    
    private static MainLayout INSTANCE;
    
    public static MainLayout getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MainLayout();
        }
        return INSTANCE;
    }
    
    private MainLayout() {
        var appBar = Layouts.row(Space.M, MenuFrame.getInstance(), Layouts.spacer(), FocusFrame.getInstance());
        appBar.getStyleClass().add(Tokens.APP_BAR);
        this.setTop(appBar);
        this.setCenter(ContentFrame.getInstance());
        this.setBottom(ActionFrame.getInstance());
    }

    // TODO unify actions performed upon starting new game
    public void setUp() {
        ContentFrame.getInstance().setUp();
    }
    
}
