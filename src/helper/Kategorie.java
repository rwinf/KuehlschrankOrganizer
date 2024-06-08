package helper;

import java.util.ArrayList;
import java.util.Objects;

public enum Kategorie {
    KEINE("Keine"),
    OBST("Obst"),
    GEMUESE("Gemüse"),
    FLEISCH("Fleisch"),
    SNACKS("Snacks"),
    GETRAENKE("Getränke");


    private final String name;

    Kategorie(String name) {
        this.name = name;
    }

    public static Kategorie get(String name) {
        int i = 0;
        while (!Objects.equals(Kategorie.values()[i].name, name)) {
            i++;
        }
        return Kategorie.values()[i];
    }

    public static Kategorie get(int index) {
        if (index < Kategorie.values().length) return Kategorie.values()[index];
        return KEINE;
    }

    public static String[] getAsStringArray() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (Kategorie kategorie : Kategorie.values()) {
            stringArrayList.add(kategorie.name);
        }
        return stringArrayList.toArray(new String[0]);
    }

    @Override
    public String toString() {
        return name;
    }
}
