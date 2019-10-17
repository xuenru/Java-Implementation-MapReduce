import java.io.IOException;

public class Hello {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hello");

        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "/tmp/xu/slave.jar");
        Process p = pb.start();
        /*BufferedReader readerStd = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String readLineStd;
        while ((readLineStd = readerStd.readLine()) != null) {
            System.out.println(readLineStd);
        }*/
        p.waitFor();
        System.out.print("test");


    }
}
