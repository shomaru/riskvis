package net.chunxie.networkdata.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 07/02/13 13:23
 */
public class ForumThread implements Serializable {
    private static final long serialVersionUID = 1L;

    private String threadtitle = "";

    private String threaduri = "";

    private String status = "";

    private Double noofviews = 0.0d;

    private Forum originalForum = null;

    private List<ForumMessage> messages = null;

    private List<Contributor> contributors = null;

    public ForumThread(String threaduri) {
        this.threaduri = threaduri;
        messages = new ArrayList<ForumMessage>();
        contributors = new ArrayList<Contributor>();
    }

    public String getThreadtitle() {
        return threadtitle;
    }

    public void setThreadtitle(String threadtitle) {
        this.threadtitle = threadtitle;
    }

    public String getThreaduri() {
        return threaduri;
    }

    public void setThreaduri(String threaduri) {
        this.threaduri = threaduri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getNoofviews() {
        return noofviews;
    }

    public void setNoofviews(Double noofviews) {
        this.noofviews = noofviews;
    }

    public Forum getOriginalForum() {
        return originalForum;
    }

    public void setOriginalForum(Forum originalForum) {
        this.originalForum = originalForum;
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public List<ForumMessage> getMessages() {
        return messages;
    }

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }

    public void setMessages(List<ForumMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (threaduri != null ? threaduri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ForumThread)) {
            return false;
        }
        ForumThread other = (ForumThread) object;
        if ((this.threaduri == null && other.threaduri != null) || (this.threaduri != null && !this.threaduri.equals(other.threaduri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return threaduri;
    }

    public String getUriShort() {
        int index1 = threaduri.indexOf("thread/");
        int index2 = threaduri.indexOf("#id");

        return threaduri.substring(index1 + 7, index2);
    }

    public String getStatusShort() {
        int index1 = status.indexOf("messageStatus/");
        int index2 = status.indexOf("#id");

        return status.substring(index1 + 14, index2);
    }
}