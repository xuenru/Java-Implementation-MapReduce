import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class Master1 {
    public static void main(String[] args) throws IOException, InterruptedException {
//        ProcessBuilder pb = new ProcessBuilder("ls", "-l", "/tmp");
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "/Users/xuenru/IdeaProjects/TP_INF727/out/artifacts/TP_INF727_jar/slave.jar");
//        pb.redirectErrorStream(true);
        Process p = pb.inheritIO().start();

        if (!p.waitFor(6, TimeUnit.SECONDS)) {
            p.destroy();
        }
        ;

        //le flux de la sortie standard
        BufferedReader readerStd = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        System.out.println("sortie standard: ");
        String readLineStd;
        while ((readLineStd = readerStd.readLine()) != null) {
            System.out.println(readLineStd);
        }


        //le flux de la sortie d'erreur
        BufferedReader readerErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//        System.out.println("sortie erreur: ");
        String readLineErr;
        while ((readLineErr = readerErr.readLine()) != null) {
            System.err.println(readLineErr);
        }
    }
}
