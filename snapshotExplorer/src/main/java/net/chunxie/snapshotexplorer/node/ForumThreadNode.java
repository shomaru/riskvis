package net.chunxie.snapshotexplorer.node;

import net.chunxie.networkdata.entity.ForumThread;
import net.chunxie.snapshotexplorer.node.factory.ForumMessageChildFactory;
import net.chunxie.snapshotexplorer.util.IconUtil;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

import java.awt.Image;
import java.beans.IntrospectionException;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 07/02/13 12:58
 */
public class ForumThreadNode extends BeanNode {

    private ForumThread thread;

    public ForumThreadNode(ForumThread bean) throws IntrospectionException {
        super(bean, Children.create(new ForumMessageChildFactory(bean.getMessages()), true), Lookups.singleton(bean));
        this.thread = bean;
        setDisplayName(thread.getThreadtitle());
    }

    @Override
    public Image getIcon(int type) {
        return IconUtil.THREAD_GREY_16;
    }
}