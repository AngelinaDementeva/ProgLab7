// 
// Decompiled by Procyon v0.5.36
// 

package commands;

import data.Coordinates;
import data.Address;
import data.OrganizationType;
import data.Organization;

public class Remove_greater
{
    public void remove_annual(final String orgAnnual) throws InterruptedException {
        final long annual = Long.parseLong(orgAnnual);
        final Organization newOrg = new Organization(0, null, annual, "0", null, null, null, null, ClientTCP.myUserID);
        final ClientTCP sender = new ClientTCP();
        sender.setCommand("remove_greater");
        sender.setOrg(newOrg);
        sender.sending(sender);
    }
}
