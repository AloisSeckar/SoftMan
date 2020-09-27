package elrh.softman.gui.table;

import elrh.softman.logic.Standing;
import java.util.List;
import javafx.beans.value.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.collections.*;
 
public class LeagueStadingsTable extends Pane {
 
    private final TableView<Standing> table;
    private final ObservableList<Standing> data;
 
    public LeagueStadingsTable(List<Standing> standings) {
        data = FXCollections.observableList(standings);
        FXCollections.sort(data);
        
        table = new TableView();
        table.setItems(data);
 
        TableColumn<Standing, String> teamCol = new TableColumn("Team");
        teamCol.setCellValueFactory((p) -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return p.getValue().getTeam();
            }
        });
        
        TableColumn<Standing, Integer> gamesCol = new TableColumn("Games");
        gamesCol.getStyleClass().add("text-center");
        gamesCol.setCellValueFactory((p) -> new ObservableValueBase<Integer>() {
            @Override
            public Integer getValue() {
                return p.getValue().getGames();
            }
        });
        
        TableColumn<Standing, Integer> runsForCol = new TableColumn("RF");
        runsForCol.getStyleClass().add("text-center");
        runsForCol.setCellValueFactory((p) -> new ObservableValueBase<Integer>() {
            @Override
            public Integer getValue() {
                return p.getValue().getRunsFor();
            }
        });
        
        TableColumn<Standing, Integer> runsAgainstCol = new TableColumn("RA");
        runsAgainstCol.getStyleClass().add("text-center");
        runsAgainstCol.setCellValueFactory((p) -> new ObservableValueBase<Integer>() {
            @Override
            public Integer getValue() {
                return p.getValue().getRunsAgainst();
            }
        });
        
        TableColumn<Standing, Integer> pointsCol = new TableColumn("Points");
        pointsCol.getStyleClass().add("text-center");
        pointsCol.setCellValueFactory((p) -> new ObservableValueBase<Integer>() {
            @Override
            public Integer getValue() {
                return p.getValue().getPoints();
            }
        });
 
        table.getColumns().setAll(teamCol, gamesCol, runsForCol, runsAgainstCol, pointsCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        super.getChildren().add(table);
    }

    public void refresh() {
        FXCollections.sort(data);
        table.refresh();
    }
}