package daniel.tran;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public abstract class GraphTraversal {
    protected final Graph<String, DefaultEdge> graph;
    protected Set<String> visited;
    protected Map<String, String> predecessors;

    public GraphTraversal(Graph<String, DefaultEdge> graph) {
        this.graph = graph;
    }

    public Path traverse(String srcLabel, String dstLabel) {
        visited = new HashSet<>();
        predecessors = new HashMap<>();
        initialize(srcLabel);

        while (!isEmpty()) {
            String current = getNextNode();

            if (current.equals(dstLabel)) {
                return constructPath(dstLabel);
            }

            for (DefaultEdge edge : graph.outgoingEdgesOf(current)) {
                String neighbor = graph.getEdgeTarget(edge);
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    addNode(neighbor);
                    predecessors.put(neighbor, current);
                }
            }
        }

        return null;
    }

    protected abstract void initialize(String srcLabel);

    protected abstract boolean isEmpty();

    protected abstract String getNextNode();

    protected abstract void addNode(String node);

    private Path constructPath(String dstLabel) {
        LinkedList<String> pathNodes = new LinkedList<>();

        for (String at = dstLabel; at != null; at = predecessors.get(at)) {
            pathNodes.addFirst(at);
        }

        return new Path(pathNodes);
    }
}

