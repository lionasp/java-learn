import java.util.Date;
import java.util.List;
import java.util.Random;

public class Block {
    private int id;
    private long timeStamp;
    private String prevHash;
    private String hash;
    private long spentTime;
    private int magicNumber;
    private int leadZeroCount;
    private long minerNum;
    private final List<String> blockData;

    public Block(String prevHash, int id, int leadZerosCount, long minerNum, List<String> blockData) {
        this.id = id;
        this.prevHash = prevHash;
        this.timeStamp = new Date().getTime();
        this.leadZeroCount = leadZerosCount;
        this.minerNum = minerNum;
        this.blockData = blockData;
        this.hash = calculateHash(leadZerosCount);
    }

    public int getLeadZeroCount() {
        return leadZeroCount;
    }

    private String calculateHash(int leadZerosCount) {
        Random r = new Random();
        String resultHash;
        long timerStart = System.currentTimeMillis();
        do {
            magicNumber = r.nextInt();
            resultHash = StringUtil.applySha256(id + " " + prevHash + " " + timeStamp + " " + magicNumber);
        } while (!isHashValid(resultHash, leadZerosCount));
        spentTime = System.currentTimeMillis() - timerStart;
        return resultHash;
    }

    public long getSpentTime() {
        return spentTime;
    }

    public List<String> getBlockData() {
        return blockData;
    }

    public int getMagicNumber() {
        return magicNumber;
    }

    public boolean isHashValid(String hash, int leadZerosCount) {
        boolean valid = true;
        for (int i = 0; i < leadZerosCount; i++) {
            if (hash.charAt(i) != '0') {
                valid = false;
                break;
            }
        }
        if (hash.charAt(leadZerosCount) == '0') {
            valid = false;
        }
        return valid;
    }

    public String getHash() {
        return this.hash;
    }

    public int getId() {
        return id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public long getMinerNum() {
        return minerNum;
    }
}