import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Clean {
    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList availableHosts = DeployP.getAvailableHosts(DeployP.hostFile);
        doClean(availableHosts);
    }


    private static void doClean(ArrayList<String> hosts) throws InterruptedException {
        ArrayList<Thread> allThreads = new ArrayList<>();
        for (String host : hosts) {
            Thread threadHost = new Thread(new ThreadHost(host, "clean"));
            threadHost.start();
            allThreads.add(threadHost);
        }
        for (Thread thread : allThreads) {
            thread.join();
        }
        System.out.println("clean finish");
    }
}
