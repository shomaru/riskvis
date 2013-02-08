package net.chunxie.networkgraph;

import net.chunxie.networkdata.entity.Contributor;
import org.openide.nodes.BeanNode;

import java.beans.IntrospectionException;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 15:44
 */
public class CollaboratorNode extends BeanNode {

    private Contributor collaborator;

    public CollaboratorNode(Contributor bean) throws IntrospectionException {
        super(bean);
        this.collaborator = bean;
        setDisplayName("Collaborator" + collaborator.getContributor());
    }
}