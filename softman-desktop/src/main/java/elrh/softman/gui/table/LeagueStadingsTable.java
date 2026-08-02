package elrh.softman.gui.table;

import elrh.softman.logic.core.League;
import elrh.softman.logic.core.stats.Standing;
import java.util.ArrayList;
import java.util.Arrays;
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
        nameLabel.getStyleClass().add("league-name");
        super.getChildren().add(nameLabel);
        super.getStyleClass().setAll("padding-5");

        table = new TableView<>();
        table.setPrefWidth(820d);
        table.setPrefHeight(225d);
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
        teamCol.setMinWidth(250d);
        teamCol.setMaxWidth(250d);
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

        table.getColumns().setAll(Arrays.asList(numberCol, teamCol, gamesCol, winsCol, losesCol, runsForCol, runsAgainstCol, pointsCol));

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
