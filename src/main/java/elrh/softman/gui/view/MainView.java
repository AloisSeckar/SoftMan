package elrh.softman.gui.view;

import elrh.softman.gui.view.tab.*;
import javafx.scene.control.*;

public class MainView extends TabPane {
    
    private static MainView INSTANCE;
    
    public static MainView getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MainView();
        }
        return INSTANCE;
    }
    
    private MainView() {
        Tab tab1 = new Tab("Team", TeamTab.getInstance());
        Tab tab2 = new Tab("Match" , MatchTab.getInstance());
        Tab tab3 = new Tab("Lineup", LineupTab.getInstance());
        Tab tab4 = new Tab("Training", new Label("Manage your player's training and progress"));
        Tab tab5 = new Tab("Standings", StandingsTab.getInstance());
        Tab tab6 = new Tab("Stats", new Label("Statistics center"));
        Tab tab7 = new Tab("Market", new Label("Buy and sell players"));

        this.getTabs().add(tab1);
        this.getTabs().add(tab2);
        this.getTabs().add(tab3);
        this.getTabs().add(tab4);
        this.getTabs().add(tab5);
        this.getTabs().add(tab6);
        this.getTabs().add(tab7);
    }
    
}
