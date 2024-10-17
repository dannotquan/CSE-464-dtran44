package daniel.tran;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.dot.DOTImporter;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Grapher {
    private Graph<String, DefaultEdge> graph;

    public Grapher() {
        this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
    }

    public void parseGraph(String path) throws FileNotFoundException {
        DOTImporter<String, DefaultEdge> importer = new DOTImporter<>();
        importer.setVertexFactory(label -> label);

        FileReader file = new FileReader(path);
        importer.importGraph(this.graph, file);
    }
}
