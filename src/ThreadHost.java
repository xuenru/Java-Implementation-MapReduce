import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadHost implements Runnable {
    String host;
    ArrayList<String> availableHosts;

    ThreadHost(String h, ArrayList availableHosts) {
        this.host = h;
        this.availableHosts = availableHosts;
    }

    @Override
    public void run() {
        try {
            if (testHosts(this.host)) {
                this.availableHosts.add(this.host);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Boolean testHosts(String host) throws IOException {
        //excuter sur PC d'ecole
        ProcessBuilder pb = new ProcessBuilder("ssh", host, "hostname");
        Process p = pb.start();
        BufferedReader readerStd = new BufferedReader(new InputStreamReader(p.getInputStream()));
        return readerStd.readLine() != null;
    }
}
