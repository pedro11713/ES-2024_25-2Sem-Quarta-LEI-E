package iscteiul.ista;

import java.util.*;

/**
 * Representa um grafo de proprietários, onde cada proprietário pode estar ligado a outros,
 * indicando relações como, por exemplo, vizinhança ou co-propriedade.
 * As ligações são bidirecionais.
 */
public class OwnerGraph {
    private final Map<String, Set<String>> adjacencyMap = new HashMap<>();

    /**
     * Adiciona um novo proprietário ao grafo.
     * Caso o proprietário já exista, não é feita qualquer alteração.
     *
     * @param owner O nome do proprietário a adicionar.
     */
    public void addOwner(String owner) {
        adjacencyMap.putIfAbsent(owner, new HashSet<>());
    }

    /**
     * Cria uma ligação (aresta) entre dois proprietários, indicando que estão relacionados.
     * A ligação é bidirecional.
     * Não são criadas ligações a si próprio.
     *
     * @param owner1 O primeiro proprietário.
     * @param owner2 O segundo proprietário.
     */
    public void addEdge(String owner1, String owner2) {
        if (!owner1.equals(owner2)) {
            adjacencyMap.get(owner1).add(owner2);
            adjacencyMap.get(owner2).add(owner1);
        }
    }

    /**
     * Obtém o conjunto de proprietários relacionados (vizinhos) com um determinado proprietário.
     *
     * @param owner O proprietário cujos vizinhos se pretendem obter.
     * @return Um conjunto com os nomes dos vizinhos. Se o proprietário não existir, devolve um conjunto vazio.
     */
    public Set<String> getNeighbors(String owner) {
        return adjacencyMap.getOrDefault(owner, Collections.emptySet());
    }

    /**
     * Obtém o conjunto de todos os proprietários presentes no grafo.
     *
     * @return Um conjunto com os nomes de todos os proprietários.
     */
    public Set<String> getAllOwners() {
        return adjacencyMap.keySet();
    }

    /**
     * Imprime no ecrã a representação textual do grafo,
     * mostrando cada proprietário e os seus vizinhos associados.
     */
    public void printGraph() {
        for (String owner : adjacencyMap.keySet()) {
            System.out.println(owner + " -> " + adjacencyMap.get(owner));
        }
    }
}
