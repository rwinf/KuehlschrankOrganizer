package utility;

public class Language {
    public static int MAX = 2;
    private static final String[] title = {"Fridge Organizer", "Kühlschrank Organizer"};

    public static String getTitle(int language) {
        return title[language];
    }

    public int next(int language) {
        if(++language >= MAX) language = 0;
        return language;
    }
}
