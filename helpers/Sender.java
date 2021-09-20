// 
// Decompiled by Procyon v0.5.36
// 

package helpers;

import data.Organization;

public class Sender
{
    private String command;
    private Organization org;
    private String filePath;
    private String ids;
    private String login;
    private String password;
    
    public String getFilePath() {
        return this.filePath;
    }
    
    public void setFilePath(final String filePath) {
        this.filePath = filePath;
    }
    
    public String getCommand() {
        return this.command;
    }
    
    public void setCommand(final String command) {
        this.command = command;
    }
    
    public Organization getOrg() {
        return this.org;
    }
    
    public void setOrg(final Organization org) {
        this.org = org;
    }
    
    public String getIds() {
        return this.ids;
    }
    
    public void setIds(final String ids) {
        this.ids = ids;
    }
    
    public String getLogin() {
        return this.login;
    }
    
    public String getPassword() {
        return this.password;
    }
}
