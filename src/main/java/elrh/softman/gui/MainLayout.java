package elrh.softman.gui;

import elrh.softman.gui.menu.MenuFrame;
import elrh.softman.gui.view.MainView;
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
        
        MainView mainView = MainView.getInstance();
        this.setCenter(mainView);
    }
    
}
