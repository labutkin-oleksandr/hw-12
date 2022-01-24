import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CreateWaterMolecules extends Thread  {
    public static volatile int oxygenCount;
    public static volatile int hydrogenCount;
    public static StringBuilder result = new StringBuilder();
    private static final CyclicBarrier waterMoleculesDone = new CyclicBarrier(3, new MoleculesCreated());
    public String incomingData;

    public CreateWaterMolecules(String incomingData) {
        this.incomingData = incomingData;
    }

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

    @Override
    public void run() {
        for (String molecule : incomingData.split("")) {
            if (molecule.equals("H") && hydrogenCount < 2) {
                try {
                    new Thread(hydrogen).start();
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (molecule.equals("O") && oxygenCount == 0) {
                try {
                    new Thread(oxygen).start();
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
