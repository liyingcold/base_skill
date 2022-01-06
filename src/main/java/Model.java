public class Model {
    public static void main(String[] args) {

    }
    private int a =0;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    private synchronized void add() throws InterruptedException {
        while (this.a<100){
            System.out.println(Thread.currentThread()+"add: "+a);
            a+=1;
            if (this.a==100){
                this.notifyAll();
            }
        }
        this.wait();
    }
    private synchronized void del() throws InterruptedException {
        while (this.a>0){
            System.out.println(Thread.currentThread()+"del: "+a);
            a-=1;
            if (this.a==0){
                this.notifyAll();
            }
        }
        this.wait();
    }

    public static void main(String[] args) throws InterruptedException {
        Model m=new Model();
        new Thread(()->{
            try {
                m.add();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                m.del();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        while (true){
            Thread.sleep(100);
        }
    }
}
