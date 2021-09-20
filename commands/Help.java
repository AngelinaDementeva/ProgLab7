// 
// Decompiled by Procyon v0.5.36
// 

package commands;

public class Help
{
    public void help() throws InterruptedException {
        final ClientTCP sender = new ClientTCP();
        sender.setCommand("help");
        sender.sending(sender);
    }
}
