package net.chunxie.networkgraph.cc;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.OverlayLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import net.chunxie.networkdata.entity.*;
import net.chunxie.networkgraph.CollaboratorNode;
import net.chunxie.networkgraph.ThreadNode;
import net.chunxie.networkgraph.cn.CollaborativeTopComponent;
import net.chunxie.networkgraph.util.GraphUtil;
import net.chunxie.networkgraph.util.SnapshotUtil;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.nodes.Node;
import org.openide.nodes.NodeOperation;
import org.openide.windows.WindowManager;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 16:42
 */
@ConvertAsProperties(dtd = "-//net.chunxie.networkgraph.cc//CollaboratorCentric//EN",
        autostore = false)
@TopComponent.Description(preferredID = "CollaboratorCentricTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "net.chunxie.networkgraph.cc.CollaboratorCentricTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_CollaboratorCentricAction",
        preferredID = "CollaboratorCentricTopComponent")
@Messages({
        "CTL_CollaboratorCentricAction=CollaboratorCentric",
        "CTL_CollaboratorCentricTopComponent=Collaborator Centric Network",
        "HINT_CollaboratorCentricTopComponent=This is a Collaborator Centric network window"
})
public final class CollaboratorCentricTopComponent extends TopComponent {

    /** The cache of opened Collaborator-centric Network components. */
    public static Map<String, CollaboratorCentricTopComponent> components = new HashMap<String, CollaboratorCentricTopComponent>();
    // CCN graph option component.
    public CollaboratorCentricOptionsTopComponent optionComponent = null;
    // CCN temporal chart component.
    public CollaboratorCentricTemporalTopComponent temporalComponent = null;
    public VisualizationViewer<Object, ForumMessage> master = null;
    public VisualizationViewer<Object, ForumMessage> satellite = null;
    public Graph<Object, ForumMessage> graph = new SparseMultigraph<Object, ForumMessage>();
    public GraphUtil<Object, ForumMessage> graphUtil = null;
    public SnapshotUtil snapshotUtil = null;
    // refers to the current Snapshot instance
    public Snapshot currentSnapshot = null;
    // refers to the centric collaborator instance
    public Contributor centricCollaborator = null;
    // create a map to save 1 degree collaborators from the centric collaborator's perspective
    private Map<String, Contributor> collaboratorMap = new HashMap<String, Contributor>();
    // create a map to save all forum threads the centric collaborator has ever posted
    private Map<String, ForumThread> threadMap = new HashMap<String, ForumThread>();
    // create a list to save all forum messages which were posted to the threads in the threadList
    private List<ForumMessage> messageList = new ArrayList<ForumMessage>();
    private DefaultModalGraphMouse graphMouse = null;
    private ScalingControl satelliteScaler = null;
    private JPanel satelliteViewPanel = new JPanel();
    private GraphZoomScrollPane graphPanel = null;
    private Layout<Object, ForumMessage> graphLayout = null;

    public CollaboratorCentricTopComponent() {
        initComponents();
        setName(Bundle.CTL_CollaboratorCentricAction());
        setToolTipText(Bundle.HINT_CollaboratorCentricTopComponent());
        ButtonGroup group = new ButtonGroup();
        group.add(moveToggleBtn);
        group.add(selectToggleBtn);
    }

    public CollaboratorCentricTopComponent(Snapshot snapshot, Contributor collaborator) {
        this();
        this.currentSnapshot = snapshot;
        this.centricCollaborator = collaborator;
        initData();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        satelliteToggleBtn = new javax.swing.JToggleButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        selectToggleBtn = new javax.swing.JToggleButton();
        moveToggleBtn = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        zoomInBtn = new javax.swing.JButton();
        zoomOutBtn = new javax.swing.JButton();
        fitToViewBtn = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        tempoChartBtn = new javax.swing.JButton();
        optionsBtn = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        satelliteToggleBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/chunxie/networkgraph/application_form_magnify.png"))); // NOI18N
        satelliteToggleBtn.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(satelliteToggleBtn, org.openide.util.NbBundle.getMessage(CollaboratorCentricTopComponent.class, "CollaboratorCentricTopComponent.satelliteToggleBtn.text")); // NOI18N
        satelliteToggleBtn.setToolTipText(org.openide.util.NbBundle.getMessage(CollaboratorCentricTopComponent.class, "CollaboratorCentricTopComponent.satelliteToggleBtn.toolTipText")); // NOI18N
        satelliteToggleBtn.setFocusable(false);
        satelliteToggleBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        satelliteToggleBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        satelliteToggleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                satelliteToggleBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(satelliteToggleBtn);
        jToolBar1.add(jSeparator3);

        selectToggleBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/chunxie/networkgraph/cursor.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(selectToggleBtn, org.openide.util.NbBundle.getMessage(CollaboratorCentricTopComponent.class, "CollaboratorCentricTopComponent.selectToggleBtn.text")); // NOI18N
        selectToggleBtn.setToolTipText(org.openide.util.NbBundle.getMessage(CollaboratorCentricTopComponent.class, "CollaboratorCentricTopComponent.selectToggleBtn.toolTipText")); // NOI18N
        selectToggleBtn.setFocusable(false);
        selectToggleBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        selectToggleBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        selectToggleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectToggleBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(selectToggleBtn);

        moveToggleBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/chunxie/networkgraph/move.png"))); // NOI18N
        moveToggleBtn.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(moveToggleBtn, org.openide.util.NbBundle.getMessage(CollaboratorCentricTopComponent.class, "CollaboratorCentricTopComponent.moveToggleBtn.text")); // NOI18N
        moveToggleBtn.setToolTipText(org.openide.util.NbBundle.getMessage(CollaboratorCentricTopComponent.class, "CollaboratorCentricTopComponent.moveToggleBtn.toolTipText")); // NOI18N
        moveToggleBtn.setFocusable(false);
        moveToggleBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        moveToggleBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        moveToggleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveToggleBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(moveToggleBtn);
        jToolBar1.add(jSeparator1);

        zoomInBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/chunxie/networkgraph/magnifier_zoom_in.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(zoomInBtn, org.openide.util.NbBundle.getMessage(CollaboratorCentricTopComponent.class, "CollaboratorCentricTopComponent.zoomInBtn.text")); // NOI18N
        zoomInBtn.setToolTipText(org.openide.util.NbBundle.getMessage(CollaboratorCentricTopComponent.class, "CollaboratorCentricTopComponent.zoomInBtn.toolTipText")); // NOI18N
        zoomInBtn.setFocusable(false);
        zoomInBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        zoomInBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        zoomInBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomInBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(zoomInBtn);

        zoomOutBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/chunxie/networkgraph/magifier_zoom_out.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(zoomOutBtn, org.openide.util.NbBundle.getMessage(CollaboratorCentricTopComponent.class, "CollaboratorCentricTopComponent.zoomOutBtn.text")); // NOI18N
        zoomOutBtn.setToolTipText(org.openide.util.NbBundle.getMessage(CollaboratorCentricTopComponent.class, "CollaboratorCentricTopComponent.zoomOutBtn.toolTipText")); // NOI18N
        zoomOutBtn.setFocusable(false);
        zoomOutBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        zoomOutBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        zoomOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomOutBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(zoomOutBtn);

        fitToViewBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/chunxie/networkgraph/fit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(fitToViewBtn, org.openide.util.NbBundle.getMessage(CollaboratorCentricTopComponent.class, "CollaboratorCentricTopComponent.fitToViewBtn.text")); // NOI18N
        fitToViewBtn.setToolTipText(org.openide.util.NbBundle.getMessage(CollaboratorCentricTopComponent.class, "CollaboratorCentricTopComponent.fitToViewBtn.toolTipText")); // NOI18N
        fitToViewBtn.setFocusable(false);
        fitToViewBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        fitToViewBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        fitToViewBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fitToViewBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(fitToViewBtn);
        jToolBar1.add(jSeparator2);

        tempoChartBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/chunxie/networkgraph/chart_16.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(tempoChartBtn, org.openide.util.NbBundle.getMessage(CollaboratorCentricTopComponent.class, "CollaboratorCentricTopComponent.tempoChartBtn.text")); // NOI18N
        tempoChartBtn.setFocusable(false);
        tempoChartBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        tempoChartBtn.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        tempoChartBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tempoChartBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(tempoChartBtn);

        optionsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/chunxie/networkgraph/cog.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(optionsBtn, org.openide.util.NbBundle.getMessage(CollaboratorCentricTopComponent.class, "CollaboratorCentricTopComponent.optionsBtn.text")); // NOI18N
        optionsBtn.setFocusable(false);
        optionsBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        optionsBtn.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        optionsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(optionsBtn);

        add(jToolBar1, java.awt.BorderLayout.NORTH);
    }// </editor-fold>

    private void selectToggleBtnActionPerformed(java.awt.event.ActionEvent evt) {
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
    }

    private void moveToggleBtnActionPerformed(java.awt.event.ActionEvent evt) {
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
    }

    private void zoomInBtnActionPerformed(java.awt.event.ActionEvent evt) {
        satelliteScaler.scale(master, 1.1f, master.getCenter());
    }

    private void zoomOutBtnActionPerformed(java.awt.event.ActionEvent evt) {
        satelliteScaler.scale(master, 1 / 1.1f, master.getCenter());
    }

    private void fitToViewBtnActionPerformed(java.awt.event.ActionEvent evt) {
        master.getRenderContext().getMultiLayerTransformer().setToIdentity();
    }

    private void satelliteToggleBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (satelliteToggleBtn.isSelected()) {
            satelliteViewPanel.setVisible(true);
        } else {
            satelliteViewPanel.setVisible(false);
        }
    }

    private void optionsBtnActionPerformed(java.awt.event.ActionEvent evt) {
        WindowManager.getDefault().findMode("properties").dockInto(optionComponent);
        optionComponent.open();
        optionComponent.requestActive();
    }

    private void tempoChartBtnActionPerformed(java.awt.event.ActionEvent evt) {
        WindowManager.getDefault().findMode("output").dockInto(temporalComponent);
        temporalComponent.open();
        temporalComponent.requestActive();
    }

    // Variables declaration - do not modify
    private javax.swing.JButton fitToViewBtn;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToggleButton moveToggleBtn;
    private javax.swing.JButton optionsBtn;
    private javax.swing.JToggleButton satelliteToggleBtn;
    private javax.swing.JToggleButton selectToggleBtn;
    private javax.swing.JButton tempoChartBtn;
    private javax.swing.JButton zoomInBtn;
    private javax.swing.JButton zoomOutBtn;
    // End of variables declaration

    public static CollaboratorCentricTopComponent getComponent(Snapshot snapshot, Contributor collaborator) {
        String key = snapshot.toString() + "," + collaborator.getContributor();
        CollaboratorCentricTopComponent win = components.get(key);
        if (win == null) {
            win = new CollaboratorCentricTopComponent(snapshot, collaborator);
            components.put(key, win);
        } else {
        }
        return win;
    }

    @Override
    protected void componentActivated() {
        for (CollaborativeTopComponent comp : CollaborativeTopComponent.cnComponents.values()) {
            if (comp.filterComponent != null) {
                comp.filterComponent.close();
            }
            if (comp.optionComponent != null) {
                comp.optionComponent.close();
            }
        }
        for (CollaboratorCentricTopComponent comp : components.values()) {
            if (comp.optionComponent != null && !comp.optionComponent.equals(this.optionComponent)) {
                comp.optionComponent.close();
            }
            if (comp.temporalComponent != null && !comp.temporalComponent.equals(this.temporalComponent)) {
                comp.temporalComponent.close();
            }
        }
    }

    @Override
    public void componentOpened() {
        createGraph();
        initGraphUtil();
        initSnapshotUtil();
        createView();
        optionComponent = new CollaboratorCentricOptionsTopComponent(this);
        temporalComponent = new CollaboratorCentricTemporalTopComponent(this);
    }

    @Override
    public void componentClosed() {
        String key = currentSnapshot.toString() + "," + centricCollaborator.getContributor();
        components.remove(key);
        optionComponent.close();
        optionComponent = null;
        temporalComponent.close();
        temporalComponent = null;
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

    private void initData() {
        Map<String, Contributor> contributors = currentSnapshot.getSnapshotContributors();
        Forum forum = currentSnapshot.getForum();
        List<ForumThread> threads = forum.getThreads();

        for (ForumThread thread : threads) {
            boolean flag = false;
            List<ForumMessage> messages = thread.getMessages();
            List<ForumMessage> iteratorList = new ArrayList<ForumMessage>();
            Map<String, Contributor> iteratorMap = new HashMap<String, Contributor>();
            for (ForumMessage message : messages) {
                String key = message.getOriginContributor().getContributor();
                Contributor contributor = contributors.get(key);
                iteratorMap.put(key, contributor);
                iteratorList.add(message);
                if (contributor.equals(centricCollaborator)) {
                    flag = true;
                }
            }
            // if there is only ONE contributor in the temp map, it will not be added to the collaborator map
            if (flag && iteratorMap.containsKey(centricCollaborator.getContributor())) {
                collaboratorMap.putAll(iteratorMap);
                messageList.addAll(iteratorList);
                threadMap.put(thread.getUriShort(), thread);
            }
        }
    }

    private void createGraph() {
        for (Contributor collaborator : collaboratorMap.values()) {
            graph.addVertex(collaborator);
        }
        for (ForumThread thread : threadMap.values()) {
            graph.addVertex(thread);
        }
        for (ForumMessage edge : messageList) {
            Contributor collaborator = collaboratorMap.get(edge.getOriginContributor().getContributor());
            ForumThread thread = threadMap.get(edge.getOriginThread().getUriShort());
            graph.addEdge(edge, collaborator, thread);
        }
    }

    private void createView() {
        graphLayout = new FRLayout<Object, ForumMessage>(graph);
        master = new VisualizationViewer<Object, ForumMessage>(graphLayout, new Dimension(800, 800));

        satellite = new SatelliteVisualizationViewer<Object, ForumMessage>(master, new Dimension(200, 200));
        satelliteScaler = new CrossoverScalingControl();
        satellite.scaleToLayout(satelliteScaler);
        graphPanel = new GraphZoomScrollPane(master);
        graphMouse = new DefaultModalGraphMouse();
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        graphMouse.add(new PopupGraphMousePlugin());
        master.setGraphMouse(graphMouse);

        satellite.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        satelliteViewPanel.setBounds(5, 5, 200, 200);
        satelliteViewPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        satelliteViewPanel.add(satellite);
        satelliteViewPanel.setOpaque(false);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.add(graphPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(satelliteViewPanel, JLayeredPane.PALETTE_LAYER);
        add(layeredPane, BorderLayout.CENTER);
    }

    private void initGraphUtil() {
        graphUtil = new GraphUtil<Object, ForumMessage>(graph);
    }

    private void initSnapshotUtil() {
        snapshotUtil = new SnapshotUtil(currentSnapshot);
    }

    protected class PopupGraphMousePlugin extends AbstractPopupGraphMousePlugin
            implements MouseListener {

        public PopupGraphMousePlugin() {
            this(MouseEvent.BUTTON3_MASK);
        }

        public PopupGraphMousePlugin(int modifiers) {
            super(modifiers);
        }

        /**
         * If this event is over a Vertex, pop up a menu to
         * allow the user to goto Collaborator-Thread Network graph
         * @param e
         */
        @Override
        protected void handlePopup(MouseEvent e) {
            final VisualizationViewer<Object, ForumMessage> vv = (VisualizationViewer<Object, ForumMessage>) e.getSource();
            final Point2D p = e.getPoint();

            GraphElementAccessor<Object, ForumMessage> pickSupport = vv.getPickSupport();
            if (pickSupport != null) {
                final Object vertex = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
                JPopupMenu popup = new JPopupMenu();
                if (vertex != null) {
                    if (vertex instanceof Contributor) {
                        // add some menu items if it is a contributor vertex
                        final Contributor c = (Contributor) vertex;
                        popup.add(new AbstractAction("Goto Collaborative Network") {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                CollaborativeTopComponent component = CollaborativeTopComponent.getComponent(currentSnapshot, c);
                                component.setName("Collaborative Network(" + currentSnapshot.getShortDesc() + ")");
                                component.open();
                                component.requestActive();
                            }
                        });

                        popup.add(new AbstractAction("<html><center>" + "Properties") {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    Node node = new CollaboratorNode(c);
                                    NodeOperation.getDefault().showProperties(node);
                                } catch (Exception ex) {

                                }
                            }
                        });
                    } else if (vertex instanceof ForumThread) {
                        // add some menu items if it is a thread vertex
                        final ForumThread t = (ForumThread) vertex;
                        popup.add(new AbstractAction("<html><center>" + "Properties") {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    Node node = new ThreadNode(t);
                                    NodeOperation.getDefault().showProperties(node);
                                } catch (Exception ex) {

                                }
                            }
                        });
                    }

                    // add some menu items if it is a vertex
                    popup.add(new AbstractAction("<html><center>" + "Centre Node") {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            graphUtil.centreVertex(vv, vertex, true);
                        }
                    });

                    popup.show(vv, e.getX(), e.getY());
                }
            }
        }
    }
}
