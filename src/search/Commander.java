/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;

/**
 *
 * @author parham
 */
public class Commander extends JTextField implements KeyListener {

    Stack q, p;
    String middleText;
    Frame frame;

    public Commander(Frame frame) {
        q = new Stack();
        p = new Stack();
        this.frame = frame;
        addKeyListener(this);
    }
    boolean a;

    public Commander() {
    }
    

    private void up() {
        p.push(middleText);
        middleText = q.pop();
        if (middleText != null) {
            setText(middleText);
        } else {
            q.push(middleText);
            middleText = p.pop();
        }
    }

    private void down() {
        q.push(middleText);
        middleText = p.pop();
        if (middleText != null) {
            setText(middleText);
        } else {
            p.push(middleText);
            middleText = q.pop();
        }
    }

    public void enter() {
        if (getText().equals("")) {
            return;
        }
        frame.console.output.append(" -> ").append(getText()).append("\n");
        frame.console.getCommand(getText());
        q.push(middleText);
        while (!p.isEmpty()) {
            q.push(p.pop());
        }
        q.push(getText());
        middleText = null;
        setText("");
        a = true;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (q.isEmpty()) {
                e.consume();
            }
            up();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            enter();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (p.isEmpty()) {
                e.consume();
            }
            down();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
