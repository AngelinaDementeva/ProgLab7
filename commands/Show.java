// 
// Decompiled by Procyon v0.5.36
// 

package commands;

public class Show
{
    public void show() throws InterruptedException {
        final ClientTCP sender = new ClientTCP();
        sender.setCommand("show");
        sender.sending(sender);
    }
}
