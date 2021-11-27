
package project.logic;

import java.util.ArrayList;
import project.DAO.UserDAO;
import project.domain.User;

public class AuthenticationService {
    private ArrayList<UserDAO> users;
    // oliomuuttujana SQLUserdatabaseDAO tms. josta haetaan kayttajat
    
    public AuthenticationService() {
        this.users = loadUsers();
    }
    
    public ArrayList<UserDAO> loadUsers() {
        //TODO: kommunikointi databasen kanssa
        //lataa kayttajat listaan ja palauta lista
        return null;
    }
    
    //testeja varten, voi poistaa kunhan olemassa sopiva rajapinta tietokannalle
    public void setUsers(ArrayList<UserDAO> users) {
        this.users = users;
    }
    
    //testeja varten, voi poistaa kunhan tietokannan rajapinta kaytossa
    public ArrayList<UserDAO> getUsers() {
        return this.users;
    }
    
    public void saveUser(UserDAO u) {
        //TODO: kommunikointi databasen kanssa
        //tallentaa kayttajan tietokantaan
    }
    
    public UserDAO login(String username, String password) {
        for (UserDAO u : this.users) {
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
    
    public UserDAO findUser(String username) {
        for (UserDAO u : this.users) {
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
