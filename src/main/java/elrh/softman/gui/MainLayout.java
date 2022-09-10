package elrh.softman.gui;

import elrh.softman.gui.frame.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainLayout extends BorderPane {
    
    private static MainLayout INSTANCE;
    
    public static MainLayout getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MainLayout();
        }
        return INSTANCE;
    }
    
    private MainLayout() {
        this.setTop(new VBox(MenuFrame.getInstance(), FocusFrame.getInstance()));
        this.setCenter(ContentFrame.getInstance());
        this.setBottom(ActionFrame.getInstance());
    }

    // TODO unify actions performed upon starting new game
    public void setUp() {
        ContentFrame.getInstance().setUp();
    }
    
}
