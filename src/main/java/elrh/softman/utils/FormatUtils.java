package elrh.softman.utils;

import javafx.geometry.Insets;

import java.time.format.DateTimeFormatter;

public class FormatUtils {

    private FormatUtils() {
    }

    public static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public static final Insets TOP_5 = new Insets(5,0,0,0);
    public static final Insets LEFT_5 = new Insets(0,0,0,5);
    public static final Insets TOPLEFT_5 = new Insets(5,0,0,5);

    public static final Insets PADDING_5 = new Insets(5);
    public static final Insets PADDING_10 = new Insets(10);
}
