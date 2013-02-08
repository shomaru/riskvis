package net.chunxie.pgsqldataimpl.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import net.chunxie.pgsqldataimpl.DataSourceConfigurationPanel;
import org.openide.util.NbPreferences;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 13:53
 */
public class DBConnection {

    private String driver;
    private String dburl;
    private String username;
    private String password;
    public static String ROBUST_SCN_FORUM;
    public static String ROBUST_SCN_MESSAGE_POINTS;
    public static String ROBUST_SCN_MESSAGE_CONTENT;
    public static String ROBUST_SCN_MESSAGES;
    public static String ROBUST_SCN_POINTS;
    public static String ROBUST_SCN_REPLIES;
    public static String ROBUST_SCN_THREADS;
    Connection con = null;
    Statement stmt = null;
    /** result set from the most recent openQueryRS call
     */
    public ResultSet rs = null;
    public boolean connectionReady = false;

    public DBConnection() {
        Preferences pref = NbPreferences.forModule(DataSourceConfigurationPanel.class);
        driver = pref.get("driver", "");
        dburl = pref.get("dburl", "");
        username = pref.get("username", "");
        password = pref.get("password", "");

        ROBUST_SCN_FORUM = pref.get("forum", "");
        ROBUST_SCN_MESSAGE_POINTS = pref.get("messagepoint", "");
        ROBUST_SCN_MESSAGE_CONTENT = pref.get("messagecontent", "");
        ROBUST_SCN_MESSAGES = pref.get("message", "");
        ROBUST_SCN_POINTS = pref.get("point", "");
        ROBUST_SCN_REPLIES = pref.get("reply", "");
        ROBUST_SCN_THREADS = pref.get("thread", "");

        pref.addPreferenceChangeListener(new PreferenceChangeListener() {

            @Override
            public void preferenceChange(PreferenceChangeEvent evt) {
                if (evt.getKey().equals("driver")) {
                    driver = evt.getNewValue();
                }
                if (evt.getKey().equals("dburl")) {
                    dburl = evt.getNewValue();
                }
                if (evt.getKey().equals("username")) {
                    username = evt.getNewValue();
                }
                if (evt.getKey().equals("password")) {
                    password = evt.getNewValue();
                }

                if (evt.getKey().equals("forum")) {
                    ROBUST_SCN_FORUM = evt.getNewValue();
                }
                if (evt.getKey().equals("messagepoint")) {
                    ROBUST_SCN_MESSAGE_POINTS = evt.getNewValue();
                }
                if (evt.getKey().equals("messagecontent")) {
                    ROBUST_SCN_MESSAGE_CONTENT = evt.getNewValue();
                }
                if (evt.getKey().equals("message")) {
                    ROBUST_SCN_MESSAGES = evt.getNewValue();
                }
                if (evt.getKey().equals("point")) {
                    ROBUST_SCN_POINTS = evt.getNewValue();
                }
                if (evt.getKey().equals("reply")) {
                    ROBUST_SCN_REPLIES = evt.getNewValue();
                }
                if (evt.getKey().equals("thread")) {
                    ROBUST_SCN_THREADS = evt.getNewValue();
                }
            }
        });
    }

    /**
     * <b>Opens default Database Connection to the BrandBuilders DB </b>
     */
    public void open() {
        connectionReady = false;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(dburl, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Couldn't load database driver: " + e.getMessage());
            throw new RuntimeException("Couldn't load database driver: " + e.getMessage(), e);
        } catch (SQLException e) {
            System.out.println(dburl + " " + username + " " + password + "SQLException caught (opening db con): " + e.getMessage());
            throw new RuntimeException(dburl + " " + username + " " + password + "SQLException caught (opening db con): "  + e.getMessage(), e);
        } catch (NullPointerException e) {
            System.out.println(dburl + " " + username + " " + password + " can't get database driver.. (opening db con): " + e.getMessage());
            throw new RuntimeException(dburl + " " + username + " " + password + " can't get database driver.. (opening db con): " + e.getMessage(), e);
        }

        if (con != null) {
            connectionReady = true;
        }

    }

    /**
     * <b>Closes current database connection</b>
     */
    public void close() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException caught (closing db conn): " + e.getMessage());
        }
        connectionReady = false;
    }

    public boolean next() {
        try {
            return rs.next();
        } catch (SQLException e) {
            System.err.println(e.toString() + ": Failed to get next record in ResultSet.");
        }
        return false;
    }

    public String getString(String param) {
        try {
            return rs.getString(param);
        } catch (SQLException e) {
            System.err.println(e.toString() + ": Failed to get parameter from result set.");
        }
        return null;
    }

    /** <b>creates a new statement,Performs the given query, closes the statement</b>
     * @param strSQL sql query string
     */
    public void executeUpdate(String strSQL) {
        try {
            stmt = con.createStatement();
        } catch (SQLException e) {
            System.out.println("SQLException caught: (create statement )" + e.getMessage());
        }

        try {
            stmt.executeUpdate(strSQL);
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException caught: (executing update )" + strSQL + e.getMessage());
        }
    }

    /** <b>Creates a new statement and result set for a given SQL query</b>
     * @param strSQL sql query string
     * @return result set from the successful query
     */
    public ResultSet openQueryRS(String strSQL) {

        try {
            if (con != null) {
                stmt = con.createStatement();
            } else {
                System.out.println("Cant create statemnt -null connection to DB (openQueryRS)");
            }

        } catch (SQLException e) {
            System.out.println("SQLException caught: (opening query rs-create statement " + strSQL + "))" + e.getMessage());
            throw new RuntimeException("SQLException caught: (opening query rs-create statement " + strSQL + "))" + e.getMessage(), e);
        }

        try {

            if (stmt != null) {
                rs = stmt.executeQuery(strSQL);
            } else {
                System.out.println("Cant execute query or open result set as the statement is null ");
            }

        } catch (SQLException e) {
            System.out.println("SQLException caught: (opening query rs-executing query))" + e.getMessage());
            throw new RuntimeException("SQLException caught: (opening query rs-executing query))" + e.getMessage(), e);
        }

        return rs;
    }

    /**
     * <b>Disposes of the current result set and statement.</b>
     */
    public void closeQueryRS() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException caught: (closing query rs) " + e.getMessage());
        }
    }
}