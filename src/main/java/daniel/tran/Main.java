package daniel.tran;

public class Main {
    public static void main(String[] args) {
        GraphManager graphManager = new GraphManager();
        String newNodes[] = {"i", "j", "a"};
        graphManager.parseGraph("input.dot");

        graphManager.addNodes(newNodes);
        graphManager.addEdge("i", "j");
        graphManager.addEdge("i", "k");
        System.out.println(graphManager);
        graphManager.outputGraph("test.txt");
    }
}