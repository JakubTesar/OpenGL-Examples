package educanet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Zapis {

    public static String readFile(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String nextline = br.readLine();
            String hah = "";
            while (nextline != null) {
                nextline = br.readLine();
                hah += nextline;
            }
            br.close();
            return hah;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
