package net.chunxie.pgsqldataimpl.dao;

import net.chunxie.networkdata.entity.Forum;
import net.chunxie.networkdata.entity.ForumThread;
import net.chunxie.pgsqldataimpl.util.DBConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 13:58
 */
public class ForumThreadDao {

    private DBConnection dc;

    public ForumThreadDao() {
        dc = new DBConnection();
    }

    public List<ForumThread> findForumThreads(String sql) {
        List<ForumThread> result = new ArrayList<ForumThread>();
        try {
            dc.open();
            dc.openQueryRS(sql);
            while (dc.rs.next()) {
                String threaduri = dc.rs.getString("threaduri");
                String threadtitle = dc.rs.getString("threadtitle");
                String status = dc.rs.getString("status");
                double noofviews = dc.rs.getDouble("noofviews");
                String forumuri = dc.rs.getString("forumuri");
                Forum originalForum = new Forum(forumuri);
                ForumThread obj = new ForumThread(threaduri);
                obj.setThreadtitle(threadtitle);
                obj.setStatus(status);
                obj.setNoofviews(noofviews);
                obj.setOriginalForum(originalForum);
                result.add(obj);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("SQLException caught: " + ex.getMessage(), ex);
        } finally {
            dc.closeQueryRS();
            dc.close();
        }
        return result;
    }
}