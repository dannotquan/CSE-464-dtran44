package daniel.tran;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.Stack;

public class DFS extends GraphTraversal {
    private Stack<String> stack;

    public DFS(Graph<String, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected void initialize(String srcLabel) {
        stack = new Stack<>();
        stack.push(srcLabel);
        visited.add(srcLabel);
    }

    @Override
    protected boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    protected String getNextNode() {
        return stack.pop();
    }

    @Override
    protected void addNode(String node) {
        stack.push(node);
    }
}
