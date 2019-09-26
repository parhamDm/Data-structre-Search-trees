/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

/**
 * 
 * @author parham
 */
public class TrieNode {

    char data;
    boolean isWord;
    TrieNode[] child;
    TreeData td;
    String word;

    public TrieNode(char data) {
        child = new TrieNode[26];
        for (int i = 0; i < 26; i++) {
            child[i] = null;
        }
        this.data = data;
        isWord = false;
        td = new TreeData(null);
    }
}
