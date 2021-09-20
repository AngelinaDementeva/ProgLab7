// 
// Decompiled by Procyon v0.5.36
// 

package helpers;

import data.Organization;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.LinkedList;
import java.util.logging.Logger;

public class CommandManager
{
    private ConsoleManager consoleManager;
    private String command;
    private String[] commandUser;
    private static final Logger logger;
    
    public CommandManager() {
        this.command = "";
    }
    
    public CommandManager(final ConsoleManager consoleManager) {
        this.command = "";
        this.consoleManager = consoleManager;
    }
    
    public CommandManager(final CollectionChecker collectionChecker) {
        this.command = "";
    }
    
    public String cmdMode(final Sender sender) {
        String result = "";
        final UserManager userManager = new UserManager();
        try {
            try {
                final String command = sender.getCommand();
                switch (command) {
                    case "": {
                        break;
                    }
                    case "help": {
                        result = this.consoleManager.help();
                        break;
                    }
                    case "info": {
                        result = this.consoleManager.info();
                        break;
                    }
                    case "show": {
                        result = this.consoleManager.show();
                        break;
                    }
                    case "add": {
                        System.out.println(this.consoleManager);
                        this.consoleManager.add(sender.getOrg());
                        this.consoleManager.save(sender.getOrg().getUserId());
                        result = this.consoleManager.show();
                        break;
                    }
                    case "update_id": {
                        final Organization org2 = sender.getOrg();
                        result = this.consoleManager.update_id(org2);
                        this.consoleManager.save(sender.getOrg().getUserId());
                        break;
                    }
                    case "remove_by_id": {
                        result = this.consoleManager.remove_by_id(sender.getOrg());
                        this.consoleManager.save(sender.getOrg().getUserId());
                        break;
                    }
                    case "clear": {
                        result = this.consoleManager.clear(sender.getOrg().getUserId());
                        this.consoleManager.save(sender.getOrg().getUserId());
                        break;
                    }
                    case "execute_script": {
                        final LinkedList<String> myFiles = new LinkedList<String>();
                        result = this.consoleManager.execute_script(sender.getFilePath(), sender.getOrg().getUserId(), myFiles);
                        this.consoleManager.save(sender.getOrg().getUserId());
                        break;
                    }
                    case "exit": {
                        this.consoleManager.exit();
                        break;
                    }
                    case "remove_first": {
                        result = this.consoleManager.remove_first(sender.getOrg().getUserId());
                        this.consoleManager.save(sender.getOrg().getUserId());
                        break;
                    }
                    case "add_if_min": {
                        result = this.consoleManager.add_if_min(sender.getOrg());
                        this.consoleManager.save(sender.getOrg().getUserId());
                        break;
                    }
                    case "remove_greater": {
                        result = this.consoleManager.remove_greater(sender.getOrg());
                        this.consoleManager.save(sender.getOrg().getUserId());
                        break;
                    }
                    case "count_by_full_name": {
                        result = this.consoleManager.count_by_full_name(sender.getOrg());
                        this.consoleManager.save(sender.getOrg().getUserId());
                        break;
                    }
                    case "filter_greater_than_annual_turnover": {
                        result = this.consoleManager.filter_greater_than_annual_turnover(sender.getOrg());
                        this.consoleManager.save(sender.getOrg().getUserId());
                        break;
                    }
                    case "print_unique_official_address": {
                        result = this.consoleManager.print_unique_official_address();
                        break;
                    }
                    case "sign_up": {
                        result = userManager.addingUser(sender.getLogin(), sender.getPassword());
                        break;
                    }
                    case "sign_in": {
                        result = userManager.testingUser(sender.getLogin(), sender.getPassword());
                        break;
                    }
                    case "checkID": {
                        result = userManager.checkingID(sender.getOrg().getName(), sender.getOrg().getId(), sender.getOrg().getUserId().toString());
                        break;
                    }
                    default: {
                        CommandManager.logger.log(Level.WARNING, "Unknown commmand. Try again! Write 'help' for list of available commands.");
                        break;
                    }
                }
            }
            catch (ArrayIndexOutOfBoundsException exception) {
                CommandManager.logger.log(Level.WARNING, "Element is not present in the array. Try again! Write 'help' for list of available commands.");
            }
        }
        catch (NoSuchElementException noSuchElementException) {
            CommandManager.logger.log(Level.INFO, "Program was stopped!");
            System.exit(1);
        }
        return result;
    }
    
    static {
        logger = Logger.getLogger(CommandManager.class.getName());
    }
}
