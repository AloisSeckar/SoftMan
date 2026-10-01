package elrh.softman.gui.frame;

import atlantafx.base.theme.Styles;
import elrh.softman.gui.kit.Icons;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.utils.FormatUtils;
import elrh.softman.logic.AssociationManager;
import java.time.LocalDate;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.feather.Feather;

public class ActionFrame extends HBox {

    private final Label dateValueLabel = new Label();
    private final DatePicker simUntilPicker = new DatePicker();

    private static ActionFrame INSTANCE;

    public static ActionFrame getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ActionFrame();
        }
        return INSTANCE;
    }

    private ActionFrame() {
        super(Space.S.getPx());
        getStyleClass().add(Tokens.STATUS_BAR);
        setAlignment(Pos.CENTER_LEFT);

        var dateCaption = new Label("Current date");
        dateCaption.getStyleClass().add(Tokens.CAPTION);
        dateValueLabel.getStyleClass().add(Styles.TEXT_BOLD);

        simUntilPicker.setPrefWidth(Layouts.em(9));

        var simUntilButton = new Button("Simulate until", Icons.of(Feather.FAST_FORWARD));
        simUntilButton.setOnAction(e -> simulateIfConfirmed());

        var nextDayButton = new Button("Next day", Icons.of(Feather.CHEVRONS_RIGHT));
        nextDayButton.getStyleClass().add(Styles.ACCENT);
        nextDayButton.setOnAction(e -> AssociationManager.getInstance().nextDay());

        getChildren().addAll(Icons.of(Feather.CALENDAR), dateCaption, dateValueLabel,
            Layouts.spacer(), simUntilPicker, simUntilButton, nextDayButton);
    }

    public void updateDateValue(LocalDate date) {
        if (date != null) {
            dateValueLabel.setText(date.format(FormatUtils.DF));
            simUntilPicker.setValue(date);
        } else {
            dateValueLabel.setText("UNDEFINED");
            simUntilPicker.setValue(null);
        }
    }

    public void simulateIfConfirmed() {
        var alert = new Alert(Alert.AlertType.CONFIRMATION, "Simulate until selected date?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            AssociationManager.getInstance().simulateUntil(simUntilPicker.getValue());
        }
    }
}
