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

public class GraphManager {
    private final Graph<String, DefaultEdge> graph;

    public GraphManager() {
        this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
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

    public void addNode(String label) {
        if (this.addN(label)) {
            System.out.println("Node \"" + label + "\" already in graph.");
        }
    }

    public void addNodes(String[] labels) {
        for (String label : labels) {
            addNode(label);
        }
    }

    public void addEdge(String srcLabel, String dstLabel) {
        this.addN(srcLabel);
        this.addN(dstLabel);

        if (graph.containsEdge(srcLabel, dstLabel)) {
            System.out.println("Edge from \"" + srcLabel + "\" to \"" + dstLabel + "\" already in graph.");
            return;
        }

        graph.addEdge(srcLabel, dstLabel);
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
}
