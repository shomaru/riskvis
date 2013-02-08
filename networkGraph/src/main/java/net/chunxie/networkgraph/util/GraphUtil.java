package net.chunxie.networkgraph.util;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import net.chunxie.networkdata.entity.Contributor;
import net.chunxie.networkdata.entity.ForumThread;
import net.chunxie.networkgraph.edge.Collaboration;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 15:40
 */
public class GraphUtil<V, E> {

    private BetweennessCentrality<V, E> bc = null;
    private ClosenessCentrality<V, E> cc = null;
    private Graph<V, E> graph = null;

    public GraphUtil(Graph<V, E> graph) {
        this.graph = graph;
    }

    public BetweennessCentrality<V, E> getBc() {
        return bc;
    }

    public ClosenessCentrality<V, E> getCc() {
        return cc;
    }

    public Graph<V, E> getGraph() {
        return graph;
    }

    public void setBc(BetweennessCentrality<V, E> bc) {
        this.bc = bc;
    }

    public void setCc(ClosenessCentrality<V, E> cc) {
        this.cc = cc;
    }

    public void setGraph(Graph<V, E> graph) {
        this.graph = graph;
    }

    /**
     * get the number of collaborators in a collaborator-centric graph
     * including centric collaborator
     */
    public Integer getCollaboratorsNum() {
        Integer num = 0;
        for (V vertex : graph.getVertices()) {
            if (vertex instanceof Contributor) {
                num++;
            }
        }
        return num;
    }

    /**
     * get the number of thread in a collaborator-centric graph
     */
    public Integer getThreadsNum() {
        Integer num = 0;
        for (V vertex : graph.getVertices()) {
            if (vertex instanceof ForumThread) {
                num++;
            }
        }
        return num;
    }

    /**
     * get max total message points in a graph
     */
    public Integer getMaxPartialPoint() {
        Integer max = 0;
        for (V vertex : graph.getVertices()) {
            if (vertex instanceof Contributor) {
                Contributor c = (Contributor) vertex;
                Integer degree = c.getPartialPoints();
                if (degree > max) {
                    max = degree;
                }
            }
        }
        return max;
    }

    /**
     * get max number of collaborations (also the edge weight) in a graph
     */
    public Integer getMaxCollaborationNum() {
        Integer max = 0;
        for (E edge : graph.getEdges()) {
            if (edge instanceof Collaboration) {
                Collaboration c = (Collaboration) edge;
                Integer collaborationNum = c.getAmount();
                if (collaborationNum > max) {
                    max = collaborationNum;
                }
            }
        }
        return max;
    }

    /**
     * get max vertex degree in a graph
     */
    public Integer getMaxDegree() {
        Integer max = 0;
        for (V vertex : graph.getVertices()) {
            Integer degree = graph.degree(vertex);
            if (degree > max) {
                max = degree;
            }
        }
        return max;
    }

    /**
     * get max vertex Betweenness Centrality in a graph
     */
    public Double getMaxBetweennessCentrality() {
        Double max = 0d;
        for (V vertex : graph.getVertices()) {
            Double score = bc.getVertexScore(vertex);
            if (score > max) {
                max = score;
            }
        }
        return max;
    }

    /**
     * get max vertex Closeness Centrality in a graph
     */
    public Double getMaxClosenessCentrality() {
        Double max = 0d;
        for (V vertex : graph.getVertices()) {
            Double score = cc.getVertexScore(vertex);
            if (score > max) {
                max = score;
            }
        }
        return max;
    }

    /**
     * pick a number of nodes
     */
    public void pickVertices(VisualizationViewer vv, List<V> list) {
        PickedState pickedVertexState = vv.getPickedVertexState();
        pickedVertexState.clear();
        for (Object vertex : list) {
            if (pickedVertexState.isPicked(vertex) == false) {
                pickedVertexState.pick(vertex, true);
            }
        }
    }

    /**
     * pick a single node
     */
    public void pickVertex(VisualizationViewer vv, V vertex) {
        List<V> list = new ArrayList<V>();
        pickVertices(vv, list);
    }

    /**
     *  start a Thread to animate the translation of the graph so that the given Vertex
     *  moves to the centre of the view
     */
    public void centreVertex(final VisualizationViewer vv, V vertex, boolean animated) {
        Point2D newCenter = null;
        if (vertex != null) {
            // center the picked vertex
            Layout<V, E> layout = vv.getGraphLayout();
            newCenter = layout.transform(vertex);
        } else {
            // they did not pick a vertex to center, so just center the graph
            newCenter = vv.getCenter();
        }

        Point2D lvc = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(vv.getCenter());

        final double scale = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).getScale();
        final double dx = (lvc.getX() - newCenter.getX()) / scale;
        final double dy = (lvc.getY() - newCenter.getY()) / scale;

        final MutableTransformer layout = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
        if (animated) {
            Runnable animator = new Runnable() {

                @Override
                public void run() {
                    for (int i = 0; i < 10; i++) {
                        layout.translate(dx / 10, dy / 10);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                        }
                    }
                }
            };
            Thread thread = new Thread(animator);
            thread.start();
        } else {
            layout.translate(dx, dy);
        }
    }
}