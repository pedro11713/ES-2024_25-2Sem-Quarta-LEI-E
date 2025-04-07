package iscteiul.ista;
import java.util.List;
import java.util.Set;

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
