package utility;

import java.time.format.DateTimeFormatter;

public class Option {
    public static final String PATH = "lebensmittelInhalte.json";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public String toString() {
        return "Option{" +
                "DATE_TIME_FORMATTER=" + DATE_TIME_FORMATTER +
                ", PATH='" + PATH + '\'' +
                '}';
    }
}
