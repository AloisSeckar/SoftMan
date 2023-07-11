package elrh.softman.logic.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ActivityType {

    TRAINING(20),
    MATCH(15);

    @Getter
    private final int baseFatigue;

}
