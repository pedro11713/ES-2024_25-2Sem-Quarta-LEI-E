package iscteiul.ista;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import java.util.Set;
import java.util.HashSet;

public class PropertyGraphVisualizer {

    public static void visualize(PropertyGraph propertyGraph) {
        Graph graph = new SingleGraph("Propriedades");

        // Adiciona os nós
        for (Property p : propertyGraph.getAllProperties()) {
            graph.addNode(p.getOBJECTID() + "").setAttribute("ui.label", p.getOBJECTID() + ""); // Usa ID como label
        }

        // Adiciona as arestas (sem duplicar)
        Set<String> addedEdges = new HashSet<>();
        for (Property p : propertyGraph.getAllProperties()) {
            for (Property neighbor : propertyGraph.getNeighbors(p)) {
                String edgeId = Math.min( p.getOBJECTID(), neighbor.getOBJECTID()) + "-" + Math.max( p.getOBJECTID(), neighbor.getOBJECTID());
                if (!addedEdges.contains(edgeId)) {
                    graph.addEdge(edgeId,  p.getOBJECTID() + "", neighbor.getOBJECTID() + "");
                    addedEdges.add(edgeId);
                }
            }
        }

        graph.display();
    }
}
