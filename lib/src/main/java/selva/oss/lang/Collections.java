package selva.oss.lang;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Collections {

    private static <T> Map<T, Long> mapFrequencyOfElements(Collection<T> collection) {
        Map<T, Long> frequencyMap = new HashMap<>();
        for (T d : collection) {
            if (frequencyMap.containsKey(d)) {
                frequencyMap.put(d, frequencyMap.get(d) + 1L);
            }
            else {
                frequencyMap.put(d, 1L);
            }
        }
        return frequencyMap;
    }

    private static <T> boolean doesElementFrequencyMatch(Collection<T> collection1, Collection<T> collection2) {
        Map<T, Long> frequencyMap1 = mapFrequencyOfElements(collection1);
        Map<T, Long> frequencyMap2 = mapFrequencyOfElements(collection2);

        if (frequencyMap1.keySet().size() != frequencyMap2.keySet().size()) {
            return false;
        }

        for (T d : frequencyMap1.keySet()) {
            if (!frequencyMap2.keySet().contains(d)) {
                return false;
            }
            if (!frequencyMap1.get(d).equals(frequencyMap2.get(d))) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean equals(Collection<T> collection1, Collection<T> collection2) {
        if (collection1.size() != collection2.size()) {
            return false;
        }
        return doesElementFrequencyMatch(collection1, collection2);
    }

    public static <T> T getCircular(List<T> collection, int index) {
        return collection.get(index % collection.size());
    }

    public static <T> Stream<T> streamIterator(Iterator<T> iterator) {
        Iterable<T> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

}
