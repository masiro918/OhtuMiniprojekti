
package project.logic;

import project.DAO.UserDAO;
import project.domain.User;

public class AuthenticationService {
    private UserDAO users;
    
    public AuthenticationService(UserDAO u) {
        this.users = u;
    }
    
    public boolean login(String username, String password) {
        for (User u : this.users.getUsers()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean createUser(String username, String password) {
        if (invalidUsername(username)) { //epäkelpo käyttäjänimi, TODO: virheviesti
            return false;
        }
        
        if (invalidPassword(password)) { //epäkelpo salasana, TODO: virheviesti
            return false;
        }
        
        if (this.users.getUsers().contains(username)) { //käyttäjänimi otettu, TODO: virheviesti
            return false;
        }
        User user = new User(username, password);
        this.users.getUsers().add(user);
        return true;
    }
    
    public boolean invalidUsername(String username) {
        if (!username.matches("[a-zA-Z]+")) {
            return false;
        }
        if (username.length() < 3) {
            return false;
        }
        return true;
    }
    
    public boolean invalidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        return true;
    }
}
