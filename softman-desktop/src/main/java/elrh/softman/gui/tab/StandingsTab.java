package elrh.softman.gui.tab;

import elrh.softman.gui.kit.Cards;
import elrh.softman.gui.kit.Icons;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.table.LeagueStadingsTable;
import elrh.softman.gui.utils.InfoUtils;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.League;
import elrh.softman.utils.Constants;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.feather.Feather;

@Slf4j
public class StandingsTab extends GridPane {
    
    private final League testLeague;
    
    private final TextArea testTextArea;  
    private final LeagueStadingsTable leagueTable;
    
    private static StandingsTab INSTANCE;
    
    public static StandingsTab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StandingsTab();
        }
        return INSTANCE;
    }
    
    private StandingsTab() {
        setPadding(Space.S.insets());
        setHgap(Space.S.getPx());
        setVgap(Space.S.getPx());

        var column1 = new ColumnConstraints();
        column1.setPercentWidth(45);
        var column2 = new ColumnConstraints();
        column2.setPercentWidth(55);
        getColumnConstraints().addAll(column1, column2);

        // TODO change dynamically according to User selection
        testLeague = AssociationManager.getInstance().getLeagues(Constants.START_YEAR).get(0);
        
        var testButton = new Button("MOCK Play league", Icons.of(Feather.FAST_FORWARD));
        testButton.setOnAction(e -> mockLeague());
        
        var testRoundButton = new Button("MOCK Play round", Icons.of(Feather.SKIP_FORWARD));
        testRoundButton.setOnAction(e -> mockRound());
        
        testTextArea = new TextArea();
        testTextArea.getStyleClass().add(Tokens.MONO);

        var mockCard = Cards.titled("League simulation",
            Layouts.column(Space.S, Layouts.row(Space.S, testButton, testRoundButton), Layouts.grow(testTextArea)));
        add(Layouts.grow(mockCard), 0, 0);
        
        leagueTable = new LeagueStadingsTable();
        leagueTable.setLeague(testLeague); // TODO get rid of this mock
        var standingsCard = Cards.titled("Standings", leagueTable);
        standingsCard.setMaxHeight(Region.USE_PREF_SIZE);
        GridPane.setValignment(standingsCard, VPos.TOP);
        add(standingsCard, 1, 0);
    }

    // TODO delete mock and connect real leagues
    private void mockLeague() {
        try {
            testLeague.mockPlayLeague(testTextArea::appendText);
            leagueTable.refresh();
            
            InfoUtils.showMessage("Finished");
            
        } catch (Exception ex) {
            LOG.error("LEAGUE FAILED", ex);
            InfoUtils.showMessage("LEAGUE FAILED");
        }
    }
    
    private void mockRound() {
        try {
            testLeague.mockPreviewCurrentRound(testTextArea::appendText);
            testLeague.mockPlayRound(testTextArea::appendText);
            leagueTable.refresh();
        } catch (Exception ex) {
            LOG.error("ROUND FAILED", ex);
            InfoUtils.showMessage("ROUND FAILED");
        }
    }
}
