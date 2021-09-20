// 
// Decompiled by Procyon v0.5.36
// 

package helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.Comparator;
import java.util.logging.Logger;
import java.util.HashMap;
import data.Organization;
import java.util.LinkedList;
import java.util.Date;

public class ConsoleManager
{
    Date dateInitial;
    LinkedList<Organization> organizations;
    private final HashMap<String, String> consoleInfo;
    private static final Logger logger;
    
    public ConsoleManager() {
        this.dateInitial = CollectionChecker.dateInitial;
        this.organizations = CollectionChecker.organizations;
        (this.consoleInfo = new HashMap<String, String>()).put("help", " Show available commands");
        this.consoleInfo.put("info", " Print collection's info to standard output");
        this.consoleInfo.put("show", " Print all collection's elements in string representation");
        this.consoleInfo.put("add {element}", " Add a new element to collection");
        this.consoleInfo.put("update_id {element}", " Update current element's value, which ID is equal to the given");
        this.consoleInfo.put("remove_by_id {id}", " Delete the element from the collection using its ID");
        this.consoleInfo.put("clear", " Purify the collection!");
        this.consoleInfo.put("execute_script {file_name}", " Read and execute a script from specified file");
        this.consoleInfo.put("exit", " End the program");
        this.consoleInfo.put("remove_first", " Delete a first element from the collection");
        this.consoleInfo.put("add_if_min {element}", " If an element's value is less than the smallest element value, add a new element to the collection");
        this.consoleInfo.put("remove_greater {element}", " Delete all collection's elements which are bigger than current element");
        this.consoleInfo.put("count_by_full_name {fullName}", " Print the amount of elements which fullName field is equal to given");
        this.consoleInfo.put("filter_greater_than_annual_turnover {annualTurnover}", " Print elements which value annualTurnover field is bigger than given");
        this.consoleInfo.put("print_unique_official_address", " Print unique values of officialAddress fields from all collection's elements");
    }
    
    public String show() {
        String result = "";
        if (this.organizations.size() != 0) {
            this.organizations.sort(Comparator.comparingInt(Organization::getId));
            for (final Organization org : this.organizations) {
                ConsoleManager.logger.log(Level.INFO, org.toString() + "\n");
                result = result + org.toString() + "\n";
            }
        }
        else {
            ConsoleManager.logger.log(Level.INFO, "Collection is empty!");
            result = "Collection is empty!";
        }
        return result;
    }
    
    public String help() {
        String result = "";
        for (final Map.Entry<String, String> entry : this.consoleInfo.entrySet()) {
            ConsoleManager.logger.log(Level.INFO, entry.getKey() + entry.getValue());
            result = result + entry.getKey() + entry.getValue() + "\n";
        }
        return result;
    }
    
    public String info() {
        ConsoleManager.logger.log(Level.INFO, "Type of collection: java.util.LinkedList\nInitialization date: " + this.dateInitial + "\nAmount of elements in the collection: " + this.organizations.size());
        return "Type of collection: java.util.LinkedList\nInitialization date: " + this.dateInitial + "\nAmount of elements in the collection: " + this.organizations.size();
    }
    
    public int makerID() {
        int ID = -1;
        for (final Organization org : this.organizations) {
            if (org.getId() < ID) {
                ID = org.getId();
            }
        }
        return ID - 1;
    }
    
    public void add(final Organization org) {
        final int id = this.makerID();
        org.setId(id);
        org.setCreationDate(this.getDate());
        this.organizations.add(org);
    }
    
    public String update_id(final Organization org) {
        String result = "";
        final Organization org2 = this.organizations.stream().filter(p -> p.getId().equals(org.getId())).findAny().orElse(null);
        if (org2 != null) {
            this.organizations.remove(org2);
            org.setCreationDate(this.getDate());
            this.organizations.add(org);
            this.organizations.sort(Comparator.comparingInt(Organization::getId));
            ConsoleManager.logger.log(Level.INFO, "Element was updated!");
            result = "Element was updated!";
            return result;
        }
        ConsoleManager.logger.log(Level.WARNING, "Element with this ID is not found. Try again!");
        result = "Element with this ID is not found. Try again!";
        return result;
    }
    
    public String remove_by_id(final Organization org) {
        String result = "";
        try {
            final Organization org2 = this.organizations.stream().filter(p -> p.getId().equals(org.getId())).findAny().orElse(null);
            if (org2 != null) {
                this.organizations.remove(org2);
                this.organizations.sort(Comparator.comparingInt(Organization::getId));
                ConsoleManager.logger.log(Level.INFO, "Element was removed!");
                result = "Element was removed successfully!";
            }
            else {
                ConsoleManager.logger.log(Level.WARNING, "Element with this ID is not found. Try again!");
                result = "Element with this ID is not found. Try again!";
            }
        }
        catch (NumberFormatException numberFormatException) {
            ConsoleManager.logger.log(Level.WARNING, "An argument must be a number! Try again!");
        }
        return result;
    }
    
    public String clear(final Integer userid) {
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
            final Connection c = DriverManager.getConnection("jdbc:postgresql://pg:5432/studs", "s313318", "mes758");
            c.setAutoCommit(false);
            ConsoleManager.logger.log(Level.INFO, "Opened database successfully");
            final Statement stmt = c.createStatement();
            final String sql = "DELETE from ORGANIZATIONS  WHERE USERID = " + userid + ";";
            stmt.executeUpdate(sql);
            c.commit();
            result = "Elements from collection by your account were removed!";
        }
        catch (SQLException | ClassNotFoundException ex2) {
            final Exception ex;
            final Exception e = ex;
            ConsoleManager.logger.log(Level.WARNING, "Error while clearing the data!");
        }
        return result;
    }
    
    public void save(final Integer userid) {
        final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        final Lock readLock = readWriteLock.readLock();
        final Lock writeLock = readWriteLock.writeLock();
        try {
            Class.forName("org.postgresql.Driver");
            final Connection c = DriverManager.getConnection("jdbc:postgresql://pg:5432/studs", "s313318", "mes758");
            c.setAutoCommit(false);
            ConsoleManager.logger.log(Level.INFO, "-- Opened database successfully");
            String sql = "";
            String idS = "";
            final LinkedList<String> ids = new LinkedList<String>();
            for (final Organization organization : this.organizations) {
                if (organization.getId() > 0) {
                    ids.add(organization.getId().toString());
                }
            }
            idS = String.join(",", ids);
            if (ids.size() > 0) {
                sql = "DELETE FROM ORGANIZATIONS WHERE ID NOT IN (" + idS + ");";
            }
            if (this.organizations.size() == 0) {
                sql = sql + "DELETE FROM ORGANIZATIONS WHERE USERID = " + userid + ";";
            }
            final Statement stmt = c.createStatement();
            for (final Organization organization : this.organizations) {
                if (organization.getId() < 0) {
                    sql = sql + "INSERT INTO ORGANIZATIONS (NAME,ANNUALTURNOVER,CREATIONDATE,FULLNAME,ORGANIZATIONTYPE,STREET,TOWNX,TOWNY,COORDX,COORDY,TOWNNAME, USERID ) VALUES ('" + organization.getName() + "', " + organization.getAnnualTurnover().toString() + ", '" + organization.getCreationDate() + "','" + organization.getFullName() + "', '" + organization.getType().toString() + "', '" + organization.getOfficialAddress().getStreet() + "', " + organization.getOfficialAddress().getTown().getX() + "," + organization.getOfficialAddress().getTown().getY() + ", " + organization.getCoordinates().getX() + ",  " + organization.getCoordinates().getY() + ", '" + organization.getOfficialAddress().getTown().getName() + "', " + organization.getUserId() + ");";
                }
                else {
                    sql = sql + "UPDATE ORGANIZATIONS SET NAME = '" + organization.getName() + "', ANNUALTURNOVER = " + organization.getAnnualTurnover().toString() + ",CREATIONDATE =  '" + organization.getCreationDate() + "', FULLNAME =  '" + organization.getFullName() + "',ORGANIZATIONTYPE = '" + organization.getType().toString() + "', STREET =  '" + organization.getOfficialAddress().getStreet() + "', TOWNX = " + organization.getOfficialAddress().getTown().getX() + " , TOWNY = " + organization.getOfficialAddress().getTown().getY() + ", COORDX = " + organization.getCoordinates().getX() + ", COORDY = " + organization.getCoordinates().getY() + ", TOWNNAME = '" + organization.getOfficialAddress().getTown().getName() + "', USERID = " + organization.getUserId() + " WHERE ID = " + organization.getId() + ";";
                }
            }
            writeLock.lock();
            readLock.lock();
            stmt.executeUpdate(sql);
            readLock.unlock();
            writeLock.unlock();
            stmt.close();
            c.commit();
            ConsoleManager.logger.log(Level.INFO, "-- Records created successfully");
            ConsoleManager.logger.log(Level.INFO, "Organizations in save method = " + this.organizations.size());
            ConsoleManager.logger.log(Level.INFO, "Collection was saved successfully!");
            this.organizations.clear();
            final CollectionChecker collectionChecker = new CollectionChecker();
        }
        catch (ClassNotFoundException | SQLException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            ConsoleManager.logger.log(Level.WARNING, "Collection wasn't saved! Try again! " + e.getMessage());
        }
    }
    
    public String add_if_min(final Organization org) {
        String result = "";
        long minAnnualTurnover = Long.MAX_VALUE;
        for (final Organization organization : this.organizations) {
            minAnnualTurnover = organization.getAnnualTurnover();
        }
        if (org.getAnnualTurnover() < minAnnualTurnover) {
            result = "The minimal element was found! Enter element's values.";
            ConsoleManager.logger.log(Level.INFO, "The minimal element was found! Enter element's values." + minAnnualTurnover);
        }
        else {
            ConsoleManager.logger.log(Level.WARNING, "The element's annual turnover is bigger than the collection's minimal element element was not added. Try another value!");
            result = "The element's annual turnover is bigger than the collection's minimal element.\nElement was not added. Try another value!";
        }
        return result;
    }
    
    public String remove_greater(final Organization org) {
        int count = 0;
        String result = "";
        try {
            final List<Organization> org2 = this.organizations.stream().filter(p -> p.getAnnualTurnover() > org.getAnnualTurnover() && p.getUserId() == org.getUserId()).collect((Collector<? super Object, ?, List<Organization>>)Collectors.toList());
            if (org2.size() > 0) {
                count = org2.size();
                for (final Organization organization : org2) {
                    this.organizations.remove(organization);
                }
                this.organizations.sort(Comparator.comparingInt(Organization::getId));
                ConsoleManager.logger.log(Level.INFO, count + " elements with annual turnover greater than " + org.getAnnualTurnover() + " were removed successfully!");
                result = count + " elements with annual turnover greater than " + org.getAnnualTurnover() + " were removed successfully!";
            }
            else {
                ConsoleManager.logger.log(Level.WARNING, "Elements with annual turnover greater than entered are not found. Try again!");
                result = "Elements with annual turnover greater than entered are not found. Try again!";
            }
        }
        catch (NumberFormatException numberFormatException) {
            ConsoleManager.logger.log(Level.WARNING, "An argument must be a number! Try again!");
        }
        return result;
    }
    
    public String remove_first(final Integer userid) {
        String result = "";
        if (this.organizations.size() > 0) {
            System.out.println(this.organizations.getFirst());
            if (this.organizations.getFirst().getUserId() == userid) {
                this.organizations.remove();
                ConsoleManager.logger.log(Level.INFO, "The first element was removed!");
                result = "The first element was removed!";
            }
            else {
                result = "You have no access to delete this element!";
            }
        }
        else {
            ConsoleManager.logger.log(Level.INFO, "The collection is empty!");
            result = "The collection is empty!";
        }
        return result;
    }
    
    public String count_by_full_name(final Organization org) {
        String result = "";
        int count = 0;
        for (final Organization organization : this.organizations) {
            if (organization.getFullName().equals(org.getFullName())) {
                ++count;
            }
        }
        result = count + " elements equal to entered value!";
        ConsoleManager.logger.log(Level.INFO, count + " elements equal to entered value!");
        return result;
    }
    
    public String filter_greater_than_annual_turnover(final Organization org) {
        int count = 0;
        String result = "";
        try {
            final List<Organization> org2 = this.organizations.stream().filter(p -> p.getAnnualTurnover() > org.getAnnualTurnover()).collect((Collector<? super Object, ?, List<Organization>>)Collectors.toList());
            if (org2.size() > 0) {
                count = org2.size();
                for (final Organization organization : org2) {
                    result = result + "\n" + organization;
                }
                ConsoleManager.logger.log(Level.INFO, result);
                this.organizations.sort(Comparator.comparingInt(Organization::getId));
                result = count + " elements with annual turnover greater than " + org.getAnnualTurnover() + " were printed successfully!" + result;
                ConsoleManager.logger.log(Level.INFO, count + " elements with annual turnover greater than " + org.getAnnualTurnover() + " were printed successfully! " + result);
            }
            else {
                ConsoleManager.logger.log(Level.WARNING, "Elements with annual turnover greater than entered are not found. Try again!");
                result = "Elements with annual turnover greater than entered are not found. Try again!";
            }
        }
        catch (NumberFormatException numberFormatException) {
            ConsoleManager.logger.log(Level.WARNING, "An argument must be a number! Try again!");
        }
        return result;
    }
    
    public String print_unique_official_address() {
        int count = 0;
        String result = "";
        final List<Organization> orgss = this.organizations.stream().filter(p -> this.organizations.stream().filter(p1 -> p1.getOfficialAddress().getStreet().equals(p.getOfficialAddress().getStreet())).count() == 1L).collect((Collector<? super Object, ?, List<Organization>>)Collectors.toList());
        for (final Organization org : orgss) {
            result = result + "\n" + org.getOfficialAddress();
        }
        count = orgss.size();
        if (count > 0) {
            result = count + " unique elements were found: " + result;
        }
        else {
            result = "No unique elements were found!";
            ConsoleManager.logger.log(Level.INFO, "No unique elements were found!");
        }
        return result;
    }
    
    public void exit() {
        ConsoleManager.logger.log(Level.INFO, "Thank you for using my program! The program will be finished now!");
        System.exit(0);
    }
    
    public String execute_script(String filepath, final Integer userId, final LinkedList<String> myFiles) {
        String result = "";
        if (myFiles.contains(filepath)) {
            result += "This script was executed already!";
            return result;
        }
        myFiles.add(filepath);
        try {
            filepath = filepath.trim().replaceAll("[ ]{2,}", " ");
            result = "Starting script file! \n";
            final BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
            final StringBuilder cmd = new StringBuilder();
            String command;
            while ((command = reader.readLine()) != null) {
                final String[] commandUser = command.trim().replaceAll("[ ]{2,}", " ").toLowerCase().split(" ", 3);
                final String s = commandUser[0];
                switch (s) {
                    case "": {
                        break;
                    }
                    case "help": {
                        result = result + "\n" + this.help();
                        break;
                    }
                    case "info": {
                        result = result + "\n" + this.info();
                        break;
                    }
                    case "show": {
                        result = result + "\n" + this.show();
                        break;
                    }
                    case "add": {
                        result += "\nCommand 'add' was read successfully!";
                        break;
                    }
                    case "update_id": {
                        result += "\nCommand 'update_id' was read successfully!";
                        break;
                    }
                    case "remove_by_id": {
                        final Organization org2 = new Organization();
                        org2.setUserId(userId);
                        if (commandUser.length > 1 && commandUser[1].trim().length() >= 1) {
                            try {
                                org2.setId(Integer.parseInt(commandUser[1]));
                            }
                            catch (NumberFormatException e) {
                                result += "\nThe value must be Integer!";
                            }
                            result = result + "\n" + this.remove_by_id(org2);
                            break;
                        }
                        result += "\nNo id was found for removing!";
                        break;
                    }
                    case "clear": {
                        result = result + "\n" + this.clear(userId);
                        break;
                    }
                    case "exit": {
                        result += "\n%%%exit%%%";
                        break;
                    }
                    case "remove_first": {
                        result = result + "\n" + this.remove_first(userId);
                        break;
                    }
                    case "add_if_min": {
                        result += "\nCommand 'add_if_min' was read successfully!";
                        break;
                    }
                    case "remove_greater": {
                        final Organization org3 = new Organization();
                        org3.setUserId(userId);
                        if (commandUser.length > 1) {
                            try {
                                org3.setAnnualTurnover(Long.parseLong(commandUser[1]));
                                result = result + "\n" + this.remove_greater(org3);
                            }
                            catch (NumberFormatException e2) {
                                result += "\nThe value must be an Integer!";
                            }
                            break;
                        }
                        result += "\nNo value 'annual turnover' was found for removing!";
                        break;
                    }
                    case "execute_script": {
                        if (commandUser.length > 1 && commandUser[1].trim().length() > 4) {
                            result = result + "\n" + this.execute_script(commandUser[1], userId, myFiles);
                            break;
                        }
                        result += "\nNo files found for execution!";
                        break;
                    }
                    case "count_by_full_name": {
                        final Organization org4 = new Organization();
                        if (commandUser.length > 1) {
                            org4.setFullName(commandUser[1]);
                            result = result + "\n" + this.count_by_full_name(org4);
                            break;
                        }
                        result += "\nNo full name was found!";
                        break;
                    }
                    case "filter_greater_than_annual_turnover": {
                        final Organization org5 = new Organization();
                        if (commandUser.length > 1) {
                            try {
                                org5.setAnnualTurnover(Long.parseLong(commandUser[1]));
                                result = result + "\n" + this.filter_greater_than_annual_turnover(org5);
                            }
                            catch (NumberFormatException e3) {
                                result += "\nThe value must be an Integer!";
                            }
                            break;
                        }
                        result += "\nNo id was found for removing!";
                        break;
                    }
                    case "print_unique_official_address": {
                        result = result + "\n" + this.print_unique_official_address();
                        break;
                    }
                    default: {
                        reader.readLine();
                        result += "\nUnknown command! Try again! Write 'help' for list of available commands.";
                        break;
                    }
                }
                System.out.println("The end of the command");
                result += "\nThe end of the command";
            }
            System.out.println("The end of the commands");
            result += "\nThe end of the commands";
            reader.close();
        }
        catch (FileNotFoundException fileNotFoundException) {
            ConsoleManager.logger.log(Level.WARNING, "File not found. Try again.");
        }
        catch (IOException ioException) {
            ConsoleManager.logger.log(Level.WARNING, "File reading exception. Try again.");
        }
        return result;
    }
    
    public String getDate() {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(calendar.getTime());
    }
    
    static {
        logger = Logger.getLogger(ConsoleManager.class.getName());
    }
}
