package net.chunxie.networkdata.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 07/02/13 13:22
 */
public class Forum implements Serializable {

    private static final long serialVersionUID = 1L;
    private String forumtitle = "";
    private String forumuri = "";
    private List<ForumThread> threads = null;

    public Forum() {
        threads = new ArrayList<ForumThread>();
    }

    public Forum(String forumuri) {
        this.forumuri = forumuri;
    }

    public String getForumtitle() {
        return forumtitle;
    }

    public void setForumtitle(String forumtitle) {
        this.forumtitle = forumtitle;
    }

    public String getForumuri() {
        return forumuri;
    }

    public void setForumuri(String forumuri) {
        this.forumuri = forumuri;
    }

    public List<ForumThread> getThreads() {
        return threads;
    }

    public void setThreads(List<ForumThread> threads) {
        this.threads = threads;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (forumuri != null ? forumuri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Forum)) {
            return false;
        }
        Forum other = (Forum) object;
        if ((this.forumuri == null && other.forumuri != null) || (this.forumuri != null && !this.forumuri.equals(other.forumuri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return forumtitle;
    }

    public String getUriShort() {
        int index1 = forumuri.indexOf("forum/");
        int index2 = forumuri.indexOf("#id");

        return forumuri.substring(index1 + 6, index2);
    }
}