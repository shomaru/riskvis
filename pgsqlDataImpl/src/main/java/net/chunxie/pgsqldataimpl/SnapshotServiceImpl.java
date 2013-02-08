package net.chunxie.pgsqldataimpl;

import java.util.List;

import net.chunxie.networkdata.api.SnapshotService;
import net.chunxie.networkdata.entity.Forum;
import net.chunxie.networkdata.entity.ForumMessage;
import net.chunxie.networkdata.entity.ForumThread;
import net.chunxie.networkdata.entity.Snapshot;
import net.chunxie.pgsqldataimpl.dao.ForumDao;
import net.chunxie.pgsqldataimpl.dao.ForumMessageDao;
import net.chunxie.pgsqldataimpl.dao.ForumThreadDao;
import net.chunxie.pgsqldataimpl.util.DBConnection;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 14:00
 */
@ServiceProvider(service = SnapshotService.class)
public class SnapshotServiceImpl implements SnapshotService {

    private Snapshot snapshot = null;
    ForumDao forumDao = new ForumDao();
    ForumThreadDao threadDao = new ForumThreadDao();
    ForumMessageDao messageDao = new ForumMessageDao();

    @Override
    public Snapshot buildSnapshot(Snapshot s) {
        this.snapshot = s;
        return snapshot;
    }

    @Override
    public List<ForumThread> findSnapshotThreads(Snapshot s) {
        String lowerTime = s.getStartDate();
        String upperTime = s.getEndDate();
        String sql = "SELECT DISTINCT(t.threaduri) AS threaduri, threadtitle, status, noofviews, forumuri "
                + "FROM " + DBConnection.ROBUST_SCN_MESSAGES + " m, " + DBConnection.ROBUST_SCN_THREADS + " t "
                + "WHERE m.threaduri = t.threaduri "
                + "AND forumuri = '" + s.getForum().getForumuri() + "' "
                + "AND creationDate::timestamp "
                + "BETWEEN '" + lowerTime + "'::TIMESTAMP "
                + "AND '" + upperTime + "'::TIMESTAMP "
                + "ORDER BY noofviews DESC";
        return threadDao.findForumThreads(sql);
    }

    @Override
    public List<Forum> findAllForums() {
        String sql = "SELECT *"
                + " FROM " + DBConnection.ROBUST_SCN_FORUM;
        return forumDao.findForums(sql);
    }

    @Override
    public List<ForumMessage> findSnapshotMessagesWithinThread(Snapshot s, ForumThread t) {
        String lowerTime = s.getStartDate();
        String upperTime = s.getEndDate();
        String sql = "SELECT m.messageuri AS messageuri, threaduri, messagetitle, contributor, creationdate::TIMESTAMP AS creationdate, awardedpoints "
                + "FROM " + DBConnection.ROBUST_SCN_MESSAGES + " m "
                //+ "INNER JOIN robust_SCN_messagecontent mc "
                //+ "ON m.messageuri = mc.messageuri "
                + "LEFT JOIN " + DBConnection.ROBUST_SCN_MESSAGE_POINTS + " mp "
                + "ON m.messageuri=mp.messageuri "
                + "WHERE threaduri = '" + t.getThreaduri() + "' "
                + "AND awardedpoints > 0 "
                + "AND creationDate::timestamp "
                + "BETWEEN '" + lowerTime + "'::TIMESTAMP "
                + "AND '" + upperTime + "'::TIMESTAMP "
                + "ORDER BY creationdate::TIMESTAMP ASC";
        return messageDao.findForumMessages(sql);
    }
}