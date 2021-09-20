// 
// Decompiled by Procyon v0.5.36
// 

package helpers;

public class User
{
    private String login;
    private String password;
    
    public User(final String login, final String password) {
        this.login = login;
        this.password = password;
    }
    
    public String getLogin() {
        return this.login;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof User) {
            final User userObj = (User)o;
            return this.login.equals(userObj.getLogin()) && this.password.equals(userObj.getPassword());
        }
        return false;
    }
    
    @Override
    public String toString() {
        return this.login + "," + this.password;
    }
}
