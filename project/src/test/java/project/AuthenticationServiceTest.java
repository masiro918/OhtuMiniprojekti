
package project;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import project.DAO.UserDAO;
import project.domain.User;
import project.logic.AuthenticationService;

public class AuthenticationServiceTest {
    AuthenticationService service;
    
    @Before
    public void setUp() {
        service = new AuthenticationService();
        service.setUsers(new ArrayList<UserDAO>());
    }
    
    @Test
    public void invalidUsernameReturnsTrueForUsernameThatHasSpecialCharacters() {
        assertTrue(service.invalidUsername("M@tti"));
    }
    
    @Test
    public void invalidUsernameReturnsTrueForTooShortUsername() {
        assertTrue(service.invalidUsername("Li"));
    }
    
    @Test
    public void invalidUsernameReturnsFalseForProperUsername() {
        assertFalse(service.invalidUsername("Matti"));
    }
    
    @Test
    public void invalidPasswordReturnsTrueForPasswordWithOnlyLetters() {
        assertTrue(service.invalidPassword("pekanSalasana"));
    }
    
    @Test
    public void invalidPasswordReturnsTrueForTooShortPassword() {
        assertTrue(service.invalidPassword("sala123"));
    }
    
    @Test
    public void invalidPasswordReturnsFalseForProperPassword() {
        assertFalse(service.invalidPassword("salasana123"));
    }
    
    @Test
    public void creationStatusReturnsFalseForBadUsernameAndBadPassword() {
        assertFalse(service.creationStatus("P3kka", "salasana"));
    }
    
    @Test
    public void creationStatusReturnsFalseForBadUsernameAndProperPassword() {
        assertFalse(service.creationStatus("P3kka", "s4lasana"));
    }
    
    @Test
    public void creationStatusReturnsFalseForProperUsernameAndBadPassword() {
        assertFalse(service.creationStatus("Pekka", "salasana"));
    }
    
    @Test
    public void creationStatusReturnsTrueForProperUsernameAndProperPassword() {
        assertTrue(service.creationStatus("Pekka", "s4lasana"));
    }
    
    @Test
    public void createUserAddsUserWithProperUsernameAndProperPasswordToUsers() {
        service.createUser("Maija", "s4lasana");
        assertEquals("Maija", service.getUsers().get(0).getUsername());
    }
    
    @Test
    public void createUserDoesNotAddUserWithBadUsername() {
        service.createUser("M4ija", "s4lasana");
        assertTrue(service.getUsers().isEmpty());
    }
    
    @Test
    public void createUserDoesNotAddUserWithBadPassword() {
        service.createUser("Maija", "salasana");
        assertTrue(service.getUsers().isEmpty());
    }
    
    @Test
    public void createUserReturnsFalseIfBadUsername() {
        assertFalse(service.createUser("M4ija", "s4lasana"));
    }
    
    @Test
    public void createUserReturnsFalseIfBadPassword() {
        assertFalse(service.createUser("Maija", "salasana"));
    }
    
    @Test
    public void createUserReturnsTrueForProperUsernameAndPassword() {
        assertTrue(service.createUser("Maija", "s4lasana"));
    }
    
    @Test
    public void creationStatusReturnsFalseIfUsernameIsTaken() {
        service.getUsers().add(new User("Maija", "s4lasana"));
        assertFalse(service.creationStatus("Maija", "s4alasana"));
    }
    
    @Test
    public void findUserFindsAndReturnsCorrectUser() {
        User user = new User("Maija", "s4lasana");
        service.getUsers().add(user);
        assertTrue(user == service.findUser("Maija"));
    }
    
    @Test
    public void findUserReturnsNullIfUserIsNotFound() {
        User user = new User("Maija", "s4lasana");
        service.getUsers().add(user);
        assertTrue(null == service.findUser("Pekka"));
    }
    
    @Test
    public void loginReturnsUserThatSuccesfullyLogsIn() {
        User user = new User("Maija", "s4lasana");
        service.getUsers().add(user);
        assertTrue(user == service.login("Maija", "s4lasana"));
    }
    
    @Test
    public void loginReturnsNullForUserThatDoesNotExist() {
        User user = new User("Maija", "s4lasana");
        service.getUsers().add(user);
        assertTrue(null == service.login("Pekka", "s4lasana"));
    }
    
    @Test
    public void loginReturnsNullForWrongPassword() {
        User user = new User("Maija", "s4lasana");
        service.getUsers().add(user);
        assertTrue(null == service.login("Maija", "wrong"));
    }
}
