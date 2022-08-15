package elrh.softman.logic;

import elrh.softman.db.orm.PlayerInfo;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Club {

    @Getter
    private final int clubID;

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String city;
    @Getter
    @Setter
    private String stadium;

    @Getter
    private List<PlayerInfo> players = new ArrayList<>();
    @Getter
    private List<Team> teams = new ArrayList<>();


}
