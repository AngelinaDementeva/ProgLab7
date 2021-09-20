// 
// Decompiled by Procyon v0.5.36
// 

package commands;

import data.Coordinates;
import data.Address;
import data.OrganizationType;
import data.Organization;

public class Execute_script
{
    public void execute_script(final String filePath) throws InterruptedException {
        final ClientTCP sender = new ClientTCP();
        final Organization newOrg = new Organization(null, null, null, "0", null, null, null, null, ClientTCP.myUserID);
        sender.setCommand("execute_script");
        sender.setOrg(newOrg);
        sender.setFilePath(filePath);
        sender.sending(sender);
    }
}
