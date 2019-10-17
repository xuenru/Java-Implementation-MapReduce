import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CountWordsSeq {
    public static void main(String[] args) throws IOException {
        //String filename = "input.txt";
        //String filename = "forestier_mayotte.txt";
        //String filename = "deontologie_police_nationale.txt";
        //String filename = "domaine_public_fluvial.txt";
        //String filename = "sante_publique.txt";
        String filename = "CC-MAIN-20170322212949-00140-ip-10-233-31-227.ec2.internal.warc.wet";
        long startTime = System.currentTimeMillis();
        HashMap<String, Integer> wordsList = countWords(filename);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Compter le nombre d’occurrences: " + totalTime + " ms");
//        System.out.println(wordsList);

        startTime = System.currentTimeMillis();
        //full list
        List<Map.Entry<String, Integer>> sortedWordList = sortByKeyValue(wordsList);
        endTime = System.currentTimeMillis();
        totalTime = endTime - startTime;
        System.out.println("Tri (par nombre d'occurrences et alphabétique): " + totalTime + " ms");
        //first 50 words
        List<Map.Entry<String, Integer>> sortedWordList50 = sortedWordList.subList(0, 50);

        for (Map.Entry<String, Integer> entry : sortedWordList50) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    /**
     * count words in file
     *
     * @param filename text file
     * @return
     * @throws IOException
     */
    private static HashMap<String, Integer> countWords(String filename) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get("files/" + filename));

        HashMap<String, Integer> wordsList = new HashMap<String, Integer>();

        for (String line : lines) {
            for (String word : line.split(" ")) {
                if (wordsList.containsKey(word)) {
                    wordsList.put(word, wordsList.get(word) + 1);
                } else {
                    wordsList.put(word, 1);
                }
            }
            ;
        }

        return wordsList;
    }

    /**
     * sort wordList by key and value
     *
     * @param wordsList word list
     * @return
     */
    private static List<Map.Entry<String, Integer>> sortByKeyValue(HashMap<String, Integer> wordsList) {
        //change hashMap to TreeMap (sort by key)
        Map<String, Integer> treeMap = new TreeMap<>(wordsList);
        Comparator<Map.Entry<String, Integer>> valueComparatordesc = (o1, o2) -> (o2.getValue() - o1.getValue());

        List<Map.Entry<String, Integer>> list = new ArrayList<>(treeMap.entrySet());

        list.sort(valueComparatordesc);

        return list;
    }

}
