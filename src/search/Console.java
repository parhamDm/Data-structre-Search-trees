/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JTextArea;

/**
 *
 * @author parham
 */
public class Console extends JTextArea {

    public static final int TST = 0;
    public static final int BST = 1;
    public static final int TRIE = 2;
    public static final int HASH = 3;
    public int changedTreeType;
    public int treeType;
    public StringBuffer output;
    private Frame frame;

    public Console(Frame f) {
        output = new StringBuffer();
        frame = f;
        treeType = 1;
        setEditable(false);
    }

    public void setText(StringBuffer t) {
        setText(t.toString());
    }

    public void getCommand(String c) {
        Scanner s = new Scanner(c);
        ArrayList<String> commands = new ArrayList<String>();
        while (s.hasNext()) {
            commands.add(s.next());
        }
        if (commands.size() < 2) {
            output.append("invalid command\n");
            setText(output);
            return;
        }
        if (!twoPartCommand(commands)) {
            if (!moreThanTwoCommands(commands)) {
                output.append("invalid command\n");
            }
        }
        setText(output);
    }

    private void searchFor(ArrayList<String> commands) {
        boolean notFound = false;
        ArrayList<ArrayList<WordNode>> tds;
        tds = new ArrayList<>();
        for (int i = 1; i < commands.size(); i++) {
            TrieNode stopword = frame.stopWords.search(commands.get(i));
            if (stopword != null) {
                continue;
            }
            if (frame.trees[treeType] instanceof BinarySearchTree &&!(frame.trees[treeType] instanceof TSTTree)) {
                BinarySearchTree bst = (BinarySearchTree) frame.trees[treeType];
                BSTNode bstn = bst.search(bst.root, new BSTNode(commands.get(i)));
                if (bstn == null) {
                    notFound = true;
                    continue;
                }
                TreeData data = bstn.data;
                tds.add((ArrayList<WordNode>) data.arrayGetter().clone());
            }
            if (frame.trees[treeType] instanceof TSTTree) {
                TSTTree tst = (TSTTree) frame.trees[treeType];
                TSTNode tstn = tst.search(commands.get(i));
                if (tstn == null) {
                    notFound = true;
                    //tds.add(new ArrayList<WordNode>());
                    continue;
                }
                TreeData data = tstn.data;
                tds.add((ArrayList<WordNode>) data.arrayGetter().clone());
            }
            if (frame.trees[treeType] instanceof TrieTree) {
                TrieTree tt = (TrieTree) frame.trees[treeType];
                TrieNode tn = tt.search(commands.get(i));
                if (tn == null) {
                    notFound = true;
                    //tds.add(new ArrayList<WordNode>());
                    continue;
                }
                TreeData data = tn.td;
                tds.add(data.arrayGetter());
            }
            if (frame.trees[treeType] instanceof HashTable) {
                HashTable ht = (HashTable) frame.trees[treeType];
                HashTable.NodeOFArray noa = ht.search(commands.get(i));
                if (noa == null) {
                    notFound = true;
                    //tds.add(new ArrayList<WordNode>());
                    continue;
                }
                TreeData data = noa.data;
                tds.add(data.arrayGetter());
            }
        }
        if (notFound) {
            output.append("nothing similar\n");
            return;
        }
        if (tds.isEmpty()) {
            output.append("no word found\n");
            return;
        }
        ArrayList<WordNode> sameWordNodes = tds.get(0);
        for (int i = 1; i < tds.size(); i++) {
            sameWordNodes.retainAll(tds.get(i));
        }
        if (sameWordNodes.isEmpty()) {
            output.append("no similarities\n");
            return;
        }
        sameWordNodes.stream().map((wn) -> {
            output.append(wn.word).append("  ").append(wn).append("\n");
            return wn;
        }).map((wn) -> {
            for (WordNode wm : wn.helperWN) {
                if (wm.fileName.equals(wn.fileName)) {

                    output.append(wm.word).append("  ").append(wm).append("\n");
                }
            }
            return wn;
        }).forEach((wn) -> {
            wn.clear();
        });
    }

    private boolean twoPartCommand(ArrayList<String> commands) {

        boolean returner = false;
        if (commands.size() >= 3) {
            return returner;
        }
        boolean foundChecker = false;

        String listingCommand = commands.get(0) + " " + commands.get(1);
        switch (commands.get(0)) {
            case "delete":
                returner = true;
                for (int j = 0; j < frame.files.size(); j++) {
                    String tempName = frame.files.get(j);
                    if (tempName.equals(commands.get(1) + ".txt")) {
                        frame.files.remove(j);
                        foundChecker = true;
                        break;
                    }
                }
                if (!foundChecker) {
                    output.append("file not found\n");
                    break;
                }

                frame.trees[treeType].deleteAll(commands.get(1) + ".txt");
                output.append("file deleted from tree\n");

                break;
            case "update":
                returner = true;
                for (Tree t : frame.trees) {
                    t.deleteAll(commands.get(1) + ".txt");
                }
                Reader r = new Reader(frame.files, frame.stopWords, this);
                if (!r.scan(frame.trees[treeType], frame.currentPath + "\\" + commands.get(1) + ".txt", 0)) {
                    output.append("no such file found\n");
                    break;
                }
                output.append("file updated\n");
                break;
            case "add":
                returner = true;
                boolean isNew = false;
                for (int jj = 0; jj < frame.files.size(); jj++) {
                    if (frame.files.get(jj).equals(commands.get(1) + ".txt")) {
                        //if file is not new here we will get
                        isNew = true;
                    }
                }
                if (!isNew) {
                    Reader rr = new Reader(frame.files, frame.stopWords, this);
                    if (!rr.scan(frame.trees[treeType], frame.currentPath + "\\" + commands.get(1)
                            + ".txt", 0)) {
                        setText(output.append("file is not in selected directory\n"));
                        break;
                    }
                    frame.files.add(commands.get(1) + ".txt");
                    setText(output.append("file added\n"));
                } else {
                    output.append("file is already added\n");
                }
                break;
        }
        switch (listingCommand) {
            case "list -w":
                returner = true;
                output.append(frame.trees[treeType].toString(0));
                output.append("number of words : ")
                        .append(frame.trees[treeType].wordCounter()).append("\n");
                setText(output);

                break;
            case "list -f":
                returner = true;
                frame.files.stream().forEach((f) -> {
                    output.append(new File(f).getName()).append("\n");
                });
                setText(output);
                break;
            case "list -l":
                returner = true;
                File folder = new File(frame.currentPath);
                File[] listOfFiles = folder.listFiles();
                if (listOfFiles == null) {
                    setText(output.append("directory is empty\n"));
                    break;
                }
                for (File f : listOfFiles) {
                    if (f.isFile()) {
                        setText(output.append(f.getName()).append("\n"));
                    }
                }

        }
        return returner;
    }

    private boolean moreThanTwoCommands(ArrayList<String> commands) {
        boolean returner = false;
        switch (commands.get(0)) {

            case "search":
                returner = true;

                if (commands.size() > 2) {
                    searchFor(commands);
                    break;
                }
                long t = System.nanoTime();
                if (frame.trees[treeType] instanceof TSTTree) {
                    TSTTree tst = (TSTTree) frame.trees[treeType];
                    TSTNode tstn = tst.search(commands.get(1));
                    if (tstn != null) {
                        output.append(tstn.data.toString(1));
                    } else {
                        output.append("not found\n");
                    }
                    break;
                }
                if (frame.trees[treeType] instanceof BinarySearchTree) {
                    BinarySearchTree bst = (BinarySearchTree) frame.trees[treeType];
                    BSTNode bstn = bst.search(bst.root, new BSTNode(commands.get(1)));
                    if (bstn != null) {
                        output.append(bstn.data.toString(1));
                    } else {
                        output.append("not found\n");
                    }
                }

                if (frame.trees[treeType] instanceof TrieTree) {
                    TrieTree tt = (TrieTree) frame.trees[treeType];
                    TrieNode tn = tt.search(commands.get(1));
                    if (tn != null) {
                        output.append(tn.td.toString(1));
                    } else {
                        output.append("not found\n");
                    }
                }
                if (frame.trees[treeType] instanceof HashTable) {
                    HashTable ht = (HashTable) frame.trees[treeType];
                    HashTable.NodeOFArray noa = ht.search(commands.get(1));
                    if (noa != null) {
                        output.append(noa.data.toString(1));
                    } else {
                        output.append("not found\n");
                    }
                }
                output.append("opereation done in " + (System.nanoTime() - t) + " nanoseconds \n");
        }
        return returner;
    }

    public String treeStatus() {
        lastStatusOfTree = "number of words: " + frame.trees[treeType].wordCounter() + "\n";
        lastStatusOfTree += "max lenght of tree: " + frame.trees[treeType].getMaxHeight() + "\n";
        output.append(lastStatusOfTree);
        setText(output);
        return lastStatusOfTree;
        //frame.trees[treeType].preorder();
    }
    String lastStatusOfTree;
}
