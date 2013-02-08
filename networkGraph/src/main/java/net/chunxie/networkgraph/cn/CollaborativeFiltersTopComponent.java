package net.chunxie.networkgraph.cn;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.text.DecimalFormat;
import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.chunxie.networkdata.entity.Contributor;
import net.chunxie.networkgraph.edge.Collaboration;
import org.apache.commons.collections15.Predicate;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 17:47
 */
@ConvertAsProperties(dtd = "-//net.chunxie.networkgraph.cn//CollaborativeFilters//EN",
        autostore = false)
@TopComponent.Description(preferredID = "CollaborativeFiltersTopComponent",
        iconBase = "net/chunxie/networkgraph/filter.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "net.chunxie.networkgraph.cn.CollaborativeFiltersTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_CollaborativeFiltersAction",
        preferredID = "CollaborativeFiltersTopComponent")
@Messages({
        "CTL_CollaborativeFiltersAction=CollaborativeFilters",
        "CTL_CollaborativeFiltersTopComponent=Dynamic Filters",
        "HINT_CollaborativeFiltersTopComponent=This is a Collaborative network dynamic filters window"
})
public final class CollaborativeFiltersTopComponent extends TopComponent {

    private CollaborativeTopComponent parent = null;
    private VisualizationViewer<Contributor, Collaboration> master = null;
    private VisualizationViewer<Contributor, Collaboration> satellite = null;
    private Predicate<Context<Graph<Contributor, Collaboration>, Contributor>> vertexPredicate = null;
    private Predicate<Context<Graph<Contributor, Collaboration>, Collaboration>> edgePredicate = null;
    private Hashtable sliderLabelTable = null;
    private String totalPointStr = "Show nodes whose awarded points >= ";
    private String degreeStr = "Show nodes whose degree centrality >= ";
    private String bcStr = "Show nodes whose betweenness ventrality >= ";
    private String ccStr = "Show nodes whose closeness centrality >= ";
    private String collaborationNumStr = "Show edges whose number of collaborations >= ";
    private final Double METRICS_SCALAR = 1000d;

    private DecimalFormat df = new DecimalFormat("0.00#");

    public CollaborativeFiltersTopComponent() {
        initComponents();
        setName(Bundle.CTL_CollaborativeFiltersTopComponent());
        setToolTipText(Bundle.HINT_CollaborativeFiltersTopComponent());
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);
    }

    public CollaborativeFiltersTopComponent(CollaborativeTopComponent cnComponent) {
        this();
        this.parent = cnComponent;
        initGraph();
        initVertexFliters();
        initEdgeFilters();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        vertexFiltersPanel = new javax.swing.JPanel();
        degreeLabel = new javax.swing.JLabel();
        degreeSlider = new javax.swing.JSlider();
        bcLabel = new javax.swing.JLabel();
        bcSlider = new javax.swing.JSlider();
        totalPointLabel = new javax.swing.JLabel();
        totalPointSlider = new javax.swing.JSlider();
        ccLabel = new javax.swing.JLabel();
        ccSlider = new javax.swing.JSlider();
        edgeFiltersPanel = new javax.swing.JPanel();
        collaborationNumLabel = new javax.swing.JLabel();
        collaborationNumSlider = new javax.swing.JSlider();

        vertexFiltersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(CollaborativeFiltersTopComponent.class, "CollaborativeFiltersTopComponent.vertexFiltersPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(degreeLabel, degreeStr);

        degreeSlider.setPaintLabels(true);
        degreeSlider.setPaintTicks(true);

        org.openide.awt.Mnemonics.setLocalizedText(bcLabel, bcStr);

        bcSlider.setPaintLabels(true);
        bcSlider.setPaintTicks(true);

        org.openide.awt.Mnemonics.setLocalizedText(totalPointLabel, totalPointStr);

        totalPointSlider.setPaintLabels(true);
        totalPointSlider.setPaintTicks(true);

        org.openide.awt.Mnemonics.setLocalizedText(ccLabel, ccStr);

        ccSlider.setPaintLabels(true);
        ccSlider.setPaintTicks(true);

        javax.swing.GroupLayout vertexFiltersPanelLayout = new javax.swing.GroupLayout(vertexFiltersPanel);
        vertexFiltersPanel.setLayout(vertexFiltersPanelLayout);
        vertexFiltersPanelLayout.setHorizontalGroup(
                vertexFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(vertexFiltersPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(vertexFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ccSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                        .addComponent(bcSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                        .addComponent(totalPointSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                        .addComponent(totalPointLabel)
                                        .addComponent(degreeLabel)
                                        .addComponent(bcLabel)
                                        .addComponent(degreeSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                        .addComponent(ccLabel))
                                .addContainerGap())
        );
        vertexFiltersPanelLayout.setVerticalGroup(
                vertexFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(vertexFiltersPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(totalPointLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totalPointSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(degreeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(degreeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(bcLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bcSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ccLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ccSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(16, Short.MAX_VALUE))
        );

        edgeFiltersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(CollaborativeFiltersTopComponent.class, "CollaborativeFiltersTopComponent.edgeFiltersPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(collaborationNumLabel, collaborationNumStr);

        collaborationNumSlider.setPaintLabels(true);
        collaborationNumSlider.setPaintTicks(true);

        javax.swing.GroupLayout edgeFiltersPanelLayout = new javax.swing.GroupLayout(edgeFiltersPanel);
        edgeFiltersPanel.setLayout(edgeFiltersPanelLayout);
        edgeFiltersPanelLayout.setHorizontalGroup(
                edgeFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(edgeFiltersPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(edgeFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(collaborationNumSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                                        .addComponent(collaborationNumLabel))
                                .addContainerGap())
        );
        edgeFiltersPanelLayout.setVerticalGroup(
                edgeFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(edgeFiltersPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(collaborationNumLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(collaborationNumSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(46, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(edgeFiltersPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(vertexFiltersPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(vertexFiltersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edgeFiltersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }// </editor-fold>
    // Variables declaration - do not modify
    private javax.swing.JLabel bcLabel;
    private javax.swing.JSlider bcSlider;
    private javax.swing.JLabel ccLabel;
    private javax.swing.JSlider ccSlider;
    private javax.swing.JLabel collaborationNumLabel;
    private javax.swing.JSlider collaborationNumSlider;
    private javax.swing.JLabel degreeLabel;
    private javax.swing.JSlider degreeSlider;
    private javax.swing.JPanel edgeFiltersPanel;
    private javax.swing.JLabel totalPointLabel;
    private javax.swing.JSlider totalPointSlider;
    private javax.swing.JPanel vertexFiltersPanel;
    // End of variables declaration

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
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

        vertexPredicate = new Predicate<Context<Graph<Contributor, Collaboration>, Contributor>>() {

            @Override
            public boolean evaluate(Context<Graph<Contributor, Collaboration>, Contributor> context) {
                Graph<Contributor, Collaboration> graph = context.graph;
                Contributor vertex = context.element;

                return (vertex.getPartialPoints() >= totalPointSlider.getValue()
                        && graph.degree(vertex) >= degreeSlider.getValue()
                        && parent.graphUtil.getBc().getVertexScore(vertex) >= bcSlider.getValue()
                        && parent.graphUtil.getCc().getVertexScore(vertex) >= ccSlider.getValue() / METRICS_SCALAR);
            }
        };

        edgePredicate = new Predicate<Context<Graph<Contributor, Collaboration>, Collaboration>>() {

            @Override
            public boolean evaluate(Context<Graph<Contributor, Collaboration>, Collaboration> context) {
                Collaboration edge = context.element;
                return (edge.getAmount() >= collaborationNumSlider.getValue());
            }
        };

        master.getRenderContext().setVertexIncludePredicate(vertexPredicate);
        master.getRenderContext().setEdgeIncludePredicate(edgePredicate);
        satellite.getRenderContext().setVertexIncludePredicate(vertexPredicate);
        satellite.getRenderContext().setEdgeIncludePredicate(edgePredicate);
    }

    private void initVertexFliters() {
        initTotalPoints();
        initDegreeCentrality();
        initBetweennessCentrality();
        initClosenessCentrality();
    }

    private void initTotalPoints() {
        Integer maxTotalPoint = parent.graphUtil.getMaxPartialPoint();
        Integer minTotalPoint = 0;

        sliderLabelTable = new Hashtable();
        sliderLabelTable.put(minTotalPoint, new JLabel(minTotalPoint.toString()));
        sliderLabelTable.put(maxTotalPoint, new JLabel(maxTotalPoint.toString()));
        totalPointSlider.setLabelTable(sliderLabelTable);
        totalPointSlider.setMinorTickSpacing(maxTotalPoint - minTotalPoint);
        totalPointSlider.setMinimum(minTotalPoint);
        totalPointSlider.setMaximum(maxTotalPoint);
        totalPointSlider.setValue(minTotalPoint);
        totalPointSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int minTotalPoint = source.getValue();
                    totalPointLabel.setText(totalPointStr + minTotalPoint);
                    master.repaint();
                }
            }
        });
    }

    private void initDegreeCentrality() {
        Integer maxDegree = parent.graphUtil.getMaxDegree();
        Integer minDegree = 0;

        sliderLabelTable = new Hashtable();
        sliderLabelTable.put(minDegree, new JLabel(minDegree.toString()));
        sliderLabelTable.put(maxDegree, new JLabel(maxDegree.toString()));
        degreeSlider.setLabelTable(sliderLabelTable);
        degreeSlider.setMinorTickSpacing(maxDegree - minDegree);
        degreeSlider.setMinimum(minDegree);
        degreeSlider.setMaximum(maxDegree);
        degreeSlider.setValue(minDegree);
        degreeSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int minDegree = source.getValue();
                    degreeLabel.setText(degreeStr + minDegree);
                    master.repaint();
                }
            }
        });
    }

    private void initBetweennessCentrality() {
        Integer maxBetweenness = parent.graphUtil.getMaxBetweennessCentrality().intValue();
        Integer minBetweenness = 0;
        sliderLabelTable = new Hashtable();
        sliderLabelTable.put(minBetweenness, new JLabel(minBetweenness.toString()));
        sliderLabelTable.put(maxBetweenness, new JLabel(maxBetweenness.toString()));
        bcSlider.setLabelTable(sliderLabelTable);
        bcSlider.setMinorTickSpacing(maxBetweenness - minBetweenness);
        bcSlider.setMinimum(minBetweenness);
        bcSlider.setMaximum(maxBetweenness);
        bcSlider.setValue(minBetweenness);
        bcSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int minBc = source.getValue();
                    bcLabel.setText(bcStr + minBc);
                    master.repaint();
                }
            }
        });
    }

    private void initClosenessCentrality() {
        Double maxCloseness = parent.graphUtil.getMaxClosenessCentrality();
        Double scaledmaxCloseness = maxCloseness * METRICS_SCALAR;
        Integer scaledmaxClosenessInt = scaledmaxCloseness.intValue();

        Integer minCloseness = 0;
        sliderLabelTable = new Hashtable();
        sliderLabelTable.put(minCloseness, new JLabel(minCloseness.toString()));
        sliderLabelTable.put(scaledmaxClosenessInt, new JLabel(df.format(maxCloseness)));
        ccSlider.setLabelTable(sliderLabelTable);
        ccSlider.setMinorTickSpacing(scaledmaxClosenessInt - minCloseness);
        ccSlider.setMinimum(minCloseness);
        ccSlider.setMaximum(scaledmaxClosenessInt);
        ccSlider.setValue(minCloseness);
        ccSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int minCc = source.getValue();
                    ccLabel.setText(ccStr + minCc / METRICS_SCALAR);
                    master.repaint();
                }
            }
        });

    }

    private void initEdgeFilters() {
        initCollaborationNum();
    }

    private void initCollaborationNum() {
        Integer maxCollaborationNum = parent.graphUtil.getMaxCollaborationNum();
        Integer minCollaborationNum = 0;

        sliderLabelTable = new Hashtable();
        sliderLabelTable.put(minCollaborationNum, new JLabel(minCollaborationNum.toString()));
        sliderLabelTable.put(maxCollaborationNum, new JLabel(maxCollaborationNum.toString()));
        collaborationNumSlider.setLabelTable(sliderLabelTable);
        collaborationNumSlider.setMinorTickSpacing(maxCollaborationNum - minCollaborationNum);
        collaborationNumSlider.setMinimum(minCollaborationNum);
        collaborationNumSlider.setMaximum(maxCollaborationNum);
        collaborationNumSlider.setValue(minCollaborationNum);
        collaborationNumSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int minCollaborationNum = source.getValue();
                    collaborationNumLabel.setText(collaborationNumStr + minCollaborationNum);
                    master.repaint();
                }
            }
        });
    }
}