package iscteiul.ista;
import java.util.*;

public class PropertyGraph {
    //Pergunta 3
    private final Map<Property, Set<Property>> adjacencyMap = new HashMap<>();

    /**
     * Adiciona uma nova propriedade ao grafo.
     * Caso a propriedade já exista, não será adicionada novamente.
     *
     * @param property A propriedade a adicionar ao grafo.
     */
    public void addProperty(Property property) {
        adjacencyMap.putIfAbsent(property, new HashSet<>());
    }

    /**
     * Cria uma ligação (aresta) entre duas propriedades, indicando que são vizinhas.
     * A ligação é bidirecional.
     *
     * @param p1 A primeira propriedade.
     * @param p2 A segunda propriedade.
     */
    public void addEdge(Property p1, Property p2) {
        adjacencyMap.get(p1).add(p2);
        adjacencyMap.get(p2).add(p1);
    }

    /**
     * Devolve o conjunto de propriedades vizinhas (adjacentes) de uma dada propriedade.
     *
     * @param property A propriedade da qual se pretendem obter os vizinhos.
     * @return Um conjunto com as propriedades vizinhas, ou um conjunto vazio se não existirem.
     */
    public Set<Property> getNeighbors(Property property) {
        return adjacencyMap.getOrDefault(property, Collections.emptySet());
    }


    /**
     * Devolve todas as propriedades presentes no grafo.
     *
     * @return Um conjunto com todas as propriedades armazenadas.
     */
    public Set<Property> getAllProperties() {
        return adjacencyMap.keySet();
    }
}
