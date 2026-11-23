package elrh.softman.gui;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import elrh.softman.Softman;
import elrh.softman.gui.frame.ActionFrame;
import elrh.softman.gui.frame.ContentFrame;
import elrh.softman.gui.frame.ContentFrame.Screen;
import elrh.softman.gui.frame.FocusFrame;
import elrh.softman.gui.frame.MenuFrame;
import elrh.softman.gui.kit.Cards;
import elrh.softman.gui.kit.Icons;
import elrh.softman.gui.kit.Images;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Ratings;
import elrh.softman.gui.kit.Tables;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.tab.LineupTab;
import elrh.softman.gui.tab.PlayerTab;
import elrh.softman.gui.tile.BoxScoreTile;
import elrh.softman.gui.tile.CalendarTile;
import elrh.softman.gui.tile.ClubInfoTile;
import elrh.softman.gui.tile.DefenseTile;
import elrh.softman.gui.tile.LineupRowTile;
import elrh.softman.gui.tile.LineupTile;
import elrh.softman.gui.tile.MatchHeaderTile;
import elrh.softman.gui.tile.PlayerAttributesTile;
import elrh.softman.gui.tile.PlayerInfoTile;
import elrh.softman.gui.tile.ScheduleRowTile;
import elrh.softman.gui.utils.FormatUtils;
import elrh.softman.logic.AssociationManager;
import elrh.softman.utils.Constants;
import elrh.softman.utils.factory.AssociationFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.AbstractPreferences;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kordamp.ikonli.feather.Feather;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
class GuiSmokeTest {

    @BeforeAll
    static void setUpWorld() {
        // isolate tests from the user's real OS preferences
        Softman.setPreferences(new InMemoryPreferences());

        var manager = AssociationManager.getInstance();
        manager.reset();
        // test mode skips the confirmation dialog and the background simulation runner
        manager.setTestMode(true);
        AssociationFactory.populateAssociation();
        manager.nextDay();
    }

    @Start
    void start(Stage stage) {
        // MainLayout is a singleton and a node can be the root of one scene only
        var scene = MainLayout.getInstance().getScene();
        if (scene == null) {
            scene = new Scene(MainLayout.getInstance(), 1280, 800);
            Softman.applyTheme(scene);
            MainLayout.getInstance().setUp();
        }
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void mainLayoutHasAllTabs() {
        assertThat(ContentFrame.getInstance().getTabs().size(), is(Screen.values().length));
    }

    @Test
    void switchToSelectsScreenTab() throws Throwable {
        var frame = ContentFrame.getInstance();
        for (var screen : Screen.values()) {
            FxToolkit.setupFixture(() -> frame.switchTo(screen));
            assertThat(frame.getSelectionModel().getSelectedItem().getText(), is(screen.getTitle()));
        }
        WaitForAsyncUtils.checkException();
    }

    @Test
    void everyTabRendersWhenSelected() throws Throwable {
        var tabs = ContentFrame.getInstance();
        for (int i = 0; i < tabs.getTabs().size(); i++) {
            final int index = i;
            FxToolkit.setupFixture(() -> {
                tabs.getSelectionModel().select(index);
                tabs.getScene().getRoot().applyCss();
                tabs.getScene().getRoot().layout();
            });
        }
        WaitForAsyncUtils.checkException();
    }

    @Test
    void allTilesConstructAndLoad() throws Throwable {
        var manager = AssociationManager.getInstance();
        var club = manager.getClubs(true).getFirst();
        var league = manager.getLeagues(Constants.START_YEAR).getFirst();
        var match = manager.getAllMatches().getFirst();
        var player = manager.getPlayers(true).getFirst().getPlayerInfo();

        var holder = new VBox();
        FxToolkit.setupFixture(() -> {
            var clubInfo = new ClubInfoTile();
            clubInfo.reload(club);

            var calendar = new CalendarTile();
            calendar.setDailySchedule(league.getId());

            var boxScore = new BoxScoreTile();
            boxScore.loadBoxScore(match);

            var header = new MatchHeaderTile();
            header.setMatch(match);

            var scheduleRow = new ScheduleRowTile();
            scheduleRow.setMatch(match);

            var lineup = new LineupTile(true);
            lineup.fillLineup(match.getAwayLineup());
            var lineupRow = new LineupRowTile(lineup, 1, true);

            var playerInfo = new PlayerInfoTile(true);
            playerInfo.reload(player);
            var playerBrief = new PlayerInfoTile(false);
            playerBrief.reload(player);

            var attributes = new PlayerAttributesTile();
            attributes.reload(player.getAttributes());

            holder.getChildren().addAll(clubInfo, calendar, boxScore, header, scheduleRow,
                lineup, lineupRow, playerInfo, playerBrief, attributes, new DefenseTile());
            new Scene(holder).getRoot().applyCss();
            holder.layout();
        });
        WaitForAsyncUtils.checkException();
        assertThat(holder.getChildren(), is(not(empty())));
    }

    @Test
    void kitComponentsConstruct() throws Throwable {
        var holder = new VBox();
        var rating = Ratings.bar("Batting");
        FxToolkit.setupFixture(() -> {
            rating.setValue(85);
            var table = Tables.<String>table();
            table.getColumns().add(Tables.column("Name", (String s) -> s).width(10).build());
            table.getColumns().add(Tables.column("Length", String::length).numeric().build());
            table.getColumns().add(Tables.column("Rating", (String s) -> s.length() * 20).rating().build());
            table.getItems().addAll("Alpha", "Beta");
            Tables.fitRows(table);

            holder.getChildren().addAll(
                Images.rounded(2),
                Cards.titled("Card", Layouts.row(Space.S, Icons.of(Feather.STAR), Layouts.spacer(), Icons.button(Feather.X, "Close"))),
                Ratings.group("Offense", rating, Ratings.bar("Power")),
                table);
            var scene = new Scene(holder);
            Softman.applyTheme(scene);
            scene.getRoot().applyCss();
            holder.layout();
        });
        WaitForAsyncUtils.checkException();
        assertThat(rating.lookup("." + Tokens.RATING_ELITE), is(not(nullValue())));
    }

    @Test
    void darkThemeRendersEveryTab() throws Throwable {
        var tabs = ContentFrame.getInstance();
        try {
            FxToolkit.setupFixture(() -> Softman.setDarkTheme(true));
            assertThat(Softman.isDarkTheme(), is(true));
            for (var screen : Screen.values()) {
                FxToolkit.setupFixture(() -> {
                    tabs.switchTo(screen);
                    tabs.getScene().getRoot().applyCss();
                    tabs.getScene().getRoot().layout();
                });
            }
            WaitForAsyncUtils.checkException();
        } finally {
            FxToolkit.setupFixture(() -> Softman.setDarkTheme(false));
        }
    }

    @Test
    void themePreferenceDefaultsToLightAndPersistsChoice() throws Throwable {
        var prefs = new InMemoryPreferences();
        try {
            Softman.setPreferences(prefs);
            assertThat(Softman.isDarkTheme(), is(false));

            FxToolkit.setupFixture(() -> Softman.setDarkTheme(true));
            Softman.setPreferences(prefs);
            assertThat(Softman.isDarkTheme(), is(true));

            FxToolkit.setupFixture(() -> Softman.setDarkTheme(false));
            Softman.setPreferences(prefs);
            assertThat(Softman.isDarkTheme(), is(false));
            WaitForAsyncUtils.checkException();
        } finally {
            Softman.setPreferences(new InMemoryPreferences());
            FxToolkit.setupFixture(() -> Softman.setDarkTheme(false));
        }
    }

    @Test
    void settingsMenuTogglesThemeLabel() throws Throwable {
        var menuBar = MenuFrame.getInstance().getChildren().stream()
            .filter(MenuBar.class::isInstance)
            .map(MenuBar.class::cast)
            .findFirst()
            .orElseThrow();
        var settingsMenu = menuBar.getMenus().stream()
            .filter(m -> "Settings".equals(m.getText()))
            .findFirst()
            .orElseThrow();
        var themeToggle = settingsMenu.getItems().getFirst();

        try {
            assertThat(Softman.isDarkTheme(), is(false));
            assertThat(themeToggle.getText(), is("Dark Theme"));

            FxToolkit.setupFixture(themeToggle::fire);
            assertThat(Softman.isDarkTheme(), is(true));
            assertThat(themeToggle.getText(), is("Light Theme"));

            FxToolkit.setupFixture(themeToggle::fire);
            assertThat(Softman.isDarkTheme(), is(false));
            assertThat(themeToggle.getText(), is("Dark Theme"));
            WaitForAsyncUtils.checkException();
        } finally {
            if (Softman.isDarkTheme()) {
                FxToolkit.setupFixture(themeToggle::fire);
            }
        }
    }

    @Test
    void teamListenersHandleNullFocus() throws Throwable {
        var team = AssociationManager.getInstance().getUser().getFocusedTeam();
        try {
            FxToolkit.setupFixture(() -> {
                PlayerTab.getInstance().focusedTeamChanged(null);
                LineupTab.getInstance().focusedTeamChanged(null);
            });
            WaitForAsyncUtils.checkException();
        } finally {
            FxToolkit.setupFixture(() -> {
                PlayerTab.getInstance().focusedTeamChanged(team);
                LineupTab.getInstance().focusedTeamChanged(team);
            });
        }
    }

    @Test
    void setUpRefreshesFramesAfterNewGame() throws Throwable {
        var manager = AssociationManager.getInstance();
        var oldClub = manager.getUser().getFocusedClub();

        FxToolkit.setupFixture(() -> {
            ContentFrame.getInstance().switchTo(Screen.TEAM);
            manager.reset();
            AssociationFactory.populateAssociation();
            MainLayout.getInstance().setUp();
        });
        WaitForAsyncUtils.checkException();

        var newClub = manager.getUser().getFocusedClub();
        var newTeam = manager.getUser().getFocusedTeam();
        assertThat(newClub, is(not(oldClub)));

        var combos = FocusFrame.getInstance().getChildren().stream()
            .filter(ComboBox.class::isInstance)
            .map(ComboBox.class::cast)
            .toList();
        assertThat(combos.get(0).getValue(), is(newClub));
        assertThat(combos.get(1).getValue(), is(newTeam));

        var expectedDate = manager.getClock().getCurrentDate().format(FormatUtils.DF);
        var dateDisplayed = ActionFrame.getInstance().getChildren().stream()
            .filter(Label.class::isInstance)
            .map(Label.class::cast)
            .anyMatch(label -> expectedDate.equals(label.getText()));
        assertThat(dateDisplayed, is(true));
        assertThat(ContentFrame.getInstance().getSelectionModel().getSelectedItem().getText(), is(Screen.CLUB.getTitle()));
    }

    private static final class InMemoryPreferences extends AbstractPreferences {

        private final Map<String, String> values = new HashMap<>();

        private InMemoryPreferences() {
            super(null, "");
        }

        @Override protected void putSpi(String key, String value) { values.put(key, value); }
        @Override protected String getSpi(String key) { return values.get(key); }
        @Override protected void removeSpi(String key) { values.remove(key); }
        @Override protected void removeNodeSpi() { values.clear(); }
        @Override protected String[] keysSpi() { return values.keySet().toArray(String[]::new); }
        @Override protected String[] childrenNamesSpi() { return new String[0]; }
        @Override protected AbstractPreferences childSpi(String name) { return new InMemoryPreferences(); }
        @Override protected void syncSpi() {}
        @Override protected void flushSpi() {}
    }
}
