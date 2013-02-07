package net.chunxie.snapshotexplorer.node.factory;

import net.chunxie.networkdata.entity.ForumThread;
import net.chunxie.snapshotexplorer.node.ForumThreadNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

import java.beans.IntrospectionException;
import java.util.List;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 07/02/13 13:03
 */
public class ForumThreadChildFactory extends ChildFactory<ForumThread> {

    private List<ForumThread> resultList;

    public ForumThreadChildFactory(List<ForumThread> resultList) {
        this.resultList = resultList;
    }

    @Override
    protected boolean createKeys(List<ForumThread> list) {
        for (ForumThread t : resultList) {
            list.add(t);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(ForumThread key) {
        try {
            return new ForumThreadNode(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }
}
