import javafx.util.Pair;

import javax.swing.*;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LA {
    private static ArrayList<String> tokens = new ArrayList<>(), regexs = new ArrayList<>();
    public static void ReadTokens(){ // reading input file contains all tokens in tha PDF and its regex and store them in tokens[], regexs[]
        Scanner sc = null;
        try {
            sc = new Scanner(new File("tokensRegex.txt"));
        } catch (Exception e) {
            System.out.println("can't input file");
        }
        while(sc.hasNext()){
            String token, regex;
            token = sc.next();
            regex = sc.next();
            tokens.add(token);
            regexs.add(regex);
        }
        sc.close();
    }

    public static ArrayList<Integer> MatchRegex(String[] buffer , String regex , String label , List<Pair<Integer , String>> out
    , ArrayList<Integer> postions) {
        // using regex library match all substrings with the current regex
        Pattern checkRegex = Pattern.compile(regex);
        Matcher regexMatcher = checkRegex.matcher(buffer[0]);
        while (regexMatcher.find()) {
            String token = "<" + label + ">: " + regexMatcher.group().trim();
            int st = regexMatcher.start(), en = regexMatcher.end();
            Pair<Integer, String> MatchedToken = new Pair<>(postions.get(st), token);
            out.add(MatchedToken);
            for (int i = st; i < en; ++i)
                postions.set(i, -1);
        }
        // remove all matched substrings from the original buffer(code)
        ArrayList<Integer> newPositions = new ArrayList<>();
        for (int i = 0; i < postions.size(); ++i) {
            if (postions.get(i) != -1)
                newPositions.add(postions.get(i));
        }
        buffer[0] = buffer[0].replaceAll(regex, "");
        return newPositions;
    }

    public static ArrayList<String> compile(String text){
        ReadTokens();   // all tokens and its regex in the "tokenRegex.txt" file
        List<Pair<Integer, String>> outputTokens = new ArrayList<>();
        // positions[] contains for the indexes of all characters in the buffer
        ArrayList<Integer> positions = new ArrayList<>();
        for(int i = 0 ; i < text.length() ; ++i)
            positions.add(i);
        // Array of Strings just to pass it with reference
        String[] buffer = new String[1];
        buffer[0] = text;
        // for each token try to match it with the remaining buffer
        for(int i = 0 ; i < tokens.size() ; ++i){
            String label = tokens.get(i);
            String regex = regexs.get(i);
            positions = MatchRegex(buffer , regex , label , outputTokens , positions);
        }
        // sort all tokens according their order in the input
        Collections.sort(outputTokens, (o1, o2) -> {
            if(o1.getKey() < o2.getKey())
                return -1;
            return 1;
        });
        // new ArrayList contains all tokens with out the indexes
        ArrayList<String> finalOutput = new ArrayList<>();
        for(int i = 0 ; i < outputTokens.size() ; ++i){
            String token = outputTokens.get(i).getValue();
            finalOutput.add(token);
        }
        if(detectErrors(buffer[0])) {
            System.out.println(buffer[0]);
            finalOutput.clear();
            finalOutput.add("ERRORS DETECTED");
        }
        return finalOutput;
    }

    public static boolean detectErrors(String buffer){
        Pattern checkRegex = Pattern.compile("\\S");
        Matcher regexMatcher = checkRegex.matcher(buffer);
        if(regexMatcher.find())
            return true;
        return false;
    }
}
