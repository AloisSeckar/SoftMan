package elrh.softman;

import elrh.softman.db.SqliteNameSource;
import elrh.softman.db.SqliteGameRepository;
import elrh.softman.gui.MainLayout;
import elrh.softman.gui.sim.SimulationController;
import elrh.softman.gui.utils.InfoUtils;
import elrh.softman.logic.AssociationManager;
import elrh.softman.utils.Constants;
import elrh.softman.utils.factory.AssociationFactory;
import elrh.softman.utils.factory.PlayerFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Softman extends Application {
    
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        setupGame();
        
        Softman.primaryStage = primaryStage;
        setupStage();
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public static void closeIfConfirmed() {
        /* TODO enable confirmation before going live!
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Really quit?", ButtonType.YES, ButtonType.NO);
        alert.initOwner(primaryStage);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
             
        }
        */
        tearDownGame();
        primaryStage.close();
    }

    ////////////////////////////////////////////////////////////////////////////
    private void setupStage() {
        var spinner = new ProgressIndicator();
        var wrappingLayout = new StackPane(MainLayout.getInstance(), spinner);

        var scene = new Scene(wrappingLayout, 0, 0);
        scene.getStylesheets().add(getClass().getResource("/css/softman.css").toExternalForm());
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

        // TODO unify actions performed upon starting new game
        AssociationManager.getInstance().setSimulationRunner(new SimulationController(spinner));
        AssociationManager.getInstance().setConfirmationPrompt(InfoUtils::confirm);
        AssociationManager.getInstance().nextDay();
        MainLayout.getInstance().setUp();
        
        primaryStage.setTitle("SOFTMAN 0.1");
        primaryStage.setScene(scene);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        // TODO switch to full screen before going live!
        // primaryStage.setFullScreen(true);
        primaryStage.setMaximized(true);

        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                closeIfConfirmed();
            }
        });
    }

    private static SqliteNameSource nameSource;

    private static void setupGame() {
        nameSource = new SqliteNameSource();
        PlayerFactory.setNameSource(nameSource);

        var world = AssociationManager.getInstance();
        world.setGameRepository(new SqliteGameRepository());
        if (world.hasSaveFile(Constants.DEFAULT_GAME_ID) && world.loadGame(Constants.DEFAULT_GAME_ID).ok()) {
            return;
        }
        AssociationFactory.populateAssociation();
    }
    
    private static void tearDownGame() {
        AssociationManager.getInstance().saveGame(Constants.DEFAULT_GAME_ID);
        if (nameSource != null) {
            nameSource.close();
        }
    }
}
