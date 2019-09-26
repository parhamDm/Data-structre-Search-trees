/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.*;

/**
 *
 * @author parham
 */
public class Reader {

    public static final int COUNTER_COLLOCTOR = 8;
    ArrayList<String> files;
    TrieTree stopTree;
    Console c;

    public Reader(ArrayList<String> files, TrieTree stopTree, Console c) {
        this.files = files;
        this.stopTree = stopTree;

        this.c = c;
    }

    public void readDirectory(String dir, Tree tree) {

        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();
        int i = 0;
        if (listOfFiles == null) {
            return;
        }

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String s = file.getAbsolutePath();
                String s1[] = s.split("\\.");
                if (s1[s1.length - 1].equals("TXT") || s1[s1.length - 1].equals("txt")) {
                    System.out.println(s);
                    files.add(new File(s).getName());
                    scan(tree, s, i);
                    i++;
                }
            }
        }
        listOfFiles = null;

    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public boolean scan(Tree t, String fileName, int ii) {
        Scanner fileScanner = null;
//        Scanner lineScanner = null;
//
//        try {
//            fileScanner = new Scanner(new File(fileName));
//        } catch (FileNotFoundException ex) {
//            System.out.println(fileName);
//            return false;
//        }
//        int i = 0;
//        int j = 0;
//        while (fileScanner.hasNextLine()) {
//            String line = fileScanner.nextLine();
//            i++;
//            lineScanner = new Scanner(line);
//            while (lineScanner.hasNext()) {
//                j++;
//                String s = lineScanner.next();
//                addToTree(s, fileName, t, j, i);
//            }
//            j = 0;
//            lineScanner.close();
//        }
//        fileScanner.close();
        StringBuilder sb = new StringBuilder();
        try {
            fileScanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException ex) {
            return false;
        }
        while (fileScanner.hasNextLine()) {
            sb.append(fileScanner.nextLine()).append("\n");
        }
        fileScanner.close();
        String lines[] = sb.toString().split("\n");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replaceFirst("^ *", "");
            lines[i]=lines[i].replaceAll("\\s+", " ");
            String words[] = lines[i].split(" ");
            for (int j = 0; j < words.length; j++) {
                addToTree(words[j], fileName, t, j + 1, i + 1);
            }
        }
        return true;
    }

    private void addToTree(String s, String filename, Tree tree, int word, int line) {
        if (s.equals("")) {
            return;
        }
        String str[] = s.split("'");
//        String str[] =s.split("[^a-zA-Z0-9']+");
        for (String a : str) {
            a = a.replaceAll("[^a-zA-Z]+", "");
            a = a.toLowerCase();
            TrieNode tn = stopTree.search(a);
            if (tn != null) {
                continue;
            }
            tree.add(a, new File(filename).getName(), line, word);
        }
    }
}
