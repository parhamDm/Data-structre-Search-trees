/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.*;
import javax.swing.*;

/**
 *
 * @author parham
 */
public class Frame extends JFrame implements ActionListener {

    public String currentPath = "";
    public Tree trees[];
    public final ArrayList<String> files = new ArrayList<>();
    public TrieTree stopWords;
    public final Console console;

    private final JLabel chooseFile = new JLabel("please choose your directory:");
    private final JLabel enterCommand = new JLabel("please enter your command here:");
    private ButtonGroup treeSelector;
    private final BinarySearchTree bst;
    private final TSTTree tst;
    private final TrieTree trie;
    private final HashTable ht;
    private final JButton built = new JButton("built");
    private final JButton reset = new JButton("clear history");
    private final JButton browse = new JButton("browse");
    private final JButton help = new JButton("help");
    private final JButton exit = new JButton("exit");
    private JPanel fileChooserPanel;
    private final JTextField fileLocation = new JTextField();
    private JScrollPane textBox;
    private final Commander commands = new Commander(this);

    Frame() {
        stopWordTreeMaker();
        bst = new BinarySearchTree();
        trie = new TrieTree();
        tst = new TSTTree();
        ht = new HashTable(1001);
        trees = new Tree[4];
        trees[0] = tst;
        trees[1] = bst;
        trees[2] = trie;
        trees[3] = ht;
        console = new Console(this);
        setSize(505, 710);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setTitle("search trees");
        construct();
        actionListenerAdder();
    }

    private void construct() {
        setLayout(null);
        setLocationRelativeTo(null);
        built.setSize(110, 40);
        built.setLocation(10, 600);
        built.addActionListener(this);
        built.setFont(new Font("arial", Font.TRUETYPE_FONT, 18));
        reset.setSize(110, 40);
        reset.setLocation(130, 600);
        reset.addActionListener(this);
        reset.setFont(new Font("arial", Font.BOLD, 12));

        help.setLocation(250, 600);
        help.setSize(110, 40);
        help.addActionListener(this);
        help.setFont(new Font("arial", Font.TRUETYPE_FONT, 18));

        exit.setBounds(370, 600, 110, 40);
        exit.addActionListener(this);
        exit.setFont(new Font("arial", Font.TRUETYPE_FONT, 18));

        add(exit);
        add(help);
        add(built);
        add(reset);
        commands.setSize(470, 40);
        commands.setLocation(10, 550);
        add(commands);
        browse.addActionListener(this);
        browse.setFont(new Font("arial", Font.TRUETYPE_FONT, 18));
        browse.setSize(100, 40);
        browse.setLocation(370, 0);
        fileLocation.setSize(350, 40);
        fileLocation.setLocation(0, 0);
        fileChooserPanel = new JPanel(null);
        fileChooserPanel.add(browse);
        fileChooserPanel.add(fileLocation);
        fileChooserPanel.setSize(500, 50);
        fileChooserPanel.setLocation(10, 50);
        fileLocation.setFont(new Font("arial", Font.CENTER_BASELINE, 16));
        fileLocation.setMargin(new Insets(5, 5, 5, 10));
        add(fileChooserPanel);
        fileLocation.setText("C:\\all");
        textBox = new JScrollPane(console);
        textBox.setSize(470, 400);
        console.setMargin(new Insets(10, 10, 10, 10));
        textBox.setLocation(10, 100);
        add(textBox);
        console.setFont(new Font("monospaced", Font.PLAIN, 14));
        textBox.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        console.setLineWrap(true);
        chooseFile.setFont(new Font("arial", Font.BOLD, 14));
        chooseFile.setBounds(10, 10, 500, 40);
        add(chooseFile);

        treeSelector = new ButtonGroup();
        treeSelector.add(BST);
        treeSelector.add(TST);
        treeSelector.add(tt);
        treeSelector.add(hash);
        treeSelector.setSelected(BST.getModel(), rootPaneCheckingEnabled);
        BST.setBounds(310, 500, 50, 50);
        add(BST);
        hash.setBounds(250, 500, 60, 50);
        add(hash);
        TST.setBounds(370, 500, 50, 50);
        add(TST);
        tt.setBounds(430, 500, 50, 50);
        add(tt);
        this.enterCommand.setBounds(10, 500, 280, 50);
        enterCommand.setFont(new Font("arial", Font.BOLD, 14));
        add(enterCommand);
        repaint();
    }

    private void actionListenerAdder() {
        BST.addActionListener((ActionEvent e) -> {
            console.changedTreeType = Console.BST;
        });
        TST.addActionListener((ActionEvent e) -> {
            console.changedTreeType = Console.TST;
        });
        tt.addActionListener((ActionEvent e) -> {
            console.changedTreeType = Console.TRIE;
        });
        hash.addActionListener((ActionEvent e) -> {
            console.changedTreeType = Console.HASH;
        });
        console.changedTreeType = 1;
    }

    private final JRadioButton BST = new JRadioButton("BST");
    private final JRadioButton TST = new JRadioButton("TST");
    private final JRadioButton tt = new JRadioButton("TRIE");
    private final JRadioButton hash = new JRadioButton("Hash");

    private void stopWordTreeMaker() {
        stopWords = new TrieTree();
        File f = new File("StopWords.txt");
        
        Scanner s = null;
        try {
            s = new Scanner(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (s.hasNext()) {
            stopWords.add(s.next(), "a", 0, 0);
        }
        s.close();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == browse) {
            JFileChooser browse = new JFileChooser();
            browse.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            browse.showOpenDialog(this);
            if (browse.getSelectedFile() != null) {
                fileLocation.setText(browse.getSelectedFile().toString());
            }
        }

        if (e.getSource() == built) {
            console.append("adding files plz wait...\n");
            int reply = JOptionPane.showConfirmDialog(this,
                    "are you sure to rebuilt trees?"
                    + " this will clean current trees",
                    "warning", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.NO_OPTION) {
                console.setText(console.output);
                return;
            }
            long timeCalc = System.currentTimeMillis();
            System.err.println(System.currentTimeMillis());
            for (Tree t : trees) {
                t.cleanTree();
            }
            files.clear();
            console.treeType = console.changedTreeType;
            currentPath = "";
            Reader r = new Reader(files, stopWords, console);
            currentPath = fileLocation.getText();
            r.readDirectory(fileLocation.getText(), trees[console.treeType]);
            console.output.append("adding done in ")
                    .append(System.currentTimeMillis() - timeCalc)
                    .append(" miliseconds\n");
            console.setText(console.output);
            console.treeStatus();
            System.out.println(console.treeType);
        }
        if (e.getSource() == reset) {
            console.output = new StringBuffer();
            console.setText(console.output);
        }
        if (e.getSource() == help) {
            JOptionPane.showMessageDialog(this, helpText, "HELP", JOptionPane.NO_OPTION);
        }
        if (e.getSource() == exit) {
            System.out.println(console.treeStatus());
            System.exit(0);
        }
    }

    static final String helpText = "<html> <font size=\"5\">"
            + "when you choosed your directory, click built to build your trees!<br/>"
            + "type <code>list -f</code> to get a full list of all files"
            + " added from directory<br/>"
            + "type <code>list -w</code> to get a full list of words exists"
            + " on selected tree<br/>"
            + "type <code> add 'selecedfile'</code> to add file which 'selected file' is your file<br/>"
            + "type <code> delete 'selecedfile'</code> to delete a file from current directory<br/>"
            + "type <code> update 'selecedfile'</code> to updae file in directory<br/>"
            + "type type <code>search 'anything'</code> to search anything you want<br/>"
            + "it can be a word, a sentence, a paragraph or whatever you want </br>"
            + "for example <code>search poor man</code></br>"
            + "</font></html>";

    public static void main(String[] args) {
        new Frame();
        HashTable a = new HashTable(101);
        a.add("as", "a1", 1, 1);
        a.add("asq", "a2", 1, 1);
        a.add("asq", "a99", 1, 1);
        a.add("asqw", "a6", 4, 1);
        a.add("asqw", "a2", 1, 1);
        a.add("asqw", "a4", 4, 1);
        a.add("asqw", "a8", 4, 1);
        //System.out.println(a.toString());
        System.out.println(a.search("as").data);
    }
}
