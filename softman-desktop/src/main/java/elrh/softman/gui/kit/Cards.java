package elrh.softman.gui.kit;

import atlantafx.base.controls.Card;
import javafx.scene.Node;
import javafx.scene.control.Label;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Cards {

    public static Card titled(String title, Node body) {
        var card = plain(body);
        card.setHeader(title(title));
        return card;
    }

    public static Card plain(Node body) {
        var card = new Card();
        card.setBody(body);
        card.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return card;
    }

    public static Label title(String text) {
        var label = new Label(text);
        label.getStyleClass().add(Tokens.CARD_TITLE);
        return label;
    }
}
