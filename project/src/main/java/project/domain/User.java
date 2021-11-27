
package project.domain;

import project.DAO.UserDAO;

public class User implements UserDAO {
    private String username;
    private String password;
    
    public User(String u, String p) {
        this.username = u;
        this.password = p;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }
}
