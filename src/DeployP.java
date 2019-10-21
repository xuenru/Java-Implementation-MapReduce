import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DeployP {
    static String tmpPath = "/tmp/xu/";
    static String slaveJarName = "slave.jar";
    static String hostFile = "files/hostList.txt";

    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList availableHosts = getAvailableHosts("files/hostList.txt");
        System.out.println(availableHosts);
        deploySlaves(availableHosts);
    }

    /**
     * get available hosts
     */
    public static ArrayList<String> getAvailableHosts(String filename) {
        List<String> lines = null;
        ArrayList<String> availableHosts = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(filename));
            ArrayList<Thread> allThreads = new ArrayList<>();
            for (String host : lines) {
                Thread threadHost = new Thread(new ThreadHost(host, availableHosts));
                threadHost.start();
                allThreads.add(threadHost);
            }
            for (Thread thread : allThreads) {
                thread.join();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Erreur lors de la lecture de " + filename);
            System.exit(1);
        }

        return availableHosts;
    }

    /**
     * deploy slave on the hosts
     *
     * @param hosts host list
     * @throws InterruptedException
     */
    private static void deploySlaves(ArrayList<String> hosts) throws InterruptedException {
        ArrayList<Thread> allThreads = new ArrayList<>();
        for (String host : hosts) {
            Thread threadHost = new Thread(new ThreadHost(host, "deploySlave"));
            threadHost.start();
            allThreads.add(threadHost);
        }
        for (Thread thread : allThreads) {
            thread.join();
        }
        System.out.println("deploy finish");
    }
}
