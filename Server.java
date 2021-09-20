import java.io.IOException;
import helpers.Reciever;
import helpers.CommandManager;
import helpers.CollectionChecker;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

// 
// Decompiled by Procyon v0.5.36
// 

public class Server
{
    public static String myResult;
    private static final Logger logger;
    
    public static void main(final String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        try {
            LogManager.getLogManager().readConfiguration(Server.class.getResourceAsStream("logging.properties"));
            Server.logger.log(Level.INFO, "Program started working!");
            final CommandManager commandManager = new CommandManager(new CollectionChecker());
            Server.logger.log(Level.INFO, "Server started!");
        }
        catch (ArrayIndexOutOfBoundsException exception) {
            Server.logger.log(Level.SEVERE, "Something wrong with Server.class");
            System.exit(1);
        }
        final Reciever reciever = new Reciever();
        reciever.checkInput();
        reciever.run();
    }
    
    static {
        logger = Logger.getLogger(Server.class.getName());
    }
}
