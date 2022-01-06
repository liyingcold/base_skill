import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.*;

public class CountDownLatchAndCyclicBarrier{
    // 订单队列
    private Vector<Map<String,Integer>> pos;
    // 派送单队列
    private Vector<Map<String,Integer>> dos;

// 执行回调的线程池
    private Executor executor =
                Executors.newFixedThreadPool(1);
    private final CyclicBarrier barrier =
                new CyclicBarrier(2, ()->{
                    executor.execute(this::check);
                });

    private void check(){
         Map<String,Integer> p = pos.remove(0);
         Map<String,Integer> d = dos.remove(0);
        // 执行对账操作
        Integer diff = check(p, d);
        // 差异写入差异库
        save(diff);
    }
    private  Integer check(Map<String,Integer> p,Map<String,Integer> d){
        return p.get("name")+d.get("name");
    }
    private void save(Integer diff){
        System.out.println(diff+"");
    }

    void checkAll(){
        // 循环查询订单库
        Thread T1 = new Thread(()->{
            while(pos.size()>0){
                // 等待
                try {
                    // 查询订单库
                    pos.add(getPOrders());
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        T1.start();
        // 循环查询运单库
        Thread T2 = new Thread(()->{
            while(dos.size()>0){
                // 等待
                try {
                    // 查询运单库
                    dos.add(getDOrders());
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        T2.start();
    }
    private Map<String,Integer> getPOrders(){ Map<String,Integer> person=new HashMap<>(); person.put("name",1); return person;}
    private Map<String,Integer> getDOrders(){ Map<String,Integer> person=new HashMap<>(); person.put("name",2); return person;}

}
