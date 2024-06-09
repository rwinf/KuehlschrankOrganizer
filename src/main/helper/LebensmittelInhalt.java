package main.helper;

import java.util.*;

public class LebensmittelInhalt extends ArrayList<Lebensmittel> {
    public LebensmittelInhalt() {
        super();
    }

    /**
     * Methode fügt neues 'Lebensmittel' zur Liste hinzu und sortiert sie
     * @param kategorie Kategorie
     * @param name Name
     * @param haltbarkeitsdatum Haltbarkeitsdatum
     */
    public void add(Kategorie kategorie, String name, String haltbarkeitsdatum) {
        super.add(new Lebensmittel(kategorie, name, haltbarkeitsdatum));
        sort(new LebensmittelInhaltComparator()); // sortiert mit Hilfe dem Comparator: 'LebensmittelInhaltComparator'
    }

    /**
     * filtert nach gegebener Kategorie und gibt neue Liste zurück (enthält nur gewünschte Kategorie)
     * @param kategorie nach welcher Kategorie gefiltert wird
     * @return LebensmittelInhalt (ArrayList) mit Lebensmitteln der gewünschten Kategorie
     */
    public LebensmittelInhalt getNachKategorie(Kategorie kategorie) {
        LebensmittelInhalt lebensmittelInhalt = new LebensmittelInhalt();
        for (Lebensmittel lebensmittel : this) {
            if(kategorie != lebensmittel.kategorie) continue;
            lebensmittelInhalt.add(lebensmittel);
        }
        return lebensmittelInhalt;
    }

    /**
     * Comparator zum Sortieren nach Haltbarkeitsdatum
     */
    private static class LebensmittelInhaltComparator implements Comparator<Lebensmittel> {
        @Override
        public int compare(Lebensmittel lebensmittel1, Lebensmittel lebensmittel2) {
            return lebensmittel1.haltbarkeitsdatum.compareTo(lebensmittel2.haltbarkeitsdatum);
        }
    }

    @Override
    public String toString() {
        return "LebensmittelInhalt{}";
    }
}
