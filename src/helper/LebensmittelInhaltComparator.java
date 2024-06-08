package helper;

import java.util.Comparator;

public class LebensmittelInhaltComparator implements Comparator<Lebensmittel> {
    @Override
    public int compare(Lebensmittel lebensmittel1, Lebensmittel lebensmittel2) {
        return lebensmittel1.haltbarkeitsdatum.compareTo(lebensmittel2.haltbarkeitsdatum);
    }
}
