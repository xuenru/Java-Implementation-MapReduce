import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Master {
    public static void main(String[] args) throws IOException, InterruptedException {
//        ProcessBuilder pb = new ProcessBuilder("ls", "-l", "/tmp");
//        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "/tmp/xu/slave.jar");
        ProcessBuilder pb = new ProcessBuilder("ssh", "c129-26", "java -jar /tmp/xu/slave.jar");
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
}
