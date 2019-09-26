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
public class Stack {

    String s[];
    int top;

    public Stack() {
        s = new String[100];
        top = -1;
    }

    public String pop() {
        if (isEmpty()) {
            return null;
        }
        return s[top--];
    }

    public void push(String x) {
        if (isFull()||x==null) {
            return;
        }
        s[++top] = x;
    }

    public boolean isEmpty() {
        if (top == -1) {
            return true;
        }
        return false;
    }

    public boolean isFull() {
        if (top == 49) {
            return true;
        }
        return false;
    }

    void pop(String text) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
