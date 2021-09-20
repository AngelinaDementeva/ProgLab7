// 
// Decompiled by Procyon v0.5.36
// 

package commands;

public class Info
{
    public void info() throws InterruptedException {
        final ClientTCP sender = new ClientTCP();
        sender.setCommand("info");
        sender.sending(sender);
    }
}
