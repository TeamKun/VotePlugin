package net.teamfruit.voteplugin;

import java.util.Map;
import java.util.UUID;

public class VoteResult {
    private final UUID[] top;
    private final Map<UUID, Integer> result;
    private final Map<String, String> destinations;

    public VoteResult(Map<UUID, Integer> result, Map<String, String> destinations) {
        int max = result.values()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
        this.top = result.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == max)
                .map(Map.Entry::getKey)
                .toArray(UUID[]::new);
        this.result = result;
        this.destinations = destinations;
    }

    public UUID[] getTop() {
        return top;
    }

    public Map<UUID, Integer> getResult() {
        return result;
    }

    public Map<String, String> getDestinations() {
        return destinations;
    }
}
