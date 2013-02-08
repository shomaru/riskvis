package net.chunxie.pgsqldataimpl.dao;

import net.chunxie.networkdata.entity.Contributor;
import net.chunxie.networkdata.entity.ForumMessage;
import net.chunxie.networkdata.entity.ForumThread;
import net.chunxie.pgsqldataimpl.util.DBConnection;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 13:57
 */
public class ForumMessageDao {

    private DBConnection dc;

    public ForumMessageDao() {
        dc = new DBConnection();
    }

    public List<ForumMessage> findForumMessages(String sql) {
        List<ForumMessage> result = new ArrayList<ForumMessage>();
        try {
            dc.open();
            dc.openQueryRS(sql);
            ResultSetMetaData metadata = dc.rs.getMetaData();
            String contributorType = metadata.getColumnTypeName(4);
            while (dc.rs.next()) {
                String messageuri = dc.rs.getString("messageuri");
                String threaduri = dc.rs.getString("threaduri");
                String messagetitle = dc.rs.getString("messagetitle");
                String contributor = "";
                if ("int4".equals(contributorType)) {
                    contributor = "" + dc.rs.getInt("contributor");
                } else {
                    contributor = dc.rs.getString("contributor");
                }
                String creationdate = dc.rs.getString("creationdate");
                Integer awardedpoints = dc.rs.getInt("awardedpoints");

                ForumThread originalForumThread = new ForumThread(threaduri);
                Contributor originalContributor = new Contributor(contributor);
                ForumMessage obj = new ForumMessage(messageuri);
                obj.setOriginThread(originalForumThread);
                obj.setMessagetitle(messagetitle);
                obj.setOriginContributor(originalContributor);
                obj.setCreationdate(creationdate);
                if (awardedpoints != null) {
                    obj.setAwardedpoints(awardedpoints);
                }
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

    public ForumMessage findOriginalMessage(ForumMessage message) {
        String sql1 = "SELECT * FROM robust_SCN_replies WHERE reply = '" + message.getMessageuri() + "'";
        ForumMessage result = null;
        try {
            dc.open();
            dc.openQueryRS(sql1);
            while (dc.rs.next()) {
                //get uri of the original message to which this reply was produced
                String originalMessageuri = dc.rs.getString("original");
                String sql2 = "SELECT * FROM robust_SCN_messages WHERE messageuri = '" + originalMessageuri + "'";
                List<ForumMessage> list = findForumMessages(sql2);
                if (list.size() == 1) {
                    result = list.get(0);
                }
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