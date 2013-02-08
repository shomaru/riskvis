package net.chunxie.pgsqldataimpl.dao;

import net.chunxie.networkdata.entity.Forum;
import net.chunxie.pgsqldataimpl.util.DBConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 13:56
 */
public class ForumDao {

    private DBConnection dc;

    public ForumDao() {
        dc = new DBConnection();
    }

    public List<Forum> findForums(String sql) {
        List<Forum> result = new ArrayList<Forum>();
        try {
            dc.open();
            dc.openQueryRS(sql);
            while (dc.rs.next()) {
                String forumTitle = dc.rs.getString("forumtitle");
                String forumUri = dc.rs.getString("forumuri");
                Forum obj = new Forum(forumUri);
                obj.setForumtitle(forumTitle);
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