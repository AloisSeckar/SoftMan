package elrh.softman.gui.table;

import elrh.softman.Softman;
import elrh.softman.db.orm.PlayerInfo;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.value.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.collections.*;

public class TeamPlayersTable extends Pane {

    private static final double COLUMN_WIDTH = 100d;

    private final TableView<PlayerInfo> table;
    private final ObservableList<PlayerInfo> data;

    public TeamPlayersTable(List<PlayerInfo> players) {
        data = FXCollections.observableList(players);
        FXCollections.sort(data);

        table = new TableView();
        table.setItems(data);

        TableColumn<PlayerInfo, Integer> numberCol = new TableColumn("#");
        numberCol.setMinWidth(COLUMN_WIDTH);
        numberCol.setMaxWidth(COLUMN_WIDTH);
        numberCol.getStyleClass().add("column-centered");
        numberCol.setCellValueFactory((p) -> new ObservableValueBase<Integer>() {
            @Override
            public Integer getValue() {
                return p.getValue().getNumber();
            }
        });

        TableColumn<PlayerInfo, String> nameCol = new TableColumn("Name");
        nameCol.setMinWidth(200d);
        nameCol.setMaxWidth(200d);
        nameCol.setCellValueFactory((p) -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return p.getValue().getName();
            }
        });

        TableColumn<PlayerInfo, Integer> ageCol = new TableColumn("Age");
        ageCol.setMinWidth(COLUMN_WIDTH);
        ageCol.setMaxWidth(COLUMN_WIDTH);
        ageCol.getStyleClass().add("column-centered");
        ageCol.setCellValueFactory((p) -> new ObservableValueBase<Integer>() {
            @Override
            public Integer getValue() {
                return p.getValue().getAge();
            }
        });

        TableColumn<PlayerInfo, Integer> skillCol = new TableColumn("Skill");
        skillCol.setMinWidth(COLUMN_WIDTH);
        skillCol.setMaxWidth(COLUMN_WIDTH);
        skillCol.getStyleClass().add("column-centered");
        skillCol.setCellValueFactory((p) -> new ObservableValueBase<Integer>() {
            @Override
            public Integer getValue() {
                PlayerInfo plr = p.getValue();
                return plr.getStats().getTotal();
            }
        });

        TableColumn<PlayerInfo, Integer> battingCol = new TableColumn("Batting");
        battingCol.setMinWidth(COLUMN_WIDTH);
        battingCol.setMaxWidth(COLUMN_WIDTH);
        battingCol.getStyleClass().add("column-centered");
        battingCol.setCellValueFactory((p) -> new ObservableValueBase<Integer>() {
            @Override
            public Integer getValue() {
                return p.getValue().getStats().getBattingSkill();
            }
        });

        TableColumn<PlayerInfo, Integer> pitchingCol = new TableColumn("Pitching");
        pitchingCol.setMinWidth(COLUMN_WIDTH);
        pitchingCol.setMaxWidth(COLUMN_WIDTH);
        pitchingCol.getStyleClass().add("column-centered");
        pitchingCol.setCellValueFactory((p) -> new ObservableValueBase<Integer>() {
            @Override
            public Integer getValue() {
                return p.getValue().getStats().getPitchingSkill();
            }
        });

        TableColumn<PlayerInfo, Integer> fieldingCol = new TableColumn("Fielding");
        fieldingCol.setMinWidth(COLUMN_WIDTH);
        fieldingCol.setMaxWidth(COLUMN_WIDTH);
        fieldingCol.getStyleClass().add("column-centered");
        fieldingCol.setCellValueFactory((p) -> new ObservableValueBase<Integer>() {
            @Override
            public Integer getValue() {
                return p.getValue().getStats().getFieldingSkill();
            }
        });

        TableColumn<PlayerInfo, Integer> physicalCol = new TableColumn("Physical");
        physicalCol.setMinWidth(COLUMN_WIDTH);
        physicalCol.setMaxWidth(COLUMN_WIDTH);
        physicalCol.getStyleClass().add("column-centered");
        physicalCol.setCellValueFactory((p) -> new ObservableValueBase<Integer>() {
            @Override
            public Integer getValue() {
                return p.getValue().getStats().getPhysicalSkill();
            }
        });
        
        table.getColumns().setAll(numberCol, nameCol, ageCol, skillCol, battingCol, pitchingCol, fieldingCol, physicalCol);
        
        table.setFixedCellSize(25);
        table.prefHeightProperty().bind(Bindings.size(table.getItems()).multiply(table.getFixedCellSize()).add(40));

        super.getChildren().add(table);
    }

    public void refresh() {
        FXCollections.sort(data);
        table.refresh();
    }
}
