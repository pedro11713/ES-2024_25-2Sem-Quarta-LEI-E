package iscteiul.ista;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyClassesTest {

    Property p1, p2, p3;

    @BeforeEach
    void setup() {
        p1 = new Property(1, "ID1", "NUM1", 100.0, 200.0, "POLYGON ((0 0, 1 1, 1 0, 0 0))", "Owner1", "Freg1", "Mun1",
                "Ilha1");
        p2 = new Property(2, "ID2", "NUM2", 110.0, 210.0, "POLYGON ((1 1, 2 2, 2 1, 1 1))", "Owner2", "Freg1", "Mun1",
                "Ilha1");
        p3 = new Property(3, "ID3", "NUM3", 120.0, 220.0, "POLYGON ((2 2, 3 3, 3 2, 2 2))", "Owner2", "Freg2", "Mun2",
                "Ilha2");
    }

    // ----- PROPERTY -----
    @Test
    void testToString() {
        assertTrue(p1.toString().contains("OBJECTID=1"));
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
        String result = p1.getGeometry("INVALID");
        assertEquals("", result);
    }

    @Test
    void testEqualsAndHashCode() {
        Property another = new Property(1, "", "", 0, 0, "", "", "", "", "");
        assertEquals(p1, another);
        assertEquals(p1.hashCode(), another.hashCode());
    }

    // ----- PROPERTYGRAPH -----
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
        assertTrue(graph.areNeighbors(p1, p2));
    }

    // ----- PROPERTYGRAPHBUILDER -----
    @Test
    void testLoadPropertiesFromCSV() throws Exception {
        Path tempFile = Files.createTempFile("test-properties", ".csv");
        Files.write(tempFile, Arrays.asList(
                "OBJECTID;PAR_ID;PAR_NUM;Shape_Length;Shape_Area;geometry;OWNER;Freguesia;Municipio;Ilha",
                "10;X1;N1;10.0;20.0;POLYGON ((0 0, 1 1, 1 0, 0 0));John;FregA;MunA;IlhaA"));
        List<Property> properties = PropertyGraphBuilder.loadPropertiesFromCSV(tempFile.toString());
        assertEquals(1, properties.size());
        assertEquals(10, properties.get(0).OBJECTID);
        Files.delete(tempFile);
    }

    @Test
    void testBuildGraphFromProperties() throws Exception {
        List<Property> props = Arrays.asList(
                new Property(1, "", "", 0, 0, "POLYGON ((0 0, 2 0, 2 2, 0 2, 0 0))", "", "", "", ""),
                new Property(2, "", "", 0, 0, "POLYGON ((2 0, 4 0, 4 2, 2 2, 2 0))", "", "", "", ""));
        PropertyGraph graph = PropertyGraphBuilder.buildGraphFromProperties(props);
        assertTrue(graph.getNeighbors(props.get(0)).contains(props.get(1)));
    }

    // ----- OWNERGRAPH -----
    @Test
    void testOwnerGraphAddAndNeighbors() {
        OwnerGraph og = new OwnerGraph();
        og.addOwner("A");
        og.addOwner("B");
        og.addEdge("A", "B");
        assertTrue(og.getNeighbors("A").contains("B"));
        assertTrue(og.getNeighbors("B").contains("A"));
        Set<String> expected = new HashSet<>();
        expected.add("A");
        expected.add("B");
        assertEquals(expected, og.getAllOwners());

    }

    @Test
    void testOwnerGraphNoSelfEdge() {
        OwnerGraph og = new OwnerGraph();
        og.addOwner("A");
        og.addEdge("A", "A");
        assertFalse(og.getNeighbors("A").contains("A"));
    }

    // ----- AREA POR OWNER -----
    @Test
    void testAreaMediaPorFreguesia() {
        List<Property> props = Arrays.asList(p1, p2, p3);
        double media = PropertyAreaByOwner.calcularAreaMedia(props, "freguesia", "Freg1");
        assertEquals((200.0 + 210.0) / 2, media);
    }

    @Test
    void testAreaMediaPorTipoInvalido() {
        List<Property> props = Arrays.asList(p1, p2);
        assertThrows(IllegalArgumentException.class,
                () -> PropertyAreaByOwner.calcularAreaMedia(props, "bairro", "Freg1"));
    }

    // ----- PROPERTYSTATISTICS -----
    @Test
    void testAreaMediaSimples() {
        List<Property> props = Arrays.asList(p1, p2, p3);
        double media = PropertyStatistics.calcularAreaMedia(props, "ilha", "Ilha1");
        assertEquals(205.0, media);
    }

    @Test
    void testAreaMediaSimplesZero() {
        List<Property> props = Arrays.asList(p2, p3);
        double media = PropertyStatistics.calcularAreaMedia(props, "freguesia", "FregX");
        assertEquals(0.0, media);
    }

    // ----- TROCAS -----
    @Test
    void testTrocaSugeridor() {
        List<Property> props = Arrays.asList(
                new Property(1, "", "", 0, 100, "POLYGON ((0 0, 1 0, 1 1, 0 1, 0 0))", "A", "", "", ""),
                new Property(2, "", "", 0, 102, "POLYGON ((1 0, 2 0, 2 1, 1 1, 1 0))", "B", "", "", ""),
                new Property(3, "", "", 0, 99, "POLYGON ((0 1, 1 1, 1 2, 0 2, 0 1))", "A", "", "", ""),
                new Property(4, "", "", 0, 101, "POLYGON ((1 1, 2 1, 2 2, 1 2, 1 1))", "B", "", "", ""));

        PropertyGraph pg = new PropertyGraph();
        for (Property p : props)
            pg.addProperty(p);
        pg.addEdge(props.get(0), props.get(1));
        pg.addEdge(props.get(2), props.get(3));

        OwnerGraph og = new OwnerGraph();
        og.addOwner("A");
        og.addOwner("B");
        og.addEdge("A", "B");

        List<String> trocas = TrocaSugeridor.sugerirTrocasDuplas(props, pg, og);
        assertFalse(trocas.isEmpty());
        assertTrue(trocas.get(0).contains("Troca dupla sugerida"));
    }
}
