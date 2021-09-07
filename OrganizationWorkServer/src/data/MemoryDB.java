package data;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class MemoryDB {

    private Set<Organization> organizations;
    private List<User> users;
    private ReentrantLock usersLock;
    private ReentrantLock orgLock;

    public Set<Organization> getOrganizations() {
        orgLock.lock();
        Set<Organization> org = organizations;
        orgLock.unlock();
        return org;
    }

    public List<User> getUsers() {
        usersLock.lock();
        List<User> us = users;
        usersLock.unlock();
        return us;
    }

    public void SAddUser(User user) {
        usersLock.lock();
        getUsers().add(user);
        usersLock.unlock();
    }

    public void SRemoveUser(User user) {
        usersLock.lock();
        getUsers().remove(user);
        usersLock.unlock();
    }

    public void SAddOrganization(Organization organization) {
        orgLock.lock();
        getOrganizations().add(organization);
        orgLock.unlock();
    }

    public void SRemoveOrganization(Organization organization) {
        orgLock.lock();
        getOrganizations().remove(organization);
        orgLock.unlock();
    }

    public MemoryDB() {
        this.organizations = new LinkedHashSet<>();
        this.users = new ArrayList<>();
        usersLock = new ReentrantLock();
        orgLock = new ReentrantLock();
    }
}
