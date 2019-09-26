/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.util.*;

/**
 *
 * @author parham
 */
public class WordNode implements Cloneable {

    final String fileName;
    public WordNode link;
    private Loc head;
    final String word;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof WordNode) {
            helperWN.add((WordNode) obj);
            return this.fileName.equals(((WordNode) obj).fileName);
        } else {
            return false;
        }
    }

    public void clear() {
        helperWN.clear();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.fileName);
        return hash;
    }
    ArrayList<WordNode> helperWN;

    public boolean contains(WordNode obj) {
        for (WordNode w : helperWN) {
            if (w == obj) {
                return true;
            }
        }
        return false;
    }

    public WordNode(String filename, int line, int number, String word) {
        helperWN = new ArrayList<WordNode>();
        head = new Loc(line, number);
        this.fileName = filename;
        link = null;
        this.word = word;
    }

    @Override
    public String toString() {
        Loc cu = head;
        String name = fileName;
        String sb = name + ": {";
        while (cu != null) {
            sb = sb + ("(" + cu.line + ", " + cu.word + ")");
            cu = cu.link;
        }
        sb = sb + "}\n";
        return sb;
    }

    void addData(int line, int word) {
        head = addRec(head, line, word);
    }

    private Loc addRec(Loc curr, int line, int word) {
        if (curr == null) {
            curr = new Loc(line, word);
        } else {
            curr.link = addRec(curr.link, line, word);
        }
        return curr;
    }

    private class Loc {
        public Loc(int line, int word) {
            this.line = line;
            this.word = word;
        }
        int line;
        int word;
        Loc link;
    }
}
