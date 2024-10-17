package daniel.tran;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.dot.DOTImporter;

import java.io.FileReader;
import java.io.IOException;

public class Grapher {
    private Graph<String, DefaultEdge> graph;

    public Grapher() {
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
}
