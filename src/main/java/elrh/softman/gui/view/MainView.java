package elrh.softman.gui.view;

import elrh.softman.Softman;
import elrh.softman.db.GameDBManager;
import elrh.softman.logic.League;
import elrh.softman.logic.Team;
import elrh.softman.mock.MockTeamFactory;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainView extends AnchorPane {
    
    private static MainView INSTANCE;
    
    public static MainView getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MainView();
        }
        return INSTANCE;
    }
    
    private MainView() {
        
        Button testButton = new Button("MOCK Play league");
        testButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            mock();
        });
        super.getChildren().add(testButton);
        AnchorPane.setLeftAnchor(testButton, 5d);
        AnchorPane.setTopAnchor(testButton, 5d);
    }

    private void mock() {
        try {
            ArrayList<Team> teams = new ArrayList<>();
            teams.add(MockTeamFactory.getMockTeam("REDS"));
            teams.add(MockTeamFactory.getMockTeam("BLUES"));
            teams.add(MockTeamFactory.getMockTeam("GREENS"));
            teams.add(MockTeamFactory.getMockTeam("YELLOWS"));
            teams.add(MockTeamFactory.getMockTeam("BLACKS"));
            teams.add(MockTeamFactory.getMockTeam("WHITES"));
            teams.add(MockTeamFactory.getMockTeam("SILVERS"));
            teams.add(MockTeamFactory.getMockTeam("VIOLETS"));
            teams.add(MockTeamFactory.getMockTeam("BROWNS"));
            teams.add(MockTeamFactory.getMockTeam("GOLDS"));
            
            League testLeague = new League("Test league", teams);
            testLeague.playLeague();
            GameDBManager.getInstance().saveLeague(testLeague);
            
            Softman.closeIfConfirmed();
            
        } catch (Exception ex) {
            LOG.error("MOCK FAILED", ex);
        }
    }
    
}
