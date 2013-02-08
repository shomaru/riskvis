package net.chunxie.networkgraph.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import org.apache.commons.collections15.Transformer;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 15:29
 */
public class WeightedEdgeBetweennessClusterer<V, E> implements Transformer<Graph<V, E>, Set<Set<V>>> {

    private int mNumEdgesToRemove;
    private Map<E, Integer> mWeight;
    private Map<E, Pair<V>> edges_removed;

    /**
     * Constructs a new clusterer for the specified graph.
     * @param numEdgesToRemove the number of edges to be progressively removed from the graph
     */
    public WeightedEdgeBetweennessClusterer(int numEdgesToRemove, Map<E, Integer> weight) {
        mWeight = weight;
        mNumEdgesToRemove = numEdgesToRemove;
        edges_removed = new LinkedHashMap<E, Pair<V>>();
    }

    /**
     * Finds the set of clusters which have the strongest "community structure".
     * The more edges removed the smaller and more cohesive the clusters.
     * @param graph the graph
     */
    public Set<Set<V>> transform(Graph<V, E> graph) {

        if (mNumEdgesToRemove < 0 || mNumEdgesToRemove > graph.getEdgeCount()) {
            throw new IllegalArgumentException("Invalid number of edges passed in.");
        }

        edges_removed.clear();

        for (int k = 0; k < mNumEdgesToRemove; k++) {
            BetweennessCentrality<V, E> bc = new BetweennessCentrality<V, E>(graph);
            E to_remove = null;
            double score = 0;
            for (E e : graph.getEdges()) {
                Integer weight = mWeight.get(e);
                if (bc.getEdgeScore(e) / weight > score) {
                    to_remove = e;
                    score = bc.getEdgeScore(e) / weight;
                }
            }
            edges_removed.put(to_remove, graph.getEndpoints(to_remove));
            graph.removeEdge(to_remove);
        }

        WeakComponentClusterer<V, E> wcSearch = new WeakComponentClusterer<V, E>();
        Set<Set<V>> clusterSet = wcSearch.transform(graph);

        for (Map.Entry<E, Pair<V>> entry : edges_removed.entrySet()) {
            Pair<V> endpoints = entry.getValue();
            graph.addEdge(entry.getKey(), endpoints.getFirst(), endpoints.getSecond());
        }
        return clusterSet;
    }

    /**
     * Retrieves the list of all edges that were removed
     * (assuming extract(...) was previously called).
     * The edges returned
     * are stored in order in which they were removed.
     *
     * @return the edges in the original graph
     */
    public List<E> getEdgesRemoved() {
        return new ArrayList<E>(edges_removed.keySet());
    }
}