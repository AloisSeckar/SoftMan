package elrh.softman.gui.frame;

import elrh.softman.Softman;
import elrh.softman.gui.MainLayout;
import elrh.softman.gui.kit.Icons;
import elrh.softman.gui.kit.Images;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.utils.GUIUtils;
import elrh.softman.gui.utils.InfoUtils;
import elrh.softman.logic.AssociationManager;
import elrh.softman.utils.Constants;
import elrh.softman.utils.factory.AssociationFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.feather.Feather;

// TODO custom title bar (title, minimize, close) before going live
public class MenuFrame extends HBox {

    private static MenuFrame INSTANCE;
    
    public static MenuFrame getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MenuFrame();
        }
        return INSTANCE;
    }
    
    private MenuFrame() {
        super(Space.M.getPx());
        setAlignment(Pos.CENTER_LEFT);

        var logo = Images.rounded(1.75);
        logo.setImage(GUIUtils.getImageOrDefault("/img/ball.png"));
        var brand = new Label("SoftMan", logo);
        brand.getStyleClass().add(Tokens.TITLE);

        var newGame = item("New game", Feather.FILE_PLUS, () -> {
            if (InfoUtils.confirm("Start a new game? Unsaved progress will be lost.")) {
                AssociationManager.getInstance().reset();
                AssociationFactory.populateAssociation();
                MainLayout.getInstance().setUp();
            }
        });

        var loadGame = item("Load game", Feather.FOLDER, () -> {
            var world = AssociationManager.getInstance();
            if (!world.hasSaveFile(Constants.DEFAULT_GAME_ID)) {
                InfoUtils.showMessage("No saved game found.");
                return;
            }
            if (InfoUtils.confirm("Load the last saved game? Unsaved progress will be lost.")) {
                var result = world.loadGame(Constants.DEFAULT_GAME_ID);
                if (result.ok()) {
                    MainLayout.getInstance().setUp();
                    InfoUtils.showMessage("Game loaded.");
                } else {
                    InfoUtils.showMessage("Load failed: " + result.message());
                }
            }
        });

        var saveGame = item("Save game", Feather.SAVE, () -> {
            var result = AssociationManager.getInstance().saveGame(Constants.DEFAULT_GAME_ID);
            InfoUtils.showMessage(result.ok() ? "Game saved." : "Save failed: " + result.message());
        });

        var info = item("About", Feather.INFO, () -> {
            // TODO about
        });

        var exit = item("Exit", Feather.LOG_OUT, Softman::closeIfConfirmed);

        var menuGame = new Menu("Game");
        menuGame.getItems().addAll(newGame, loadGame, saveGame, new SeparatorMenuItem(), info, new SeparatorMenuItem(), exit);

        var menuShow = new Menu("Show");

        getChildren().addAll(brand, new MenuBar(menuGame, menuShow));
    }

    private static MenuItem item(String text, Ikon icon, Runnable action) {
        var item = new MenuItem(text, Icons.of(icon));
        item.setOnAction(e -> action.run());
        return item;
    }

}
