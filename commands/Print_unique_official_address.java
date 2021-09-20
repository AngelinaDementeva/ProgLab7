// 
// Decompiled by Procyon v0.5.36
// 

package commands;

public class Print_unique_official_address
{
    public void print_unique_official_address() throws InterruptedException {
        final ClientTCP sender = new ClientTCP();
        sender.setCommand("print_unique_official_address");
        sender.sending(sender);
    }
}
