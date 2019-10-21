import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Slave {
    public static void main(String[] args) throws InterruptedException, IOException {
        //java -jar slave.jar 0 /tmp/<votre nom d’utilisateur>/splits/S0.txt
    	String id = args[0];
    	String splitFileName = "S" + id + ".txt";
    	String umFileName = "UM" + id + ".txt";
    	
    	
    	generateUM(splitFileName, umFileName);
    }
    
    private static void generateUM(String splitFileName, String umFileName) throws IOException, InterruptedException {
    	Process pMkdir = Runtime.getRuntime().exec("mkdir -p " + DeployP.tmpPath + "maps/");
        pMkdir.waitFor();
        
        List<String> lines = Files.readAllLines(Paths.get(DeployP.tmpPath+"splits/" + splitFileName));
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(DeployP.tmpPath + "maps/"+umFileName));
        
        for (String line : lines) {
            for (String word : line.split(" ")) {
                writer.write(word + " 1\n");
            }
        }
        writer.close();
     
        System.out.println("read from "+splitFileName);
    	System.out.println("write into" + umFileName);
    }

}
