// 
// Decompiled by Procyon v0.5.36
// 

package commands;

import data.Coordinates;
import data.Address;
import data.OrganizationType;
import data.Organization;

public class CheckerID
{
    public void checker(final String orgId, final String command) throws InterruptedException {
        final int id = Integer.parseInt(orgId);
        final Organization newOrg = new Organization(id, command, null, "0", null, null, null, null, ClientTCP.myUserID);
        final ClientTCP sender = new ClientTCP();
        sender.setCommand("checkID");
        sender.setOrg(newOrg);
        sender.sending(sender);
    }
}
