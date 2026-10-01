package elrh.softman.gui.kit;

import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import java.util.function.Function;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Tables {

    public static <S> TableView<S> table(String... styleClasses) {
        var table = new TableView<S>();
        table.getStyleClass().add(Styles.STRIPED);
        table.getStyleClass().addAll(styleClasses);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        return table;
    }

    public static <S, T> ColumnBuilder<S, T> column(String title, Function<S, T> value) {
        return new ColumnBuilder<>(title, value);
    }

    // dense table sized to its rows; AtlantaFX dense rows are 2em, the header about 2.5em
    public static void fitRows(TableView<?> table) {
        table.getStyleClass().add(Styles.DENSE);
        table.setFixedCellSize(Layouts.em(2));
        table.prefHeightProperty().bind(Bindings.size(table.getItems()).multiply(Layouts.em(2)).add(Layouts.em(3)));
        table.setMinHeight(Region.USE_PREF_SIZE);
    }

    public static final class ColumnBuilder<S, T> {

        private final TableColumn<S, T> column;

        private ColumnBuilder(String title, Function<S, T> value) {
            column = new TableColumn<>(title);
            column.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(value.apply(cell.getValue())));
        }

        public ColumnBuilder<S, T> numeric() {
            column.getStyleClass().add(Tweaks.ALIGN_RIGHT);
            return this;
        }

        public ColumnBuilder<S, T> centered() {
            column.getStyleClass().add(Tweaks.ALIGN_CENTER);
            return this;
        }

        public ColumnBuilder<S, T> width(double ems) {
            column.setPrefWidth(Layouts.em(ems));
            return this;
        }

        public ColumnBuilder<S, T> sortable(boolean sortable) {
            column.setSortable(sortable);
            return this;
        }

        public ColumnBuilder<S, T> style(String... styleClasses) {
            column.getStyleClass().addAll(styleClasses);
            return this;
        }

        public ColumnBuilder<S, T> cells(Callback<TableColumn<S, T>, TableCell<S, T>> factory) {
            column.setCellFactory(factory);
            return this;
        }

        // 0-100 value tinted by rating tier
        public ColumnBuilder<S, T> rating() {
            column.getStyleClass().add(Tweaks.ALIGN_CENTER);
            column.setCellFactory(c -> new TableCell<>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    getStyleClass().removeAll(Tokens.RATING_TIERS);
                    getStyleClass().remove(Tokens.RATING_CELL);
                    if (empty || item == null) {
                        setText(null);
                        return;
                    }
                    setText(item.toString());
                    if (item instanceof Integer value) {
                        getStyleClass().addAll(Tokens.RATING_CELL, Ratings.tier(value));
                    }
                }
            });
            return this;
        }

        public TableColumn<S, T> build() {
            return column;
        }
    }
}
