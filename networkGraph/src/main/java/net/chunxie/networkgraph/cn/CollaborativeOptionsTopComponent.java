package net.chunxie.networkgraph.cn;

import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.chunxie.networkdata.entity.Contributor;
import net.chunxie.networkgraph.edge.Collaboration;
import net.chunxie.networkgraph.util.ClusterUtil;
import net.chunxie.networkgraph.util.LayoutComboCellRenderer;
import org.apache.commons.collections15.Transformer;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 17:52
 */
@ConvertAsProperties(dtd = "-//net.chunxie.networkgraph.cn//CollaborativeOptions//EN",
        autostore = false)
@TopComponent.Description(preferredID = "CollaborativeOptionsTopComponent",
        iconBase = "net/chunxie/networkgraph/cog.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "net.chunxie.networkgraph.cn.CollaborativeOptionsTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_CollaborativeOptionsAction",
        preferredID = "CollaborativeOptionsTopComponent")
@Messages({
        "CTL_CollaborativeOptionsAction=CollaborativeOptions",
        "CTL_CollaborativeOptionsTopComponent=Graph Options",
        "HINT_CollaborativeOptionsTopComponent=This is a Collaborative network graph options window"
})
public final class CollaborativeOptionsTopComponent extends TopComponent {

    private CollaborativeTopComponent parent = null;
    private VisualizationViewer<Contributor, Collaboration> master = null;
    private VisualizationViewer<Contributor, Collaboration> satellite = null;
    private AggregateLayout<Contributor, Collaboration> globalLayout = null;
    // options for global layout combobox
    private Class[] layoutClasses = new Class[]{
            CircleLayout.class,
            ISOMLayout.class,
            FRLayout.class,
            KKLayout.class,
            SpringLayout.class
    };
    // options for sub layout combobox
    private Class[] subLayoutClasses = new Class[]{
            CircleLayout.class,
            ISOMLayout.class//,
            //KKLayout.class
    };
    // options for sub layout dimension combobox
    private Dimension[] subLayoutDimension = new Dimension[]{
            new Dimension(50, 50),
            new Dimension(75, 75),
            new Dimension(100, 100)
    };
    private Class subLayoutClass = CircleLayout.class;
    private Dimension subLayoutSize = new Dimension(50, 50);
    private Graph<Contributor, Collaboration> graph = null;
    private Map<Collaboration, Paint> clusterEdgePaints = null;
    private Map<Contributor, Paint> clusterVertexPaints = null;
    private Map<Collaboration, Integer> edgeWeightMap = null;
    private Map<Contributor, Double> vertexSizeMap = new HashMap<Contributor, Double>();
    private String edgeRemovedStr = "Edges removed: ";
    private String showVertexLabelsStr = "Hide node labels when node size <= ";
    private String vertexSizeMappingStr = "<html>Node size is mapped to ";
    private ClusterUtil<Contributor, Collaboration> clusterUtil = null;
    private Transformer<Contributor, Paint> vertexPaint = null;
    private Transformer<Collaboration, Paint> edgePaint = null;
    private Transformer<Contributor, Shape> vertexShape = null;
    private Transformer<Contributor, String> vertexTooltipTrans = null;
    private Transformer<Collaboration, String> edgeTooltipTrans = null;
    private Transformer<Contributor, Stroke> vertexStroke = null;
    private Transformer<Collaboration, Stroke> edgeStroke = null;
    private final Integer EDGE_REMOVED_MIN = 0;
    private final Integer VERTEX_DIAMETER_MIN = 8;
    private final Integer VERTEX_DIAMETER_MAX = 58;
    private DateFormat df = new SimpleDateFormat("dd/MM/yy");

    public CollaborativeOptionsTopComponent() {
        initComponents();
        setName(Bundle.CTL_CollaborativeOptionsTopComponent());
        setToolTipText(Bundle.HINT_CollaborativeOptionsTopComponent());
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);

    }

    public CollaborativeOptionsTopComponent(CollaborativeTopComponent cnComponent) {
        this();
        this.parent = cnComponent;
        initGraph();
        initEdgeWeightMap();
        initVertexShape();
        initVertexAndEdgeStroke();
        initVertexAndEdgeDrawPaint();
        initVertexAndEdgeTooltips();
        initVertexLabel();
        initLayout();
        initCluster();
        initPreRender();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        edgeRemovedLabel = new javax.swing.JLabel();
        enableClusterCheckBox = new javax.swing.JCheckBox();
        groupClusterCheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        globalLayoutComboBox = new JComboBox(layoutClasses);
        jLabel2 = new javax.swing.JLabel();
        subLayoutLabel = new javax.swing.JLabel();
        subLayoutComboBox = new JComboBox(subLayoutClasses);
        subLayoutDimensionLabel = new javax.swing.JLabel();
        subLayoutDimensionComboBox = new JComboBox(subLayoutDimension);
        globalLayoutLabel = new javax.swing.JLabel();
        edgeRemovedSlider = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        showVertexLabelsLabel = new javax.swing.JLabel();
        showVertexLabelsSlider = new javax.swing.JSlider();
        vertexSizeMappingLabel = new javax.swing.JLabel();
        vertexSizeMappingComboBox = new javax.swing.JComboBox();
        showNetworkInfoCheckBox = new javax.swing.JCheckBox();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(CollaborativeOptionsTopComponent.class, "CollaborativeOptionsTopComponent.jPanel1.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(edgeRemovedLabel, edgeRemovedStr);
        edgeRemovedLabel.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(enableClusterCheckBox, org.openide.util.NbBundle.getMessage(CollaborativeOptionsTopComponent.class, "CollaborativeOptionsTopComponent.enableClusterCheckBox.text")); // NOI18N
        enableClusterCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableClusterCheckBoxActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(groupClusterCheckBox, org.openide.util.NbBundle.getMessage(CollaborativeOptionsTopComponent.class, "CollaborativeOptionsTopComponent.groupClusterCheckBox.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(CollaborativeOptionsTopComponent.class, "CollaborativeOptionsTopComponent.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(CollaborativeOptionsTopComponent.class, "CollaborativeOptionsTopComponent.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(subLayoutLabel, org.openide.util.NbBundle.getMessage(CollaborativeOptionsTopComponent.class, "CollaborativeOptionsTopComponent.subLayoutLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(subLayoutDimensionLabel, org.openide.util.NbBundle.getMessage(CollaborativeOptionsTopComponent.class, "CollaborativeOptionsTopComponent.subLayoutDimensionLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(globalLayoutLabel, org.openide.util.NbBundle.getMessage(CollaborativeOptionsTopComponent.class, "CollaborativeOptionsTopComponent.globalLayoutLabel.text")); // NOI18N

        edgeRemovedSlider.setPaintLabels(true);
        edgeRemovedSlider.setPaintTicks(true);
        edgeRemovedSlider.setValue(0);
        edgeRemovedSlider.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(CollaborativeOptionsTopComponent.class, "CollaborativeOptionsTopComponent.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(38, 38, 38)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(globalLayoutLabel)
                                                        .addComponent(subLayoutLabel)
                                                        .addComponent(subLayoutDimensionLabel))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(globalLayoutComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(subLayoutComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(subLayoutDimensionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(groupClusterCheckBox))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(enableClusterCheckBox))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(edgeRemovedLabel))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(edgeRemovedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel3)))
                                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(globalLayoutLabel)
                                        .addComponent(globalLayoutComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(enableClusterCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(groupClusterCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(subLayoutLabel)
                                        .addComponent(subLayoutComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(subLayoutDimensionLabel)
                                        .addComponent(subLayoutDimensionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(edgeRemovedLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edgeRemovedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(CollaborativeOptionsTopComponent.class, "CollaborativeOptionsTopComponent.jPanel2.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(showVertexLabelsLabel, showVertexLabelsStr + VERTEX_DIAMETER_MAX);

        showVertexLabelsSlider.setMaximum(VERTEX_DIAMETER_MAX);
        showVertexLabelsSlider.setMinimum(VERTEX_DIAMETER_MIN);
        showVertexLabelsSlider.setMinorTickSpacing(2);
        showVertexLabelsSlider.setPaintLabels(true);
        showVertexLabelsSlider.setPaintTicks(true);
        showVertexLabelsSlider.setValue(VERTEX_DIAMETER_MAX);

        org.openide.awt.Mnemonics.setLocalizedText(vertexSizeMappingLabel, vertexSizeMappingStr);

        vertexSizeMappingComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Awarded Points", "Degree Centrality", "Betweenness Centrality", "Closeness Centrality" }));
        vertexSizeMappingComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                vertexSizeMappingComboBoxItemStateChanged(evt);
            }
        });

        showNetworkInfoCheckBox.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(showNetworkInfoCheckBox, org.openide.util.NbBundle.getMessage(CollaborativeOptionsTopComponent.class, "CollaborativeOptionsTopComponent.showNetworkInfoCheckBox.text")); // NOI18N
        showNetworkInfoCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showNetworkInfoCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(showNetworkInfoCheckBox)
                                        .addComponent(showVertexLabelsLabel)
                                        .addComponent(showVertexLabelsSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(vertexSizeMappingLabel)
                                        .addComponent(vertexSizeMappingComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(showVertexLabelsLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(showVertexLabelsSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vertexSizeMappingLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vertexSizeMappingComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(showNetworkInfoCheckBox)
                                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }// </editor-fold>

    private void enableClusterCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (enableClusterCheckBox.isSelected()) {
            enableClusterFunc();
        } else {
            disableClusterFunc();
        }
    }

    private void vertexSizeMappingComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {
        master.repaint();
    }

    private void showNetworkInfoCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        master.repaint();
    }
    // Variables declaration - do not modify
    private javax.swing.JLabel edgeRemovedLabel;
    private javax.swing.JSlider edgeRemovedSlider;
    private javax.swing.JCheckBox enableClusterCheckBox;
    private javax.swing.JComboBox globalLayoutComboBox;
    private javax.swing.JLabel globalLayoutLabel;
    private javax.swing.JCheckBox groupClusterCheckBox;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JCheckBox showNetworkInfoCheckBox;
    private javax.swing.JLabel showVertexLabelsLabel;
    private javax.swing.JSlider showVertexLabelsSlider;
    private javax.swing.JComboBox subLayoutComboBox;
    private javax.swing.JComboBox subLayoutDimensionComboBox;
    private javax.swing.JLabel subLayoutDimensionLabel;
    private javax.swing.JLabel subLayoutLabel;
    private javax.swing.JComboBox vertexSizeMappingComboBox;
    private javax.swing.JLabel vertexSizeMappingLabel;
    // End of variables declaration

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    private void initGraph() {
        master = parent.master;
        satellite = parent.satellite;
        globalLayout = parent.graphLayout;
        graph = globalLayout.getGraph();

        clusterEdgePaints = new HashMap<Collaboration, Paint>();
        clusterVertexPaints = new HashMap<Contributor, Paint>();
    }

    private void initCluster() {
        clusterUtil = new ClusterUtil<Contributor, Collaboration>(clusterVertexPaints, clusterEdgePaints, master);
        Integer edgeCount = graph.getEdgeCount();
        edgeRemovedSlider.setMaximum(edgeCount);
        //Create the edgeRemovedSlider label table
        Hashtable edgeRemovedSliderLabelTable = new Hashtable();
        edgeRemovedSliderLabelTable.put(EDGE_REMOVED_MIN, new JLabel(EDGE_REMOVED_MIN.toString()));
        edgeRemovedSliderLabelTable.put(edgeCount, new JLabel(edgeCount.toString()));
        edgeRemovedSlider.setLabelTable(edgeRemovedSliderLabelTable);
        edgeRemovedSlider.setMinorTickSpacing(edgeCount);
        edgeRemovedSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (enableClusterCheckBox.isSelected()) {
                    JSlider source = (JSlider) e.getSource();
                    if (!source.getValueIsAdjusting()) {
                        int minEdgeRemoved = source.getValue();
                        edgeRemovedLabel.setText(edgeRemovedStr + minEdgeRemoved);
                        clusterUtil.clusterAndRecolor(globalLayout, subLayoutClass, subLayoutSize, minEdgeRemoved, edgeWeightMap, groupClusterCheckBox.isSelected());
                        master.repaint();
                    }
                }
            }
        });

        groupClusterCheckBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (enableClusterCheckBox.isSelected()) {
                    clusterUtil.clusterAndRecolor(globalLayout, subLayoutClass, subLayoutSize, edgeRemovedSlider.getValue(), edgeWeightMap, e.getStateChange() == ItemEvent.SELECTED);
                    master.repaint();
                }
            }
        });

        subLayoutComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                subLayoutClass = (Class) e.getItem();
                if (enableClusterCheckBox.isSelected() && groupClusterCheckBox.isSelected()) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        clusterUtil.clusterAndRecolor(globalLayout, subLayoutClass, subLayoutSize, edgeRemovedSlider.getValue(), edgeWeightMap, groupClusterCheckBox.isSelected());
                        master.repaint();
                    }
                }
            }
        });

        subLayoutDimensionComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                subLayoutSize = (Dimension) e.getItem();
                if (enableClusterCheckBox.isSelected() && groupClusterCheckBox.isSelected()) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        clusterUtil.clusterAndRecolor(globalLayout, subLayoutClass, subLayoutSize, edgeRemovedSlider.getValue(), edgeWeightMap, groupClusterCheckBox.isSelected());
                        master.repaint();
                    }
                }
            }
        });
    }

    private void enableClusterFunc() {
        // if allowing end users to change global layout when cluster feature is on, it may cause StackOverflow exception
        globalLayoutComboBox.setEnabled(false);
        globalLayoutLabel.setEnabled(false);

        edgeRemovedSlider.setEnabled(true);
        edgeRemovedLabel.setEnabled(true);
        clusterUtil.clusterAndRecolor(globalLayout, subLayoutClass, subLayoutSize, edgeRemovedSlider.getValue(), edgeWeightMap, groupClusterCheckBox.isSelected());

        // Tell the renderer to use our own customized color rendering
        master.repaint();
    }

    private void disableClusterFunc() {
        globalLayoutComboBox.setEnabled(true);
        globalLayoutLabel.setEnabled(true);

        edgeRemovedSlider.setEnabled(false);

        edgeRemovedLabel.setText(edgeRemovedStr);
        edgeRemovedLabel.setEnabled(false);
        master.repaint();
    }

    private void initEdgeWeightMap() {
        edgeWeightMap = new HashMap<Collaboration, Integer>();
        for (Collaboration edge : graph.getEdges()) {
            edgeWeightMap.put(edge, edge.getAmount());
        }
    }

    private void initVertexAndEdgeDrawPaint() {
        vertexPaint = new Transformer<Contributor, Paint>() {

            @Override
            public Paint transform(Contributor node) {
                Paint result = null;
                PickedState<Contributor> pickedState = master.getPickedVertexState();
                if (pickedState.isPicked(node)) {
                    return Color.YELLOW;
                }
                for (Contributor w : graph.getNeighbors(node)) {
                    if (pickedState.isPicked(w)) {
                        return Color.RED;
                    }
                }
                if (enableClusterCheckBox.isSelected()) {
                    result = clusterVertexPaints.get(node);
                }
                if (result == null) {
                    return Color.WHITE;
                }
                return result;
            }
        };

        edgePaint = new Transformer<Collaboration, Paint>() {

            @Override
            public Paint transform(Collaboration edge) {
                Paint result = null;
                Pair<Contributor> pair = graph.getEndpoints(edge);
                Contributor first = pair.getFirst();
                Contributor second = pair.getSecond();
                PickedState<Contributor> pickedState = master.getPickedVertexState();
                if (pickedState.isPicked(first) || pickedState.isPicked(second)) {
                    return Color.RED;
                }
                if (enableClusterCheckBox.isSelected()) {
                    result = clusterEdgePaints.get(edge);
                }
                if (result == null) {
                    return Color.LIGHT_GRAY;
                }
                return result;
            }
        };

        master.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        master.getRenderContext().setEdgeDrawPaintTransformer(edgePaint);

        satellite.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        satellite.getRenderContext().setEdgeDrawPaintTransformer(edgePaint);
    }

    private void initVertexShape() {
        final Integer maxTotalPoint = parent.graphUtil.getMaxPartialPoint();
        final Integer maxDegree = parent.graphUtil.getMaxDegree();
        final Double maxBetweenness = parent.graphUtil.getMaxBetweennessCentrality();
        final Double maxCloseness = parent.graphUtil.getMaxClosenessCentrality();


        vertexShape = new Transformer<Contributor, Shape>() {

            @Override
            public Shape transform(Contributor vertex) {
                Double value = 0d;
                Double max = 0d;
                if (vertexSizeMappingComboBox.getSelectedIndex() == 0) {
                    value = vertex.getPartialPoints().doubleValue();
                    max = maxTotalPoint.doubleValue();
                } else if (vertexSizeMappingComboBox.getSelectedIndex() == 1) {
                    value = new Double(graph.degree(vertex));
                    max = maxDegree.doubleValue();
                } else if (vertexSizeMappingComboBox.getSelectedIndex() == 2) {
                    value = parent.graphUtil.getBc().getVertexScore(vertex).doubleValue();
                    max = maxBetweenness;
                } else if (vertexSizeMappingComboBox.getSelectedIndex() == 3) {
                    value = parent.graphUtil.getCc().getVertexScore(vertex).doubleValue();
                    max = maxCloseness;
                }
                Double vertexSize = VERTEX_DIAMETER_MIN.doubleValue();
                if (max != 0d) {
                    vertexSize = VERTEX_DIAMETER_MIN + (VERTEX_DIAMETER_MAX - VERTEX_DIAMETER_MIN) * value / max;
                    vertexSizeMap.put(vertex, vertexSize);
                }
                return new Ellipse2D.Double(-vertexSize / 2, -vertexSize / 2, vertexSize, vertexSize);
            }
        };

        master.getRenderContext().setVertexShapeTransformer(vertexShape);
        satellite.getRenderContext().setVertexShapeTransformer(vertexShape);
    }

    private void initVertexAndEdgeTooltips() {
        final DecimalFormat df = new DecimalFormat("0.00#");
        vertexTooltipTrans = new Transformer<Contributor, String>() {

            @Override
            public String transform(Contributor vertex) {
                return "<html><table><th>collaborator</th><tr><td><i>id</i></td><td>"
                        + vertex.getContributor()
                        + "</td></tr><tr><td><i>awarded points</i></td><td>"
                        + vertex.getPartialPoints()
                        + "</td></tr><tr><td><i>degree centrality</i></td><td>"
                        + graph.degree(vertex)
                        + "</td></tr><tr><td><i>betweenness centrality</i></td><td>"
                        + df.format(parent.graphUtil.getBc().getVertexScore(vertex))
                        + "</td></tr><tr><td><i>closeness centrality</i></td><td>"
                        + df.format(parent.graphUtil.getCc().getVertexScore(vertex))
                        + "</td></tr></table></html>";
            }
        };

        edgeTooltipTrans = new Transformer<Collaboration, String>() {

            @Override
            public String transform(Collaboration edge) {
                return "<html><table><th>Collaboration</th><tr><td><i>collaborator A</i></td><td>"
                        + edge.getContributorA()
                        + "</td></tr><tr><td><i>collaborator B</i></td><td>"
                        + edge.getContributorB()
                        + "</td></tr><tr><td><i>number of collaborations</i></td><td>"
                        + edge.getAmount()
                        + "</td></tr></table></html>";
            }
        };

        master.setVertexToolTipTransformer(vertexTooltipTrans);
        master.setEdgeToolTipTransformer(edgeTooltipTrans);
    }

    private void initVertexAndEdgeStroke() {
        final Integer maxWeight = getMaxEdgeWeight();
        vertexStroke = new Transformer<Contributor, Stroke>() {

            @Override
            public Stroke transform(Contributor node) {
                PickedState<Contributor> pickedState = master.getPickedVertexState();
                Integer value = 1;
                if (pickedState.isPicked(node)) {
                    value = 3;
                } else {
                    for (Contributor w : graph.getNeighbors(node)) {
                        if (pickedState.isPicked(w)) {
                            value = 2;
                        } else {
                            value = 1;
                        }
                    }
                }

                return new BasicStroke(value);
            }
        };

        edgeStroke = new Transformer<Collaboration, Stroke>() {

            @Override
            public Stroke transform(Collaboration i) {
                Integer weight = i.getAmount();
                Integer stroke = 1;
                if (maxWeight != 0) {
                    stroke = 1 + 4 * weight / maxWeight;
                }
                return new BasicStroke(stroke);
            }
        };

        master.getRenderContext().setVertexStrokeTransformer(vertexStroke);
        master.getRenderContext().setEdgeStrokeTransformer(edgeStroke);

        satellite.getRenderContext().setVertexStrokeTransformer(vertexStroke);
        satellite.getRenderContext().setEdgeStrokeTransformer(edgeStroke);
    }

    private Integer getMaxEdgeWeight() {
        Integer max = 0;
        for (Integer weight : edgeWeightMap.values()) {
            if (weight > max) {
                max = weight;
            }
        }
        return max;
    }

    private void initVertexLabel() {
        master.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        master.getRenderContext().setVertexLabelTransformer(new Transformer<Contributor, String>() {

            @Override
            public String transform(Contributor vertex) {
                String result = "";
                if (vertexSizeMap.get(vertex) > showVertexLabelsSlider.getValue()) {
                    result = "<html><center>" + vertex.getContributor();
                }
                return result;
            }
        });

        //Create the showVertexLabelsSlider label table
        Hashtable showVertexLabelsSliderLabelTable = new Hashtable();
        showVertexLabelsSliderLabelTable.put(VERTEX_DIAMETER_MIN, new JLabel(VERTEX_DIAMETER_MIN.toString()));
        showVertexLabelsSliderLabelTable.put(VERTEX_DIAMETER_MAX, new JLabel(VERTEX_DIAMETER_MAX.toString()));
        showVertexLabelsSlider.setLabelTable(showVertexLabelsSliderLabelTable);
        showVertexLabelsSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int minDiameter = source.getValue();
                    showVertexLabelsLabel.setText(showVertexLabelsStr + minDiameter);
                    master.repaint();
                }
            }
        });
    }

    private void initLayout() {
        globalLayoutComboBox.setRenderer(new LayoutComboCellRenderer());
        globalLayoutComboBox.setSelectedItem(FRLayout.class);
        globalLayoutComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Class clazz = (Class) e.getItem();
                    Layout<Contributor, Collaboration> layout = clusterUtil.getLayoutFor(clazz, graph);
                    globalLayout.setDelegate(layout);
                    globalLayout.removeAll();
                    master.setGraphLayout(globalLayout);
                    master.getRenderContext().getMultiLayerTransformer().setToIdentity();

                    satellite.getRenderContext().getMultiLayerTransformer().setToIdentity();
                    satellite.scaleToLayout(new CrossoverScalingControl());
                }
            }
        });

        subLayoutComboBox.setRenderer(new LayoutComboCellRenderer());
        subLayoutDimensionComboBox.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String valueString = value.toString();
                valueString = valueString.substring(valueString.lastIndexOf('['));
                valueString = valueString.replaceAll("idth", "");
                valueString = valueString.replaceAll("eight", "");
                return super.getListCellRendererComponent(list, valueString, index, isSelected,
                        cellHasFocus);
            }
        });
    }

    private void initPreRender() {
        master.addPreRenderPaintable(new VisualizationViewer.Paintable() {

            String STR_LINE_01 = "Collaborative network";
            String STR_LINE_02 = "Forum: " + parent.currentSnapshot.getForum().getForumtitle();
            String STR_LINE_03 = "Start Date: " + df.format(parent.currentSnapshot.getStartCal().getTime());
            String STR_LINE_04 = "End Date: " + df.format(parent.currentSnapshot.getEndCal().getTime());
            String STR_LINE_05 = "Node (Collaborators): " + graph.getVertexCount();
            String STR_LINE_06 = "Edge (Collaborations): " + graph.getEdgeCount();

            @Override
            public void paint(Graphics g) {
                if (showNetworkInfoCheckBox.isSelected()) {
                    String STR_LINE_07 = "Clustring: " + (enableClusterCheckBox.isSelected() ? "ON" : "OFF");
                    String STR_LINE_08 = "Node Size: " + vertexSizeMappingComboBox.getSelectedItem();
                    int x = 20;
                    int y = 260;
                    Color oldColor = g.getColor();
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawString(STR_LINE_01, x, y - 15);
                    g.drawString(STR_LINE_02, x, y);
                    g.drawString(STR_LINE_03, x, y + 15);
                    g.drawString(STR_LINE_04, x, y + 30);
                    g.drawString(STR_LINE_05, x, y + 60);
                    g.drawString(STR_LINE_06, x, y + 75);
                    g.drawString(STR_LINE_07, x, y + 90);
                    g.drawString(STR_LINE_08, x, y + 105);
                    g.setColor(oldColor);
                }

            }

            @Override
            public boolean useTransform() {
                return false;
            }
        });
    }
}