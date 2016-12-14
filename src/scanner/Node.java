/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import java.util.ArrayList;

/**
 *
 * @author anwer
 */
public class Node {
    public Node(Node parent, String text) {
        this.value = "";
        this.parent = parent;
        this.childrenCount = 0;
        this.text = text;
        children = new ArrayList<>();
    }
    
    public Node(Node parent, String text, boolean isTerminal) {
        this.value = "";
        this.parent = parent;
        this.childrenCount = 0;
        this.text = text;
        children = new ArrayList<>();
        this.isTerminal = isTerminal;
    }
    
    public Node(Node parent, String text, String val, boolean isTerminal) {
        this.value = val;
        this.parent = parent;
        this.childrenCount = 0;
        this.text = text;
        children = new ArrayList<>();
        this.isTerminal = isTerminal;
    }
    
    @Override
    public String toString() {
        return value.isEmpty() ? text: text + " (" + value + ")";
    }
    
    public void addChild(Node n) {
        children.add(n);
        childrenCount++;
    }
    
    public Node parent() {
        return parent;
    }
    
    public Integer childrenCount() {
        return childrenCount;
    }
    
    public ArrayList<Node> children() {
        return children;
    }
    
    public boolean isTerminal() {
        return isTerminal;
    }
    
    public void setParent(Node n){
        this.parent = n;
    }
    
    private final String value;
    private boolean isTerminal = false;
    private Node parent;
    private final ArrayList<Node> children;
    private Integer childrenCount;
    private final String text;
}
