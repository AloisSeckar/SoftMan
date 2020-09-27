package elrh.softman.gui.menu;

import de.jensd.fx.glyphs.fontawesome.*;
import elrh.softman.Softman;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MenuFrame extends AnchorPane {
    
    private final MenuBar menu = new MenuBar();
    
    private static MenuFrame INSTANCE;
    
    public static MenuFrame getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MenuFrame();
        }
        return INSTANCE;
    }
    
    private MenuFrame() {
        
        super.getStyleClass().setAll("menu-frame");
        
        MenuItem newGame = new MenuItem("New game", new FontAwesomeIconView(FontAwesomeIcon.FILE_ALT));
        newGame.setOnAction((ActionEvent t) -> {
            // TODO new
        });
        
        MenuItem loadGame = new MenuItem("Load game", new FontAwesomeIconView(FontAwesomeIcon.FOLDER_OPEN_ALT));
        loadGame.setOnAction((ActionEvent t) -> {
            // TODO load
        });
        
        MenuItem saveGame = new MenuItem("Save game", new FontAwesomeIconView(FontAwesomeIcon.FLOPPY_ALT));
        saveGame.setOnAction((ActionEvent t) -> {
            // TODO save
        });
        
        MenuItem info = new MenuItem("About", new FontAwesomeIconView(FontAwesomeIcon.INFO));
        info.setOnAction((ActionEvent t) -> {
            // TODO about
        });
        
        MenuItem exit = new MenuItem("Exit", new FontAwesomeIconView(FontAwesomeIcon.CLOSE));
        exit.setOnAction((ActionEvent t) -> {
            Softman.closeIfConfirmed();
        });
        
        Menu menuGame = new Menu("Game");
        menuGame.getItems().addAll(newGame, loadGame, saveGame, info, exit);
        
        Menu menuShow = new Menu("Show");
        // menuShow.getItems().addAll();
        
        menu.getMenus().addAll(menuGame, menuShow);
        
        menu.prefWidthProperty().bind(Softman.getPrimaryStage().widthProperty());
        menu.getStyleClass().setAll("menu-bar");
        
        super.getChildren().add(menu);
        AnchorPane.setLeftAnchor(menu, 0d);
        AnchorPane.setTopAnchor(menu, 30d);
        
        Label titleLabel = new Label("SOFTMAN 0.1");
        titleLabel.getStyleClass().setAll("title-label");
        super.getChildren().add(titleLabel);
        AnchorPane.setLeftAnchor(titleLabel, 5d);
        AnchorPane.setTopAnchor(titleLabel, 5d);
        
        Button closeButton = new Button("", new FontAwesomeIconView(FontAwesomeIcon.CLOSE));
        closeButton.getStyleClass().setAll("close-button");
        closeButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            Softman.closeIfConfirmed();
        });
        super.getChildren().add(closeButton);
        AnchorPane.setRightAnchor(closeButton, 0d);
        AnchorPane.setTopAnchor(closeButton, 0d);
    }
    
}
