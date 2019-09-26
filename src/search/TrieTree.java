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
public class TrieTree extends Tree {

    private TrieNode root;

    public static final int WORDS_LENTH = 26;

    public TrieTree() {
        root = new TrieNode('1');
    }

    @Override
    public void add(String newData, String fileName, int line, int w) {
        newData = newData.toLowerCase();
        newData = newData.replaceAll("[^a-zA-Z]+", "");
        if (newData.equals("")) {
            return;
        }
        TrieNode current = root;
        char word[] = newData.toCharArray();
        for (int i = 0; i < word.length; i++) {
            int number = word[i] - 'a';
            if (current.child[number] == null) {
                current.child[number] = new TrieNode(word[i]);
                current = current.child[number];
            } else {
                current = current.child[number];
            }
        }
        current.td.data = newData.toCharArray();
        current.isWord = true;
        current.word = newData;
        current.td.data = word;
        current.td.add(fileName, line, w);
    }
    private int helperJ;

    public TrieNode search(String s) {
        s = s.toLowerCase();
        s = s.replaceAll("[^a-zA-Z]+", "");
        if (s.equals("")) {
            return null;
        }
        helperJ = 0;
        TrieNode current = root;
        char word[] = s.toCharArray();
        for (int i = 0; i < word.length; i++) {
            int number = word[i] - 'a';
            if (current.child[number] == null) {
                return null;
            } else {
                current = current.child[number];
            }
            helperJ++;
        }
        if (!current.isWord) {
            return null;
        }
        return current;
    }

    private int helperI;

    @Override
    public void delete(String s, String filename) {
        char word[] = s.toCharArray();
        helperI = 0;
        root = deleteRec(word, root, filename);
        if (root == null) {
            root = new TrieNode((char) 0);
        }
    }

    @Override
    public void deleteAll(String Filename) {
        root = deleteAllRec(root, Filename);
        if (root == null) {
            root = new TrieNode((char) 0);
        }
    }

    private TrieNode deleteAllRec(TrieNode node, String fileName) {
        if (node == null) {
            return null;
        }
        for (int i = 0; i < WORDS_LENTH; i++) {
            node.child[i] = deleteAllRec(node.child[i], fileName);
        }

        if (!node.td.isEmpty()) {
            node.td.head = node.td.delete(fileName, node.td.head);
            if (!node.td.isEmpty()) {
                return node;
            }
        }
        node.isWord = false;
        node.word = null;
        boolean a = false;
        for (int i = 0; i < WORDS_LENTH; i++) {
            if (node.child[i] != null) {
                a = true;
            }
        }

        if (!a && !node.isWord) {
            node = null;
        }
        return node;
    }

    private TrieNode deleteRec(char[] cData, TrieNode current, String fileName) {
        if (current == null) {
            return null;
        }
        if (helperI < (cData.length)) {

            current.child[cData[helperI] - 'a'] = deleteRec(cData, current.child[cData[helperI++] - 'a'], fileName);
        }
        if (helperI == (cData.length)) {
            if (!current.td.isEmpty()) {
                current.td.delete(fileName, current.td.head);
                if (!current.td.isEmpty()) {
                    return current;
                }
            }
            current.isWord = false;
            helperI = cData.length + 1;
        }

        boolean a = false;
        for (int i = 0; i < WORDS_LENTH; i++) {
            if (current.child[i] != null) {
                a = true;
            }
        }
        if (!a && !current.isWord) {
            current = null;
        }
        return current;
    }

    private StringBuffer PrintRec(TrieNode node, StringBuffer sb,int kind) {
        if (node == null) {
            return sb;
        }

        if (node.isWord) {
            sb.append("<<" + new String(node.td.data) + ">>").append("\n").append(node.td.toString(kind));
        }
        for (int i = 0; i < WORDS_LENTH; i++) {
            sb = PrintRec(node.child[i], sb,kind);
        }
        return sb;
    }

    @Override
    public String toString(int kind) {
        StringBuffer sb = new StringBuffer();
        sb = PrintRec(root, sb,kind);
        return sb.toString();
    }

    @Override
    public void cleanTree() {
        if (root == null) {
            root = new TrieNode((char) 0);
        }
        for (int i = 0; i < WORDS_LENTH; i++) {
            root.child[i] = null;
        }
    }

    @Override
    public int wordCounter() {
        counter(root);
        int x = numberOfWords;
        numberOfWords = 0;
        return x;
    }

    private void counter(TrieNode currNode) {
        if (currNode == null) {
            return;
        }
        if (currNode.isWord) {
            numberOfWords++;
        }
        for (int i = 0; i < WORDS_LENTH; i++) {
            counter(currNode.child[i]);
        }
    }

    @Override
    public int getMaxHeight() {
        return maxHeightRec(root);
    }

    private int maxHeightRec(TrieNode cur) {
        if (cur == null) {
            return 0;
        }
        int x = 0;
        for (int i = 0; i < WORDS_LENTH; i++) {
            x = Integer.max(x, maxHeightRec(cur.child[i]));
        }
        return 1 + x;
    }
}
