package net.chunxie.snapshotexplorer.node.factory;

import net.chunxie.networkdata.entity.ForumMessage;
import net.chunxie.snapshotexplorer.node.ForumMessageNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

import java.beans.IntrospectionException;
import java.util.List;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 07/02/13 13:04
 */
public class ForumMessageChildFactory extends ChildFactory<ForumMessage> {

    private List<ForumMessage> resultList;

    public ForumMessageChildFactory(List<ForumMessage> resultList) {
        this.resultList = resultList;
    }

    @Override
    protected boolean createKeys(List<ForumMessage> list) {
        for (ForumMessage m : resultList) {
            list.add(m);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(ForumMessage key) {
        try {
            return new ForumMessageNode(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }
}