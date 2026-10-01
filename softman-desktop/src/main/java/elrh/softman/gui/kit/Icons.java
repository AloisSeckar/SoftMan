package elrh.softman.gui.kit;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import atlantafx.base.theme.Styles;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Icons {

    // size and colour come from the theme; pass Styles.ACCENT / SUCCESS / DANGER etc. to tint
    public static FontIcon of(Ikon icon, String... styleClasses) {
        var fontIcon = new FontIcon(icon);
        fontIcon.getStyleClass().addAll(styleClasses);
        return fontIcon;
    }

    public static Button button(Ikon icon, String tooltip) {
        var button = new Button(null, of(icon));
        button.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
        button.setTooltip(new Tooltip(tooltip));
        return button;
    }
}
