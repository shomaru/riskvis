package net.chunxie.networkgraph.cn;

import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
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
import net.chunxie.networkgraph.cc.CollaboratorCentricTopComponent;
import net.chunxie.networkgraph.edge.Collaboration;
import net.chunxie.networkgraph.util.GraphUtil;
import org.apache.commons.collections15.Transformer;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.WindowManager;
import org.openide.util.NbBundle.Messages;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 17:36
 */
@ConvertAsProperties(dtd = "-//net.chunxie.networkgraph.cn//Collaborative//EN",
        autostore = false)
@TopComponent.Description(preferredID = "CollaborativeTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "net.chunxie.networkgraph.cn.CollaborativeTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_CollaborativeAction",
        preferredID = "CollaborativeTopComponent")
@Messages({
        "CTL_CollaborativeAction=CollaborativeNetwork",
        "CTL_CollaborativeTopComponent=Collaborative Network",
        "HINT_CollaborativeTopComponent=This is a Collaborative network window"
})
public final class CollaborativeTopComponent extends TopComponent {

    /** The cache of opened Collaborative Network (CN) components. */
    public static Map<String, CollaborativeTopComponent> cnComponents = new HashMap<String, CollaborativeTopComponent>();
    // CN dynamic filter components.
    public CollaborativeFiltersTopComponent filterComponent = null;
    // CN graph option components.
    public CollaborativeOptionsTopComponent optionComponent = null;

    // master & satellite view of the graph
    public VisualizationViewer<Contributor, Collaboration> master = null;
    public VisualizationViewer<Contributor, Collaboration> satellite = null;

    // use an aggregate layout for clustering features
    public AggregateLayout<Contributor, Collaboration> graphLayout = null;

    // refers to the current Snapshot instance
    public Snapshot currentSnapshot = null;

    public GraphUtil<Contributor, Collaboration> graphUtil = null;
    // create a map to save collaborators
    private Map<String, Contributor> collaborators = new HashMap<String, Contributor>();
    // create a map to save collaborations
    private Map<String, Collaboration> collaborations = new HashMap<String, Collaboration>();
    private ScalingControl satelliteScaler = null;
    private DefaultModalGraphMouse graphMouse;

    private Graph<Contributor, Collaboration> graph = null;
    private Contributor selectedCollaborator = null;

    // create a panel to add into LayerPane
    private GraphZoomScrollPane masterLayer = null;
    private JPanel satelliteLayer = new JPanel();

    public CollaborativeTopComponent() {
        initComponents();
        setName(Bundle.CTL_CollaborativeAction());
        setToolTipText(Bundle.HINT_CollaborativeTopComponent());
        ButtonGroup group = new ButtonGroup();
        group.add(moveToggleBtn);
        group.add(selectToggleBtn);
    }

    public CollaborativeTopComponent(Snapshot snapshot) {
        this();
        currentSnapshot = snapshot;
        initData();
    }

    public CollaborativeTopComponent(Snapshot snapshot, Contributor collaborator) {
        this(snapshot);
        selectedCollaborator = collaborator;
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
        jSeparator2 = new javax.swing.JToolBar.Separator();
        selectToggleBtn = new javax.swing.JToggleButton();
        moveToggleBtn = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        zoomInBtn = new javax.swing.JButton();
        zoomOutBtn = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        filtersBtn = new javax.swing.JButton();
        optionsBtn = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        satelliteToggleBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/chunxie/networkgraph/application_form_magnify.png"))); // NOI18N
        satelliteToggleBtn.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(satelliteToggleBtn, org.openide.util.NbBundle.getMessage(CollaborativeTopComponent.class, "CollaborativeTopComponent.satelliteToggleBtn.text")); // NOI18N
        satelliteToggleBtn.setToolTipText(org.openide.util.NbBundle.getMessage(CollaborativeTopComponent.class, "CollaborativeTopComponent.satelliteToggleBtn.toolTipText")); // NOI18N
        satelliteToggleBtn.setFocusable(false);
        satelliteToggleBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        satelliteToggleBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        satelliteToggleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                satelliteToggleBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(satelliteToggleBtn);
        jToolBar1.add(jSeparator2);

        selectToggleBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/chunxie/networkgraph/cursor.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(selectToggleBtn, org.openide.util.NbBundle.getMessage(CollaborativeTopComponent.class, "CollaborativeTopComponent.selectToggleBtn.text")); // NOI18N
        selectToggleBtn.setToolTipText(org.openide.util.NbBundle.getMessage(CollaborativeTopComponent.class, "CollaborativeTopComponent.selectToggleBtn.toolTipText")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(moveToggleBtn, org.openide.util.NbBundle.getMessage(CollaborativeTopComponent.class, "CollaborativeTopComponent.moveToggleBtn.text")); // NOI18N
        moveToggleBtn.setToolTipText(org.openide.util.NbBundle.getMessage(CollaborativeTopComponent.class, "CollaborativeTopComponent.moveToggleBtn.toolTipText")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(zoomInBtn, org.openide.util.NbBundle.getMessage(CollaborativeTopComponent.class, "CollaborativeTopComponent.zoomInBtn.text")); // NOI18N
        zoomInBtn.setToolTipText(org.openide.util.NbBundle.getMessage(CollaborativeTopComponent.class, "CollaborativeTopComponent.zoomInBtn.toolTipText")); // NOI18N
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
        org.openide.awt.Mnemonics.setLocalizedText(zoomOutBtn, org.openide.util.NbBundle.getMessage(CollaborativeTopComponent.class, "CollaborativeTopComponent.zoomOutBtn.text")); // NOI18N
        zoomOutBtn.setToolTipText(org.openide.util.NbBundle.getMessage(CollaborativeTopComponent.class, "CollaborativeTopComponent.zoomOutBtn.toolTipText")); // NOI18N
        zoomOutBtn.setFocusable(false);
        zoomOutBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        zoomOutBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        zoomOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomOutBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(zoomOutBtn);

        resetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/chunxie/networkgraph/fit.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(resetButton, org.openide.util.NbBundle.getMessage(CollaborativeTopComponent.class, "CollaborativeTopComponent.resetButton.text")); // NOI18N
        resetButton.setToolTipText(org.openide.util.NbBundle.getMessage(CollaborativeTopComponent.class, "CollaborativeTopComponent.resetButton.toolTipText")); // NOI18N
        resetButton.setFocusable(false);
        resetButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        resetButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(resetButton);
        jToolBar1.add(jSeparator3);

        filtersBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/chunxie/networkgraph/filter.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(filtersBtn, org.openide.util.NbBundle.getMessage(CollaborativeTopComponent.class, "CollaborativeTopComponent.filtersBtn.text")); // NOI18N
        filtersBtn.setFocusable(false);
        filtersBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        filtersBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filtersBtnActionPerformed(evt);
            }
        });
        jToolBar1.add(filtersBtn);

        optionsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/chunxie/networkgraph/cog.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(optionsBtn, org.openide.util.NbBundle.getMessage(CollaborativeTopComponent.class, "CollaborativeTopComponent.optionsBtn.text")); // NOI18N
        optionsBtn.setFocusable(false);
        optionsBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        optionsBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
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

    private void satelliteToggleBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (satelliteToggleBtn.isSelected()) {
            satelliteLayer.setVisible(true);
        } else {
            satelliteLayer.setVisible(false);
        }
    }

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {
        master.getRenderContext().getMultiLayerTransformer().setToIdentity();
    }

    private void filtersBtnActionPerformed(java.awt.event.ActionEvent evt) {
        WindowManager.getDefault().findMode("properties").dockInto(filterComponent);
        filterComponent.open();
        filterComponent.requestActive();
    }

    private void optionsBtnActionPerformed(java.awt.event.ActionEvent evt) {
        WindowManager.getDefault().findMode("properties").dockInto(optionComponent);
        optionComponent.open();
        optionComponent.requestActive();
    }

    // Variables declaration - do not modify
    private javax.swing.JButton filtersBtn;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToggleButton moveToggleBtn;
    private javax.swing.JButton optionsBtn;
    private javax.swing.JButton resetButton;
    private javax.swing.JToggleButton satelliteToggleBtn;
    private javax.swing.JToggleButton selectToggleBtn;
    private javax.swing.JButton zoomInBtn;
    private javax.swing.JButton zoomOutBtn;
    // End of variables declaration

    public static CollaborativeTopComponent getComponent(Snapshot snapshot, Contributor c) {
        CollaborativeTopComponent win = cnComponents.get(snapshot.toString());
        if (win == null) {
            if (c == null) {
                win = new CollaborativeTopComponent(snapshot);
            } else {
                win = new CollaborativeTopComponent(snapshot, c);
            }
            cnComponents.put(snapshot.toString(), win);
        } else {
            win.pickVertex(c);
        }
        return win;
    }

    @Override
    protected void componentActivated() {
        for (CollaboratorCentricTopComponent comp : CollaboratorCentricTopComponent.components.values()) {
            if (comp.temporalComponent != null) {
                comp.temporalComponent.close();
            }
            if (comp.optionComponent != null) {
                comp.optionComponent.close();
            }
        }

        for (CollaborativeTopComponent comp : cnComponents.values()) {
            if (comp.filterComponent != null && !comp.filterComponent.equals(this.filterComponent)) {
                comp.filterComponent.close();
            }
            if (comp.optionComponent != null && !comp.optionComponent.equals(this.optionComponent)) {
                comp.optionComponent.close();
            }
        }
    }

    @Override
    public void componentOpened() {
        createGraph();
        initGraphUtil();
        createView();
        filterComponent = new CollaborativeFiltersTopComponent(this);
        optionComponent = new CollaborativeOptionsTopComponent(this);
        pickVertex(selectedCollaborator);
    }

    @Override
    public void componentClosed() {
        cnComponents.remove(currentSnapshot.toString());
        filterComponent.close();
        filterComponent = null;
        optionComponent.close();
        optionComponent = null;
    }

    public void pickVertex(Contributor c) {
        if (c != null) {
            Contributor vertex = collaborators.get(c.getContributor());
            PickedState pickedVertexState = master.getPickedVertexState();
            pickedVertexState.clear();
            if (pickedVertexState.isPicked(vertex) == false) {
                pickedVertexState.pick(vertex, true);
            }

            graphUtil.centreVertex(master, vertex, false);
        }
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

    private void createGraph() {
        graph = new UndirectedSparseGraph<Contributor, Collaboration>();
        for (Contributor collaborator : collaborators.values()) {
            graph.addVertex(collaborator);
        }
        for (Collaboration collaboration : collaborations.values()) {
            graph.addEdge(collaboration, collaboration.getContributorA(), collaboration.getContributorB());
        }
    }

    private void createView() {
        graphLayout = new AggregateLayout<Contributor, Collaboration>(new FRLayout<Contributor, Collaboration>(graph));
        master = new VisualizationViewer<Contributor, Collaboration>(graphLayout, new Dimension(800, 800));

        satellite = new SatelliteVisualizationViewer<Contributor, Collaboration>(master, new Dimension(200, 200));
        satelliteScaler = new CrossoverScalingControl();
        satellite.scaleToLayout(satelliteScaler);
        masterLayer = new GraphZoomScrollPane(master);
        graphMouse = new DefaultModalGraphMouse();
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        graphMouse.add(new PopupGraphMousePlugin());
        master.setGraphMouse(graphMouse);

        satellite.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        satellite.setBounds(5, 5, 200, 200);
        satelliteLayer.setLayout(null);
        satelliteLayer.add(satellite);
        satelliteLayer.setOpaque(false);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.add(masterLayer, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(satelliteLayer, JLayeredPane.PALETTE_LAYER);
        add(layeredPane, BorderLayout.CENTER);
    }

    private void initData() {
        Map<String, Contributor> contributors = currentSnapshot.getSnapshotContributors();
        Forum forum = currentSnapshot.getForum();
        List<ForumThread> threads = forum.getThreads();
        for (ForumThread thread : threads) {
            List<ForumMessage> messages = thread.getMessages();

            Map<String, Contributor> iteratorMap = new HashMap<String, Contributor>();
            for (ForumMessage message : messages) {
                Integer point = message.getAwardedpoints();
                // if this message has been awarded message point.
                if (point != 0) {
                    String key = message.getOriginContributor().getContributor();
                    Contributor contributor = contributors.get(key);
                    iteratorMap.put(key, contributor);
                }
            }
            // if there is only ONE contributor in the temp map, it will not be added to the collaborator map
            if (iteratorMap.size() > 1) {
                collaborators.putAll(iteratorMap);
                Object[] array = (Object[]) iteratorMap.values().toArray();
                String key = "";
                for (int i = 0; i < iteratorMap.size(); i++) {
                    Contributor c1 = (Contributor) array[i];
                    String cid1 = c1.getContributor();
                    for (int j = i + 1; j < iteratorMap.size(); j++) {
                        Contributor c2 = (Contributor) array[j];
                        String cid2 = c2.getContributor();
                        if (cid1.hashCode() > cid2.hashCode()) {
                            key = cid2 + "-" + cid1;
                        } else {
                            key = cid1 + "-" + cid2;
                        }
                        if (collaborations.containsKey(key)) {
                            Collaboration obj = collaborations.get(key);
                            obj.setAmount(obj.getAmount() + 1);
                        } else {
                            Collaboration newObj = new Collaboration();
                            newObj.setAmount(1);
                            if (cid1.hashCode() > cid2.hashCode()) {
                                newObj.setContributorA(c2);
                                newObj.setContributorB(c1);
                            } else {
                                newObj.setContributorA(c1);
                                newObj.setContributorB(c2);
                            }
                            collaborations.put(key, newObj);
                        }
                    }
                }
            }
        }
    }

    private void initGraphUtil() {
        graphUtil = new GraphUtil<Contributor, Collaboration>(graph);
        Transformer<Collaboration, Integer> edgeWeights = new Transformer<Collaboration, Integer>() {

            @Override
            public Integer transform(Collaboration collaboration) {
                return collaboration.getAmount();
            }
        };
        BetweennessCentrality<Contributor, Collaboration> bc = new BetweennessCentrality<Contributor, Collaboration>(graph, edgeWeights);
        ClosenessCentrality<Contributor, Collaboration> cc = new ClosenessCentrality<Contributor, Collaboration>(graph, edgeWeights);
        graphUtil.setBc(bc);
        graphUtil.setCc(cc);
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
            final VisualizationViewer<Contributor, Collaboration> vv = (VisualizationViewer<Contributor, Collaboration>) e.getSource();
            final Point2D p = e.getPoint();

            GraphElementAccessor<Contributor, Collaboration> pickSupport = vv.getPickSupport();
            if (pickSupport != null) {
                final Contributor vertex = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
                if (vertex != null) {
                    JPopupMenu popup = new JPopupMenu();

                    popup.add(new AbstractAction("Goto Collaborator Centric Network") {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            CollaboratorCentricTopComponent ccc = CollaboratorCentricTopComponent.getComponent(currentSnapshot, vertex);
                            ccc.setName(vertex.getContributor() + "'s Collaborator Centric Network(" + currentSnapshot.getShortDesc() + ")");
                            ccc.open();
                            ccc.requestActive();
                        }
                    });

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