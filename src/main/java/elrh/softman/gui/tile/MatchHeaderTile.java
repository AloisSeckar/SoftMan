package elrh.softman.gui.tile;

import elrh.softman.logic.core.Match;
import elrh.softman.utils.FormatUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class MatchHeaderTile extends BorderPane {

    private static final int LOGO_SIZE = 100;

    private final Label titleLabel;
    private final ImageView awayImage;
    private final ImageView homeImage;

    public MatchHeaderTile() {

        super.getStyleClass().add("framed");
        super.setPadding(FormatUtils.PADDING_5);

        awayImage = new ImageView();
        awayImage.setFitWidth(LOGO_SIZE);
        awayImage.setFitHeight(LOGO_SIZE);
        super.setLeft(awayImage);

        titleLabel = new Label(" @ ");
        titleLabel.getStyleClass().setAll("h3");
        super.setCenter(titleLabel);

        homeImage = new ImageView();
        homeImage.setFitWidth(LOGO_SIZE);
        homeImage.setFitHeight(LOGO_SIZE);
        super.setRight(homeImage);

    }

    public void setMatch(Match match) {
        if (match != null) {
            awayImage.setImage(new Image(getClass().getResourceAsStream(match.getAwayLineup().getTeamLogo())));
            titleLabel.setText(match.getAwayLineup().getTeamName() + " vs. " + match.getHomeLineup().getTeamName());
            homeImage.setImage(new Image(getClass().getResourceAsStream(match.getHomeLineup().getTeamLogo())));
        }
    }

}
