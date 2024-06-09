package main.helper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Jede Konstante in Deutsch & Englisch angegeben
 */

public enum Kategorie {
    KEINE(new String[] {"Keine", "None"}),
    OBST(new String[] {"Obst", "Fruit"}),
    GEMUESE(new String[] {"Gemüse", "Vegetable"}),
    FLEISCH(new String[] {"Fleisch", "Meat"}),
    SNACKS(new String[] {"Snacks", "Snacks"}),
    GETRAENKE(new String[] {"Getränke", "Drinks"});


    /**
     * Speichern von Namen der Kategorie
     */
    private final String[] name;

    Kategorie(String[] name) { // initialisieren jeder Konstante mit entsprechendem String-Array
        this.name = name;
    }

    /**
     * Methode durchläuft Konstanten der Kategorien und deren Namen, um passende Kategorie zu finden -> wenn nicht: "Kategorie.KEINE"
     * @param name
     * @return
     */
    public static Kategorie get(String name) {
        for(Kategorie kategorie : values()) {
            for(String kategorieString : kategorie.name) {
                if (kategorieString.equals(name)) return kategorie;
            }
        }
        return Kategorie.KEINE;
    }

    /**
     * Methode gibt Kategorie an angegebenen Index zurück wenn gültig, wenn ungültig -> "Kategorie.KEINE"
     * @param index
     * @return
     */
    public static Kategorie get(int index) {
        if (index < Kategorie.values().length) return Kategorie.values()[index];
        return KEINE;
    }

    /**
     * Methode gibt Array von Strings zurück, enthält die Namen aller Kategorien in angegebener Sprache -> 0=Deutsch, 1=Englisch
     * @param sprache
     * @return
     */
    public static String[] getAlsStringArray(int sprache) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (Kategorie kategorie : Kategorie.values()) {
            stringArrayList.add(kategorie.name[sprache]);
        }
        return stringArrayList.toArray(new String[0]);
    }

    public String toString(int sprache) {
        return name[sprache];
    }

    @Override
    public String toString() {
        return Arrays.toString(name);
    }
}
