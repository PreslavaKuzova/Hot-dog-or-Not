package data.cache;

import utils.constants.CacheConstants;

import java.util.*;

class LFUCache <K> {

    private Map<String, List<K>> cache;
    private Map<String, Integer> usageFrequency;
    private Map<Integer, Set<List<K>>> order;

    protected LFUCache() {
        cache = new HashMap<>();
        usageFrequency = new HashMap<>();
        order = new HashMap<>();
    }

    protected synchronized void add(String criteria, List<K> product) {
        if (!cache.containsKey(criteria)) {
            remove();
            cache.putIfAbsent(criteria, product);
            usageFrequency.putIfAbsent(criteria, CacheConstants.INITIAL_VALUE);

            if (!order.containsKey(CacheConstants.INITIAL_VALUE)) {
                order.put(CacheConstants.INITIAL_VALUE, new LinkedHashSet<>());
            }

            order.get(CacheConstants.INITIAL_VALUE).add(product);
        }
    }

    private synchronized void remove() {
        if (cache.size() == CacheConstants.CAPACITY) {
            int leastFrequent = Collections.min(order.keySet());
            List<K> toRemove = order.get(leastFrequent).iterator().next();
            order.get(leastFrequent).remove(toRemove);

            if (order.get(leastFrequent).size() == 0) {
                order.remove(leastFrequent);
            }
        }
    }

    protected synchronized List<K> retrieveInformation(String criteria) {
        if (cache.containsKey(criteria)) {
            int frequency = usageFrequency.get(criteria);
            List<K> product = cache.get(criteria);

            usageFrequency.put(criteria, frequency + 1);
            order.get(frequency).remove(product);

            if (order.get(frequency).size() == 0) {
                order.remove(frequency);
            }
            if (!order.containsKey(frequency + 1)) {
                order.put(frequency + 1, new LinkedHashSet<>());
            }

            order.get(frequency + 1).add(product);

            return cache.get(criteria);
        }
        return null;
    }

}
