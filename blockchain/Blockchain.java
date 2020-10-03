import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private long MAX_SPENT_TIME = 600;
    private static Blockchain blockchain = new Blockchain();

    private volatile int leadZerosCount = 0;
    private List<Block> chain;

    private Blockchain() {
        chain = new ArrayList<>();
    }

    public static synchronized Blockchain getInstance() {
        return blockchain;
    }


    public synchronized boolean addBlock(Block block) {
        String prevHash = chain.size() == 0 ? "0" : chain.get(chain.size() - 1).getHash();
        if (block.getPrevHash().equals(prevHash) && block.isHashValid(block.getHash(), leadZerosCount)) {
            chain.add(block);
            if (block.getSpentTime() >= MAX_SPENT_TIME && leadZerosCount != 0) {
                leadZerosCount--;
            } else if (block.getSpentTime() < MAX_SPENT_TIME) {
                leadZerosCount++;
            }
            return true;
        }
        return false;
    }

    public synchronized List<Block> getChain() {
        return chain;
    }

    public synchronized int getNewBlockId() {
        return chain.size() + 1;
    }

    public synchronized int getLeadZerosCount() {
        return leadZerosCount;
    }
}