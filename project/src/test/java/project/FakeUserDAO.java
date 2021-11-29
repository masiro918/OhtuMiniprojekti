
package project;

import java.util.ArrayList;
import project.db.UserDAO;
import project.domain.UserInterface;

public class FakeUserDAO implements UserDAO{
    ArrayList<UserInterface> fakeUsers;
    
    public FakeUserDAO() {
        this.fakeUsers = new ArrayList<UserInterface>();
    }
    
    @Override
    public void add(UserInterface user) {
        this.fakeUsers.add(user);
    }

    @Override
    public ArrayList<UserInterface> fetchAllUsers() {
        return this.fakeUsers;
    }

    @Override
    public boolean login(UserInterface user) throws Exception {
        return true;
    }

    @Override
    public void remove(UserInterface user) throws Exception {
        this.fakeUsers.remove(user);
    }

    @Override
    public boolean tableExists() throws Exception {
        return true;
    }
    
}
