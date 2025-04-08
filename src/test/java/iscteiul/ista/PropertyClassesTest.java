package iscteiul.ista;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PropertyClassesTest {

    Property p1, p2, p3;

    @BeforeEach
    void setup() {
        p1 = new Property(1, "ID1", "NUM1", 100.0, 200.0, "POLYGON ((0 0, 1 1, 1 0, 0 0))", "Owner1", "Freg1", "Mun1", "Ilha1");
        p2 = new Property(2, "ID2", "NUM2", 110.0, 210.0, "POLYGON ((1 1, 2 2, 2 1, 1 1))", "Owner2", "Freg2", "Mun2", "Ilha2");
        p3 = new Property(1, "ID3", "NUM3", 120.0, 220.0, "POLYGON ((2 2, 3 3, 3 2, 2 2))", "Owner3", "Freg3", "Mun3", "Ilha3");
    }

    @Test
    void testToString() {
        String str = p1.toString();
        assertTrue(str.contains("OBJECTID=1"));
    }


    @Test
    void testGetGeometryValidDoubleParenthesis() {
        String result = p1.getGeometry("POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))");
        assertEquals("30 10, 40 40, 20 40, 10 20, 30 10", result);
    }

    @Test
    void testGetGeometryValidSingleParenthesis() {
        String result = p1.getGeometry("POLYGON (30 10, 40 40, 20 40, 10 20, 30 10)");
        assertEquals("30 10, 40 40, 20 40, 10 20, 30 10", result);
    }

    @Test
    void testGetGeometryInvalid() {
        String result = p1.getGeometry("INVALID POLYGON STRING");
        assertEquals("", result);
    }

    @Test
    void testAddPropertyAndGetAll() {
        PropertyGraph graph = new PropertyGraph();
        graph.addProperty(p1);
        graph.addProperty(p2);
        assertTrue(graph.getAllProperties().contains(p1));
        assertEquals(2, graph.getAllProperties().size());
    }

    @Test
    void testAddEdgeAndNeighbors() {
        PropertyGraph graph = new PropertyGraph();
        graph.addProperty(p1);
        graph.addProperty(p2);
        graph.addEdge(p1, p2);
        assertTrue(graph.getNeighbors(p1).contains(p2));
        assertTrue(graph.getNeighbors(p2).contains(p1));
    }

    @Test
    void testLoadPropertiesFromCSV() throws Exception {
        Path tempFile = Files.createTempFile("test-properties", ".csv");
        Files.write(tempFile, Arrays.asList(
                "OBJECTID;PAR_ID;PAR_NUM;Shape_Length;Shape_Area;geometry;OWNER;Freguesia;Municipio;Ilha",
                "10;X1;N1;10.0;20.0;POLYGON ((0 0, 1 1, 1 0, 0 0));John;FregA;MunA;IlhaA"
        ));

        List<Property> properties = PropertyGraphBuilder.loadPropertiesFromCSV(tempFile.toString());
        assertEquals(1, properties.size());
        assertEquals(10, properties.get(0).OBJECTID);

        Files.delete(tempFile);
    }

    @Test
    void testBuildGraphFromProperties() throws Exception {
        List<Property> props = Arrays.asList(
                new Property(1, "", "", 0, 0, "POLYGON ((0 0, 2 0, 2 2, 0 2, 0 0))", "", "", "", ""),
                new Property(2, "", "", 0, 0, "POLYGON ((2 0, 4 0, 4 2, 2 2, 2 0))", "", "", "", "")
        );

        PropertyGraph graph = PropertyGraphBuilder.buildGraphFromProperties(props);

        assertEquals(2, graph.getAllProperties().size());
        assertTrue(graph.getNeighbors(props.get(0)).contains(props.get(1)));
        assertTrue(graph.getNeighbors(props.get(1)).contains(props.get(0)));
    }
}
