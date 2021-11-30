package project;

import java.util.ArrayList;
import project.db.UserDAO;
import project.domain.UserInterface;

/**
 * A fake database for AuthenticationServiceTest-class.
 */
public class FakeUserDAO implements UserDAO {
    private ArrayList<UserInterface> fakeUsers;
    
    public FakeUserDAO() {
        this.fakeUsers = new ArrayList<UserInterface>();
    }
    
    /**
     * Adds user to "database".
     * 
     * @param user user that is added
     */
    @Override
    public void add(UserInterface user) {
        this.fakeUsers.add(user);
    }
    
    /**
     * Gets all user's information from the "database".
     * 
     * @return true if user was found, false if not.
     */
    @Override
    public ArrayList<UserInterface> fetchAllUsers() {
        return this.fakeUsers;
    }
    
    /**
     * Adds user to "database".
     * 
     * @param user user that is added
     */
    @Override
    public boolean login(UserInterface user) throws Exception {
        return true;
    }
    
    /**
     * Removes user from "database".
     * 
     * @param user user that is removed
     */
    @Override
    public void remove(UserInterface user) throws Exception {
        this.fakeUsers.remove(user);
    }
    
}
