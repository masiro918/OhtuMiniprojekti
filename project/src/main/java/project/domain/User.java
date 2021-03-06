package project.domain;

/**
 * A class that defines a user for the software.
 */
public class User implements UserInterface{
    private String username;
    private String password;
    private int id;
    
    public User(String u, String p) {
        this.username = u;
        this.password = p;
    }
    
    @Override
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    @Override
    public String getUsername() {
        return this.username;
    }
    
    @Override
    public String getPassword() {
        return this.password;
    }
}
