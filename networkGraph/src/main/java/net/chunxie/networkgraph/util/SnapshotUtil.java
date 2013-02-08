package net.chunxie.networkgraph.util;

import net.chunxie.networkdata.entity.Snapshot;

import java.util.Calendar;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 15:26
 */
public class SnapshotUtil {

    private Snapshot snapshot;

    public SnapshotUtil(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    public String getStartDay() {
        return snapshot.getStartDate().substring(8, 10);
    }

    public String getStartMonth() {
        return snapshot.getStartDate().substring(5, 7);
    }

    public String getStartYear() {
        return snapshot.getStartDate().substring(0, 4);
    }

    public String getEndDay() {
        return snapshot.getEndDate().substring(8, 10);
    }

    public String getEndMonth() {
        return snapshot.getEndDate().substring(5, 7);
    }

    public String getEndYear() {
        return snapshot.getEndDate().substring(0, 4);
    }

    public Calendar getStartCal() {
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(getStartYear()), Integer.parseInt(getStartMonth()) - 1, Integer.parseInt(getStartDay()));
        return cal;
    }

    public Calendar getEndCal() {
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(getEndYear()), Integer.parseInt(getEndMonth()) - 1, Integer.parseInt(getEndDay()));
        cal.add(Calendar.DATE, -1);
        return cal;
    }

    public int getDayNum() {
        long DAY_IN_MILLIS = 24L * 60L * 60L * 1000L;
        return (int) (getEndCal().getTimeInMillis() / DAY_IN_MILLIS - getStartCal().getTimeInMillis() / DAY_IN_MILLIS);
    }
}