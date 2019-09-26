/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

/**
 * AVLtree is a self merged base on a BST
 *
 * @author parham
 */
public class BinarySearchTree extends Tree {

    public BSTNode root;

    public BinarySearchTree() {
        numberOfWords = 0;
        root = null;
    }

    private BSTNode addRec(BSTNode newNode, BSTNode current, String filename, int line, int word) {

        if (current == null) {
            return newNode;
        }
        if (equality(newNode, current)) {
            //sample
            current.addNew(filename, line, word);

            return current;
        }
        if (comparer(newNode, current) && !equality(newNode, current)) {
            current.rightChild = addRec(newNode, current.rightChild, filename, line, word);
        } else {
            current.leftChild = addRec(newNode, current.leftChild, filename, line, word);
        }

        current.height = Integer.max(getHeight(current.leftChild), getHeight(current.rightChild)) + 1;
        int differ = getHeight(current.leftChild) - getHeight(current.rightChild);
        if (differ > 1) {
            if (getHeight(current.leftChild.leftChild) > getHeight(current.leftChild.rightChild)) {
                current = LLRotate(current);
            } else {
                current.leftChild = RRRotate(current.leftChild);
                current = LLRotate(current);
            }
        }
        if (differ < -1) {
            if (getHeight(current.rightChild.rightChild) > getHeight(current.rightChild.leftChild)) {
                current = RRRotate(current);
                ;
            } else {
                current.rightChild = LLRotate(current.rightChild);
                current = RRRotate(current);
            }

        }
        return current;

    }

    @Override
    public void add(String newData, String fileName, int line, int word) {

        newData = newData.toLowerCase();
        newData = newData.replaceAll("[^a-zA-Z]+", "");
        if (newData.equals("")) {
            return;
        }

        BSTNode n = new BSTNode(newData);
        root = addRec(n, root, fileName, line, word);
        n.addNew(fileName, line, word);
    }

    private boolean comparer(BSTNode newNode, BSTNode walker) {
        int k = Integer.min(newNode.data.data.length, walker.data.data.length);
        int i = 0;
        for (; i < k; i++) {
            if (newNode.data.data[i] == walker.data.data[i]) {
            } else {
                break;
            }
        }
        boolean a = false;
        if (i == k) {
            i--;
            a = true;
        }
        if (i == k - 1 && (newNode.data.data.length > walker.data.data.length) && a) {
            return true;
        }

        if (i == k - 1 && (newNode.data.data.length < walker.data.data.length && a)) {
            return false;
        }

        if (StaticClass.VALUEOF(newNode.data.data[i]) > StaticClass.VALUEOF(walker.data.data[i])) {
            return true;
        }
        if (StaticClass.VALUEOF(newNode.data.data[i]) < StaticClass.VALUEOF(walker.data.data[i])) {
            return false;
        }

        return false;
    }

    private boolean equality(BSTNode newNode, BSTNode walker) {
        int l = newNode.data.data.length;

        if (newNode.data.data.length != walker.data.data.length) {

            return false;
        }
        for (int i = 0; i < l; i++) {
            if (newNode.data.data[i] != walker.data.data[i]) {
                return false;
            }
        }
        return true;
    }

    public void preorder(BSTNode root) {
        if (root != null) {
            for (int i = 0; i < root.data.data.length; i++) {
                System.out.print(root.data.data[i]);
            }
            System.out.println("");
            preorder(root.leftChild);
            preorder(root.rightChild);
        }
    }

    protected BSTNode LLRotate(BSTNode root) {
        BSTNode newRoot = root.leftChild;
        BSTNode LCRoot = newRoot.rightChild;
        newRoot.rightChild = root;
        root.leftChild = LCRoot;
        root.height = Math.max(getHeight(root.leftChild),
                getHeight(root.rightChild)) + 1;
        newRoot.height = Math.max(getHeight(newRoot.leftChild),
                getHeight(newRoot.rightChild)) + 1;
        return newRoot;
    }

    protected BSTNode RRRotate(BSTNode root) {
        BSTNode newRoot = root.rightChild;
        BSTNode LCRoot = newRoot.leftChild;
        newRoot.leftChild = root;
        root.rightChild = LCRoot;
        root.height = Math.max(getHeight(root.leftChild),
                getHeight(root.rightChild)) + 1;
        newRoot.height = Math.max(getHeight(newRoot.leftChild),
                getHeight(newRoot.rightChild)) + 1;
        return newRoot;

    }

    protected int getHeight(BSTNode a) {
        if (a == null) {
            return 0;
        }
        return a.height;

    }

    public BSTNode search(BSTNode current, BSTNode searched) {
        if (current == null) {
            searched = null;
            return null;
        }

        if (equality(current, searched)) {
            searched = null;
            return current;
        }
        if (comparer(searched, current)) {
            return search(current.rightChild, searched);
        } else {
            return search(current.leftChild, searched);
        }

    }

    public void delete(BSTNode z, String filename) {
        root = deleteRec(root, z, filename);
    }

    @Override
    public void delete(String word, String filename) {
        word = word.toLowerCase();
        word = word.replaceAll("[^a-zA-Z]+", "");
        BSTNode z = new BSTNode(word);
        root = deleteRec(root, z, filename);
    }

    @Override
    public void deleteAll(String filename) {
        //root = preOrderDeleteRrc(root, filename);
        root = deleteAll(root, filename);
    }

    public BSTNode preOrderDeleteRrc(BSTNode s, String filename) {

        if (s == null) {
            return null;
        }

        delete(s.toString(), filename);
        return s;
    }

    public BSTNode findMin(BSTNode node) {
        if (node == null) {
            return null;
        }
        if (node.leftChild == null) {
            return node;
        }
        return findMin(node.leftChild);
    }

    public BSTNode deleteAll(BSTNode current, String f) {
        if (current == null) {
            return null;
        }
        current.leftChild = deleteAll(current.leftChild, f);
        current.rightChild = deleteAll(current.rightChild, f);
        
        if (!current.data.isEmpty()) {
            current.Delete(f);
            if (!current.data.isEmpty()) {
                current.height = Integer.max(getHeight(current.leftChild), getHeight(current.rightChild)) + 1;
                return current;
            }
        }
        if (current.rightChild == null && current.leftChild == null) {
            current = null;
        } else if (current.leftChild == null && current.rightChild != null) {
            current = current.rightChild;

        } else if (current.rightChild == null && current.leftChild != null) {
            current = current.leftChild;
        } else {
            BSTNode temp = findMin(current.rightChild);
            current.data = temp.data;
            current.rightChild = deleteRec(current.rightChild, temp, f);
        }
        if (current == null) {
            return current;
        }

        //update length
        current.height = Integer.max(getHeight(current.leftChild), getHeight(current.rightChild)) + 1;
        int differ = getHeight(current.leftChild) - getHeight(current.rightChild);
        if (differ > 1) {
            if (getHeight(current.leftChild.leftChild) > getHeight(current.leftChild.rightChild)) {
                current = LLRotate(current);
            } else {
                current.leftChild = RRRotate(current.leftChild);
                current = LLRotate(current);
            }

        }
        if (differ < -1) {
            if (getHeight(current.rightChild.rightChild) > getHeight(current.rightChild.leftChild)) {
                current = RRRotate(current);
            } else {
                current.rightChild = LLRotate(current.rightChild);
                current = RRRotate(current);
            }
        }
        return current;
    }

    public BSTNode deleteRec(BSTNode current, BSTNode del, String f) {

        if (current == null) {
            return null;
        }
        if (comparer(current, del) && !equality(del, current)) {
            current.leftChild = deleteRec(current.leftChild, del, f);
        }

        if (!comparer(current, del) && !equality(current, del)) {
            current.rightChild = deleteRec(current.rightChild, del, f);
        } //equal!!
        if (equality(current, del)) {
            if (current.rightChild == null && current.leftChild == null) {
                current = null;
            } else if (current.leftChild == null && current.rightChild != null) {
                current = current.rightChild;

            } else if (current.rightChild == null && current.leftChild != null) {
                current = current.leftChild;
            } else {
                BSTNode temp = findMin(current.rightChild);
                current.data = temp.data;
                current.rightChild = deleteRec(current.rightChild, temp, f);
            }
        }
        if (current == null) {
            return null;
        }

//update length
        current.height = Integer.max(getHeight(current.leftChild), getHeight(current.rightChild)) + 1;
        int differ = getHeight(current.leftChild) - getHeight(current.rightChild);
        if (differ > 1) {
            if (getHeight(current.leftChild.leftChild) > getHeight(current.leftChild.rightChild)) {
                current = LLRotate(current);
            } else {
                current.leftChild = RRRotate(current.leftChild);
                current = LLRotate(current);
            }

        }
        if (differ < -1) {
            if (getHeight(current.rightChild.rightChild) > getHeight(current.rightChild.leftChild)) {
                current = RRRotate(current);
                ;
            } else {
                current.rightChild = LLRotate(current.rightChild);
                current = RRRotate(current);
            }
        }
        return current;
    }

    private StringBuffer printRec(BSTNode node, StringBuffer sb,int kind) {
        if (node == null) {
            return sb;
        }
        sb = printRec(node.leftChild, sb,kind);
        sb.append(node.toString()).append("\n").append(node.data.toString(kind));
        sb = printRec(node.rightChild, sb,kind);
        return sb;
    }

    @Override
    public String toString(int kind) {
        StringBuffer sb = new StringBuffer();
        sb = printRec(root, sb,kind);
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

    private void counter(BSTNode currNode) {
        if (currNode == null) {
            return;
        }
        numberOfWords++;
        counter(currNode.leftChild);
        counter(currNode.rightChild);
    }

    @Override
    public int getMaxHeight() {
        if (root == null) {
            return 0;
        } else {
            return root.height;
        }
    }
}
