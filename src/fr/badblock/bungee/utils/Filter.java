package fr.badblock.bungee.utils;

import java.util.*;

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
     * Filter a collection
     *
     * @param filter     the filter
     * @param collection the collection to be filtered
     * @return the filtered collection
     */
    static <E> List<E> filterCollectionStatic(Filter<E> filter, Collection<E> collection) {
        return filter.filterCollection(collection);
    }

    /**
     * Filter a list
     *
     * @param filter the filter
     * @param list   the list to be filtered
     * @return the filtered list
     */
    static <E> List<E> filterListStatic(Filter<E> filter, List<E> list) {
        return filter.filterList(list);
    }

    /**
     * Filter a set
     *
     * @param filter the filter
     * @param set    the set to be filtered
     * @return the filtered set
     */
    static <E> List<E> filterSetStatic(Filter<E> filter, Set<E> set) {
        return filter.filterSet(set);
    }

    /**
     * Filter an array
     *
     * @param filter the filter
     * @param array  the array to be filtered
     * @return the filtered array
     */
    @SuppressWarnings("unchecked")
    static <E> E[] filterArrayStatic(Filter<E> filter, E[] array) {
        return filter.filterArray(array);
    }

    /**
     * Filter a collection
     *
     * @param collection the collection to be filtered
     * @return the filtered collection
     */
    default List<T> filterCollection(Collection<T> collection) {
        List<T> filtered = new ArrayList<>();
        collection.forEach(t -> {
            if (keep(t)) filtered.add(t);
        });
        return filtered;
    }

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

    /**
     * Filter a set
     *
     * @param set the set to be filtered
     * @return the filtered set
     */
    default List<T> filterSet(Set<T> set) {
        List<T> filtered = new ArrayList<>();
        set.forEach(t -> {
            if (keep(t)) filtered.add(t);
        });
        return filtered;
    }
}
