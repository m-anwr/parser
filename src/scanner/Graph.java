/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import java.awt.Color;
import java.awt.Frame;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.border.EmptyBorder;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;
/**
 *
 * @author anwer
 */
public class Graph extends JDialog {

    /**
     * Creates new form Graph
     * @param root
     * @param parent
     * @param modal
     */
    public Graph(Node root, Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        this.root = root;
        
        graph = new ListenableUndirectedGraph(DefaultEdge.class);
        
        
        m_jgAdapter = new JGraphModelAdapter(graph);
        
        AttributeMap defaultMap = m_jgAdapter.getDefaultVertexAttributes();
        defaultMap.put("bounds", new Rectangle2D.Double(50,50,120,40));
        defaultMap.put("border", new EmptyBorder(0,0,0,0));
        m_jgAdapter.setDefaultEdgeAttributes(defaultMap);
        
        terminalAttrs = new AttributeMap();
        terminalAttrs.putAll(defaultMap);
        terminalAttrs.put("backgroundColor", new Color(23, 151, 103));
        
        
        constructTree(this.root);
        
        int i = 0;
        while(collistionDetected()) {
            positionVertices(
                this.root, 
                CANVAS_WIDTH+(i*300), 
                0, 
                STARTING_DEPTH
            );
            
            i++;
        }
            
        
        JGraph graphicalGraph = new JGraph( m_jgAdapter );
        
        
        jScrollPane1.getViewport().add(graphicalGraph);
        
    }
    
    private void constructTree(Node n) {
        if (n == null)
            return;
        
        graph.addVertex(n);
        
        if (n.parent() != null) 
            graph.addEdge(n.parent(), n, new GraphEdge());
        
        if (n.childrenCount() != 0) {
            for (int i = 0; i < n.childrenCount(); i++) {
                constructTree(n.children().get(i));
            }
        }
    }
    
    private void positionVertices(Node n, double width, double x, double y) {
        if (n == null)
            return;
        
        positionVertexAt(n, ((width + x) + x) / 2, y);
        
        if (n.childrenCount() != 0) {
            for (int i = 0; i < n.childrenCount(); i++) {
                positionVertices(
                    n.children().get(i),
                    width/n.childrenCount(),
                    x + i * width/n.childrenCount(),
                    y + LEVEL_MARGIN_DEPTH
                );
            }
        }
    }
    
    private boolean collistionDetected() {
        
        for (Iterator it1 = graph.vertexSet().iterator(); it1.hasNext();) {
            Node n1 = (Node) it1.next();
            
            Iterator it2;
            for (it2 = graph.vertexSet().iterator(); it2.hasNext();) {
                      
                Node n2 = (Node) it2.next();
                
                if(n1 == n2) continue;

                DefaultGraphCell cell1 = m_jgAdapter.getVertexCell(n1);
                AttributeMap attr1 = cell1.getAttributes();
                Rectangle2D bounds1 = GraphConstants.getBounds(attr1);

                DefaultGraphCell cell2 = m_jgAdapter.getVertexCell(n2);
                AttributeMap attr2 = cell2.getAttributes();
                Rectangle2D bounds2 = GraphConstants.getBounds(attr2);
                        
                if (bounds1.intersects(bounds2))
                    return true;
            }
        }
        
        return false;
    }
    
    private void positionVertexAt( Node vertex, double x, double y ) {
        DefaultGraphCell cell = m_jgAdapter.getVertexCell(vertex);
        AttributeMap attr = cell.getAttributes();
        
        if (vertex.isTerminal())
            attr.putAll(terminalAttrs);
        
        Rectangle2D bounds = GraphConstants.getBounds(attr);

        Rectangle2D newBounds = 
                new Rectangle2D.Double(
                    x - bounds.getWidth()/2,
                    y,
                    bounds.getWidth(),
                    bounds.getHeight()
                );

        GraphConstants.setBounds(attr, newBounds);

        // TODO: Clean up generics once JGraph goes generic
        AttributeMap cellAttr = new AttributeMap();
        cellAttr.put(cell, attr);
        m_jgAdapter.edit(cellAttr, null, null, null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new java.awt.Dimension(800, 600));

        jScrollPane1.setMinimumSize(new java.awt.Dimension(1280, 720));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1280, 720));
        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private final AttributeMap terminalAttrs;
    private final ListenableUndirectedGraph graph;
    private final Node root;
    private final JGraphModelAdapter m_jgAdapter;
    private final double CANVAS_WIDTH = 1280;
    private final double LEVEL_MARGIN_DEPTH = 80;
    private final double STARTING_DEPTH = 20; // initially 20px height margin
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
