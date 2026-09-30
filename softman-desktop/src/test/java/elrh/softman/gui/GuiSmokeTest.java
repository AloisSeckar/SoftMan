package elrh.softman.gui;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import elrh.softman.Softman;
import elrh.softman.gui.frame.ContentFrame;
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
import elrh.softman.logic.AssociationManager;
import elrh.softman.utils.Constants;
import elrh.softman.utils.factory.AssociationFactory;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

@ExtendWith(ApplicationExtension.class)
class GuiSmokeTest {

    @BeforeAll
    static void setUpWorld() {
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
        assertThat(ContentFrame.getInstance().getTabs().size(), is(9));
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

            var scheduleRow = new ScheduleRowTile(true);
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
}
