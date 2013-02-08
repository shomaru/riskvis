package net.chunxie.networkgraph;

import net.chunxie.networkdata.entity.ForumThread;
import org.openide.nodes.BeanNode;

import java.beans.IntrospectionException;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 15:45
 */
public class ThreadNode extends BeanNode {

    private ForumThread thread;

    public ThreadNode(ForumThread bean) throws IntrospectionException {
        super(bean);
        this.thread = bean;
        setDisplayName("Thread " + thread.getUriShort());
    }
}