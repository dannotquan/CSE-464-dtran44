package daniel.tran;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.LinkedList;
import java.util.Queue;

public class BFS extends GraphTraversal {
    private Queue<String> queue;

    public BFS(Graph<String, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected void initialize(String srcLabel) {
        queue = new LinkedList<>();
        queue.add(srcLabel);
        visited.add(srcLabel);
    }

    @Override
    protected boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    protected String getNextNode() {
        return queue.poll();
    }

    @Override
    protected void addNode(String node) {
        queue.add(node);
    }
}
