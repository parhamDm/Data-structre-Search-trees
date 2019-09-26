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
public abstract class Tree {

    int numberOfWords;

    /**
     * adds a new word to selected tree. if word is not in node, tree will make
     * a new node
     *
     * @param newData is new word need to be added
     * @param fileName is name of file which contains current word
     * @param line line number
     * @param word word number in line
     */
    public abstract void add(String newData, String fileName, int line, int word);

    /**
     * deletes a filename from treeData of Node if treeData is empty it will
     * delete node;
     *
     * @param word is word you want to delete
     * @param Filename is file name contains selected word
     */
    public abstract void delete(String word, String Filename);

    /**
     * deletes a filename from every node
     *
     * @param filename is name of file you want to delete from directory
     */
    public abstract void deleteAll(String filename);

    /**
     * cleans tree for filling again
     */
    public abstract void cleanTree();

    /**
     * count number of words is located in tree
     *
     * @return number of words are in tree
     */
    public abstract int wordCounter();
    public abstract  int getMaxHeight();
    public abstract String toString(int kind);
}
