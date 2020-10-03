import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    final private static String fileName = "blockchain.ser";

    public static long msecToSec(long msec) {
        return msec / 1000;
    }

    public static void main(String[] args) {
        Blockchain blockchain = Blockchain.getInstance();
        final int MINERS_COUNT = 4;
        final int MAX_BLOCKS_COUNT = 5;

        ExecutorService miners = Executors.newFixedThreadPool(MINERS_COUNT);

        for (int i = 0; i < MINERS_COUNT; i++) {
            miners.submit(() -> {
                while (true) {
                    List<Block> chain = Blockchain.getInstance().getChain();
                    if (chain.size() > MAX_BLOCKS_COUNT) {
                        break;
                    }
                    String prevHash = chain.size() == 0 ? "0" : chain.get(chain.size() - 1).getHash();
                    Block block = new Block(
                            prevHash,
                            Blockchain.getInstance().getNewBlockId(),
                            Blockchain.getInstance().getLeadZerosCount(),
                            Thread.currentThread().getId(),
                            chain.size() == 0 ? List.of() : List.of("hello from: " + Thread.currentThread().getId())
                    );

                    Blockchain.getInstance().addBlock(block);
                }
            });
        }

        miners.shutdown();
        boolean terminated = false;
        do {
            try {
                terminated = miners.awaitTermination(60, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!terminated);

        List<Block> chain = blockchain.getChain();
        for (int i = 0; i < chain.size(); i++) {
            Block block = chain.get(i);
            Block nextBlock = chain.get(i);
            System.out.println("Block:");
            System.out.println("Created by miner # " + block.getMinerNum());
            System.out.println("Id: " + block.getId());
            System.out.println("Timestamp: " + block.getTimeStamp());
            System.out.println("Magic number: " + block.getMagicNumber());
            System.out.println("Hash of the previous block:");
            System.out.println(block.getPrevHash());
            System.out.println("Hash of the block:");
            System.out.println(block.getHash());
            System.out.println("Block data:" + (block.getBlockData().isEmpty() ? " no messages": ""));
            for (String data:block.getBlockData()) {
                System.out.println(data);
            }
            System.out.println("Block was generating for " + msecToSec(block.getSpentTime()) + " seconds");
            System.out.println(
                    "N was " + (
                            block.getLeadZeroCount() < nextBlock.getLeadZeroCount() ? "increased" : "decreased"
                    ) + " to " + nextBlock.getLeadZeroCount())
            ;
            System.out.println();
        }
    }
}