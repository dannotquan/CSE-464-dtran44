package daniel.tran;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public class RandomWalk extends GraphTraversal {
    private final Random random;
    private String currentNode;
    private final List<String> nodesWithUnvisitedNeighbors;

    public RandomWalk(Graph<String, DefaultEdge> graph) {
        super(graph);
        this.random = new Random();
        this.nodesWithUnvisitedNeighbors = new ArrayList<>();
    }

    @Override
    protected void initialize(String srcLabel) {
        visited = new HashSet<>();
        predecessors = new HashMap<>();
        currentNode = srcLabel;
        visited.add(srcLabel);
        nodesWithUnvisitedNeighbors.add(srcLabel);

        System.out.println("Starting Random Walk from: " + srcLabel);
    }

    @Override
    protected boolean isEmpty() {
        return nodesWithUnvisitedNeighbors.isEmpty();
    }

    @Override
    protected String getNextNode() {
        return currentNode;
    }

    @Override
    protected void addNode(String node) {
    }

    private Iterable<String> getNeighbors(String current) {
        List<String> neighbors = new ArrayList<>();

        for (DefaultEdge edge : graph.outgoingEdgesOf(current)) {
            String neighbor = graph.getEdgeTarget(edge);
            neighbors.add(neighbor);
        }

        return neighbors;
    }

    @Override
    public Path traverse(String srcLabel, String dstLabel) {
        initialize(srcLabel);

        while (!isEmpty()) {
            String current = getNextNode();

            if (verbose)
                System.out.println("Visiting Node: " + current);

            if (current.equals(dstLabel)) {
                if (verbose)
                    System.out.println("Destination node found!");

                return constructPath(dstLabel);
            }

            List<String> unvisitedNeighbors = new ArrayList<>();
            for (String neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    unvisitedNeighbors.add(neighbor);
                }
            }

            if (!unvisitedNeighbors.isEmpty()) {
                String nextNode = unvisitedNeighbors.get(random.nextInt(unvisitedNeighbors.size()));
                predecessors.put(nextNode, current);
                visited.add(nextNode);
                currentNode = nextNode;

                if (hasUnvisitedNeighbors(nextNode)) {
                    nodesWithUnvisitedNeighbors.add(nextNode);
                }

                if (!hasUnvisitedNeighbors(current)) {
                    nodesWithUnvisitedNeighbors.remove(current);
                }
            } else {
                nodesWithUnvisitedNeighbors.remove(current);

                if (nodesWithUnvisitedNeighbors.isEmpty()) {
                    System.out.println("No nodes with unvisited neighbors left. Ending search.");
                    return null;
                }

                currentNode = nodesWithUnvisitedNeighbors.get(random.nextInt(nodesWithUnvisitedNeighbors.size()));
            }
        }

        if (verbose)
            System.out.println("Destination not found after exploring all nodes.");

        return null;
    }

    private boolean hasUnvisitedNeighbors(String node) {
        for (String neighbor : getNeighbors(node)) {
            if (!visited.contains(neighbor)) {
                return true;
            }
        }
        return false;
    }
}
