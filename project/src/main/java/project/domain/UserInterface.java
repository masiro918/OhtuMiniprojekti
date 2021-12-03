package project.domain;

import java.util.List;
import project.domain.User;

/**
 * A interface for a user of the software.
 */
public interface UserInterface {
    void setId(int id);
    int getId();
    String getUsername();
    String getPassword();
}
