package daniel.tran;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class BFS_Strategy implements SearchStrategy {
    private final BFS bfs;

    public BFS_Strategy(Graph<String, DefaultEdge> graph) {
        this.bfs = new BFS(graph);
    }

    @Override
    public Path search(String srcLabel, String dstLabel) {
        return bfs.traverse(srcLabel, dstLabel);
    }
}
