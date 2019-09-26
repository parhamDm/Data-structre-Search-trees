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
public class HashTable extends Tree {

    int numberOFWords;
    NodeOFArray[] hashTable;
    private final int hashSize;

    public HashTable(int size) {
        if (size < 10) {
            throw new UnsupportedOperationException("lenth is too short!!!!");
        }
        numberOFWords = 0;
        hashSize = size;
        hashTable = new NodeOFArray[hashSize];
    }

    private int hashCode(String str) {
        double hash = 7;
        for (int i = 0; i < str.length(); i++) {
            hash = hash * 31 + (str.charAt(i) - 'a');
        }
        return ((int) hash % hashSize);

    }

    public NodeOFArray add(String newData, String fileName, int line, int word, NodeOFArray hash) {
        if (hash == null) {
            numberOFWords++;
            return hash = new NodeOFArray(newData, fileName, line, word);
        }
        if (new String(hash.data.data).equals(newData)) {
            hash.data.add(fileName, line, word);
            return hash;
        }
        hash.link = add(newData, fileName, line, word, hash.link);
        return hash;
    }

    @Override
    public void add(String newData, String fileName, int line, int word) {
        if (newData.equals("")) {
            return;
        }
        int hashCode = hashCode(newData);
        hashTable[hashCode] = add(newData, fileName, line, word, hashTable[hashCode]);
    }

    @Override
    public void delete(String word, String Filename) {
        int hashCode = hashCode(word);
        hashTable[hashCode] = deleteRec(word, Filename, hashTable[hashCode]);
    }

    @Override
    public void deleteAll(String filename) {
        for (int i = 0; i < hashSize; i++) {
            hashTable[i] = deleteAllRec(filename, hashTable[i]);
        }
    }

    private NodeOFArray deleteAllRec(String filename, NodeOFArray noa) {
        if (noa == null) {
            return null;
        }
        noa.data.head = noa.data.delete(filename, noa.data.head);
        if (noa.data.isEmpty()) {
            noa = noa.link;
            numberOFWords--;
            noa = deleteAllRec(filename, noa);
        } else {
            noa.link = deleteAllRec(filename, noa.link);
        }
        return noa;
    }

    private NodeOFArray searchRec(NodeOFArray noa, String word) {
        if (noa == null) {
            return null;
        }
        if ((new String(noa.data.data)).equals(word)) {
            return noa;
        } else {
            return searchRec(noa.link, word);
        }
    }

    public NodeOFArray search(String word) {
        return searchRec(hashTable[hashCode(word)], word);
    }

    @Override
    public void cleanTree() {
        numberOFWords=0;
        for (int i = 0; i < hashSize; i++) {
            hashTable[i] = null;
        }
    }

    @Override
    public int wordCounter() {
        return numberOFWords;
    }

    @Override
    public int getMaxHeight() {
        return 0;
    }

    private NodeOFArray deleteRec(String word, String fileName, NodeOFArray hash) {
        if (hash == null) {
            return null;
        }
        if (new String(hash.data.data).equals(word)) {
            hash.data.head = hash.data.delete(fileName, hash.data.head);
            if (hash.data.isEmpty()) {
                hash = hash.link;
            }
            return hash;
        }
        hash.link = deleteRec(word, fileName, hash.link);
        return hash;
    }

    public class NodeOFArray {

        TreeData data;
        NodeOFArray link;

        public NodeOFArray(String newData, String fileName, int line, int word) {
            data = new TreeData(newData.toCharArray());
            data.add(fileName, line, word);
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    @Override
    public String toString(int kind) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < hashSize; i++) {
            if (hashTable[i] == null) {
                continue;
            }
            NodeOFArray noa = hashTable[i];
            while (noa != null) {
                s.append(new String(noa.data.data)).append("\n").append(noa.data.toString(kind));
                noa = noa.link;
            }
        }
        return s.toString();
    }
}
