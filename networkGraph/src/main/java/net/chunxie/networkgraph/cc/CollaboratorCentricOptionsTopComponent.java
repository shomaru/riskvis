package net.chunxie.networkgraph.cc;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.LayeredIcon;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.DefaultVertexIconTransformer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Stroke;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.chunxie.networkdata.entity.Contributor;
import net.chunxie.networkdata.entity.ForumMessage;
import net.chunxie.networkdata.entity.ForumThread;
import net.chunxie.networkgraph.util.IconUtil;
import org.apache.commons.collections15.Predicate;
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
 * @created 08/02/13 17:08
 */
@ConvertAsProperties(dtd = "-//net.chunxie.networkgraph.cc//CollaboratorCentricOptions//EN",
        autostore = false)
@TopComponent.Description(preferredID = "CollaboratorCentricOptionsTopComponent",
        iconBase = "net/chunxie/networkgraph/cc/cog.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "net.chunxie.networkgraph.cc.CollaboratorCentricOptionsTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_CollaboratorCentricOptionsAction",
        preferredID = "CollaboratorCentricOptionsTopComponent")
@Messages({
        "CTL_CollaboratorCentricOptionsAction=CollaboratorCentricOptions",
        "CTL_CollaboratorCentricOptionsTopComponent=Graph Options",
        "HINT_CollaboratorCentricOptionsTopComponent=This is a Collaborator Centric network graph options window"
})
public final class CollaboratorCentricOptionsTopComponent extends TopComponent {

    private CollaboratorCentricTopComponent parent = null;
    private String totalPointStr = "Show nodes whose awarded points >= ";
    private String messagePointsStr = "Show edges whose message points >= ";
    private DateFormat df = new SimpleDateFormat("dd/MM/yy");
    // a Map for the Icons
    private Map<Object, Icon> iconMap = new HashMap<Object, Icon>();
    private Hashtable sliderLabelTable = null;

    public CollaboratorCentricOptionsTopComponent() {
        initComponents();
        setName(Bundle.CTL_CollaboratorCentricOptionsAction());
        setToolTipText(Bundle.HINT_CollaboratorCentricOptionsTopComponent());
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);

    }

    public CollaboratorCentricOptionsTopComponent(CollaboratorCentricTopComponent ccTopComponent) {
        this();
        this.parent = ccTopComponent;
        initVertexAndEdgeStroke();
        //initVertexShape();
        initVertexIcon();
        initVertexAndEdgeTooltips();
        initVertexLabel();
        initPreRender();
        initIncludePredicate();
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

        jPanel2 = new javax.swing.JPanel();
        showNetworkInfoCheckBox = new javax.swing.JCheckBox();
        showVertexLabelsCheckBox = new javax.swing.JCheckBox();
        vertexFiltersPanel = new javax.swing.JPanel();
        totalPointLabel = new javax.swing.JLabel();
        totalPointSlider = new javax.swing.JSlider();
        edgeFiltersPanel = new javax.swing.JPanel();
        messagePointsLabel = new javax.swing.JLabel();
        messagePointsSlider = new javax.swing.JSlider();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(CollaboratorCentricOptionsTopComponent.class, "CollaboratorCentricOptionsTopComponent.jPanel2.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        showNetworkInfoCheckBox.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(showNetworkInfoCheckBox, org.openide.util.NbBundle.getMessage(CollaboratorCentricOptionsTopComponent.class, "CollaboratorCentricOptionsTopComponent.showNetworkInfoCheckBox.text")); // NOI18N
        showNetworkInfoCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showNetworkInfoCheckBoxActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(showVertexLabelsCheckBox, org.openide.util.NbBundle.getMessage(CollaboratorCentricOptionsTopComponent.class, "CollaboratorCentricOptionsTopComponent.showVertexLabelsCheckBox.text")); // NOI18N
        showVertexLabelsCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showVertexLabelsCheckBoxActionPerformed(evt);
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
                                        .addComponent(showVertexLabelsCheckBox))
                                .addContainerGap(111, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(showNetworkInfoCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(showVertexLabelsCheckBox)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        vertexFiltersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(CollaboratorCentricOptionsTopComponent.class, "CollaboratorCentricOptionsTopComponent.vertexFiltersPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(totalPointLabel, totalPointStr);

        totalPointSlider.setPaintLabels(true);
        totalPointSlider.setPaintTicks(true);

        javax.swing.GroupLayout vertexFiltersPanelLayout = new javax.swing.GroupLayout(vertexFiltersPanel);
        vertexFiltersPanel.setLayout(vertexFiltersPanelLayout);
        vertexFiltersPanelLayout.setHorizontalGroup(
                vertexFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(vertexFiltersPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(vertexFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(totalPointSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                                        .addComponent(totalPointLabel))
                                .addContainerGap())
        );
        vertexFiltersPanelLayout.setVerticalGroup(
                vertexFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(vertexFiltersPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(totalPointLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totalPointSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(13, Short.MAX_VALUE))
        );

        edgeFiltersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, org.openide.util.NbBundle.getMessage(CollaboratorCentricOptionsTopComponent.class, "CollaboratorCentricOptionsTopComponent.edgeFiltersPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(messagePointsLabel, messagePointsStr);

        messagePointsSlider.setMaximum(10);
        messagePointsSlider.setMinimum(2);
        messagePointsSlider.setPaintLabels(true);
        messagePointsSlider.setPaintTicks(true);
        messagePointsSlider.setValue(2);

        javax.swing.GroupLayout edgeFiltersPanelLayout = new javax.swing.GroupLayout(edgeFiltersPanel);
        edgeFiltersPanel.setLayout(edgeFiltersPanelLayout);
        edgeFiltersPanelLayout.setHorizontalGroup(
                edgeFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(edgeFiltersPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(edgeFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(messagePointsSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                                        .addComponent(messagePointsLabel))
                                .addContainerGap())
        );
        edgeFiltersPanelLayout.setVerticalGroup(
                edgeFiltersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(edgeFiltersPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(messagePointsLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(messagePointsSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(edgeFiltersPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(vertexFiltersPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(vertexFiltersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edgeFiltersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }// </editor-fold>

    private void showNetworkInfoCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        parent.master.repaint();
    }

    private void showVertexLabelsCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        parent.master.repaint();
    }
    // Variables declaration - do not modify
    private javax.swing.JPanel edgeFiltersPanel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel messagePointsLabel;
    private javax.swing.JSlider messagePointsSlider;
    private javax.swing.JCheckBox showNetworkInfoCheckBox;
    private javax.swing.JCheckBox showVertexLabelsCheckBox;
    private javax.swing.JLabel totalPointLabel;
    private javax.swing.JSlider totalPointSlider;
    private javax.swing.JPanel vertexFiltersPanel;
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

    private void initPreRender() {
        parent.master.addPreRenderPaintable(new VisualizationViewer.Paintable() {

            String STR_LINE_01 = "Centric Collaborator: " + parent.centricCollaborator.getContributor();
            String STR_LINE_02 = "Forum: " + parent.currentSnapshot.getForum().getForumtitle();
            String STR_LINE_03 = "Start Date: " + df.format(parent.currentSnapshot.getStartCal().getTime());
            String STR_LINE_04 = "End Date: " + df.format(parent.currentSnapshot.getEndCal().getTime());
            String STR_LINE_05 = "Node (Collaborators): " + parent.graphUtil.getCollaboratorsNum();
            String STR_LINE_06 = "Node (Threads): " + parent.graphUtil.getThreadsNum();
            String STR_LINE_07 = "Edge (Messages): " + parent.graph.getEdgeCount();

            @Override
            public void paint(Graphics g) {
                if (showNetworkInfoCheckBox.isSelected()) {
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
                    g.setColor(oldColor);
                }
            }

            @Override
            public boolean useTransform() {
                return false;
            }
        });
    }

    private void initVertexAndEdgeTooltips() {
        parent.master.setVertexToolTipTransformer(new Transformer<Object, String>() {

            @Override
            public String transform(Object vertex) {
                String result = "";
                if (vertex instanceof ForumThread) {
                    ForumThread thread = (ForumThread) vertex;
                    result = "<html><table><th>Thread</th><tr><td><i>id</i></td><td>"
                            + thread.getUriShort()
                            + "</td></tr><tr><td><i>title</i></td><td>"
                            + thread.getThreadtitle()
                            + "</td></tr><tr><td><i>status</i></td><td>"
                            + thread.getStatusShort()
                            + "</td></tr><tr><td><i>views</i></td><td>"
                            + thread.getNoofviews()
                            + "</td></tr></table></html>";
                } else if (vertex instanceof Contributor) {
                    Contributor collaborator = (Contributor) vertex;
                    result = "<html><table><th>Collaborator</th><tr><td><i>id</i></td><td>"
                            + collaborator.getContributor()
                            + "</td></tr><tr><td><i>awarded points</i></td><td>"
                            + collaborator.getPartialPoints()
                            + "</td></tr></table></html>";
                }
                return result;
            }
        });
        parent.master.setEdgeToolTipTransformer(new Transformer<ForumMessage, String>() {

            @Override
            public String transform(ForumMessage edge) {
                return "<html><table><th>Message</th><tr><td><i>id</i></td><td>"
                        + edge.getUriShort()
                        + "</td></tr><tr><td><i>title</i></td><td>"
                        + edge.getMessagetitle()
                        + "</td></tr><tr><td><i>points</i></td><td>"
                        + edge.getAwardedpoints()
                        + "</td></tr><tr><td><i>creation date</i></td><td>"
                        + edge.getCreationdate()
                        + "</td></tr></table></html>";
            }
        });
    }

    private void initVertexLabel() {
        //parent.master.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        parent.master.getRenderContext().setVertexLabelTransformer(new Transformer<Object, String>() {

            @Override
            public String transform(Object vertex) {
                String result = null;
                if (showVertexLabelsCheckBox.isSelected()) {
                    if (vertex instanceof ForumThread) {
                        ForumThread thread = (ForumThread) vertex;
                        result = "<html><center>T-" + thread.getUriShort();
                    } else if (vertex instanceof Contributor) {
                        Contributor collaborator = (Contributor) vertex;
                        result = "<html><center>C-" + collaborator.getContributor();
                    }
                }
                return result;
            }
        });

    }

    private void initVertexIcon() {
        for (Object vertex : parent.graph.getVertices()) {
            if (vertex instanceof ForumThread) {
                ForumThread thread = (ForumThread) vertex;
                iconMap.put(thread, new LayeredIcon(IconUtil.THREAD_GREY_24));
            } else if (vertex instanceof Contributor) {
                Contributor collaborator = (Contributor) vertex;
                iconMap.put(collaborator, new LayeredIcon(IconUtil.USER_GREY_24));
            }
        }


        final DefaultVertexIconTransformer<Object> vertexIconFunction = new DefaultVertexIconTransformer<Object>() {

            @Override
            public Icon transform(Object node) {
                PickedState<Object> pickedState = parent.master.getPickedVertexState();
                if (pickedState.isPicked(node)) {
                    if (node instanceof ForumThread) {
                        return new LayeredIcon(IconUtil.THREAD_YELLOW_24);
                    } else if (node instanceof Contributor) {
                        return new LayeredIcon(IconUtil.USER_YELLOW_24);
                    }
                }
                for (Object neighbour : parent.graph.getNeighbors(node)) {
                    if (pickedState.isPicked(neighbour)) {
                        if (node instanceof ForumThread) {
                            return new LayeredIcon(IconUtil.THREAD_RED_24);
                        } else if (node instanceof Contributor) {
                            return new LayeredIcon(IconUtil.USER_RED_24);
                        }
                    }
                }
                return super.transform(node);
            }
        };
        vertexIconFunction.setIconMap(iconMap);

        parent.master.getRenderContext().setVertexIconTransformer(vertexIconFunction);
        parent.satellite.getRenderContext().setVertexIconTransformer(vertexIconFunction);
    }

    private void initVertexFliters() {
        initTotalPointsFilter();
    }

    private void initEdgeFilters() {
        initMessagePointsFilter();
    }

    private void initTotalPointsFilter() {
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
                    parent.master.repaint();
                }
            }
        });
    }

    private void initIncludePredicate() {
        Predicate<Context<Graph<Object, ForumMessage>, Object>> vertexPredicate = new Predicate<Context<Graph<Object, ForumMessage>, Object>>() {

            @Override
            public boolean evaluate(Context<Graph<Object, ForumMessage>, Object> context) {
                boolean result = true;
                Object vertex = context.element;
                if (vertex instanceof Contributor) {
                    Contributor collaborator = (Contributor) vertex;
                    result = (collaborator.getPartialPoints() >= totalPointSlider.getValue()
                            || collaborator.equals(parent.centricCollaborator));
                }
                return result;
            }
        };
        Predicate<Context<Graph<Object, ForumMessage>, ForumMessage>> edgePredicate = new Predicate<Context<Graph<Object, ForumMessage>, ForumMessage>>() {

            @Override
            public boolean evaluate(Context<Graph<Object, ForumMessage>, ForumMessage> context) {
                ForumMessage edge = context.element;
                return (edge.getAwardedpoints() >= messagePointsSlider.getValue());
            }
        };

        parent.master.getRenderContext().setVertexIncludePredicate(vertexPredicate);
        parent.master.getRenderContext().setEdgeIncludePredicate(edgePredicate);
        parent.satellite.getRenderContext().setVertexIncludePredicate(vertexPredicate);
        parent.satellite.getRenderContext().setEdgeIncludePredicate(edgePredicate);
    }

    private void initMessagePointsFilter() {

        sliderLabelTable = new Hashtable();
        sliderLabelTable.put(2, new JLabel("<html><center>Helpful<br>Answer"));
        sliderLabelTable.put(6, new JLabel("<html><center>Very Helpful<br>Answer"));
        sliderLabelTable.put(10, new JLabel("<html><center>Solved<br>Problem"));
        messagePointsSlider.setLabelTable(sliderLabelTable);
        messagePointsSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int minTotalPoint = source.getValue();
                    messagePointsLabel.setText(messagePointsStr + minTotalPoint);
                    parent.master.repaint();
                }
            }
        });
    }

    private void initVertexAndEdgeStroke() {

        Transformer<ForumMessage, Stroke> edgeStroke = new Transformer<ForumMessage, Stroke>() {

            @Override
            public Stroke transform(ForumMessage edge) {
                Integer value = 1;
                if (edge.getAwardedpoints() == 10) {
                    value = 4;
                } else if (edge.getAwardedpoints() == 6) {
                    value = 2;
                }

                return new BasicStroke(value);
            }
        };

        parent.master.getRenderContext().setEdgeStrokeTransformer(edgeStroke);
        parent.satellite.getRenderContext().setEdgeStrokeTransformer(edgeStroke);
    }
}
