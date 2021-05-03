package educanet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Zapis {

    public static String loadText(String textFile) {
        String maze = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(textFile));
            String line = br.readLine();
            for (int i = 0; line != null; i++) {
                maze += line + "\n";
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maze;
    }
}
