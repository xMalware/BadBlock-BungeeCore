package fr.badblock.bungee.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Util for filter arrays and list
 *
 * @param <T> the type of object to be filtered
 * @author RedSpri
 */
public interface Filter<T> {
    /**
     * Define if a object is filtered or not
     *
     * @param object the object on which the judgment will be carried :)
     * @return true if object is keeped, false if he is removed
     */
    boolean keep(T object);

    /**
     * Filter a list
     *
     * @param list the list to be filtered
     * @return the filtered list
     */
    default List<T> filterList(List<T> list) {
        List<T> filtered = new ArrayList<>();
        list.forEach(t -> {
            if (keep(t)) filtered.add(t);
        });
        return filtered;
    }

    /**
     * Filter an array
     *
     * @param array the array to be filtered
     * @return the filtered array
     */
    @SuppressWarnings("unchecked")
	default T[] filterArray(T[] array) {
        return (T[]) filterList(Arrays.asList(array)).toArray();
    }
}
