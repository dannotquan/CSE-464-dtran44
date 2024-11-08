package daniel.tran;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphManagerTest {
    public String getResourcePath(String filename) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(filename);
        assertNotNull(resource, "File not found.");

        return Paths.get(resource.toURI()).toString();
    }

    public String generateResourcePath(String filename) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("");
        assertNotNull(resource, "Resource directory not found.");

        return Paths.get(resource.toURI()).resolve(filename).toString();
    }

    @Test
    public void testFeature1() throws IOException, URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_input.dot"));

        int expectedNodesNum = 8;
        int expectedEdgesNum = 9;
        String expectedVertices = "[a, b, c, d, e, f, g, h]";
        String expectedEdges = "[(a : b), (b : c), (c : d), (d : a), (a : e), (e : f), (e : g), (f : h), (g : h)]";

        assertEquals(expectedNodesNum, graphManager.getGraph().vertexSet().size());  //  Number of nodes should be 8
        assertEquals(expectedEdgesNum, graphManager.getGraph().edgeSet().size());    //  Number of edges should be 9
        assertEquals(expectedVertices, graphManager.getGraph().vertexSet().toString());  //  Vertices should be [a, b, c, d, e, f, g, h]
        assertEquals(expectedEdges, graphManager.getGraph().edgeSet().toString());   //  Edges should be [(a : b), (b : c), (c : d), (d : a), (a : e), (e : f), (e : g), (f : h), (g : h)]

        assertEquals(Files.readString(Paths.get(getResourcePath("expected_feature_1.txt"))), graphManager.toString());
    }

    @Test
    public void testFeature2() throws IOException, URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_input.dot"));

        graphManager.addNode("i");
        assertEquals(9, graphManager.getGraph().vertexSet().size());  //  Number of nodes should be 9

        graphManager.addNodes(Arrays.asList("j", "a", "k").toArray(new String[0]));
        assertEquals(11, graphManager.getGraph().vertexSet().size());  //  Number of nodes should be 11
    }

    @Test
    public void testFeature3() throws IOException, URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_input.dot"));

        graphManager.addEdge("i", "j");
        assertEquals(10, graphManager.getGraph().vertexSet().size());  //  Number of nodes should be 10
        assertEquals(10, graphManager.getGraph().edgeSet().size());    //  Number of edges should be 10
    }

    @Test
    public void testFeature4() throws IOException, URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_input.dot"));

        graphManager.addEdge("i", "j");
        graphManager.outputDOTGraph(generateResourcePath("output_feature_4.dot"));
        graphManager.outputGraphics(generateResourcePath("output_feature_4_graphic"), "png");

        Path dotPath = Paths.get(getResourcePath("output_feature_4.dot"));
        String outputContent = Files.readString(dotPath);
        assertTrue(outputContent.contains("i -> j;"));

        File graphicOutput = new File(getResourcePath("output_feature_4_graphic.png"));
        assertTrue(graphicOutput.exists());

        Files.deleteIfExists(dotPath);
        Files.deleteIfExists(Path.of(getResourcePath("output_feature_4_graphic.png")));
    }

    public GraphManagerTest() throws IOException, URISyntaxException {
        testFeature1();
        testFeature2();
        testFeature3();
        testFeature4();
    }
}
