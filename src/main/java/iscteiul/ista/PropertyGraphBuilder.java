package iscteiul.ista;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;

public class PropertyGraphBuilder {

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


 //Pergunta 3

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

// pergunta 2, vizinhos entre propriedades



// pergunta 3, vizinhos de cada proprietario

}
