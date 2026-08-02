package elrh.softman.logic.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ActivityType {

    TRAINING(25),
    MATCH(20);

    @Getter
    private final int baseFatigue;

}
