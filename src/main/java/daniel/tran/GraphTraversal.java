package daniel.tran;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public abstract class GraphTraversal {
    protected final Graph<String, DefaultEdge> graph;
    protected Set<String> visited;
    protected Map<String, String> predecessors;
    protected boolean verbose = false;

    public GraphTraversal(Graph<String, DefaultEdge> graph) {
        this.graph = graph;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public Path traverse(String srcLabel, String dstLabel) {
        visited = new HashSet<>();
        predecessors = new HashMap<>();
        initialize(srcLabel);

        while (!isEmpty()) {
            String current = getNextNode();

            if (verbose) {
                System.out.println("Visiting Node: " + current);
            }

            if (current.equals(dstLabel)) {
                if (verbose) {
                    System.out.println("Destination node found!");
                }

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

        if (verbose) {
            System.out.println("Destination not found.");
        }

        return null;
    }

    protected abstract void initialize(String srcLabel);

    protected abstract boolean isEmpty();

    protected abstract String getNextNode();

    protected abstract void addNode(String node);

    protected Path constructPath(String dstLabel) {
        LinkedList<String> pathNodes = new LinkedList<>();

        for (String at = dstLabel; at != null; at = predecessors.get(at)) {
            pathNodes.addFirst(at);
        }

        return new Path(pathNodes);
    }
}

