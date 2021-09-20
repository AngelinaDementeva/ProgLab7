// 
// Decompiled by Procyon v0.5.36
// 

package helpers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import data.Coordinates;
import data.Location;
import data.Address;
import data.OrganizationType;
import java.util.logging.Level;
import java.sql.DriverManager;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import data.Organization;
import java.util.LinkedList;
import java.util.Date;

public class CollectionChecker
{
    public static Date dateInitial;
    public static LinkedList<Organization> organizations;
    ReadWriteLock readWriteLock;
    Lock readLock;
    Lock writeLock;
    private static final Logger logger;
    
    public CollectionChecker() throws IOException {
        this.readWriteLock = new ReentrantReadWriteLock();
        this.readLock = this.readWriteLock.readLock();
        this.writeLock = this.readWriteLock.writeLock();
        Integer id = null;
        String name = null;
        Long annnualTurnover = null;
        String creationDate = null;
        String fullName = null;
        OrganizationType type = null;
        String street = null;
        Location town = null;
        Address address = null;
        Coordinates coordinates = null;
        Integer userid = null;
        try {
            Class.forName("org.postgresql.Driver");
            final Connection c = DriverManager.getConnection("jdbc:postgresql://pg:5432/studs", "s313318", "mes758");
            c.setAutoCommit(false);
            CollectionChecker.logger.log(Level.INFO, "-- Opened database successfully");
            final Statement stmt = c.createStatement();
            this.writeLock.lock();
            final ResultSet rs = stmt.executeQuery("SELECT * FROM ORGANIZATIONS;");
            this.writeLock.unlock();
            final LinkedList<String> ids = new LinkedList<String>();
            int goodElements = 0;
            int badElements = 0;
            boolean ok = true;
            while (rs.next()) {
                try {
                    id = rs.getInt("id");
                }
                catch (NumberFormatException e2) {
                    ok = false;
                }
                try {
                    ids.add(id.toString());
                }
                catch (Exception e3) {
                    ok = false;
                }
                try {
                    name = rs.getString("name");
                }
                catch (Exception e3) {
                    ok = false;
                }
                try {
                    annnualTurnover = rs.getLong("annualturnover");
                }
                catch (NumberFormatException e2) {
                    ok = false;
                }
                try {
                    creationDate = rs.getString("creationDate");
                    if (DataChecker.isValidDate(creationDate)) {
                        creationDate = rs.getString("creationDate");
                    }
                }
                catch (Exception e3) {
                    ok = false;
                }
                try {
                    fullName = rs.getString("fullName");
                }
                catch (Exception e3) {
                    ok = false;
                }
                try {
                    final String string;
                    final String type2 = string = rs.getString("organizationtype");
                    switch (string) {
                        case "TRUST": {
                            type = OrganizationType.TRUST;
                            break;
                        }
                        case "PRIVATE_LIMITED_COMPANY": {
                            type = OrganizationType.PRIVATE_LIMITED_COMPANY;
                            break;
                        }
                        case "OPEN_JOINT_STOCK_COMPANY": {
                            type = OrganizationType.OPEN_JOINT_STOCK_COMPANY;
                            break;
                        }
                    }
                }
                catch (IllegalArgumentException e4) {
                    ok = false;
                }
                try {
                    street = rs.getString("street");
                }
                catch (Exception e3) {
                    ok = false;
                }
                address = new Address();
                address.setStreet(street);
                town = new Location();
                try {
                    town.setX(rs.getInt("townx"));
                }
                catch (NumberFormatException e2) {
                    ok = false;
                }
                try {
                    town.setY(rs.getLong("towny"));
                }
                catch (NumberFormatException e2) {
                    ok = false;
                }
                town.setName(rs.getString("townname"));
                address.setTown(town);
                coordinates = new Coordinates();
                try {
                    coordinates.setX(rs.getDouble("coordx"));
                }
                catch (NumberFormatException e2) {
                    ok = false;
                }
                try {
                    coordinates.setY(rs.getFloat("coordy"));
                }
                catch (NumberFormatException e2) {
                    ok = false;
                }
                try {
                    userid = rs.getInt("userid");
                }
                catch (NumberFormatException e2) {
                    ok = false;
                }
                if (ok) {
                    final Organization organizationDB = new Organization(id, name, annnualTurnover, creationDate, fullName, type, address, coordinates, userid);
                    CollectionChecker.organizations.addFirst(organizationDB);
                    ++goodElements;
                }
                else {
                    ++badElements;
                }
            }
            rs.close();
            stmt.close();
            c.commit();
            CollectionChecker.logger.log(Level.INFO, "Collection was loaded succesfully! \nNumber of correct elements: " + goodElements + "\nNumber of corrupted elements: " + badElements);
            CollectionChecker.dateInitial = new Date();
        }
        catch (ClassNotFoundException | SQLException ex2) {
            final Exception ex;
            final Exception e = ex;
            CollectionChecker.logger.log(Level.SEVERE, "Syntax error! Try again! " + e.getMessage());
            System.exit(1);
        }
    }
    
    static {
        CollectionChecker.organizations = new LinkedList<Organization>();
        logger = Logger.getLogger(CollectionChecker.class.getName());
    }
}
