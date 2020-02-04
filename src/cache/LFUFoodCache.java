package cache;

import model.Food;
import utils.Constants;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LFUFoodCache {
    private static LFUFoodCache instance;
    private Map<String, Food> cache;
    private Map<String, Integer> usageFrequency;
    private Map<Integer, Set<Food>> order;

    private LFUFoodCache() {
        cache = new HashMap<>();
        usageFrequency = new HashMap<>();
        order = new HashMap<>();
    }

    public static LFUFoodCache getInstance() {
        //double checked locking principle is used to avoid overhead
        if (instance == null) {
            synchronized (LFUFoodCache.class) {
                if (instance == null) {
                    instance = new LFUFoodCache();
                }
            }
        }
        return instance;
    }

    public synchronized void add(Food food) {
        if (!cache.containsKey(food.getFdcId())) {
            remove();
            cache.putIfAbsent(food.getFdcId(), food);
            usageFrequency.putIfAbsent(food.getFdcId(), Constants.INITIAL_VALUE);

            if (!order.containsKey(Constants.INITIAL_VALUE)) {
                order.put(Constants.INITIAL_VALUE, new LinkedHashSet<>());
            }

            order.get(Constants.INITIAL_VALUE).add(food);
        }
    }

    public synchronized void remove() {
        if (cache.size() == Constants.CAPACITY) {
            int leastFrequent = Collections.min(order.keySet());
            Food toRemove = order.get(leastFrequent).iterator().next();
            order.get(leastFrequent).remove(toRemove);

            if(order.get(leastFrequent).size() == 0) {
                order.remove(leastFrequent);
            }
        }
    }

    public synchronized Food retrieveInformation(String fdcId) {
        if (cache.containsKey(fdcId)) {
            int frequency = usageFrequency.get(fdcId);
            Food food = cache.get(fdcId);

            usageFrequency.put(fdcId, frequency + 1);
            order.get(frequency).remove(food);

            if(order.get(frequency).size() == 0) {
                order.remove(frequency);
            }
            if (!order.containsKey(frequency + 1)) {
                order.put(frequency + 1, new LinkedHashSet<>());
            }

            order.get(frequency + 1).add(food);

            return cache.get(fdcId);
        }
        return null;
    }

}
