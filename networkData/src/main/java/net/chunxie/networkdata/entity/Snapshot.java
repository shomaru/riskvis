package net.chunxie.networkdata.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 07/02/13 13:24
 */
public class Snapshot implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer startYear;
    private Integer startMonth;
    private Integer duration;
    private Integer threadsToConsider;
    private String startDate = null;
    private String endDate = null;
    private Forum forum = null;
    //this lists the total number of snapshot messages produces by all contributors across all threads and forums (if there are more than one)
    private List<ForumMessage> snapshotMessages = new ArrayList<ForumMessage>();
    //this lists the total number of snapshot contributors belonging to forums and threads considered in this snapshot
    private Map<String, Contributor> snapshotContributors = new HashMap<String, Contributor>();
    // this lists the total number of snapshot collaborators belonging to forums and threads considered in this snapshot
    private Map<String, Contributor> snapshotCollaborators;

    public Snapshot(Integer startYear, Integer startMonth, Integer duration, Integer threadsToConsider) {
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.duration = duration;
        this.threadsToConsider = threadsToConsider;
    }

    public Snapshot(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public Map<String, Contributor> getSnapshotContributors() {
        return snapshotContributors;
    }

    public void setSnapshotContributors(Map<String, Contributor> snapshotContributors) {
        this.snapshotContributors = snapshotContributors;
    }

    public Map<String, Contributor> getSnapshotCollaborators() {
        return snapshotCollaborators;
    }

    public void setSnapshotCollaborators(Map<String, Contributor> snapshotCollaborators) {
        this.snapshotCollaborators = snapshotCollaborators;
    }

    public List<ForumMessage> getSnapshotMessages() {
        return snapshotMessages;
    }

    public void setSnapshotMessages(List<ForumMessage> snapshotMessages) {
        this.snapshotMessages = snapshotMessages;
    }

    public Integer getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(Integer startMonth) {
        this.startMonth = startMonth;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getThreadsToConsider() {
        return threadsToConsider;
    }

    public void setThreadsToConsider(Integer threadsToConsider) {
        this.threadsToConsider = threadsToConsider;
    }

    @Override
    public String toString() {
        return forum.getUriShort() + ", " + getStartDate() + ", " + getEndDate();
    }

    public String getShortDesc() {
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        return forum.getForumtitle() + ", " + df.format(getStartCal().getTime()) + "-" + df.format(getEndCal().getTime());
    }

    public String getStartDate() {
        String result = null;
        if (startDate == null) {
            String fixStartMonth = "" + startMonth;
            if (startMonth < 10) {
                fixStartMonth = "0" + fixStartMonth;
            }
            result = startYear + "-" + fixStartMonth + "-" + "01";
        } else {
            result = startDate;
        }
        return result;
    }

    public String getEndDate() {
        String result = null;
        if (endDate == null) {
            //in months
            int distance = 1;
            int currentSnapshotYear = getStartYear();
            int currentSnapshotMonth = getStartMonth();

            while (distance <= duration) {
                if (currentSnapshotMonth == 12) {
                    currentSnapshotMonth = 1;
                    currentSnapshotYear++;
                    distance++;
                } else {
                    currentSnapshotMonth++;
                    distance++;
                }
            }
            String fixCurrentSnapshotMonth = "" + currentSnapshotMonth;
            if (currentSnapshotMonth < 10) {
                fixCurrentSnapshotMonth = "0" + fixCurrentSnapshotMonth;
            }
            result = currentSnapshotYear + "-" + fixCurrentSnapshotMonth + "-" + "01";
        } else {
            result = endDate;
        }
        return result;
    }

    public Calendar getStartCal() {
        startDate = getStartDate();
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(startDate.substring(0, 4)), Integer.parseInt(startDate.substring(5, 7)) - 1, Integer.parseInt(startDate.substring(8, 10)));
        return cal;
    }

    public Calendar getEndCal() {
        endDate = getEndDate();
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(endDate.substring(0, 4)), Integer.parseInt(endDate.substring(5, 7)) - 1, Integer.parseInt(endDate.substring(8, 10)));
        cal.add(Calendar.DATE, -1);
        return cal;
    }
}