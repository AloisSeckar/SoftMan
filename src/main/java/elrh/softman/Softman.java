package elrh.softman;

import elrh.softman.logic.db.*;
import elrh.softman.gui.MainLayout;
import elrh.softman.logic.AssociationManager;
import elrh.softman.utils.factory.AssociationFactory;
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
        AssociationManager.getInstance().setGuiSpinner(spinner);
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

    private static void setupGame() {
        String gameId = "test";
        GameDBManager.getInstance().setConnection(gameId);
        
        AssociationManager.getInstance();
        AssociationFactory.populateAssociation();
    }
    
    private static void tearDownGame() {
        SourcesDBManager.getInstance().closeConnection();
        GameDBManager.getInstance().closeConnection();
    }
}
