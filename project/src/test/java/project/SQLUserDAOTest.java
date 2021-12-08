package project;

import project.db.*;
import project.domain.*;
import project.logic.*;
import org.junit.Before;
import org.junit.Test;

import java.beans.Transient;
import java.rmi.server.RMIClassLoader;
import java.util.ArrayList;
import static org.junit.Assert.*;

import javax.sound.sampled.SourceDataLine;

public class SQLUserDAOTest {

    private SQLUserDAO sqlUserDAO = null;
    private TableCreator tb = new TableCreator();

    @Before
    public void setUp() throws Exception {
        try {
            tb.destroyDatabase();
        } catch (Exception e) {

        }

        this.sqlUserDAO = new SQLUserDAO();

        tb.createUser();
    }

    @Test
    public void addUserCorrectUsernameTest() {
        // Huom. ei ota kantaa esim. salasanan rakenteeseen
        sqlUserDAO.add(new User("testi", "testi"));
        assertEquals(sqlUserDAO.fetchAllUsers().get(0).getUsername(), "testi");
    }

    @Test
    public void addUserCorrectPasswordTest() {
        // Huom. ei ota kantaa esim. salasanan rakenteeseen
        sqlUserDAO.add(new User("testi", "testi"));
        assertEquals(sqlUserDAO.fetchAllUsers().get(0).getPassword(), "testi");
    }

    @Test
    public void loginWithCorrectCredentialsTest() {
        User user = new User("testi", "testi");
        sqlUserDAO.add(user);
        try {
            assertTrue(sqlUserDAO.login(user));
        } catch (Exception e) {

        }
    }

    @Test
    public void loginWithIncorrectCredentialsTest() {
        User user = new User("testi", "testi");
        try {
            assertFalse(sqlUserDAO.login(user));
        } catch (Exception e) {

        }
    }

    @Test
    public void allUsersAreFetchedTest() {
        User user = new User("testi", "testi");
        sqlUserDAO.add(user);
        User user2 = new User("testi", "testi");
        sqlUserDAO.add(user2);
        assertEquals(sqlUserDAO.fetchAllUsers().size(), 2);
    }

    @Test
    public void userRemovalTest() {
        User user = new User("testi", "testi");
        sqlUserDAO.add(user);
        try {
            sqlUserDAO.remove(user);
            assertEquals(sqlUserDAO.fetchAllUsers().size(), 0);
        } catch (Exception e) {

        }
    }
}