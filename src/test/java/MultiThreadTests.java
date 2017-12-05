/**
 * Created by Claire on 12/5/17.
 */
class MyThread extends Thread {
    private int tid;

    public MyThread(int tid) {
        this.tid = tid;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                System.out.println(String.format("%d:%d", tid, i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class MultiThreadTests {
    public static void testThread() {
//        for (int i = 0; i < 10; i++) {
//            new MyThread(i).start();
//        }
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int j = 0; j < 10; j++) {
                            Thread.sleep(1000);
                            System.out.println(String.format("%d:%d", j, finalI));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        testThread();
    }
}
