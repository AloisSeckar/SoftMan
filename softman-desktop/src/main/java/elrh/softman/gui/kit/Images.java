package elrh.softman.gui.kit;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Images {

    // square image with rounded corners, so opaque logos and faces sit softly on light cards
    public static ImageView rounded(double ems) {
        double size = Layouts.em(ems);
        var view = new ImageView();
        view.setFitWidth(size);
        view.setFitHeight(size);
        view.setPreserveRatio(true);
        view.setSmooth(true);
        var clip = new Rectangle(size, size);
        clip.setArcWidth(size / 4);
        clip.setArcHeight(size / 4);
        view.setClip(clip);
        return view;
    }
}
