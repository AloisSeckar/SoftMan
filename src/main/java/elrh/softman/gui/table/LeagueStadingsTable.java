package elrh.softman.gui.table;

import elrh.softman.Softman;
import elrh.softman.logic.core.League;
import elrh.softman.logic.core.stats.Standing;
import java.util.ArrayList;
import javafx.beans.binding.Bindings;
import javafx.beans.value.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.collections.*;

public class LeagueStadingsTable extends VBox {

    private static final double WIDTH = 80d;

    private final Label nameLabel;
    private final TableView<Standing> table;
    private final ObservableList<Standing> data;

    public LeagueStadingsTable() {
        data = FXCollections.observableList(new ArrayList<>());

        nameLabel = new Label();
        nameLabel.getStyleClass().setAll("h3");
        super.getChildren().add(nameLabel);

        table = new TableView<>();
        table.setMinWidth(770d);
        table.setMaxWidth(770d);
        table.setMinHeight(270d);
        table.setMaxHeight(270d);
        table.setItems(data);

        TableColumn<Standing, String> numberCol = new TableColumn<>("#");
        numberCol.setMinWidth(WIDTH);
        numberCol.setMaxWidth(WIDTH);
        numberCol.getStyleClass().add("column-centered");
        numberCol.setCellValueFactory((p) -> new ObservableValueBase<>() {
            @Override
            public String getValue() {
                return String.valueOf(table.getItems().indexOf(p.getValue()) + 1);
            }
        });
        numberCol.setSortable(false);

        TableColumn<Standing, String> teamCol = new TableColumn<>("Team");
        teamCol.setMinWidth(200d);
        teamCol.setMaxWidth(200d);
        teamCol.setCellValueFactory((p) -> new ObservableValueBase<>() {
            @Override
            public String getValue() {
                return p.getValue().getTeam();
            }
        });
        teamCol.setSortable(false);

        TableColumn<Standing, Integer> gamesCol = new TableColumn<>("Games");
        gamesCol.setMinWidth(WIDTH);
        gamesCol.setMaxWidth(WIDTH);
        gamesCol.getStyleClass().add("column-centered");
        gamesCol.setCellValueFactory((p) -> new ObservableValueBase<>() {
            @Override
            public Integer getValue() {
                return p.getValue().getGames();
            }
        });

        TableColumn<Standing, Integer> winsCol = new TableColumn<>("W");
        winsCol.setMinWidth(WIDTH);
        winsCol.setMaxWidth(WIDTH);
        winsCol.getStyleClass().add("column-centered");
        winsCol.setCellValueFactory((p) -> new ObservableValueBase<>() {
            @Override
            public Integer getValue() {
                return p.getValue().getWins();
            }
        });

        TableColumn<Standing, Integer> losesCol = new TableColumn<>("L");
        losesCol.setMinWidth(WIDTH);
        losesCol.setMaxWidth(WIDTH);
        losesCol.getStyleClass().add("column-centered");
        losesCol.setCellValueFactory((p) -> new ObservableValueBase<>() {
            @Override
            public Integer getValue() {
                return p.getValue().getLoses();
            }
        });

        TableColumn<Standing, Integer> runsForCol = new TableColumn<>("RF");
        runsForCol.setMinWidth(WIDTH);
        runsForCol.setMaxWidth(WIDTH);
        runsForCol.getStyleClass().add("column-centered");
        runsForCol.setCellValueFactory((p) -> new ObservableValueBase<>() {
            @Override
            public Integer getValue() {
                return p.getValue().getRunsFor();
            }
        });

        TableColumn<Standing, Integer> runsAgainstCol = new TableColumn<>("RA");
        runsAgainstCol.setMinWidth(WIDTH);
        runsAgainstCol.setMaxWidth(WIDTH);
        runsAgainstCol.getStyleClass().add("column-centered");
        runsAgainstCol.setCellValueFactory((p) -> new ObservableValueBase<>() {
            @Override
            public Integer getValue() {
                return p.getValue().getRunsAgainst();
            }
        });

        TableColumn<Standing, Integer> pointsCol = new TableColumn<>("Points");
        pointsCol.setMinWidth(WIDTH);
        pointsCol.setMaxWidth(WIDTH);
        pointsCol.getStyleClass().add("column-centered");
        pointsCol.setCellValueFactory((p) -> new ObservableValueBase<>() {
            @Override
            public Integer getValue() {
                return p.getValue().getPoints();
            }
        });

        table.getColumns().setAll(numberCol, teamCol, gamesCol, winsCol, losesCol, runsForCol, runsAgainstCol, pointsCol);
        
        table.setFixedCellSize(25);
        table.prefHeightProperty().bind(Bindings.size(table.getItems()).multiply(table.getFixedCellSize()).add(40));

        super.prefWidthProperty().bind(Softman.getPrimaryStage().widthProperty().multiply(0.5));
        super.getChildren().add(table);
    }

    public void setLeague(League league) {
        if (data.size() > 0) {
            data.clear();
        }
        if (league != null) {
            nameLabel.setText(league.getName());
            data.addAll(league.getStandings());
        } else {
            nameLabel.setText("Not participating in any league");
        }
        refresh();
    }

    public void refresh() {
        FXCollections.sort(data);
        table.refresh();
    }
}
