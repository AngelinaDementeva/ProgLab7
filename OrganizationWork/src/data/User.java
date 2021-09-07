package data;

import java.io.Serializable;

public class User implements Serializable {

    private Integer ID;
    private String userName;
    private String password;
    private String newPassword;

    /**
     * @return the ID
     */
    public Integer getID() {
        return ID;
    }
    /**
     * @param id the ID to set
     */
    public void setID(Integer id) {
        this.ID = id;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }
    /**
     * @param user the userName to set
     */
    public void setUserName(String user) {
        this.userName = user;
    }
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }
    /**
     * @param oldPassword the newPassword to set
     */
    public void setNewPassword(String oldPassword) {
        this.newPassword = oldPassword;
    }

    @Override
    public String toString() {
        return userName + ' ' + password;
    }

    public User(Integer id, String user, String password) {
        this.ID = id;
        this.userName = user;
        this.password = password;
    }

    public User(Integer id, String user, String password, String oldPassword) {
        this.ID = id;
        this.userName = user;
        this.password = password;
        this.newPassword = oldPassword;
    }
}
