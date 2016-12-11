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
        this.parent = parent;
        this.childrenCount = 0;
        this.text = text;
        children = new ArrayList<>();
    }
    
    @Override
    public String toString() {
        return text;
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
    
    private final Node parent;
    private final ArrayList<Node> children;
    private Integer childrenCount;
    private final String text;
}
