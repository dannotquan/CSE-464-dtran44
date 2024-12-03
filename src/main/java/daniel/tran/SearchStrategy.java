package daniel.tran;

public interface SearchStrategy {
    Path search(String srcLabel, String dstLabel);
    void setVerbose(boolean verbose);
}

