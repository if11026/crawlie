/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author samuellouvan
 */
public class FeatureVectorTransformer {

    public static HashMap<String, Integer> generateVector(String nGramFileNamePos, String nGramFileNameNeg) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(nGramFileNamePos));
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int i = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String tokens[] = line.split("\t");
            map.put(tokens[0], i++);
        }

        scanner.close();

        scanner = new Scanner(new File(nGramFileNameNeg));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String tokens[] = line.split("\t");
            map.put(tokens[0], i++);
        }

        scanner.close();
        return map;
    }

    public static void printHashMap(HashMap<String, Integer> map) {
        for (String s : map.keySet()) {
            System.out.println(s + ":" + map.get(s));
        }
    }

    public static void generateARFFHeader(int size) throws FileNotFoundException
    {
        PrintWriter writer = new PrintWriter("header.arff");
        writer.println("@RELATION sentiment-twitter");
        writer.println("@DATA");
        for (int i = 1; i <= size; i++)
        {
            writer.println("@ATTRIBUTE word"+i+" NUMERIC");
        }
        writer.println("@ATTRIBUTE class {0,1}");
        
        writer.close();
    }
    public static void transformToFeatureVectorUnigram(String fileName, int classType, HashMap<String, Integer> map) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(fileName));
        PrintWriter writer = new PrintWriter(fileName + ".vector");
        int cnt = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String tokens[] = line.split(" ");
            HashMap<Integer, Integer> idxOn = new HashMap<Integer, Integer>();
            for (int i = 0; i < tokens.length; i++) {
                idxOn.put(map.get(tokens[i]), i);
            }
            String vector = "";
            for (int i = 0; i < map.size(); i++) {
                if (idxOn.get(i) != null) {
                    vector += "1,";
                } else {
                    vector += "0,";
                }
            }
            if (classType == 1) {
                vector += "1";
            } else {
                vector += "0";
            }
            writer.println(vector);
            writer.flush();
            System.out.println(++cnt);

        }
        writer.close();
        scanner.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Read unigram file, store it in hashmap
        HashMap<String, Integer> map = generateVector("pos_5000_unigram.txt", "neg_5000_unigram.txt");
        //printHashMap(map);
        transformToFeatureVectorUnigram("testing/netral.txt", 0, map);
        generateARFFHeader(map.size());
        // Read the corpus, transform it into feature vector

    }
}
