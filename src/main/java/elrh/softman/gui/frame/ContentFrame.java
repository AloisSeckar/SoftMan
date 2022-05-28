package elrh.softman.gui.frame;

import elrh.softman.gui.tab.*;
import javafx.scene.control.*;

public class ContentFrame extends TabPane {
    
    private static ContentFrame INSTANCE;
    
    public static ContentFrame getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContentFrame();
        }
        return INSTANCE;
    }
    
    private ContentFrame() {
        Tab tab1 = new Tab("Index" , IndexTab.getInstance());
        Tab tab2 = new Tab("Match" , MatchTab.getInstance());
        Tab tab3 = new Tab("Team", TeamTab.getInstance());
        Tab tab4 = new Tab("Lineup", LineupTab.getInstance());
        Tab tab5 = new Tab("Training", new Label("Manage your player's training and progress"));
        Tab tab6 = new Tab("Standings", StandingsTab.getInstance());
        Tab tab7 = new Tab("Stats", new Label("Statistics center"));
        Tab tab8 = new Tab("Market", new Label("Buy and sell players"));

        this.getTabs().add(tab1);
        this.getTabs().add(tab2);
        this.getTabs().add(tab3);
        this.getTabs().add(tab4);
        this.getTabs().add(tab5);
        this.getTabs().add(tab6);
        this.getTabs().add(tab7);
        this.getTabs().add(tab8);
    }

    public void setUp() {
        IndexTab.getInstance().setDailySchedule();
        MatchTab.setMatch();
    }
}
