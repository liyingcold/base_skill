import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Model1 {

    private class BlockedQueue<T>{
        final Lock lock =new ReentrantLock();
        // 条件变量：队列不满
        final Condition notFull =
                lock.newCondition();
        // 条件变量：队列不空
        final Condition notEmpty =
                lock.newCondition();
        Queue queue=new ArrayDeque(3);

        // 入队
        void enq(T x) {
            lock.lock();
            try {
                while (queue.size()>3){
                    // 等待队列不满
                    notFull.await();
                }
                // 省略入队操作...
                queue.add(x);
                //入队后,通知可出队
                notEmpty.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        // 出队
        Object deq(){
            Object m=new Object();
            lock.lock();
            try {
                while (queue.size()==0){
                    // 等待队列不空
                    notEmpty.await();
                }
                // 省略出队操作...
                m=queue.poll();
                //出队后，通知可入队
                notFull.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            return m;
        }
    }
}
