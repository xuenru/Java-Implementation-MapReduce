import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DeployP {
    public static void main(String[] args) throws IOException {
        ArrayList availableHosts = getAvailableHosts("files/hostList.txt");
        System.out.println(availableHosts);
    }

    /**
     * get available hosts
     */
    private static ArrayList getAvailableHosts(String filename) {
        List<String> lines = null;
        ArrayList<String> availableHosts = new ArrayList<String>();
        try {
            lines = Files.readAllLines(Paths.get(filename));
            ArrayList<Thread> allThreads = new ArrayList<Thread>();
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
}
