package elrh.softman;

import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.NordLight;
import atlantafx.base.theme.Theme;
import elrh.softman.db.SqliteNameSource;
import elrh.softman.db.SqliteGameRepository;
import elrh.softman.gui.MainLayout;
import elrh.softman.gui.sim.SimulationController;
import elrh.softman.gui.utils.InfoUtils;
import elrh.softman.logic.AssociationManager;
import elrh.softman.utils.Constants;
import elrh.softman.utils.factory.AssociationFactory;
import elrh.softman.utils.factory.PlayerFactory;
import java.util.Objects;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    
    /** Requests closing the GUI window; the close-request handler may still abort it. */
    public static void requestClose() {
        primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    /** Interceptor that can still abort closing the GUI window */
    private static void onCloseRequest(WindowEvent event) {
        /* TODO enable confirmation before going live!
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Really quit?", ButtonType.YES, ButtonType.NO);
        alert.initOwner(primaryStage);
        alert.showAndWait();
        if (alert.getResult() != ButtonType.YES) {
            event.consume();
        }
        */
    }

    /** Runs after the window closed, on every exit path (title bar, Esc, Game → Exit), and saves the game. */
    @Override
    public void stop() {
        tearDownGame();
    }

    ////////////////////////////////////////////////////////////////////////////
    private void setupStage() {
        var spinner = new ProgressIndicator();
        spinner.setVisible(false);

        var wrappingLayout = new StackPane(MainLayout.getInstance(), spinner);

        var scene = new Scene(wrappingLayout, 0, 0);
        applyTheme(scene);

        AssociationManager.getInstance().setSimulationRunner(new SimulationController(spinner));
        AssociationManager.getInstance().setConfirmationPrompt(InfoUtils::confirm);
        MainLayout.getInstance().setUp();
        
        primaryStage.setTitle("SOFTMAN 0.1");
        primaryStage.setScene(scene);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        // TODO switch to full screen before going live!
        // primaryStage.setFullScreen(true);
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(Softman::onCloseRequest);

        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                requestClose();
            }
        });
    }

    private static final String PREF_DARK_THEME = "darkTheme";
    private static Preferences preferences = Preferences.userNodeForPackage(Softman.class);
    private static boolean darkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

    /**
     * Applies the active theme and the application stylesheet to a scene.
     *
     * @param scene the scene to style
     */
    public static void applyTheme(Scene scene) {
        applyStylesheet(darkTheme);
        scene.getStylesheets().add(Softman.class.getResource("/css/softman.css").toExternalForm());
    }

    /**
     * Returns whether the dark theme is active.
     *
     * @return {@code true} if dark theme is enabled, {@code false} for light theme
     */
    public static boolean isDarkTheme() {
        return darkTheme;
    }

    /**
     * Switches the active theme and persists the choice in user preferences.
     *
     * @param dark {@code true} for dark theme, {@code false} for light theme
     */
    public static void setDarkTheme(boolean dark) {
        darkTheme = dark;
        preferences.putBoolean(PREF_DARK_THEME, dark);
        applyStylesheet(dark);
    }

    /**
     * Replaces the backing preference store and reloads the saved theme choice.
     *
     * @param prefs the preference node to use
     */
    public static void setPreferences(Preferences prefs) {
        preferences = Objects.requireNonNull(prefs);
        darkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
    }

    // the user-agent stylesheet is global, so every open scene switches at once
    private static void applyStylesheet(boolean dark) {
        Theme theme = dark ? new NordDark() : new NordLight();
        Application.setUserAgentStylesheet(theme.getUserAgentStylesheet());
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
