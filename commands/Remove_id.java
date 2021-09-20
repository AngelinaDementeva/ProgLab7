// 
// Decompiled by Procyon v0.5.36
// 

package commands;

import data.Coordinates;
import data.Address;
import data.OrganizationType;
import data.Organization;

public class Remove_id
{
    public void remove_id(final String orgId) throws InterruptedException {
        final int id = Integer.parseInt(orgId);
        final Organization newOrg = new Organization(id, null, null, "0", null, null, null, null, null);
        final ClientTCP sender = new ClientTCP();
        sender.setCommand("remove_by_id");
        sender.setOrg(newOrg);
        sender.sending(sender);
    }
}
