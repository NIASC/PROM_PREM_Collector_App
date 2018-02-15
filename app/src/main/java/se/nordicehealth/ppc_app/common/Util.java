package se.nordicehealth.ppc_app.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Util {
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, final boolean reverse) {
        List<Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Entry<K, V>>() {
            @Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return (reverse ? -1 : 1) * o1.getValue().compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
