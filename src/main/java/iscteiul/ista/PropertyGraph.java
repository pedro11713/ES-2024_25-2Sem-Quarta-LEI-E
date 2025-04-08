package iscteiul.ista;
import java.util.*;

public class PropertyGraph {
    //Pergunta 3
    private final Map<Property, Set<Property>> adjacencyMap = new HashMap<>();

    public void addProperty(Property property) {
        adjacencyMap.putIfAbsent(property, new HashSet<>());
    }

    public void addEdge(Property p1, Property p2) {
        adjacencyMap.get(p1).add(p2);
        adjacencyMap.get(p2).add(p1);
    }

    public Set<Property> getNeighbors(Property property) {
        return adjacencyMap.getOrDefault(property, Collections.emptySet());
    }

    public Set<Property> getAllProperties() {
        return adjacencyMap.keySet();
    }
}
