// 
// Decompiled by Procyon v0.5.36
// 

package helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.sql.DriverManager;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.sql.Statement;
import java.sql.Connection;

public class UserManager
{
    private Connection c;
    private Statement stmt;
    private ReadWriteLock readWriteLock;
    private Lock readLock;
    private Lock writeLock;
    private static final Logger logger;
    
    public UserManager() {
        this.readWriteLock = new ReentrantReadWriteLock();
        this.readLock = this.readWriteLock.readLock();
        this.writeLock = this.readWriteLock.writeLock();
    }
    
    public String addingUser(final String login, final String password) {
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
            (this.c = DriverManager.getConnection("jdbc:postgresql://pg:5432/studs", "s313318", "mes758")).setAutoCommit(false);
            UserManager.logger.log(Level.INFO, "-- Opened database successfully");
            String sql = "";
            UserManager.logger.log(Level.INFO, "Login = " + login + " Password = " + password);
            this.stmt = this.c.createStatement();
            sql = "INSERT INTO USERS (LOGIN, PASSWORD) VALUES ('" + login + "', '" + password + "');";
            this.writeLock.lock();
            this.stmt.executeUpdate(sql);
            this.writeLock.unlock();
            final ResultSet rs = this.stmt.executeQuery("SELECT * FROM USERS;");
            Integer id = null;
            while (rs.next()) {
                id = rs.getInt("id");
            }
            this.stmt.close();
            this.c.commit();
            UserManager.logger.log(Level.INFO, "--New user was added successfully!" + id);
            result = result + "New user was added successfully!" + id;
        }
        catch (ClassNotFoundException | SQLException | NullPointerException ex2) {
            final Exception ex;
            final Exception e = ex;
            UserManager.logger.log(Level.WARNING, "User was not added! " + e.getMessage());
        }
        return result;
    }
    
    public String checkingID(final String command, final Integer id, final String userId) {
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
            (this.c = DriverManager.getConnection("jdbc:postgresql://pg:5432/studs", "s313318", "mes758")).setAutoCommit(false);
            this.stmt = this.c.createStatement();
            UserManager.logger.log(Level.INFO, "-- Opened database successfully");
            this.readLock.lock();
            final ResultSet rs = this.stmt.executeQuery("SELECT USERID FROM ORGANIZATIONS WHERE ID = " + id + " ;");
            this.readLock.unlock();
            result = "$CheckId$=" + command + "=" + id + "=";
            if (rs.next()) {
                final String userFromData = rs.getString(1);
                if (!userFromData.equals(userId)) {
                    result += "NoAccess";
                }
                else {
                    result += "true";
                }
            }
            else {
                result += "false";
            }
            rs.close();
            this.stmt.close();
            this.c.commit();
        }
        catch (ClassNotFoundException | SQLException ex2) {
            final Exception ex;
            final Exception e = ex;
            UserManager.logger.log(Level.WARNING, "Error with checking id!");
        }
        return result;
    }
    
    public String testingUser(final String login, final String password) {
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
            (this.c = DriverManager.getConnection("jdbc:postgresql://pg:5432/studs", "s313318", "mes758")).setAutoCommit(false);
            UserManager.logger.log(Level.INFO, "-- Opened database successfully");
            String sql = "";
            UserManager.logger.log(Level.INFO, "Login = " + login + " Password = " + password);
            this.stmt = this.c.createStatement();
            sql = "SELECT * FROM USERS WHERE LOGIN = '" + login + "' AND PASSWORD = '" + password + "';";
            this.writeLock.lock();
            final ResultSet rs = this.stmt.executeQuery(sql);
            this.writeLock.unlock();
            Integer id = null;
            if (rs.next()) {
                id = rs.getInt("id");
                result = "Welcome registered user!" + id;
            }
            else {
                result = "Users with this login/password don't exist";
            }
            this.stmt.close();
            this.c.commit();
        }
        catch (ClassNotFoundException | SQLException | NullPointerException ex2) {
            final Exception ex;
            final Exception e = ex;
            UserManager.logger.log(Level.INFO, "Error with testing login and password! " + e.getMessage());
        }
        return result;
    }
    
    static {
        logger = Logger.getLogger(UserManager.class.getName());
    }
}
