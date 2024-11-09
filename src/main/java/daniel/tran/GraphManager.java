package daniel.tran;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.dot.DOTImporter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GraphManager {
    private final Graph<String, DefaultEdge> graph;

    public GraphManager() {
        this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
    }

    public Graph<String, DefaultEdge> getGraph() {
        return this.graph;
    }

    public void parseGraph(String path) {
        DOTImporter<String, DefaultEdge> importer = new DOTImporter<>();
        importer.setVertexFactory(label -> label);

        try (FileReader file = new FileReader(path)) {
            importer.importGraph(graph, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Number of nodes: ").append(graph.vertexSet().size()).append("\n");
        sb.append("Number of edges: ").append(graph.edgeSet().size()).append("\n\nNodes: [");

        for (String vertex : graph.vertexSet()) {
            sb.append(vertex).append(", ");
        }

        sb.deleteCharAt(sb.length() - 2); //  Delete the last ","
        sb.deleteCharAt(sb.length() - 1); //  Delete the last " "

        sb.append("]\nEdges:\n");

        for (DefaultEdge edge : graph.edgeSet()) {
            sb.append(graph.getEdgeSource(edge)).append(" -> ").append(graph.getEdgeTarget(edge)).append("\n");
        }

        return sb.toString();
    }

    public void outputGraph(String filepath) {
        try {
            Files.writeString(Paths.get(filepath), toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean addN(String label) {
        if (graph.containsVertex(label)) {
            return true;
        }

        graph.addVertex(label);
        return false;
    }

    public boolean addNode(String label) {
        boolean result = this.addN(label);

        if (result) {
            System.out.println("Node \"" + label + "\" already in graph.");
            return false;
        }

        return true;
    }

    public boolean addNodes(String[] labels) {
        boolean state = true;

        for (String label : labels) {
            boolean success = addNode(label);

            if (!success) {
                state = false;
            }
        }

        return state;
    }

    public boolean addEdge(String srcLabel, String dstLabel) {
        this.addN(srcLabel);
        this.addN(dstLabel);

        if (graph.containsEdge(srcLabel, dstLabel)) {
            System.out.println("Edge from \"" + srcLabel + "\" to \"" + dstLabel + "\" already in graph.");
            return false;
        }

        graph.addEdge(srcLabel, dstLabel);
        return graph.containsEdge(srcLabel, dstLabel);
    }

    public void outputDOTGraph(String path) {
        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>(v -> v.toString());

        try {
            exporter.exportGraph(graph, Files.newBufferedWriter(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void outputGraphics(String path, String format) {
        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>(v -> v.toString());

        Format fileFormat = switch (format.toLowerCase()) {
            case "svg" -> Format.SVG;
            case "plain" -> Format.PLAIN;
            case "plain-ext" -> Format.PLAIN_EXT;
            case "ps" -> Format.PS;
            case "json" -> Format.JSON;
            case "dot" -> Format.DOT;
            case "xdot" -> Format.XDOT;
            default -> Format.PNG;
        };

        StringWriter writer = new StringWriter();
        exporter.exportGraph(graph, writer);

        Parser parser = new Parser();
        try {
            MutableGraph exportGraph = parser.read(writer.toString());
            Graphviz.fromGraph(exportGraph).render(fileFormat).toFile(new File(path + "." + format.toLowerCase()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean removeNode(String label) {
        if (!graph.containsVertex(label)) {
            System.out.println("Node \"" + label + "\" doesn't exist in graph.");
            return false;
        }

        graph.removeVertex(label);
        return true;
    }

    public boolean removeNodes(String[] labels) {
        boolean state = true;

        for (String label : labels) {
            boolean success = removeNode(label);

            if (!success) {
                state = false;
            }
        }

        return state;
    }

    public boolean removeEdge(String srcLabel, String dstLabel) {
        if (!graph.containsEdge(srcLabel, dstLabel)) {
            System.out.println("Edge from \"" + srcLabel + "\" to \"" + dstLabel + "\" doesn't exist in graph.");
            return false;
        }

        graph.removeEdge(srcLabel, dstLabel);
        return true;
    }

    public Path GraphSearch(String srcLabel, String dstLabel) {
        if (!graph.containsVertex(srcLabel)) {
            System.out.println("Source node \"" + srcLabel + "\" doesn't exist in graph.");
            return null;
        }

        if (!graph.containsVertex(dstLabel)) {
            System.out.println("Destination node \"" + dstLabel + "\" doesn't exist in graph.");
            return null;
        }

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> predecessors = new HashMap<>();

        queue.add(srcLabel);
        visited.add(srcLabel);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equals(dstLabel)) {
                List<String> pathNodes = new LinkedList<>();
                for (String at = dstLabel; at != null; at = predecessors.get(at)) {
                    pathNodes.add(0, at);
                }
                return new Path(pathNodes);
            }

            for (DefaultEdge edge : graph.outgoingEdgesOf(current)) {
                String neighbor = graph.getEdgeTarget(edge);
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    predecessors.put(neighbor, current);
                }
            }
        }

        return null;
    }

    public Path GraphSearch(String srcLabel, String dstLabel) {
        if (!graph.containsVertex(srcLabel)) {
            System.out.println("Source node \"" + srcLabel + "\" doesn't exist in graph.");
            return null;
        }

        if (!graph.containsVertex(dstLabel)) {
            System.out.println("Destination node \"" + dstLabel + "\" doesn't exist in graph.");
            return null;
        }

        Set<String> discovered = new HashSet<>();
        Map<String, String> predecessors = new HashMap<>();
        Stack<Iterator<DefaultEdge>> stack = new Stack<>();
        Stack<String> nodeStack = new Stack<>();

        discovered.add(srcLabel);
        nodeStack.push(srcLabel);
        stack.push(graph.outgoingEdgesOf(srcLabel).iterator());

        while (!stack.isEmpty()) {
            Iterator<DefaultEdge> edges = stack.peek();

            if (edges.hasNext()) {
                DefaultEdge edge = edges.next();
                String neighbor = graph.getEdgeTarget(edge);

                if (!discovered.contains(neighbor)) {
                    discovered.add(neighbor);
                    predecessors.put(neighbor, nodeStack.peek());
                    if (neighbor.equals(dstLabel)) {
                        List<String> pathNodes = new LinkedList<>();
                        for (String node = dstLabel; node != null; node = predecessors.get(node)) {
                            pathNodes.add(0, node);
                        }
                        return new Path(pathNodes);
                    }
                    nodeStack.push(neighbor);
                    stack.push(graph.outgoingEdgesOf(neighbor).iterator());
                }
            } else {
                stack.pop();
                nodeStack.pop();
            }
        }

        return null;
    }
}