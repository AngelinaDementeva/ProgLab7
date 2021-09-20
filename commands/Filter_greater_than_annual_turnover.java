// 
// Decompiled by Procyon v0.5.36
// 

package commands;

import data.Coordinates;
import data.Address;
import data.OrganizationType;
import data.Organization;

public class Filter_greater_than_annual_turnover
{
    public void filter_annual_turnover(final String orgAnnual) throws InterruptedException {
        final long annual = Long.parseLong(orgAnnual);
        final Organization newOrg = new Organization(0, null, annual, "0", null, null, null, null, null);
        final ClientTCP sender = new ClientTCP();
        sender.setCommand("filter_greater_than_annual_turnover");
        sender.setOrg(newOrg);
        sender.sending(sender);
    }
}
