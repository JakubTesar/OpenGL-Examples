package educanet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Zapis {

    public static String bruh() {
        String maze = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("maze1.txt"));
            String nextline = br.readLine();

            while (nextline != null) {
                maze += nextline + "\n";
                nextline = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maze;
    }

}
