import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // read from file
        Scanner sc = null;
        try {
            sc = new Scanner(new File("input.txt"));
        } catch (Exception e) {
            System.out.println("can't input file");
        }
        // read the whole text
        String text = "";
        while(sc.hasNext()){
            text += sc.nextLine();
            text += " \n";
        }
        sc.close();
        LA ob = new LA();
        ArrayList<String> tokens = ob.compile(text); // do Lexical Analyzer Stuff
//        for(String token : tokens)
//            System.out.println(token);

        try {
            FileWriter ot = new FileWriter("output.txt");
            for(String token : tokens)
                ot.write(token + "\n");
            ot.close();
        } catch (Exception e) {
            System.out.println("can't output file");
        }
    }
}
