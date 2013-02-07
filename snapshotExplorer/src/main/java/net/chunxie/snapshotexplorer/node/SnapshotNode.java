package net.chunxie.snapshotexplorer.node;

import net.chunxie.networkdata.entity.Snapshot;
import net.chunxie.snapshotexplorer.action.OpenCollaborativeNetworkAction;
import net.chunxie.snapshotexplorer.node.factory.ForumThreadChildFactory;
import net.chunxie.snapshotexplorer.util.IconUtil;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

import javax.swing.*;
import java.awt.Image;
import java.beans.IntrospectionException;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 07/02/13 12:53
 */
public class SnapshotNode extends BeanNode {

    private Snapshot snapshot;

    public SnapshotNode(Snapshot snapshot) throws IntrospectionException {
        super(snapshot, Children.create(new ForumThreadChildFactory(snapshot.getForum().getThreads()), true), Lookups.singleton(snapshot));
        this.snapshot = snapshot;
        setDisplayName(snapshot.getShortDesc());
    }

    @Override
    public Image getIcon(int type) {
        return IconUtil.SNAPSHOT_ICON_16;
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[] {
                new OpenCollaborativeNetworkAction(snapshot)
        };
    }


}
