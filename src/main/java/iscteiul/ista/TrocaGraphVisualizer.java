package iscteiul.ista;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Classe responsável por visualizar graficamente as trocas duplas sugeridas entre proprietários.
 * Cada nó representa um proprietário e cada aresta representa uma troca entre dois.
 * A cor da aresta indica o ganho líquido total resultante da troca.
 */
public class TrocaGraphVisualizer {

    /**
     * Visualiza uma lista de trocas duplas entre proprietários, representando cada troca como uma aresta
     * entre dois nós identificados pelos IDs dos proprietários. A cor da aresta varia consoante o ganho líquido total:
     * verde para ganhos superiores a 100, laranja para ganhos entre 0 e 100, e vermelho para ganhos negativos ou nulos.
     *
     * @param trocasDuplas Lista de descrições de trocas duplas, no formato de texto.
     */
    public static void visualize(List<String> trocasDuplas) {
        Graph graph = getNodes();

        Set<String> processed = new HashSet<>();

        for (String troca : trocasDuplas) {
            // Extrai os IDs dos proprietários com regex
            Matcher matcher = Pattern.compile("Troca dupla sugerida entre (\\d+) e (\\d+):").matcher(troca);
            if (matcher.find()) {
                String owner1 = matcher.group(1);
                String owner2 = matcher.group(2);
                String edgeId = owner1 + "-" + owner2;

                if (processed.contains(edgeId)) continue;
                processed.add(edgeId);

                // Extrai os ganhos líquidos dos dois proprietários
                double ganho1 = extractGanho(troca, owner1);
                double ganho2 = extractGanho(troca, owner2);
                double ganhoTotal = ganho1 + ganho2;

                // Adiciona os nós, se ainda não existirem
                if (graph.getNode(owner1) == null) {
                    graph.addNode(owner1).setAttribute("ui.label", owner1);
                }
                if (graph.getNode(owner2) == null) {
                    graph.addNode(owner2).setAttribute("ui.label", owner2);
                }

                // Adiciona a aresta
                Edge e = graph.addEdge(edgeId, owner1, owner2, false);

                // Define a cor da aresta com base no ganho total
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

    /**
     * Cria e configura o grafo com os estilos visuais dos nós e arestas.
     *
     * @return Um grafo com estilos definidos, pronto para uso na visualização.
     */
    private static Graph getNodes() {
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
        return graph;
    }

    /**
     * Extrai o ganho líquido de um proprietário específico a partir do texto de uma troca.
     *
     * @param troca Texto que descreve a troca.
     * @param owner O ID do proprietário cujo ganho se pretende extrair.
     * @return O valor do ganho líquido, ou 0.0 se não for encontrado.
     */
    private static double extractGanho(String troca, String owner) {
        Pattern p = Pattern.compile(owner + ".*?ganho líquido: ([\\d\\.\\-]+)");
        Matcher m = p.matcher(troca);
        if (m.find()) {
            return Double.parseDouble(m.group(1));
        }
        return 0.0;
    }
}
