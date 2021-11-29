package project.logic;

import java.util.ArrayList;
import project.domain.UserInterface;
import project.db.SQLUserDAO;
import project.domain.User;

/**
 * A class responsible for handling useraccount functions (create a user, log user in, save users).
 */
public class AuthenticationService {
    private ArrayList<UserInterface> users;
    private SQLUserDAO userDb = new SQLUserDAO();
    
    public AuthenticationService() {
        this.users = loadUsers();
    }
    
    public ArrayList<UserInterface> loadUsers() {
        return userDb.fetchAllUsers();
    }

    //testeja varten, voi poistaa kunhan olemassa sopiva rajapinta tietokannalle
    public void setUsers(ArrayList<UserInterface> users) {
        this.users = users;
    }

    //testeja varten, voi poistaa kunhan tietokannan rajapinta kaytossa
    public ArrayList<UserInterface> getUsers() {
        return this.users;
    }
    
    public void saveUser(UserInterface u) {
        userDb.add(u);
    }
    
    public UserInterface login(String username, String password) {
        for (UserInterface u : this.users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
    
    public boolean createUser(String username, String password) {
        if (creationStatus(username, password)) {
            User user = new User(username, password);
            saveUser(user);
            this.users.add(user);
            return true;
        }
        return false;
    }
    
    public UserInterface findUser(String username) {
        for (UserInterface u : this.users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
    
    public boolean creationStatus(String username, String password) {
        if (invalidUsername(username)) { //epakelpo kayttajanimi, TODO: virheviesti
            return false;
        }
        
        if (invalidPassword(password)) { //epakelpo salasana, TODO: virheviesti
            return false;
        }
        
        if (findUser(username) != null) { //kayttajanimi otettu, TODO: virheviesti
            return false;
        }
        
        return true;
    }
    
    //palauttaa true, jos huono kayttajanimi
    public boolean invalidUsername(String username) {
        if (!username.matches("[a-zA-Z]+")) {
            return true;
        }
        
        if (username.length() < 3) {
            return true;
        }
        
        return false;
    }
    
    //palauttaa true, jos huono salasana
    public boolean invalidPassword(String password) {
        if (password.matches("[a-zA-Z]+")) {
            return true;
        }
        
        if (password.length() < 8) {
            return true;
        }
        return false;
    }
}
