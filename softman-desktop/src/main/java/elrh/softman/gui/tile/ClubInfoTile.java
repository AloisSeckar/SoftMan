package elrh.softman.gui.tile;

import atlantafx.base.controls.Card;
import atlantafx.base.theme.Styles;
import elrh.softman.gui.kit.Icons;
import elrh.softman.gui.kit.Images;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.utils.GUIUtils;
import elrh.softman.logic.AssociationManager;
import elrh.softman.logic.core.Club;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.feather.Feather;

public class ClubInfoTile extends Card {

    private final ImageView logoView = Images.rounded(4);
    private final Label nameLabel = new Label();
    private final Label ownerChip = new Label();
    private final Region colorStripe = new Region();
    private final Label stadiumValue = new Label();
    private final Label moneyValue = new Label();
    private final Label teamsValue = new Label();
    private final Label statusValue = new Label();

    public ClubInfoTile() {
        nameLabel.getStyleClass().add(Tokens.DISPLAY);
        nameLabel.setWrapText(true);
        ownerChip.getStyleClass().add(Tokens.CHIP);

        setHeader(Layouts.row(Space.M, logoView, Layouts.column(Space.XS, nameLabel, ownerChip)));

        colorStripe.getStyleClass().add(Tokens.CLUB_STRIPE);

        var banner = new Region();
        banner.getStyleClass().add(Tokens.CLUB_BANNER);

        var facts = new GridPane(Space.M.getPx(), Space.S.getPx());
        facts.getStyleClass().add(Tokens.CLUB_FACTS);
        addFact(facts, 0, Feather.MAP_PIN, "Stadium", stadiumValue);
        addFact(facts, 1, Feather.DOLLAR_SIGN, "Budget", moneyValue);
        addFact(facts, 2, Feather.USERS, "Teams", teamsValue);
        addFact(facts, 3, Feather.CHECK_CIRCLE, "Status", statusValue);

        setBody(Layouts.column(Space.M, colorStripe, banner, facts));
    }

    public void reload(Club club) {
        if (club != null) {
            var info = club.getClubInfo();
            nameLabel.setText(info.getName());
            stadiumValue.setText(info.getStadium());
            moneyValue.setText(String.format("$ %,d", info.getMoney()));
            teamsValue.setText(String.valueOf(club.getTeams().size()));
            statusValue.setText(club.isActive() ? "Active" : "Inactive");
            setOwner(AssociationManager.getInstance().getUser().getActiveClub() == club);
            logoView.setImage(GUIUtils.getImageOrDefault(info.getLogo()));
            GUIUtils.setBackgroundColor(colorStripe, club.getColor());
        } else {
            nameLabel.setText("No club selected");
            stadiumValue.setText("");
            moneyValue.setText("");
            teamsValue.setText("");
            statusValue.setText("");
            ownerChip.setVisible(false);
            logoView.setImage(GUIUtils.getImageOrDefault("/img/ball.png"));
            colorStripe.setBackground(null);
        }
    }

    private void setOwner(boolean managedByUser) {
        ownerChip.setVisible(true);
        ownerChip.setText(managedByUser ? "Your club" : "Computer");
        ownerChip.getStyleClass().remove(Styles.ACCENT);
        if (managedByUser) {
            ownerChip.getStyleClass().add(Styles.ACCENT);
        }
    }

    private static void addFact(GridPane grid, int row, Ikon icon, String caption, Label value) {
        var captionLabel = new Label(caption);
        captionLabel.getStyleClass().add(Tokens.CAPTION);
        value.getStyleClass().add(Styles.TEXT_BOLD);
        grid.addRow(row, Icons.of(icon), captionLabel, value);
    }

}
