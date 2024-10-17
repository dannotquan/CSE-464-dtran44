package daniel.tran;

public class Main {
    public static void main(String[] args) {
        GraphManager graphManager = new GraphManager();
        String newNodes[] = {"i", "j", "a"};
        graphManager.parseGraph("input.dot");

        graphManager.addNodes(newNodes);
        System.out.println(graphManager);
        graphManager.outputGraph("test.txt");
    }
}