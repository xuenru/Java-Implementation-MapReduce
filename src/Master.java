import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Master {
    public static void main(String[] args) throws IOException, InterruptedException {
//        playSlaveTest();
        deploySplits();

    }

    static void playSlaveTest() throws InterruptedException, IOException {
        //        ProcessBuilder pb = new ProcessBuilder("ls", "-l", "/tmp");
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "/tmp/xu/slave.jar");
//        ProcessBuilder pb = new ProcessBuilder("ssh", "c129-26", "java -jar /tmp/xu/slave.jar");
//        pb.redirectErrorStream(true);
        Process p = pb.start();
        LinkedBlockingQueue queue = new LinkedBlockingQueue();
        Thread threadStd = new Thread(new ThreadMsg(p, queue, true));
        Thread threadErr = new Thread(new ThreadMsg(p, queue, false));
        threadStd.start();
        threadErr.start();

        String msg = (String) queue.poll(6, TimeUnit.SECONDS);
        if (msg == null) {
            p.destroy();
            System.err.println("Timeout!");
        } else {
            System.out.println(msg);
        }
    }

    /**
     * deploy the split files
     *
     * @throws InterruptedException
     */
    static void deploySplits() throws InterruptedException {
        ArrayList<String> availableHosts = DeployP.getAvailableHosts("files/hostList.txt");
        System.out.println(availableHosts);
        int i = 0;
        ArrayList<Thread> allThreads = new ArrayList<>();
        for (String host : availableHosts) {
            Thread threadHost = new Thread(new ThreadHost(host, "deployFile", "/tmp/xu/splits/", "S" + i + ".txt"));
            threadHost.start();
            allThreads.add(threadHost);
            i++;
        }
        for (Thread thread : allThreads) {
            thread.join();
        }
        System.out.println("deploy split finish");
    }
}
