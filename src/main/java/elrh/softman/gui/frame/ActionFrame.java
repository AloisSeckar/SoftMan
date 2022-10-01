package elrh.softman.gui.frame;

import elrh.softman.logic.AssociationManager;
import java.time.LocalDate;
import elrh.softman.utils.FormatUtils;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class ActionFrame extends HBox {

    private final Label dateValueLabel;
    private final DatePicker simUntilPicker = new DatePicker();

    private static ActionFrame INSTANCE;

    public static ActionFrame getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ActionFrame();
        }
        return INSTANCE;
    }

    private ActionFrame() {

        var nextDayButton = new Button("Next day");
        this.getChildren().add(nextDayButton);
        nextDayButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> AssociationManager.getInstance().nextDay());

        var dateLabel = new Label("Current date: ");
        dateLabel.setPadding(FormatUtils.TOPLEFT_5);
        this.getChildren().add(dateLabel);

        dateValueLabel = new Label();
        dateValueLabel.getStyleClass().add("date-label");
        this.getChildren().add(dateValueLabel);

        var simUntilButton = new Button("Simulate until");
        this.getChildren().add(simUntilButton);
        simUntilButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> simulateIfConfirmed());

        simUntilPicker.setPrefWidth(100d);
        this.getChildren().add(simUntilPicker);

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
