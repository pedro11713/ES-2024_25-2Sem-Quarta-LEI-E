package iscteiul.ista;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        String filePath = "Madeira-Moodle-1.1.csv";

        try {
            // Carrega as propriedades
            List<Property> properties = PropertyGraphBuilder.loadPropertiesFromCSV(filePath);

            // Mostra as duas primeiras propriedades
            if (properties.size() >= 2) {
                System.out.println("Primeiras propriedades:");
                System.out.println(properties.get(0));
                System.out.println(properties.get(1));
            }

            // Constrói o grafo
            PropertyGraph graph = PropertyGraphBuilder.buildGraphFromProperties(properties);

            // Imprime as vizinhanças da primeira propriedade
            Property first = properties.get(0);
            Set<Property> vizinhos = graph.getNeighbors(first);

            System.out.println("\nVizinhos da primeira propriedade:");
            for (Property viz : vizinhos) {
                System.out.println(viz);
            }

            // Visualiza o grafo
            List<Property> sample = properties.subList(0, Math.min(500, properties.size()));
            PropertyGraph sampleGraph = PropertyGraphBuilder.buildGraphFromProperties(sample); // Cambié el nombre de la
                                                                                               // variable a sampleGraph
            PropertyGraphVisualizer.visualize(sampleGraph); // Uso de sampleGraph en vez de graph

            OwnerGraph ownerGraph = PropertyGraphBuilder.buildOwnerGraphFromPropertyGraph(graph);

            // Remover donos além dos 20 primeiros (manual, só para debug)
            Set<String> toKeep = ownerGraph.getAllOwners().stream().limit(20).collect(Collectors.toSet());
            OwnerGraph filtered = new OwnerGraph();
            for (String o : toKeep) {
                filtered.addOwner(o);
                for (String neighbor : ownerGraph.getNeighbors(o)) {
                    if (toKeep.contains(neighbor)) {
                        filtered.addOwner(neighbor);
                        filtered.addEdge(o, neighbor);
                    }
                }
            }
            OwnerGraphVisualizer.visualize(filtered);

            String owner1 = "1";
            Set<String> neighbors1 = ownerGraph.getNeighbors(owner1);
            System.out.println("Vizinhos do proprietário " + owner1 + ": " + neighbors1);

            String owner2 = "2";
            Set<String> neighbors2 = ownerGraph.getNeighbors(owner2);
            System.out.println("Vizinhos do proprietário " + owner2 + ": " + neighbors2);

            // Mostrar vizinhos do proprietário "55"
            String owner801 = "801";
            Set<String> neighbors801 = ownerGraph.getNeighbors(owner801);
            System.out.println("Vizinhos do proprietário " + owner801 + ": " + neighbors801);

            // tarefa 4 (area)
            // Cálculo de área média por área administrativa
            String tipoArea = "freguesia"; // Pode ser "freguesia", "municipio" ou "ilha"
            String nomeArea = "Arco da Calheta"; // Nome da freguesia/município/ilha desejada

            double media = PropertyStatistics.calcularAreaMedia(properties, tipoArea, nomeArea);
            System.out.println("Área média das propriedades em " + nomeArea + " (" + tipoArea + "): " + media + " m²");

            // tarefa 5 (area by owner)
            double media2 = PropertyAreaByOwner.calcularAreaMedia(properties, tipoArea, nomeArea);
            System.out.println(
                    "Área média das propriedades by owner em " + nomeArea + " (" + tipoArea + "): " + media2 + " m²");

            System.out.println("\n=== Sugestões de trocas duplas entre proprietários ===");

            List<String> trocasDuplas = TrocaSugeridor.sugerirTrocasDuplas(properties, graph, ownerGraph);

            if (trocasDuplas.isEmpty()) {
                System.out.println("Nenhuma troca dupla sugerida.");
            } else {
                for (String troca : trocasDuplas) {
                    System.out.println(troca);
                }
            }

           

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
