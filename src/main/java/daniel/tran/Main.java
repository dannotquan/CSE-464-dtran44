package daniel.tran;

public class Main {
    public static void main(String[] args) {
        GraphManager graphManager = new GraphManager();

        graphManager.parseGraph("input.dot");
        graphManager.outputGraph("test.txt");

        System.out.println(graphManager);
    }
}