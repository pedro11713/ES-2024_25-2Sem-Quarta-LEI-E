package iscteiul.ista;

import java.util.*;

public class OwnerGraph {
    private final Map<String, Set<String>> adjacencyMap = new HashMap<>();

    public void addOwner(String owner) {
        adjacencyMap.putIfAbsent(owner, new HashSet<>());
    }

    public void addEdge(String owner1, String owner2) {
        if (!owner1.equals(owner2)) {
            adjacencyMap.get(owner1).add(owner2);
            adjacencyMap.get(owner2).add(owner1);
        }
    }

    public Set<String> getNeighbors(String owner) {
        return adjacencyMap.getOrDefault(owner, Collections.emptySet());
    }

    public Set<String> getAllOwners() {
        return adjacencyMap.keySet();
    }

    public void printGraph() {
        for (String owner : adjacencyMap.keySet()) {
            System.out.println(owner + " -> " + adjacencyMap.get(owner));
        }
    }
}
