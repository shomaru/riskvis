package net.chunxie.networkdata.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 07/02/13 13:24
 */
public class ForumMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private String messageuri;
    private String threaduri;
    private String messagetitle;
    private String creationdate;
    private String text;
    private Integer awardedpoints = 0;
    //if this is a reply message - specify the reference to original message
    private ForumMessage originalMessage = null;
    //forum thread from which this message originates from
    private ForumThread originThread = null;
    private Contributor originContributor = null;

    public ForumMessage(String messageuri) {
        this.messageuri = messageuri;
    }

    public String getMessageuri() {
        return messageuri;
    }

    public void setMessageuri(String messageuri) {
        this.messageuri = messageuri;
    }

    public String getThreaduri() {
        return threaduri;
    }

    public void setThreaduri(String threaduri) {
        this.threaduri = threaduri;
    }

    public String getMessagetitle() {
        return messagetitle;
    }

    public void setMessagetitle(String messagetitle) {
        this.messagetitle = messagetitle;
    }

    public String getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(String creationdate) {
        this.creationdate = creationdate;
    }

    public Integer getAwardedpoints() {
        return awardedpoints;
    }

    public void setAwardedpoints(Integer awardedpoints) {
        this.awardedpoints = awardedpoints;
    }

    public ForumThread getOriginThread() {
        return originThread;
    }

    public void setOriginThread(ForumThread originThread) {
        this.originThread = originThread;
    }

    public ForumMessage getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(ForumMessage originalMessage) {
        this.originalMessage = originalMessage;
    }

    public Contributor getOriginContributor() {
        return originContributor;
    }

    public void setOriginContributor(Contributor originContributor) {
        this.originContributor = originContributor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUriShort() {
        int index1 = messageuri.indexOf("message/");
        int index2 = messageuri.indexOf("#id");

        return messageuri.substring(index1 + 8, index2);
    }

    public Calendar getCreationCal() {
        Calendar cal = Calendar.getInstance();
        if (creationdate != null) {
            Integer year = Integer.parseInt(creationdate.substring(0, 4));
            Integer month = Integer.parseInt(creationdate.substring(5, 7));
            Integer date = Integer.parseInt(creationdate.substring(8, 10));
            Integer hourOfDay = Integer.parseInt(creationdate.substring(11, 13));
            Integer minute = Integer.parseInt(creationdate.substring(14, 16));
            Integer second = Integer.parseInt(creationdate.substring(17, 19));
            cal.set(year, month - 1, date, hourOfDay, minute, second);
        }
        return cal;
    }

    public boolean isSameDate(Calendar anotherCal) {
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        return (df.format(getCreationCal().getTime()).equals(df.format(anotherCal.getTime())));
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (messageuri != null ? messageuri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ForumMessage)) {
            return false;
        }
        ForumMessage other = (ForumMessage) object;
        if ((this.messageuri == null && other.messageuri != null) || (this.messageuri != null && !this.messageuri.equals(other.messageuri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return messageuri;
    }
}