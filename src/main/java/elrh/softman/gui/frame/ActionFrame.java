package elrh.softman.gui.frame;

import elrh.softman.logic.AssociationManager;
import java.time.LocalDate;
import elrh.softman.utils.FormatUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class ActionFrame extends HBox {

    private final Label dateValueLabel;

    private static ActionFrame INSTANCE;

    public static ActionFrame getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ActionFrame();
        }
        return INSTANCE;
    }

    private ActionFrame() {

        Button nextDayButton = new Button("Next day");
        this.getChildren().add(nextDayButton);
        nextDayButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> AssociationManager.getInstance().nextDay());

        var dateLabel = new Label("Current date: ");
        dateLabel.setPadding(FormatUtils.TOPLEFT_5);
        this.getChildren().add(dateLabel);

        dateValueLabel = new Label();
        dateValueLabel.getStyleClass().add("date-label");
        this.getChildren().add(dateValueLabel);
    }

    public void updateDateValue(LocalDate date) {
        if (date != null) {
            dateValueLabel.setText(date.format(FormatUtils.DF));
        } else {
            dateValueLabel.setText("UNDEFINED");
        }
    }
}
