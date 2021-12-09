package project.domain;


/**
 * A interface for a user of the software.
 */
public interface UserInterface {
    public void setId(int id);
    public int getId();
    public String getUsername();
    public String getPassword();
}
