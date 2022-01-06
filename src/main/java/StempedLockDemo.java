import java.util.concurrent.locks.StampedLock;

public class StempedLockDemo {
    private double x=0.2;
    private double y=0.2;
    private  StampedLock sl = new StampedLock();

    // 存在问题的方法
    void moveIfAtOrigin ( double newX, double newY){
        long stamp = sl.readLock();
        try {
            while (x == 0.0 && y == 0.0) {
                long ws = sl.tryConvertToWriteLock(stamp);
                if (ws != 0L) {
                    x = newX;
                    y = newY;
                    break;
                } else {
                    sl.unlockRead(stamp);
                    stamp = sl.writeLock();
                }
            }
        } finally {
            sl.unlock(stamp);
        }
    }
}
