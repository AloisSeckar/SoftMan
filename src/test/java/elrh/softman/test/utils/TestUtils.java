package elrh.softman.test.utils;

import elrh.softman.logic.core.Club;

public class TestUtils {

    public static final String ELEMENT_NAME = "Test";

    public static Club getTestClub() {
        return new Club(ELEMENT_NAME, ELEMENT_NAME.substring(0, 3), ELEMENT_NAME, ELEMENT_NAME);
    }

}
