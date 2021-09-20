// 
// Decompiled by Procyon v0.5.36
// 

package commands;

import data.Coordinates;
import data.Address;
import data.OrganizationType;
import data.Organization;

public class Add_if_min
{
    public void add_min(final String orgAnnual) throws InterruptedException {
        final long annual = Long.parseLong(orgAnnual);
        final Organization newOrg = new Organization(0, null, annual, "0", null, null, null, null, null);
        final ClientTCP sender = new ClientTCP();
        sender.setCommand("add_if_min");
        sender.setOrg(newOrg);
        sender.sending(sender);
    }
}
