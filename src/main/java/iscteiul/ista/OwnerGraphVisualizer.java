package iscteiul.ista;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import java.util.Set;
import java.util.HashSet;

public class OwnerGraphVisualizer {

    public static void visualize(OwnerGraph ownerGraph) {
        Graph graph = new SingleGraph("Proprietários");

        // Adiciona os nós (proprietários)
        for (String owner : ownerGraph.getAllOwners()) {
            graph.addNode(owner).setAttribute("ui.label", owner); // label = nome do proprietário
        }

        // Adiciona as arestas (sem duplicar)
        Set<String> addedEdges = new HashSet<>();
        for (String owner : ownerGraph.getAllOwners()) {
            for (String neighbor : ownerGraph.getNeighbors(owner)) {
                // Evita arestas duplicadas
                String edgeId = owner.compareTo(neighbor) < 0
                        ? owner + "-" + neighbor
                        : neighbor + "-" + owner;

                if (!addedEdges.contains(edgeId)) {
                    graph.addEdge(edgeId, owner, neighbor);
                    addedEdges.add(edgeId);
                }
            }
        }

        // Estilo simples e igual ao das propriedades
        graph.setAttribute("ui.stylesheet",
                "node { fill-color: lightblue; size: 15px; text-size: 12; text-alignment: under; }" +
                        "edge { fill-color: gray; }");
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");

        graph.display();
    }
}
