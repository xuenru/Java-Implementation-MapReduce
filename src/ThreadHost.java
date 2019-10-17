import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadHost implements Runnable {
    String host;
    ArrayList<String> availableHosts;
    String action;

    /**
     * for getting available hosts list
     *
     * @param host           host name
     * @param availableHosts available Hosts names
     */
    ThreadHost(String host, ArrayList availableHosts) {
        this.host = host;
        this.availableHosts = availableHosts;
        this.action = "test";
    }

    /**
     * for deploy
     *
     * @param host host name
     */
    ThreadHost(String host, String action) {
        this.host = host;
        this.action = action;
    }

    @Override
    public void run() {
        try {
            switch (this.action) {
                case "test":
                    if (testHosts(this.host)) {
                        this.availableHosts.add(this.host);
                    }
                    break;
                case "deploy":
                    deploySlave(this.host);
                    break;
                case "clean":
                    clean(this.host);
                    break;
                default:
                    throw new Exception("unknown thread action");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
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

    /**
     * clean slave on the host
     *
     * @param host host name
     * @throws IOException
     * @throws InterruptedException
     */
    private static void clean(String host) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("ssh", host, "rm -rf " + DeployP.tmpPath);
        Process p = pb.start();
        p.waitFor();
        System.out.println(Deploy.tmpPath + " is removed on host " + host);
    }
}
