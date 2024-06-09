package helper;

import java.util.ArrayList;
import java.util.Arrays;

public enum Kategorie {
    KEINE(new String[] {"Keine", "None"}),
    OBST(new String[] {"Obst", "Fruit"}),
    GEMUESE(new String[] {"Gemüse", "Vegetable"}),
    FLEISCH(new String[] {"Fleisch", "Meat"}),
    SNACKS(new String[] {"Snacks", "Snacks"}),
    GETRAENKE(new String[] {"Getränke", "Drinks"});


    private final String[] name;

    Kategorie(String[] name) {
        this.name = name;
    }

    public static Kategorie get(String name) {
        int i = 0;
        for(Kategorie kategorie : values()) {
            for(String kategorieString : kategorie.name) {
                if (kategorieString.equals(name)) return values()[i];
            }
            i++;
        }
        return Kategorie.values()[i];
    }

    public static Kategorie get(int index) {
        if (index < Kategorie.values().length) return Kategorie.values()[index];
        return KEINE;
    }

    public static String[] getAsStringArray(int sprache) {
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
