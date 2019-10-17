import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadHost implements Runnable {
    String host;
    ArrayList<String> availableHosts;
    Boolean deploy = false;

    /**
     * for getting available hosts list
     *
     * @param host           host name
     * @param availableHosts available Hosts names
     */
    ThreadHost(String host, ArrayList availableHosts) {
        this.host = host;
        this.availableHosts = availableHosts;
    }

    /**
     * for deploy
     *
     * @param host host name
     */
    ThreadHost(String host) {
        this.host = host;
        this.deploy = true;
    }

    @Override
    public void run() {
        try {
            if (this.deploy) {
                deploySlave(this.host);
            } else if (testHosts(this.host)) {
                this.availableHosts.add(this.host);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param host host name
     * @return Boolean if the host available
     * @throws IOException
     */
    private static Boolean testHosts(String host) throws IOException {
        //excuter sur PC d'ecole
        ProcessBuilder pb = new ProcessBuilder("ssh", host, "hostname");
        Process p = pb.start();
        BufferedReader readerStd = new BufferedReader(new InputStreamReader(p.getInputStream()));
        return readerStd.readLine() != null;
    }

    /**
     *
     * @param host host name
     * @throws IOException
     * @throws InterruptedException
     */
    private static void deploySlave(String host) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("ssh", host, "mkdir -p " + DeployP.tmpPath);
        Process p = pb.start();
        p.waitFor();
        ProcessBuilder pbDeploy = new ProcessBuilder("scp", DeployP.slaveJarName, host + ":" + DeployP.tmpPath + DeployP.slaveJarName);
        Process pDeploy = pbDeploy.start();
        pDeploy.waitFor();
        System.out.println("slave deployed on host " + host);
    }
}
