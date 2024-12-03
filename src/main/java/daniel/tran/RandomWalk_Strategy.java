package daniel.tran;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class RandomWalk_Strategy implements SearchStrategy {
    private final RandomWalk randomWalk;

    public RandomWalk_Strategy(Graph<String, DefaultEdge> graph) {
        this.randomWalk = new RandomWalk(graph);
    }

    @Override
    public Path search(String srcLabel, String dstLabel) {
        return randomWalk.traverse(srcLabel, dstLabel);
    }

    @Override
    public void setVerbose(boolean verbose) {
        randomWalk.setVerbose(verbose);
    }
}
