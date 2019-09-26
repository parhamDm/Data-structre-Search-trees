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
public class TSTNode extends BSTNode{
    TSTNode equalChild;
    char charOfNode;
    boolean isWord;
    String word;

    public TSTNode(char a) {
        super(a);
        char words[] = new char[1];
        data = new TreeData(words);
        charOfNode = a;
        isWord = false;
        rightChild = null;
        leftChild = null;
    }

    public void addNew(String s, int l, int w) {
        data.add(s, l, w);
    }
}
