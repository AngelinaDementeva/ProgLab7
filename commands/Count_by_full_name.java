// 
// Decompiled by Procyon v0.5.36
// 

package commands;

import data.Coordinates;
import data.Address;
import data.OrganizationType;
import data.Organization;

public class Count_by_full_name
{
    public void count(final String fullName) throws InterruptedException {
        final Organization newOrg = new Organization(0, null, null, "0", fullName.trim(), null, null, null, null);
        final ClientTCP sender = new ClientTCP();
        sender.setCommand("count_by_full_name");
        sender.setOrg(newOrg);
        sender.sending(sender);
    }
}
