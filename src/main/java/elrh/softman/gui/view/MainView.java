package elrh.softman.gui.view;

import elrh.softman.db.GameDBManager;
import elrh.softman.logic.League;
import elrh.softman.logic.Team;
import elrh.softman.mock.MockTeamFactory;
import elrh.softman.utils.InfoUtils;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainView extends AnchorPane {
    
    private League testLeague;
    
    private final TextArea testTextArea;  
    
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
            mockLeague();
        });
        super.getChildren().add(testButton);
        AnchorPane.setLeftAnchor(testButton, 5d);
        AnchorPane.setTopAnchor(testButton, 5d);
        
        Button testRoundButton = new Button("MOCK Play round");
        testRoundButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            mockRound();
        });
        super.getChildren().add(testRoundButton);
        AnchorPane.setLeftAnchor(testRoundButton, 5d);
        AnchorPane.setTopAnchor(testRoundButton, 45d);
        
        testTextArea = new TextArea();
        testTextArea.getStyleClass().setAll("output-window");
        testTextArea.setPrefWidth(800d);
        testTextArea.setPrefHeight(500d);
        super.getChildren().add(testTextArea);
        AnchorPane.setLeftAnchor(testTextArea, 5d);
        AnchorPane.setTopAnchor(testTextArea, 85d);
    }

    private void mockLeague() {
        try {
            prepareLeague();
            testLeague.playLeague();
            
            InfoUtils.showMessage("Finished");
            
        } catch (Exception ex) {
            LOG.error("LEAGUE FAILED", ex);
            InfoUtils.showMessage("LEAGUE FAILED");
        }
    }
    
    private void mockRound() {
        try {
            prepareLeague();
            testLeague.previewCurrentRound();
            testLeague.playRound();
            
            InfoUtils.showMessage("Round played");
            
        } catch (Exception ex) {
            LOG.error("ROUND FAILED", ex);
            InfoUtils.showMessage("ROUND FAILED");
        }
    }
    
    private void prepareLeague() {
        if (testLeague == null) {
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
            
            testLeague = new League("Test league", teams);
            GameDBManager.getInstance().saveLeague(testLeague);
        }
    }
    
    public void writeIntoConsole(String message) {
        testTextArea.appendText(message + "\n");
    }
}
