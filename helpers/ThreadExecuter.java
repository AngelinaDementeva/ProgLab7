// 
// Decompiled by Procyon v0.5.36
// 

package helpers;

import java.util.concurrent.Callable;

public class ThreadExecuter implements Callable<String>
{
    Sender s;
    
    public ThreadExecuter(final Sender s) {
        this.s = s;
    }
    
    @Override
    public String call() throws Exception {
        final ConsoleManager consoleManager = new ConsoleManager();
        final CommandManager commandManager = new CommandManager(consoleManager);
        final String toClient = commandManager.cmdMode(this.s);
        return toClient;
    }
}
