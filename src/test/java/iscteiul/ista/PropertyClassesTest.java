package iscteiul.ista;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;



import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.*;
import java.util.*;

/**
 * Classe de testes unitários para verificar o correto funcionamento das classes
 * Property, PropertyGraph e PropertyGraphBuilder.
 * Utiliza JUnit 5 para validar comportamentos esperados como:
 * - formatação textual da propriedade
 * - extração da geometria
 * - relações de vizinhança entre propriedades
 * - leitura de ficheiros CSV
 * - construção do grafo de propriedades
 */
public class PropertyClassesTest {

    Property p1, p2, p3;

    /**
     * Inicializa três propriedades de teste antes de cada execução.
     */
    @BeforeEach
    void setup() {
        p1 = new Property(1, "ID1", "NUM1", 100.0, 200.0, "POLYGON ((0 0, 1 1, 1 0, 0 0))", "Owner1", "Freg1", "Mun1", "Ilha1");
        p2 = new Property(2, "ID2", "NUM2", 110.0, 210.0, "POLYGON ((1 1, 2 2, 2 1, 1 1))", "Owner2", "Freg2", "Mun2", "Ilha2");
        p3 = new Property(1, "ID3", "NUM3", 120.0, 220.0, "POLYGON ((2 2, 3 3, 3 2, 2 2))", "Owner3", "Freg3", "Mun3", "Ilha3");
    }

    /**
     * Verifica se o método toString da classe Property inclui corretamente o OBJECTID.
     */
    @Test
    void testToString() {
        String str = p1.toString();
        assertTrue(str.contains("OBJECTID=1"));
    }

    /**
     * Testa a extração de coordenadas de uma geometria válida com parênteses duplos.
     */
    @Test
    void testGetGeometryValidDoubleParenthesis() {
        String result = p1.getGeometry("POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))");
        assertEquals("30 10, 40 40, 20 40, 10 20, 30 10", result);
    }

    /**
     * Testa a extração de coordenadas de uma geometria válida com parênteses simples.
     */
    @Test
    void testGetGeometryValidSingleParenthesis() {
        String result = p1.getGeometry("POLYGON (30 10, 40 40, 20 40, 10 20, 30 10)");
        assertEquals("30 10, 40 40, 20 40, 10 20, 30 10", result);
    }

    /**
     * Testa a resposta do método getGeometry quando a string fornecida não é válida.
     */
    @Test
    void testGetGeometryInvalid() {
        String result = p1.getGeometry("INVALID POLYGON STRING");
        assertEquals("", result);
    }

    /**
     * Verifica se as propriedades são corretamente adicionadas ao grafo
     * e se o número de propriedades no grafo está correto.
     */
    @Test
    void testAddPropertyAndGetAll() {
        PropertyGraph graph = new PropertyGraph();
        graph.addProperty(p1);
        graph.addProperty(p2);
        assertTrue(graph.getAllProperties().contains(p1));
        assertEquals(2, graph.getAllProperties().size());
    }

    /**
     * Verifica se a ligação entre duas propriedades é corretamente registada
     * e se cada uma reconhece a outra como vizinha.
     */
    @Test
    void testAddEdgeAndNeighbors() {
        PropertyGraph graph = new PropertyGraph();
        graph.addProperty(p1);
        graph.addProperty(p2);
        graph.addEdge(p1, p2);
        assertTrue(graph.getNeighbors(p1).contains(p2));
        assertTrue(graph.getNeighbors(p2).contains(p1));
    }

    /**
     * Testa o carregamento de propriedades a partir de um ficheiro CSV temporário.
     * Garante que a propriedade é corretamente lida e interpretada.
     */
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

    /**
     * Testa a construção de um grafo de propriedades com base em geometrias adjacentes.
     * Garante que as propriedades vizinhas são corretamente ligadas no grafo.
     */
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
