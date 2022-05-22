package elrh.softman.gui;

import elrh.softman.gui.frame.*;
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
        MenuFrame menu = MenuFrame.getInstance();
        this.setTop(menu);
        ContentFrame content = ContentFrame.getInstance();
        this.setCenter(content);
        ActionFrame action = ActionFrame.getInstance();
        this.setBottom(action);
    }

    // TODO unify actions performed upon starting new game
    public void setUp() {
        ContentFrame.getInstance().setUp();
    }
    
}
