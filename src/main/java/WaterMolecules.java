import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class WaterMolecules {
    public static volatile int oxygenCount;
    public static volatile int hydrogenCount;
    public static StringBuilder result = new StringBuilder();
    private static final CyclicBarrier waterMoleculesDone = new CyclicBarrier(3, new MoleculesCreated());

    private final static Thread oxygen = new Thread(() -> {
        try {
            releaseOxygen();
            waterMoleculesDone.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    });

    private final static Thread hydrogen = new Thread(() -> {
        try {
            releaseHydrogen();
            waterMoleculesDone.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    });

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            if (hydrogenCount < 2) {
                new Thread(hydrogen).start();
                Thread.sleep(400);
            }

            if (oxygenCount == 0) {
                new Thread(oxygen).start();
                Thread.sleep(400);
            }
        }
    }

    private static void releaseHydrogen() throws InterruptedException {
            result.append("H");
            hydrogenCount++;
    }

    private static void releaseOxygen() throws InterruptedException {
            result.append("O");
            oxygenCount++;
    }

    public static class MoleculesCreated implements Runnable {
        @Override
        public void run() {
            System.out.println(result.toString());
            result.setLength(0);
            oxygenCount = 0;
            hydrogenCount = 0;
        }
    }
}
