package net.chunxie.snapshotexplorer.node.factory;

import net.chunxie.networkdata.entity.Snapshot;
import net.chunxie.snapshotexplorer.node.SnapshotNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

import java.beans.IntrospectionException;
import java.util.List;
import java.util.Map;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 07/02/13 13:01
 */
public class SnapshotChildFactory extends ChildFactory<Snapshot> {

    private Map<String, Snapshot> snapshotMap;

    public SnapshotChildFactory(Map<String, Snapshot> snapshotMap) {
        this.snapshotMap = snapshotMap;
    }

    @Override
    protected boolean createKeys(List<Snapshot> list) {
        for (Snapshot s : snapshotMap.values()) {
            list.add(s);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(Snapshot key) {
        try {
            return new SnapshotNode(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }
}