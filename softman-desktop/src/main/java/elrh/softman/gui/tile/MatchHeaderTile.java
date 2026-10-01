package elrh.softman.gui.tile;

import atlantafx.base.controls.Card;
import elrh.softman.gui.kit.Images;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.utils.FormatUtils;
import elrh.softman.gui.utils.GUIUtils;
import elrh.softman.logic.core.Match;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class MatchHeaderTile extends Card {

    private final ImageView awayImage = Images.rounded(4);
    private final ImageView homeImage = Images.rounded(4);
    private final Label awayName = new Label();
    private final Label homeName = new Label();
    private final Label detailLabel = new Label();

    public MatchHeaderTile() {
        awayName.getStyleClass().add(Tokens.TITLE);
        homeName.getStyleClass().add(Tokens.TITLE);
        detailLabel.getStyleClass().add(Tokens.CAPTION);

        var awayText = Layouts.column(Space.XS, awayName, caption("Away"));
        var away = Layouts.row(Space.M, awayImage, awayText);

        var homeText = Layouts.column(Space.XS, homeName, caption("Home"));
        homeText.setAlignment(Pos.CENTER_RIGHT);
        var home = Layouts.row(Space.M, homeText, homeImage);
        home.setAlignment(Pos.CENTER_RIGHT);

        // zero pref width + grow on both sides keeps the centre block centred
        for (var side : new HBox[] {away, home}) {
            Layouts.grow(side);
            side.setPrefWidth(0);
        }

        var atLabel = new Label("@");
        atLabel.getStyleClass().add(Tokens.DISPLAY);
        var center = Layouts.column(Space.XS, atLabel, detailLabel);
        center.setAlignment(Pos.CENTER);

        setHeader(Layouts.row(Space.L, away, center, home));
    }

    public void setMatch(Match match) {
        if (match != null) {
            var awayInfo = match.getAwayLineup().getLineupInfo();
            var homeInfo = match.getHomeLineup().getLineupInfo();
            awayImage.setImage(GUIUtils.getImageOrDefault(awayInfo.getTeamLogo()));
            awayName.setText(awayInfo.getTeamName());
            homeImage.setImage(GUIUtils.getImageOrDefault(homeInfo.getTeamLogo()));
            homeName.setText(homeInfo.getTeamName());
            var matchInfo = match.getMatchInfo();
            detailLabel.setText(matchInfo.getMatchDay().format(FormatUtils.DF) + ", " + matchInfo.getStadium());
        }
    }

    private static Label caption(String text) {
        var label = new Label(text);
        label.getStyleClass().add(Tokens.CAPTION);
        return label;
    }

}
