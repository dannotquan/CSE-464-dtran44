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

import static org.junit.jupiter.api.Assertions.*;

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
    public void feature_1_test() throws IOException, URISyntaxException {
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
    public void feature_2_test() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_input.dot"));

        assertTrue(graphManager.addNode("i"));
        assertEquals(9, graphManager.getGraph().vertexSet().size());  //  Number of nodes should be 9

        assertFalse(graphManager.addNodes(Arrays.asList("j", "a", "k").toArray(new String[0]))); // "a" is duplicated
        assertEquals(11, graphManager.getGraph().vertexSet().size());  //  Number of nodes should be 11
    }

    @Test
    public void feature_3_test() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_input.dot"));

        assertTrue(graphManager.addEdge("i", "j"));
        assertEquals(10, graphManager.getGraph().vertexSet().size());  //  Number of nodes should be 10
        assertEquals(10, graphManager.getGraph().edgeSet().size());    //  Number of edges should be 10
    }

    @Test
    public void feature_4_test() throws IOException, URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_input.dot"));

        assertTrue(graphManager.addEdge("i", "j"));
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

    @Test
    public void scenario_1_test() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_input.dot"));

        assertTrue(graphManager.removeNode("a"));
        assertTrue(graphManager.removeNode("b"));
        assertTrue(graphManager.removeNodes(Arrays.asList("c", "d").toArray(new String[0])));

        assertTrue(graphManager.removeEdge("e", "f"));
    }

    @Test
    public void scenario_2_test() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_input.dot"));

        assertTrue(graphManager.removeNode("a"));
//        assertTrue(graphManager.removeNode("z"));
    }

    @Test
    public void scenario_3_test() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_input.dot"));

        assertTrue(graphManager.removeEdge("a", "e"));
//        assertTrue(graphManager.removeEdge("e", "a"));
    }

    @Test
    public void BFS_test_1() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_bfs_least_efficient.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("a", "z", GraphManager.Algorithm.BFS);
        assertNotNull(path, "Path should exist.");
        assertEquals(path.toString(), "a -> b -> c -> d -> e -> f -> g -> h -> i -> j -> k -> l -> m -> n -> o -> p -> q -> r -> s -> t -> u -> v -> w -> x -> y -> z", "Expected: a -> b -> c -> d -> e -> f -> g -> h -> i -> j -> k -> l -> m -> n -> o -> p -> q -> r -> s -> t -> u -> v -> w -> x -> y -> z");
        System.out.println(path);
    }

    @Test
    public void BFS_test_2() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_input.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("b", "h", GraphManager.Algorithm.BFS);
        assertNotNull(path, "Path should exist.");
        assertEquals(path.toString(), "b -> c -> d -> a -> e -> f -> h", "Expected: b -> c -> d -> a -> e -> f -> h");
        System.out.println(path);
    }

    @Test
    public void BFS_test_3() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_bfs_least_efficient.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("z", "a", GraphManager.Algorithm.BFS);
        assertNull(path, "Path should exist.");
    }

    @Test
    public void BFS_test_4() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_input.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("a", "z", GraphManager.Algorithm.BFS);
        assertNull(path, "Path should be null due to z not exist.");
    }

    @Test
    public void BFS_test_5() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_bfs_least_efficient_loop.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("z", "y", GraphManager.Algorithm.BFS);
        assertNotNull(path, "Path should exist.");
        assertEquals(path.toString(), "z -> a -> b -> c -> d -> e -> f -> g -> h -> i -> j -> k -> l -> m -> n -> o -> p -> q -> r -> s -> t -> u -> v -> w -> x -> y", "Expected: z -> a -> b -> c -> d -> e -> f -> g -> h -> i -> j -> k -> l -> m -> n -> o -> p -> q -> r -> s -> t -> u -> v -> w -> x -> y");
        System.out.println(path);
    }

    @Test
    public void BFS_performance_test() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("kilo_graph.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("a", "zz", GraphManager.Algorithm.BFS);
        assertNotNull(path, "Path should exist.");
        System.out.println(path);
    }

    @Test
    public void BFS_mega_performance_test() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("mega_graph.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("a", "zzz", GraphManager.Algorithm.BFS);
        assertNotNull(path, "Path should exist.");
        System.out.println(path);
    }

    @Test
    public void BFS_giga_performance_test() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("giga_graph.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("a", "z9999", GraphManager.Algorithm.BFS);
        assertNotNull(path, "Path should exist.");
        System.out.println(path);
    }

    @Test
    public void BFS_tera_performance_test() throws URISyntaxException {
//        GraphManager graphManager = new GraphManager();
//        graphManager.parseGraph(getResourcePath("tera_graph.dot"));
//
//        daniel.tran.Path path = graphManager.GraphSearch("a", "z99999", GraphManager.Algorithm.BFS);
//        assertNotNull(path, "Path should exist.");
//        System.out.println(path);
    }

    @Test
    public void DFS_test_1() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_dfs_least_efficient.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("a", "z", GraphManager.Algorithm.DFS);
        assertNotNull(path, "Path should exist.");
        assertEquals(path.toString(), "a -> b -> c -> d -> e -> f -> g -> h -> i -> j -> k -> l -> m -> n -> o -> p -> q -> r -> s -> t -> u -> v -> w -> x -> y -> z", "Expected: a -> b -> c -> d -> e -> f -> g -> h -> i -> j -> k -> l -> m -> n -> o -> p -> q -> r -> s -> t -> u -> v -> w -> x -> y -> z");
        System.out.println(path);
    }

    @Test
    public void DFS_test_2() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_dfs_least_efficient.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("a", "z5", GraphManager.Algorithm.DFS);
        assertNotNull(path, "Path should exist.");
        assertEquals(path.toString(), "a -> b -> c -> d -> e -> f -> g -> h -> i -> j -> k -> l -> m -> n -> o -> p -> q -> r -> s -> t -> u -> v -> w -> x -> y -> z -> z1 -> z2 -> z3 -> z4 -> z5", "Expected: a -> b -> c -> d -> e -> f -> g -> h -> i -> j -> k -> l -> m -> n -> o -> p -> q -> r -> s -> t -> u -> v -> w -> x -> y -> z -> z1 -> z2 -> z3 -> z4 -> z5");
        System.out.println(path);
    }

    //    DFS biggest weakness test
    @Test
    public void DFS_test_3() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_dfs_least_efficient.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("a", "a5", GraphManager.Algorithm.DFS);
        assertNotNull(path, "Path should exist.");

        //  Most efficient would be a -> a1 -> a2 -> a3 -> a4 -> a5
        assertEquals(path.toString(), "a -> a1 -> a2 -> a3 -> a4 -> a5", "Expected: a -> a1 -> a2 -> a3 -> a4 -> a5");
        System.out.println(path);
    }

    @Test
    public void DFS_test_4() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_dfs_least_efficient.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("z", "a", GraphManager.Algorithm.DFS);
        assertNull(path, "Path should not exist.");
        System.out.println(path);
    }

    @Test
    public void DFS_test_5() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("test_dfs_least_efficient.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("a", "aa", GraphManager.Algorithm.DFS);
        assertNull(path, "Path should not exist due to aa not exist.");
        System.out.println(path);
    }

    @Test
    public void DFS_performance_test() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("kilo_graph.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("a", "zz", GraphManager.Algorithm.DFS);
        assertNotNull(path, "Path should exist.");
        System.out.println(path);
    }

    @Test
    public void DFS_mega_performance_test() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("mega_graph.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("a", "zzz", GraphManager.Algorithm.DFS);
        assertNotNull(path, "Path should exist.");
        System.out.println(path);
    }

    @Test
    public void DFS_giga_performance_test() throws URISyntaxException {
        GraphManager graphManager = new GraphManager();
        graphManager.parseGraph(getResourcePath("giga_graph.dot"));

        daniel.tran.Path path = graphManager.GraphSearch("a", "z9999", GraphManager.Algorithm.DFS);
        assertNotNull(path, "Path should exist.");
        System.out.println(path);
    }

    @Test
    public void DFS_tera_performance_test() throws URISyntaxException {
//        GraphManager graphManager = new GraphManager();
//        graphManager.parseGraph(getResourcePath("tera_graph.dot"));
//
//        daniel.tran.Path path = graphManager.GraphSearch("a", "z99999", GraphManager.Algorithm.DFS);
//        assertNotNull(path, "Path should exist.");
//        System.out.println(path);
    }
}