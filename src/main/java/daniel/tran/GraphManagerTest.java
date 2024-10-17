package daniel.tran;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class GraphManagerTest {
    @Test
    public void testFeature1() throws IOException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph("test/test_input.dot");

        int expectedNodesNum = 8;
        int expectedEdgesNum = 9;
        String expectedVertices = "[a, b, c, d, e, f, g, h]";
        String expectedEdges = "[(a : b), (b : c), (c : d), (d : a), (a : e), (e : f), (e : g), (f : h), (g : h)]";

        assertEquals(expectedNodesNum, graphManager.getGraph().vertexSet().size());  //  Number of nodes should be 8
        assertEquals(expectedEdgesNum, graphManager.getGraph().edgeSet().size());    //  Number of edges should be 9
        assertEquals(expectedVertices, graphManager.getGraph().vertexSet().toString());  //  Vertices should be [a, b, c, d, e, f, g, h]
        assertEquals(expectedEdges, graphManager.getGraph().edgeSet().toString());   //  Edges should be [(a : b), (b : c), (c : d), (d : a), (a : e), (e : f), (e : g), (f : h), (g : h)]

        assertEquals(Files.readString(Paths.get("test/expected_feature_1.txt")), graphManager.toString());
    }

    @Test
    public void testFeature2() throws IOException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph("test/test_input.dot");

        graphManager.addNode("i");
        assertEquals(9, graphManager.getGraph().vertexSet().size());  //  Number of nodes should be 9

        graphManager.addNodes(Arrays.asList("j", "a", "k").toArray(new String[0]));
        assertEquals(11, graphManager.getGraph().vertexSet().size());  //  Number of nodes should be 11
    }

    @Test
    public void testFeature3() throws IOException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph("test/test_input.dot");

        graphManager.addEdge("i", "j");
        assertEquals(10, graphManager.getGraph().vertexSet().size());  //  Number of nodes should be 10
        assertEquals(10, graphManager.getGraph().edgeSet().size());    //  Number of edges should be 10
    }

    @Test
    public void testFeature4() throws IOException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph("test/test_input.dot");

        graphManager.addEdge("i", "j");
        graphManager.outputDOTGraph("test/output_feature_4.dot");

        Path path = Paths.get("test/output_feature_4.dot");
        String outputContent = Files.readString(path);
        assertTrue(outputContent.contains("i -> j;"));

        Files.deleteIfExists(path);
    }

    public GraphManagerTest() throws IOException {
        testFeature1();
        testFeature2();
        testFeature3();
        testFeature4();
    }
}
