package elrh.softman.gui.frame;

import atlantafx.base.theme.Styles;
import elrh.softman.gui.kit.Icons;
import elrh.softman.gui.kit.Layouts;
import elrh.softman.gui.kit.Layouts.Space;
import elrh.softman.gui.kit.Tokens;
import elrh.softman.gui.tab.ClubTab;
import elrh.softman.gui.tab.LineupTab;
import elrh.softman.gui.tab.MatchTab;
import elrh.softman.gui.tab.PlayerTab;
import elrh.softman.gui.tab.StandingsTab;
import elrh.softman.gui.tab.TeamTab;
import elrh.softman.gui.tab.TrainingTab;
import elrh.softman.logic.AssociationManager;
import elrh.softman.utils.Utils;
import java.util.EnumMap;
import java.util.Map;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.feather.Feather;

public class ContentFrame extends TabPane {

    @Getter
    @RequiredArgsConstructor
    public enum Screen {
        CLUB("Club", Feather.HOME),
        MATCH("Match", Feather.PLAY_CIRCLE),
        TEAM("Team", Feather.USERS),
        PLAYER("Player", Feather.USER),
        LINEUP("Lineup", Feather.LIST),
        TRAINING("Training", Feather.ACTIVITY),
        STANDINGS("Standings", Feather.BAR_CHART_2),
        STATS("Stats", Feather.PIE_CHART),
        MARKET("Market", Feather.SHOPPING_CART);

        private final String title;
        private final Ikon icon;
    }

    private static ContentFrame INSTANCE;
    
    public static ContentFrame getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContentFrame();
        }
        return INSTANCE;
    }

    private final Map<Screen, Tab> tabs = new EnumMap<>(Screen.class);
    
    private ContentFrame() {
        getStyleClass().add(Styles.TABS_FLOATING);
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        addTab(Screen.CLUB, ClubTab.getInstance());
        addTab(Screen.MATCH, MatchTab.getInstance());
        addTab(Screen.TEAM, TeamTab.getInstance());
        addTab(Screen.PLAYER, PlayerTab.getInstance());
        addTab(Screen.LINEUP, LineupTab.getInstance());
        addTab(Screen.TRAINING, TrainingTab.getInstance());
        addTab(Screen.STANDINGS, StandingsTab.getInstance());
        addTab(Screen.STATS, placeholder(Screen.STATS, "Statistics center"));
        addTab(Screen.MARKET, placeholder(Screen.MARKET, "Buy and sell players"));
    }

    public void setUp() {
        ClubTab.getInstance().setDailySchedule();

        var testMatches = AssociationManager.getInstance().getDailyMatchesForUser();
        var match = Utils.getFirstItem(testMatches);
        if (match != null) {
            MatchTab.getInstance().setMatch(match);
        }
    }

    public void switchTo(Screen screen) {
        getSelectionModel().select(tabs.get(screen));
    }

    private void addTab(Screen screen, Node content) {
        var tab = new Tab(screen.getTitle(), content);
        tab.setGraphic(Icons.of(screen.getIcon()));
        tabs.put(screen, tab);
        getTabs().add(tab);
    }

    private static Node placeholder(Screen screen, String text) {
        var icon = Icons.of(screen.getIcon());
        icon.setIconSize((int) Layouts.em(3));
        var label = new Label(text + " - coming soon");
        label.getStyleClass().add(Tokens.CAPTION);
        var box = Layouts.column(Space.M, icon, label);
        box.getStyleClass().add(Tokens.PLACEHOLDER);
        box.setAlignment(Pos.CENTER);
        return box;
    }
}
