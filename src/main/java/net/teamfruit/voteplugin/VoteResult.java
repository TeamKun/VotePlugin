package net.teamfruit.voteplugin;

import java.util.Map;
import java.util.UUID;

public class VoteResult {
    private final String[] top;
    private final Map<String, Integer> result;
    private final Map<String, String> destinations;
    private final Map<String, UUID> uniqueIdMap;

    public VoteResult(Map<String, Integer> result, Map<String, String> destinations, Map<String, UUID> uniqueIdMap) {
        this.uniqueIdMap = uniqueIdMap;
        int max = result.values()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
        this.top = result.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == max)
                .map(Map.Entry::getKey)
                .toArray(String[]::new);
        this.result = result;
        this.destinations = destinations;
    }

    public String[] getTop() {
        return top;
    }

    public Map<String, Integer> getResult() {
        return result;
    }

    public Map<String, String> getDestinations() {
        return destinations;
    }

    public Map<String, UUID> getUniqueIdMap() {
        return uniqueIdMap;
    }
}
