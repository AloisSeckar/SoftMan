package elrh.softman.gui.view;

import elrh.softman.gui.table.LeagueStadingsTable;
import elrh.softman.gui.table.TeamPlayersTable;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.League;
import elrh.softman.utils.InfoUtils;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainView extends AnchorPane {
    
    private final League testLeague;
    
    private final TextArea testTextArea;  
    private final LeagueStadingsTable leagueTable;  
    private final TeamPlayersTable playersTable;  
    
    private static MainView INSTANCE;
    
    public static MainView getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MainView();
        }
        return INSTANCE;
    }
    
    private MainView() {
        
        testLeague = AssociationManager.getInstance().getPlayerLeague();
        
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
        
        leagueTable = new LeagueStadingsTable(testLeague.getStandings());
        super.getChildren().add(leagueTable);
        AnchorPane.setRightAnchor(leagueTable, 5d);
        AnchorPane.setTopAnchor(leagueTable, 85d);
        
        playersTable = new TeamPlayersTable(AssociationManager.getInstance().getPlayerTeam().getPlayers());
        super.getChildren().add(playersTable);
        AnchorPane.setRightAnchor(playersTable, 5d);
        AnchorPane.setBottomAnchor(playersTable, 5d);
    }

    private void mockLeague() {
        try {
            testLeague.playLeague();
            leagueTable.refresh();
            
            InfoUtils.showMessage("Finished");
            
        } catch (Exception ex) {
            LOG.error("LEAGUE FAILED", ex);
            InfoUtils.showMessage("LEAGUE FAILED");
        }
    }
    
    private void mockRound() {
        try {
            testLeague.previewCurrentRound();
            testLeague.playRound();
            leagueTable.refresh();
        } catch (Exception ex) {
            LOG.error("ROUND FAILED", ex);
            InfoUtils.showMessage("ROUND FAILED");
        }
    }
    
    public void writeIntoConsole(String message) {
        testTextArea.appendText(message + "\n");
    }
}
