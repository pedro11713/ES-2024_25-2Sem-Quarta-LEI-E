package iscteiul.ista;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrocaGraphVisualizer {

    public static void visualize(List<String> trocasDuplas) {
        Graph graph = new SingleGraph("Trocas Duplas");

        graph.setAttribute("ui.stylesheet",
            "node {" +
            "   fill-color: #3498db;" +
            "   size: 20px;" +
            "   text-size: 16px;" +
            "   text-alignment: above;" +
            "}" +
            "edge {" +
            "   text-size: 14px;" +
            "   fill-color: #2ecc71;" +
            "   text-alignment: above;" +
            "}");

        Set<String> processed = new HashSet<>();

        for (String troca : trocasDuplas) {
            // Extraer IDs de los propietarios con regex
            Matcher matcher = Pattern.compile("Troca dupla sugerida entre (\\d+) e (\\d+):").matcher(troca);
            if (matcher.find()) {
                String owner1 = matcher.group(1);
                String owner2 = matcher.group(2);
                String edgeId = owner1 + "-" + owner2;

                if (processed.contains(edgeId)) continue;
                processed.add(edgeId);

                // Extraer ganhos para los dos propietarios
                double ganho1 = extractGanho(troca, owner1);
                double ganho2 = extractGanho(troca, owner2);
                double ganhoTotal = ganho1 + ganho2;

                // Agregar nodos solo si no existen
                if (graph.getNode(owner1) == null) {
                    graph.addNode(owner1).setAttribute("ui.label", owner1);
                }
                if (graph.getNode(owner2) == null) {
                    graph.addNode(owner2).setAttribute("ui.label", owner2);
                }

                // Agregar arista
                Edge e = graph.addEdge(edgeId, owner1, owner2, false);
                

                // Colorear según ganancia total
                if (ganhoTotal > 100) {
                    e.setAttribute("ui.style", "fill-color: green;");
                } else if (ganhoTotal > 0) {
                    e.setAttribute("ui.style", "fill-color: orange;");
                } else {
                    e.setAttribute("ui.style", "fill-color: red;");
                }
            }
        }

        graph.display();
    }

    private static double extractGanho(String troca, String owner) {
        Pattern p = Pattern.compile(owner + ".*?ganho líquido: ([\\d\\.\\-]+)");
        Matcher m = p.matcher(troca);
        if (m.find()) {
            return Double.parseDouble(m.group(1));
        }
        return 0.0;
    }
}
