package cache;

import utils.Constants;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

public abstract class LFUCache <K> {

    private Map<String, K[]> cache;
    private Map<String, Integer> usageFrequency;
    private Map<Integer, Set<K[]>> order;

    protected LFUCache() {
        cache = new HashMap<>();
        usageFrequency = new HashMap<>();
        order = new HashMap<>();
    }

    public synchronized void add(String criteria, K[] product) {
        if (!cache.containsKey(criteria)) {
            remove();
            cache.putIfAbsent(criteria, product);
            usageFrequency.putIfAbsent(criteria, Constants.INITIAL_VALUE);

            if (!order.containsKey(Constants.INITIAL_VALUE)) {
                order.put(Constants.INITIAL_VALUE, new LinkedHashSet<>());
            }

            order.get(Constants.INITIAL_VALUE).add(product);
        }
    }

    public synchronized void remove() {
        if (cache.size() == Constants.CAPACITY) {
            int leastFrequent = Collections.min(order.keySet());
            K[] toRemove = order.get(leastFrequent).iterator().next();
            order.get(leastFrequent).remove(toRemove);

            if (order.get(leastFrequent).size() == 0) {
                order.remove(leastFrequent);
            }
        }
    }

    public synchronized K[] retrieveInformation(String criteria) {
        if (cache.containsKey(criteria)) {
            int frequency = usageFrequency.get(criteria);
            K[] product = cache.get(criteria);

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
