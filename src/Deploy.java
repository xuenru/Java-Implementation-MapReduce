import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Deploy {
    static String tmpPath = "/tmp/xu/";
    static String slaveJarName = "slave.jar";
    static String hostFile = "files/hostList.txt";

    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList availableHosts = getAvailableHosts(hostFile);
        System.out.println(availableHosts);
        deploySlaves(availableHosts);
    }

    /**
     * get available hosts
     */
    public static ArrayList getAvailableHosts(String filename) {
        List<String> lines = null;
        ArrayList<String> availableHosts = new ArrayList<String>();
        try {
            lines = Files.readAllLines(Paths.get(filename));
            for (String host : lines) {
                if (testHost(host)) {
                    availableHosts.add(host);
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture de " + filename);
            System.exit(1);
        }

        return availableHosts;
    }

    private static Boolean testHost(String host) throws IOException {
        //excuter sur PC d'ecole
        ProcessBuilder pb = new ProcessBuilder("ssh", host, "hostname");
        Process p = pb.start();
        BufferedReader readerStd = new BufferedReader(new InputStreamReader(p.getInputStream()));
        return readerStd.readLine() != null;
    }

    private static void deploySlaves(ArrayList<String> hosts) throws IOException, InterruptedException {
        for (String host : hosts) {
            ProcessBuilder pb = new ProcessBuilder("ssh", host, "mkdir -p " + tmpPath);
            Process p = pb.start();
            p.waitFor();
            ProcessBuilder pbDeploy = new ProcessBuilder("scp", slaveJarName, host + ":" + tmpPath + slaveJarName);
            Process pDeploy = pbDeploy.start();
            pDeploy.waitFor();
            System.out.println("slave deployed on host " + host);
        }
    }
}
