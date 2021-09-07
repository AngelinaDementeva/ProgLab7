package psql;

import data.Address;
import data.Coordinates;
import data.Organization;
import data.OrganizationType;
import data.User;
import java.rmi.ConnectIOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class PSQLWorker {

    private String cString;
    private String usersTableName;
    private String colnsTableName;
    private String userName;
    private String password;
    private ReentrantLock lock;

    public static final String selectSQLString = "SELECT * FROM %s";
    public static final String addUSQLString = "INSERT INTO %s VALUES(%d,'%s','%s')";
    public static final String addCSQLString = "INSERT INTO %s VALUES(%d,'%s',%d,%d,%f,'%s','%s','%s',%d,'%s')";
    public static final String updateUSQLString = "UPDATE %s SET \"USER\"='%s', \"PASSWORD\"='%s' WHERE \"USER_ID\"=%d";
    public static final String updateCSQLString = "UPDATE %s SET \"NAME\"='%s', \"X\"=%d, \"Y\"=%d, \"SUM_PER_YEAR\"=%f, "
            + "\"ORG_TYPE\"='%s', \"STREET\"='%s', \"ZIPCODE\"='%s', \"USER_ID\"=%d, \"DATE\"='%s' WHERE \"ID\"=%d";
    public static final String newCIDSQLString = "SELECT nextval('collections_id')";
    public static final String newUIDSQLString = "SELECT nextval('user_id')";
    public static final String deleteWSQLString = "DELETE FROM %s WHERE \"ID\"=%d";
    public static final String deleteSQLString = "DELETE FROM %s";

    /**
     * @return the cString
     */
    public String getcString() {
        return cString;
    }

    /**
     * @return the usersTableName
     */
    public String getUsersTableName() {
        return usersTableName;
    }

    /**
     * @return the colnsTableName
     */
    public String getColnsTableName() {
        return colnsTableName;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    public boolean read(List<User> users, Set<Organization> organizations) {
        lock.lock();
        try {
            Class.forName("org.postgresql.Driver");
            try ( Connection connection = DriverManager.getConnection(getcString(), getUserName(), getPassword())) {
                if (connection == null) {
                    throw new ConnectIOException("Connection to PSQL Server failed");
                }
                try ( Statement statement = connection.createStatement()) {
                    String qString;
                    ResultSet rs;
                    if (Objects.nonNull(users)) {
                        qString = selectSQLString.formatted(getUsersTableName());
                        rs = statement.executeQuery(qString);
                        users.clear();
                        while (rs.next())
                            users.add(new User(rs.getInt("USER_ID"), rs.getString("USER"), rs.getString("PASSWORD")));
                        rs.close();
                    }
                    if (Objects.nonNull(organizations)) {
                        qString = selectSQLString.formatted(getColnsTableName());
                        rs = statement.executeQuery(qString);
                        organizations.clear();
                        while (rs.next()) {
                            ZonedDateTime creationDate = ZonedDateTime.parse(rs.getString("DATE"));
                            String street = rs.getString("STREET");
                            String zipCode = rs.getString("ZIPCODE");
                            Address postalAddress = null;
                            if (Objects.nonNull(street) && Objects.nonNull(zipCode)) {
                                postalAddress = new Address(street, zipCode);
                            }
                            Organization organization = new Organization(rs.getString("NAME"), new Coordinates(rs.getInt("X"), rs.getInt("Y")), rs.getFloat("SUM_PER_YEAR"),
                                    OrganizationType.valueOf(rs.getString("ORG_TYPE")), postalAddress);
                            organization.setId(rs.getLong("ID"));
                            organization.setUserID(rs.getInt("USER_ID"));
                            organizations.add(organization);
                        }
                        rs.close();
                    }
                }
            }
        } catch (ClassNotFoundException | ConnectIOException | SQLException exception) {
            return false;
        } finally {
            lock.unlock();
        }
        return true;
    }

    public boolean addOrUpdate(List<User> users, Set<Organization> organizations, boolean add) {
        lock.lock();
        try {
            Class.forName("org.postgresql.Driver");
            try ( Connection connection = DriverManager.getConnection(getcString(), getUserName(), getPassword())) {
                if (connection == null) {
                    throw new ConnectIOException("Connection to PSQL Server failed");
                }
                try ( Statement statement = connection.createStatement()) {
                    Formatter formatter = new Formatter(null, Locale.US);
                    String sqlString;
                    if (Objects.nonNull(users)) {
                        for (User user : users) {
                            ((StringBuilder) formatter.out()).setLength(0);
                            if (add)
                                sqlString = formatter.format(addUSQLString, getUsersTableName(), user.getID(), user.getUserName(), user.getPassword()).toString();
                            else
                                sqlString = formatter.format(updateUSQLString, getUsersTableName(), user.getUserName(), user.getPassword(), user.getID()).toString();
                            statement.executeUpdate(sqlString);
                        }
                    }
                    if (Objects.nonNull(organizations)) {
                        for (Organization organization : organizations) {
                            ((StringBuilder) formatter.out()).setLength(0);
                            String street = Objects.isNull(organization.getPostalAddress())
                                    || Objects.isNull(organization.getPostalAddress().getStreet()) ? "null" : organization.getPostalAddress().getStreet();
                            String zipCode = Objects.isNull(organization.getPostalAddress())
                                    || Objects.isNull(organization.getPostalAddress().getZipCode()) ? "null" : organization.getPostalAddress().getZipCode();
                            if (add) {
                                sqlString = formatter.format(addCSQLString, getColnsTableName(), organization.getID(), organization.getName(), organization.getCoordinates().getX(),
                                        organization.getCoordinates().getY(), organization.getAnnualTurnover(), organization.getType().name(),
                                        street, zipCode, organization.getUserID(), organization.getCreationDate().toString()).toString();
                            } else {
                                sqlString = formatter.format(updateCSQLString, getColnsTableName(), organization.getName(), organization.getCoordinates().getX(),
                                        organization.getCoordinates().getY(), organization.getAnnualTurnover(), organization.getType().name(),
                                        street, zipCode, organization.getUserID(),
                                        organization.getCreationDate().toString(), organization.getID()).toString();
                            }
                            sqlString = sqlString.replace("'null'", "null");
                            statement.executeUpdate(sqlString);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | ConnectIOException | SQLException exception) {
            return false;
        } finally {
            lock.unlock();
        }
        return true;
    }

    public boolean delete(List<User> users, Set<Organization> organizations) {
        lock.lock();
        try {
            Class.forName("org.postgresql.Driver");
            try ( Connection connection = DriverManager.getConnection(getcString(), getUserName(), getPassword())) {
                if (connection == null) {
                    throw new ConnectIOException("Connection to PSQL Server failed");
                }
                try ( Statement statement = connection.createStatement()) {
                    Formatter formatter = new Formatter(null, Locale.US);
                    String uString;
                    if (Objects.nonNull(users)) {
                        for (User user : users) {
                            ((StringBuilder) formatter.out()).setLength(0);
                            uString = formatter.format(deleteWSQLString, getUsersTableName(), user.getID()).toString();
                            statement.executeUpdate(uString);
                        }
                    }
                    if (Objects.nonNull(organizations)) {
                        for (Organization organization : organizations) {
                            ((StringBuilder) formatter.out()).setLength(0);
                            uString = formatter.format(deleteWSQLString, getColnsTableName(), organization.getID()).toString();
                            statement.executeUpdate(uString);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | ConnectIOException | SQLException exception) {
            return false;
        } finally {
            lock.unlock();
        }
        return true;
    }

    public boolean allDelete(boolean colns) {
        lock.lock();
        try {
            Class.forName("org.postgresql.Driver");
            try ( Connection connection = DriverManager.getConnection(getcString(), getUserName(), getPassword())) {
                if (connection == null) {
                    throw new ConnectIOException("Connection to PSQL Server failed");
                }
                try ( Statement statement = connection.createStatement()) {
                    Formatter formatter = new Formatter(null, Locale.US);
                    String uString = formatter.format(deleteSQLString, (colns) ? getColnsTableName() : getUsersTableName()).toString();
                    statement.executeUpdate(uString);
                }
            }
        } catch (ClassNotFoundException | ConnectIOException | SQLException exception) {
            return false;
        } finally {
            lock.unlock();
        }
        return true;
    }

    private long executeNextID(List<User> users, Set<Organization> organizations) {
        lock.lock();
        try {
            Class.forName("org.postgresql.Driver");
            try ( Connection connection = DriverManager.getConnection(getcString(), getUserName(), getPassword())) {
                if (connection == null)
                    throw new ConnectIOException("Connection to PSQL Server failed");
                try ( Statement statement = connection.createStatement()) {
                    try ( ResultSet rs = statement.executeQuery((users != null) ? newUIDSQLString : newCIDSQLString)) {
                        if (rs.next()) {
                            return rs.getLong(1);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | ConnectIOException | SQLException exception) {
        }
        if (users != null) {
            if (!users.isEmpty())
                return users.stream().max((us1, us2) -> Long.compare(us1.getID(), us2.getID())).get().getID() + 1;
        } else {
            if (Objects.nonNull(organizations) && !organizations.isEmpty())
                return organizations.stream().max((org1, org2) -> Long.compare(org1.getID(), org2.getID())).get().getID() + 1;
        }
        lock.unlock();
        return 1;
    }

    public long getCollNextID(Set<Organization> organizations) {
        return executeNextID(null, organizations);
    }

    public int getUsersNextID(List<User> users) {
        return (int) executeNextID(users, null);
    }

    public PSQLWorker(String cString, String usersTableName, String colnsTableName, String user, String password, boolean logging) {
        this.cString = cString;
        this.usersTableName = usersTableName;
        this.colnsTableName = colnsTableName;
        this.userName = user;
        this.password = password;
        this.lock = new ReentrantLock();
    }
}
