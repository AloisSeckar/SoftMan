package elrh.softman.logic.core.data;

import java.util.UUID;
import lombok.*;

@Data @EqualsAndHashCode(callSuper=true) @NoArgsConstructor
public class ClubInfo extends AbstractEntity {

    private UUID clubId = UUID.randomUUID();

    private String name;

    private String shortName;

    private String logo;

    private String city;

    private String stadium;

    private String color; // web hex, e.g. "#ADD8E6"

    private int registered;

    private long money;

    @Override
    public UUID getId() {
        return getClubId();
    }

}
