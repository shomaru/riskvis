package net.chunxie.networkgraph.util;

import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 15:39
 */
public class ClusterUtil<V, E> {

    private Map<E, Paint> edgePaints;
    private Map<V, Paint> vertexPaints;
    private VisualizationViewer<V, E> vv;
    private Set<Color> similarColors = null;
    private Object[] colorsArray;

    public ClusterUtil(Map<V, Paint> vertexPaints, Map<E, Paint> edgePaints, VisualizationViewer<V, E> vv) {
        this.vv = vv;
        this.vertexPaints = vertexPaints;
        this.edgePaints = edgePaints;
        init();
    }

    public Layout getLayoutFor(Class layoutClass, Graph<V, E> graph) {
        Layout<V, E> layout = null;
        if (layoutClass.equals(CircleLayout.class)) {
            layout = new CircleLayout<V, E>(graph);
        }
        if (layoutClass.equals(ISOMLayout.class)) {
            layout = new ISOMLayout<V, E>(graph);
        }
        if (layoutClass.equals(FRLayout.class)) {
            layout = new FRLayout<V, E>(graph);
        }
        if (layoutClass.equals(KKLayout.class)) {
            layout = new KKLayout<V, E>(graph);
        }
        if (layoutClass.equals(SpringLayout.class)) {
            layout = new SpringLayout<V, E>(graph);
        }
        return layout;
    }

    public void clusterAndRecolor(AggregateLayout<V, E> layout, Class subLayout, Dimension dimension,
                                  int numEdgesToRemove, Map<E, Integer> weightMap, boolean groupClusters) {
        Graph<V, E> g = layout.getGraph();
        layout.removeAll();

        WeightedEdgeBetweennessClusterer<V, E> clusterer =
                new WeightedEdgeBetweennessClusterer<V, E>(numEdgesToRemove, weightMap);
        Set<Set<V>> clusterSet = clusterer.transform(g);
        List<E> edges = clusterer.getEdgesRemoved();

        int i = 0;
        //Set the colors of each node so that each cluster's vertices have the same color
        for (Iterator<Set<V>> cIt = clusterSet.iterator(); cIt.hasNext();) {

            Set<V> vertices = cIt.next();
            Color c = (Color) colorsArray[i % colorsArray.length];

            colorCluster(vertices, c);
            if (groupClusters == true) {
                groupCluster(layout, subLayout, dimension, vertices);
            }
            i++;
        }
        for (E e : g.getEdges()) {
            if (edges.contains(e)) {
                edgePaints.put(e, Color.WHITE.brighter());
            } else {
                edgePaints.put(e, Color.GRAY);
            }
        }
    }

    private void colorCluster(Set<V> vertices, Color c) {
        for (V v : vertices) {
            vertexPaints.put(v, c);
        }
    }

    private void groupCluster(AggregateLayout<V, E> layout, Class subLayoutClass, Dimension dimension, Set<V> vertices) {
        if (vertices.size() < layout.getGraph().getVertexCount()) {
            Point2D center = layout.transform(vertices.iterator().next());
            Graph<V, E> subGraph = SparseMultigraph.<V, E>getFactory().create();
            for (V v : vertices) {
                subGraph.addVertex(v);
            }
            Layout<V, E> subLayout = getLayoutFor(subLayoutClass, subGraph);

            subLayout.setInitializer(vv.getGraphLayout());
            subLayout.setSize(dimension);

            layout.put(subLayout, center);
        }
    }

    private void init() {
        similarColors = new HashSet<Color>();
        for (int i = 0; i < 1000; i++) {
            Random random = new Random();
            float hue = random.nextFloat();
            // Saturation between 0.1 and 0.3
            float saturation = (random.nextInt(2000) + 1000) / 10000f;
            // luminance between 0.7 and 0.95
            float luminance = (random.nextInt(1500) + 7000) / 10000f;
            Color color = Color.getHSBColor(hue, saturation, luminance);

            //fill our array with random colors
            similarColors.add(color);
        }
        colorsArray = similarColors.toArray();
    }
}