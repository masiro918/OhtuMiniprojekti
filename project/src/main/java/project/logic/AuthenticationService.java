package project.logic;

import java.util.ArrayList;
import project.domain.UserInterface;
import project.db.UserDAO;
import project.domain.User;

/**
 * A class responsible for handling user account functions (create a user, log user in, save users).
 */
public class AuthenticationService {
    private UserDAO userDb;
    private ArrayList<String> errorMessages;
    
    public AuthenticationService(UserDAO userdao) {
        this.userDb = userdao;
        this.errorMessages = new ArrayList<String>();
    }
    
    public String getErrorMessages() {
        String print = "" + this.errorMessages;
        return print.substring(1, print.length() - 1);
    }
    
    /**
     * Save user to database.
     * 
     * @param user user that is saved
     */
    public void saveUser(UserInterface user) {
        userDb.add(user);
    }
    
    /**
     * Returns user upon successful login.
     * 
     * @param username username of user that tries to log in
     * @param password password of user that tries to log in
     * @return user if login successful, null if not
     */
    public UserInterface login(String username, String password) {
        for (UserInterface u : this.userDb.fetchAllUsers()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
    
    /**
     * Create and save new user.
     * 
     * @param username new username
     * @param password proper password
     * @return true if creation successful, false if not
     */
    public boolean createUser(String username, String password) {
        if (creationStatus(username, password)) {
            User user = new User(username, password);
            saveUser(user);
            return true;
        }
        return false;
    }
    
    /**
     * Find user from database.
     * 
     * @param username user to be found
     * @return user if was found, null if not
     */
    public UserInterface findUser(String username) {
        for (UserInterface u : this.userDb.fetchAllUsers()) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
    
    /**
     * Check if username and password are valid.
     * 
     * @param username username that is tried
     * @param password password that is tried
     * @return true if both username and password are acceptable, otherwise false
     */
    public boolean creationStatus(String username, String password) {
        this.errorMessages = new ArrayList<>();
        if (invalidUsername(username)) {
            this.errorMessages.add("username must be at least 3 characters long and contain only letters");
        }
        
        if (invalidPassword(password)) {
            this.errorMessages.add("password must be at least 8 characters long and must include numbers or special characters");
        }
        
        if (findUser(username) != null) {
            this.errorMessages.add("username is taken");
        }
        
        return this.errorMessages.isEmpty();
    }
    
    /**
     * Check if username is invalid.
     * Username must consist of only letters and be at least 3 characters long.
     * 
     * @param username username that is tested
     * @return true if username is invalid, false if username is valid
     */
    public boolean invalidUsername(String username) {
        if (!username.matches("[a-zA-Z]+")) {
            return true;
        }
        
        if (username.length() < 3) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Check if password is invalid.
     * Password must include special characters and be at least 8 characters long.
     * 
     * @param password password that is tested
     * @return true if password is invalid, false if password is valid
     */
    public boolean invalidPassword(String password) {
        if (password.matches("[a-zA-Z]+")) {
            return true;
        }
        
        if (password.length() < 8) {
            return true;
        }
        return false;
    }
    
    // debuggausta varten
    public ArrayList<UserInterface> getAll() {
        return this.userDb.fetchAllUsers();
    }
}
