/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.util.ArrayList;

/**
 *
 * @author parham
 */
public class TreeData {

    char[] data;
    WordNode head;

    public TreeData(char[] data) {
        this.data = data;
        head = null;
    }

    public void add(String firstFileName, int line, int word) {
        head = add(head, firstFileName, line, word);
    }

    public WordNode add(WordNode current, String firstFileName, int line, int word) {
        if (current == null) {
            current = new WordNode(firstFileName, line, word, new String(data));
        } else if (current.fileName.equals(firstFileName)) {
            current.addData(line, word);
        } else {
            current.link = add(current.link, firstFileName, line, word);
        }

        return current;
    }

    public boolean isEmpty() {
        if (head == null) {
            return true;
        }
        return false;
    }

    public WordNode delete(String s, WordNode wn) {
        if (wn == null) {
            return null;
        }
        if (wn.fileName.equals(s)) {
            wn = wn.link;
            wn = delete(s, wn);
        } else {
            wn.link = delete(s, wn.link);
        }
        return wn;
    }

    public void printLinkedList() {
        WordNode curr = head;
        while (curr != null) {
            curr = curr.link;
        }
    }

    public ArrayList<WordNode> arrayGetter() {
        ArrayList<WordNode> arr = new ArrayList<WordNode>();
        WordNode curr = head;
        while (curr != null) {
            arr.add((WordNode) curr);
            curr = curr.link;
        }
        return arr;
    }

    public void printLinkedList(WordNode w) {
        if (w == null) {
            return;
        }
        printLinkedList(w.link);
    }

    public String toString(int kind) {
        WordNode curr = head;
        StringBuilder s = new StringBuilder();
        while (curr != null) {
            if (kind == 1) {
                s.append(curr);
            }
            else{
                s.append(curr.fileName+"\n");
            }
            curr = curr.link;
        }
        return s.toString()+"\n";
    }
}
