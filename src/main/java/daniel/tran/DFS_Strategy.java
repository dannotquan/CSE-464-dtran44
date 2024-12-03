package daniel.tran;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class DFS_Strategy implements SearchStrategy {
    private final DFS dfs;

    public DFS_Strategy(Graph<String, DefaultEdge> graph) {
        this.dfs = new DFS(graph);
    }

    @Override
    public Path search(String srcLabel, String dstLabel) {
        return dfs.traverse(srcLabel, dstLabel);
    }

    @Override
    public void setVerbose(boolean verbose) {
        dfs.setVerbose(verbose);
    }
}
