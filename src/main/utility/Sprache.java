package main.utility;

/**
 * Sprach-Modul wo alle Strings in unterschiedlichen Sprachen gespeichert werden
 */
public class Sprache {
    public static final int DEUTSCH = 0;
    public static final int ENGLISCH = 1;
    private static final int MAX = 2; //Anzahl an Sprachen
    private static final String[] titel = {"Kühlschrank Organizer", "Fridge Organizer"};
    private static final String[] spracheName = {"Deutsch", "English"};
    private static final String[] eingabe = {"Eingabe", "Input"};
    private static final String[] haltbarkeitsdatum = {"Haltbarkeitsdatum", "Expiry Date"};
    private static final String[] hinzufuegen = {"Hinzufügen", "Add"};
    private static final String[] entfernen = {"Entfernen", "Remove"};
    private static final String[] ungueltiges = {"Ungülitges", "Invalid"};

    public static String getTitel(int sprache) {
        if (sprache >= MAX) return titel[0];
        return titel[sprache];
    }

    public static String getSpracheName(int sprache) {
        if (sprache >= MAX) return spracheName[0];
        if (++sprache >= MAX) return spracheName[0];
        return spracheName[sprache];
    }

    public static String getEingabe(int sprache) {
        if (sprache >= MAX) return eingabe[0];
        return eingabe[sprache];
    }

    public static String getHaltbarkeitsdatum(int sprache) {
        if (sprache >= MAX) return haltbarkeitsdatum[0];
        return haltbarkeitsdatum[sprache];
    }

    public static String getHinzufuegen(int sprache) {
        if (sprache >= MAX) return hinzufuegen[0];
        return hinzufuegen[sprache];
    }

    public static String getEntfernen(int sprache) {
        if (sprache >= MAX) return entfernen[0];
        return entfernen[sprache];
    }

    public static String getUngueltiges(int sprache) {
        if (sprache >= MAX) return ungueltiges[0];
        return ungueltiges[sprache];
    }

    public static int next(int language) {
        if (++language >= MAX) language = 0;
        return language;
    }
}
