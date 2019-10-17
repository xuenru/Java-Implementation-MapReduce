import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.concurrent.LinkedBlockingQueue;

public class ThreadMsg implements Runnable {
    private Process process;
    private LinkedBlockingQueue queue;
    private Boolean isStd;

    ThreadMsg(Process p, LinkedBlockingQueue queue, Boolean isStd) {
        this.process = p;
        this.queue = queue;
        this.isStd = isStd;
    }

    @Override
    public void run() {
        try {
            if (this.isStd) {
                //le flux de la sortie standard
                BufferedReader readerStd = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
                String readLineStd;
                while ((readLineStd = readerStd.readLine()) != null) {
                    this.queue.put(readLineStd);
                }
            } else {
                //le flux de la sortie d'erreur
                BufferedReader readerErr = new BufferedReader(new InputStreamReader(this.process.getErrorStream()));
                String readLineErr;
                while ((readLineErr = readerErr.readLine()) != null) {
                    this.queue.put(readLineErr);
                }

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
