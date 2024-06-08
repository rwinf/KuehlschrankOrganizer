package helper;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class LebensmittelInhalt extends ArrayList<Lebensmittel> {
    public LebensmittelInhalt() {
        super();
    }

    public void add(Kategorie kategorie, String name, String haltbarkeitsdatum) {
        super.add(new Lebensmittel(kategorie, name, haltbarkeitsdatum));
        sort(new LebensmittelInhaltComparator());
    }

    public void remove(Kategorie kategorie, String name, String haltbarkeitsdatum) {
        super.remove(new Lebensmittel(kategorie, name, haltbarkeitsdatum));
    }

    public Map<Kategorie, LebensmittelInhalt> getNachKategorie() {
        Map<Kategorie, LebensmittelInhalt> kategorieLebensmittelInhaltEnumMap = new EnumMap<>(Kategorie.class);
        for (Kategorie kategorie : Kategorie.values()) {
            kategorieLebensmittelInhaltEnumMap.put(kategorie, new LebensmittelInhalt());
        }
        for (Lebensmittel lebensmittel : this) {
            kategorieLebensmittelInhaltEnumMap.get(lebensmittel.kategorie).add(lebensmittel);
        }
        return kategorieLebensmittelInhaltEnumMap;
    }
}
