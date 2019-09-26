/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

/**
 * a node of BinarySearchTree
 *
 * @author parham
 */
public class BSTNode {
    
    TreeData data;
    protected BSTNode leftChild;
    protected BSTNode rightChild;
    int height;
    //etc4

    public BSTNode(String a) {
        data = new TreeData(a.toCharArray());
        height = 1;
    }

    @Override
    public String toString() {
        return "<<" + new String(data.data) + ">>";
    }

    /**
     * adds a new wordNode in data
     *
     * @param s is word
     * @param l is line of word
     * @param w is word of node
     */
    public void addNew(String s, int l, int w) {
        data.add(s, l, w);
    }

    /**
     * deletes a field from tree
     *
     * @param s is filename user wants to delete
     */
    public void Delete(String s) {
        data.head = data.delete(s, data.head);
    }
    public BSTNode(char a) {
        
    }
}
