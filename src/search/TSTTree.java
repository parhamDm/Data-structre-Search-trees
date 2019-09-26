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
public class TSTTree extends BinarySearchTree {

    TSTNode root;

    public TSTNode addRec(TSTNode current, char word[], int counter, String filename, int line, int w) {
        if (current == null) {
            current = new TSTNode(word[counter]);
        }
        if (word[counter] > current.charOfNode) {
            current.rightChild = addRec((TSTNode) current.rightChild, word, counter, filename, line, w);
        } else if (word[counter] < current.charOfNode) {
            current.leftChild = addRec((TSTNode) current.leftChild, word, counter, filename, line, w);
            //are equal
        } else if (counter < word.length - 1) {
            current.equalChild = addRec((TSTNode) current.equalChild, word, ++counter, filename, line, w);
        } else {
            current.isWord = true;
            current.addNew(filename, line, w);
            current.word = new String(word);
            current.data.data = word;
        }
        current.height = Integer.max(getHeight(current.leftChild), getHeight(current.rightChild)) + 1;
        int differ = getHeight(current.leftChild) - getHeight(current.rightChild);
        if (differ > 1) {
            if (getHeight(current.leftChild.leftChild) > getHeight(current.leftChild.rightChild)) {
                current = (TSTNode) LLRotate(current);
            } else {
                current.leftChild = RRRotate(current.leftChild);
                current = (TSTNode) LLRotate(current);
            }

        }
        if (differ < -1) {
            if (getHeight(current.rightChild.rightChild) > getHeight(current.rightChild.leftChild)) {
                current = (TSTNode) RRRotate(current);
                ;
            } else {
                current.rightChild = LLRotate(current.rightChild);
                current = (TSTNode) RRRotate(current);
            }
        }
        return current;
    }

    @Override
    public void add(String newData, String fileName, int line, int word) {
        newData = newData.replaceAll("[^a-zA-Z]+", "");
        newData = newData.toLowerCase();
        if (newData.equals("") || newData.equals(" ")) {
            return;
        }
        root = addRec(root, newData.toCharArray(), 0, fileName, line, word);
    }

    @Override
    public void delete(String word, String Filename) {

    }

    public TSTNode deleteAllRec(TSTNode node, String filename) {

        if (node == null) {
            return null;
        }
        deleteAllRec((TSTNode) node.rightChild, filename);
        deleteAllRec((TSTNode) node.leftChild, filename);
        deleteAllRec((TSTNode) node.equalChild, filename);
        if (node.isWord) {
            node.data.head = node.data.delete(filename, node.data.head);
            if (!node.data.isEmpty()) {
                return node;
            }
            node.isWord = false;
            node.word = null;
        }
        if (node.rightChild == null && node.leftChild == null && node.equalChild == null) {
            return node = null;
        }

        node.height = Integer.max(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;
        int differ = getHeight(node.leftChild) - getHeight(node.rightChild);
        if (differ > 1) {
            if (getHeight(node.leftChild.leftChild) > getHeight(node.leftChild.rightChild)) {
                node = (TSTNode) LLRotate(node);
            } else {
                node.leftChild = RRRotate(node.leftChild);
                node = (TSTNode) LLRotate(node);
            }

        }
        if (differ < -1) {
            if (getHeight(node.rightChild.rightChild) > getHeight(node.rightChild.leftChild)) {
                node = (TSTNode) RRRotate(node);
                ;
            } else {
                node.rightChild = LLRotate(node.rightChild);
                node = (TSTNode) RRRotate(node);
            }
        }
        return node;
    }

    @Override
    public void deleteAll(String filename) {
        root = deleteAllRec(root, filename);
    }

    public TSTNode search(String word) {
        return searchRec(root, word.toCharArray(), 0);
    }

    private TSTNode searchRec(TSTNode curr, char word[], int counter) {
        if (curr == null) {
            return null;
        }
        if (word[counter] > curr.charOfNode) {
            return searchRec((TSTNode) curr.rightChild, word, counter);
        } else if (word[counter] < curr.charOfNode) {
            return searchRec((TSTNode) curr.leftChild, word, counter);

        } else if (counter < word.length - 1) {
            return searchRec((TSTNode) curr.equalChild, word, ++counter);
        } else if (curr.isWord) {
            return curr;
        }
        return null;
    }

    private StringBuffer printAllRec(TSTNode node, StringBuffer sb,int kind) {
        if (node == null) {
            return sb;
        }
        sb = printAllRec((TSTNode) node.leftChild, sb,kind);
        if (node.isWord) {
            sb = sb.append("<<" + new String(node.word) + ">>").append("\n").append(node.data.toString(kind));
        }
        sb = printAllRec((TSTNode) node.equalChild, sb,kind);
        sb = printAllRec((TSTNode) node.rightChild, sb,kind);
        return sb;
    }

    @Override
    public String toString(int kind) {
        StringBuffer sb = new StringBuffer();
        sb = printAllRec(root, sb,kind);
        return sb.toString();
    }

    @Override
    public void cleanTree() {
        root = null;
    }

    @Override
    public int wordCounter() {
        counter(root);
        int x = numberOfWords;
        numberOfWords = 0;
        return x;
    }

    private void counter(TSTNode currNode) {
        if (currNode == null) {
            return;
        }
        if (currNode.isWord) {
            numberOfWords++;
        }
        counter((TSTNode) currNode.leftChild);
        counter((TSTNode) currNode.rightChild);
        counter((TSTNode) currNode.equalChild);
    }

    @Override
    public int getMaxHeight() {
        return maxHeightRec(root);
    }
    private int maxHeightRec(TSTNode cur) {
        if (cur == null) {
            return 0;
        }
        int x = Math.max(maxHeightRec((TSTNode) cur.rightChild),
                maxHeightRec((TSTNode) cur.leftChild));
        return 1 + Math.max(x, maxHeightRec((TSTNode) cur.equalChild));
    }
}
