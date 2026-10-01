package elrh.softman.gui.kit;

import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import java.util.function.Function;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

        public TableColumn<S, T> build() {
            return column;
        }
    }
}
