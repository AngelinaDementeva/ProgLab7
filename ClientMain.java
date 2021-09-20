import helpers.CommandManager;
import helpers.AunthecationSupervisor;
import java.util.Scanner;

// 
// Decompiled by Procyon v0.5.36
// 

public class ClientMain
{
    public static void main(final String[] args) throws InterruptedException {
        try {
            System.out.println("\u0420\u040elient program started!");
            final AunthecationSupervisor handle = new AunthecationSupervisor(new Scanner(System.in));
            handle.handle();
            final CommandManager commandManager = new CommandManager();
            commandManager.cmdMode();
        }
        catch (ArrayIndexOutOfBoundsException exception) {
            System.out.println("Incorrect filepath was entered! Try again!");
            System.exit(1);
        }
    }
}
