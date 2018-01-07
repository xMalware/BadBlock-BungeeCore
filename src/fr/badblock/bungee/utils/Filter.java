package fr.badblock.bungee.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Filter<T> {
    boolean keep(T object);

    default List<T> filterList(List<T> list) {
        List<T> filtered = new ArrayList<>();
        list.forEach(t -> {
            if (keep(t)) filtered.add(t);
        });
        return filtered;
    }

    default T[] filterArray(T[] array) {
        return (T[]) filterList(Arrays.asList(array)).toArray();
    }
}
