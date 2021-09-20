// 
// Decompiled by Procyon v0.5.36
// 

package commands;

import data.Coordinates;
import data.Address;
import data.OrganizationType;
import data.Organization;

public class Remove_first
{
    public void remove_first() throws InterruptedException {
        final Organization newOrg = new Organization(null, null, null, "0", null, null, null, null, ClientTCP.myUserID);
        final ClientTCP sender = new ClientTCP();
        sender.setCommand("remove_first");
        sender.setOrg(newOrg);
        sender.sending(sender);
    }
}
