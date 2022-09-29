package elrh.softman.gui.frame;

import elrh.softman.gui.tab.*;
import elrh.softman.logic.AssociationManager;
import elrh.softman.utils.Utils;
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
        this.getTabs().add(new Tab("Club" , ClubTab.getInstance()));
        this.getTabs().add(new Tab("Match" , MatchTab.getInstance()));
        this.getTabs().add(new Tab("Team", TeamTab.getInstance()));
        this.getTabs().add(new Tab("Player", PlayerTab.getInstance()));
        this.getTabs().add(new Tab("Lineup", LineupTab.getInstance()));
        this.getTabs().add(new Tab("Training", new Label("Manage your player's training and progress")));
        this.getTabs().add(new Tab("Standings", StandingsTab.getInstance()));
        this.getTabs().add(new Tab("Stats", new Label("Statistics center")));
        this.getTabs().add(new Tab("Market", new Label("Buy and sell players")));
    }

    public void setUp() {
        ClubTab.getInstance().setDailySchedule();

        var testMatches = AssociationManager.getInstance().getDailyMatchesForUser();
        var match = Utils.getFirstItem(testMatches);
        if (match != null) {
            MatchTab.getInstance().setMatch(match);
        }
    }

    public void switchTo(String tab) {
        // TODO make it more error prone
        switch (tab) {
            case "Index" -> this.getSelectionModel().select(0);
            case "Match" -> this.getSelectionModel().select(1);
        }
    }
}
