package net.chunxie.networkgraph.edge;

import net.chunxie.networkdata.entity.Contributor;
import net.chunxie.networkdata.entity.ForumMessage;
import net.chunxie.networkdata.entity.ForumThread;

import java.util.List;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 15:43
 */
public class MessagesEdge {

    private Contributor contributor;
    private ForumThread thread;

    private Integer amount = 0;
    private Integer points = 0;

    private List<ForumMessage> messages;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Contributor getContributor() {
        return contributor;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
    }

    public List<ForumMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ForumMessage> messages) {
        this.messages = messages;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public ForumThread getThread() {
        return thread;
    }

    public void setThread(ForumThread thread) {
        this.thread = thread;
    }
}