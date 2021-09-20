// 
// Decompiled by Procyon v0.5.36
// 

package commands;

public class Login
{
    public void sign_up(final String login, final String password) throws InterruptedException {
        final ClientTCP sender = new ClientTCP();
        sender.setCommand("sign_up");
        sender.setLogin(login);
        sender.setPassword(password);
        sender.sending(sender);
    }
    
    public void sign_in(final String login, final String password) throws InterruptedException {
        final ClientTCP sender = new ClientTCP();
        sender.setCommand("sign_in");
        sender.setLogin(login);
        sender.setPassword(password);
        sender.sending(sender);
    }
}
