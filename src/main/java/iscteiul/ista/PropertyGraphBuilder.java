package iscteiul.ista;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;

/**
 * Classe utilitária responsável por construir grafos de propriedades
 * e carregar dados a partir de ficheiros CSV.
 */
public class PropertyGraphBuilder {

    /**
     * Carrega uma lista de propriedades a partir de um ficheiro CSV.
     * Cada linha do ficheiro representa uma propriedade com os respetivos atributos separados por ponto e vírgula (;).
     *
     * @param filepath Caminho para o ficheiro CSV.
     * @return Lista de objetos Property carregados do ficheiro.
     * @throws Exception Caso ocorra algum erro na leitura do ficheiro ou no parsing dos dados.
     */
    public static List<Property> loadPropertiesFromCSV(String filepath) throws Exception {
        List<Property> properties = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filepath));
        lines.remove(0); // remove header

        for (String line : lines) {
            String[] tokens = line.split(";", -1);
            Property p = new Property(
                    Integer.parseInt(tokens[0]),
                    tokens[1],
                    tokens[2],
                    Double.parseDouble(tokens[3]),
                    Double.parseDouble(tokens[4]),
                    tokens[5],
                    tokens[6],
                    tokens[7],
                    tokens[8],
                    tokens[9]
            );
            properties.add(p);
        }
        return properties;
    }

    /**
     * Constrói um grafo de propriedades a partir de uma lista de propriedades.
     * As propriedades serão ligadas entre si no grafo caso os seus limites se toquem ou se intersectem (vizinhança espacial).
     *
     * @param properties Lista de propriedades a incluir no grafo.
     * @return Um objeto PropertyGraph representando as ligações entre propriedades vizinhas.
     * @throws Exception Caso ocorra algum erro ao processar a geometria das propriedades.
     */
    public static PropertyGraph buildGraphFromProperties(List<Property> properties) throws Exception {
        PropertyGraph graph = new PropertyGraph();
        WKTReader reader = new WKTReader();
        Map<Property, Geometry> geometryMap = new HashMap<>();

        for (Property p : properties) {
            graph.addProperty(p);
            Geometry geom = reader.read(p.geometry);
            geometryMap.put(p, geom);
        }

        for (int i = 0; i < properties.size(); i++) {
            for (int j = i + 1; j < properties.size(); j++) {
                Property a = properties.get(i);
                Property b = properties.get(j);

                Geometry geomA = geometryMap.get(a);
                Geometry geomB = geometryMap.get(b);

                if (geomA.touches(geomB) || geomA.intersects(geomB)) {
                    graph.addEdge(a, b);
                }
            }
        }

        return graph;
    }

    /**
     * Constrói um grafo de proprietários a partir de um grafo de propriedades.
     * Cada proprietário é representado como um nó e são criadas ligações (arestas)
     * entre proprietários cujas propriedades são vizinhas no grafo de propriedades.
     *
     * @param propertyGraph O grafo de propriedades a partir do qual se constrói o grafo de proprietários.
     * @return Um objeto OwnerGraph com as ligações entre proprietários vizinhos.
     */
    public static OwnerGraph buildOwnerGraphFromPropertyGraph(PropertyGraph propertyGraph) {
        OwnerGraph ownerGraph = new OwnerGraph();

        for (Property property : propertyGraph.getAllProperties()) {
            ownerGraph.addOwner(property.OWNER);

            for (Property neighbor : propertyGraph.getNeighbors(property)) {
                ownerGraph.addOwner(neighbor.OWNER);
                ownerGraph.addEdge(property.OWNER, neighbor.OWNER);
            }
        }

        return ownerGraph;
    }
}
