package library.exceptions;

public class MemberLimitExceededException extends Exception {

    private int currentCount;
    private int maxLimit;

    public MemberLimitExceededException(int currentCount, int maxLimit) {
        super("Book limit exceeded! Current: " + currentCount + " / Max: " + maxLimit);
        this.currentCount = currentCount;
        this.maxLimit = maxLimit;
    }

    public int getCurrentCount() { return currentCount; }
    public int getMaxLimit() { return maxLimit; }
}
