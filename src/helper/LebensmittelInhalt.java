package helper;

import java.util.*;

public class LebensmittelInhalt extends ArrayList<Lebensmittel> {
    public LebensmittelInhalt() {
        super();
    }

    public void add(Kategorie kategorie, String name, String haltbarkeitsdatum) {
        super.add(new Lebensmittel(kategorie, name, haltbarkeitsdatum));
        sort(new LebensmittelInhaltComparator());
    }

    public LebensmittelInhalt getNachKategorie(Kategorie kategorie) {
        LebensmittelInhalt lebensmittelInhalt = new LebensmittelInhalt();
        for (Lebensmittel lebensmittel : this) {
            if(kategorie != lebensmittel.kategorie) continue;
            lebensmittelInhalt.add(lebensmittel);
        }
        return lebensmittelInhalt;
    }

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
