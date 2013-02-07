package net.chunxie.snapshotexplorer.node;

import net.chunxie.networkdata.entity.ForumMessage;
import net.chunxie.snapshotexplorer.util.IconUtil;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

import java.awt.Image;
import java.beans.IntrospectionException;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 07/02/13 13:00
 */
public class ForumMessageNode extends BeanNode {

    private ForumMessage message;

    public ForumMessageNode(ForumMessage bean) throws IntrospectionException {
        super(bean, Children.LEAF, Lookups.singleton(bean));
        this.message = bean;
        setDisplayName(bean.getMessagetitle());
    }

    @Override
    public Image getIcon(int type) {
        Image result = IconUtil.FORUM_MESSAGE_ICON_16;
        if (10 == message.getAwardedpoints()) {
            result = IconUtil.FULL_STAR_ICON_16;
        } else if (6 == message.getAwardedpoints()) {
            result = IconUtil.HALF_STAR_ICON_16;
        } else if (2 == message.getAwardedpoints()) {
            result = IconUtil.OFF_STAR_ICON_16;
        }
        return result;
    }
}