package project;

import project.db.*;

import java.beans.Transient;
import java.sql.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreateTablesTest {

    private Connection connection = null;
    private Statement statement = null;
    private TableCreator tc = null;

    @Before
    public void setUp() {
        this.tc = new TableCreator();
    }

    @Test
    public void createUserTableTest() throws Exception {
        boolean success = false;

        try {
            this.tc.createUser();
            createConnection();
            ResultSet rs = this.statement.executeQuery( "SELECT * FROM Users;" );
            
            while (rs.next()) {

            }

            rs.close();
            closeConnection();
            success = true;
        } catch (Exception e) {
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void createReadingRecommendationsTableTest() throws Exception {
        boolean success = false;

        try {
            this.tc.createReadingRecommendations();
            createConnection();
            ResultSet rs = this.statement.executeQuery( "SELECT * FROM ReadingRecommendations;" );
            
            while (rs.next()) {

            }

            rs.close();
            closeConnection();
            success = true;
        } catch (Exception e) {
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void createRelatedCoursesTableTest() throws Exception {
        boolean success = false;

        try {
            this.tc.createRelatedCourses();
            createConnection();
            ResultSet rs = this.statement.executeQuery( "SELECT * FROM RelatedCourses;" );
            
            while (rs.next()) {

            }

            rs.close();
            closeConnection();
            success = true;
        } catch (Exception e) {
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void createTagsTestTableTest() throws Exception {
        boolean success = false;

        try {
            this.tc.createTags();
            createConnection();
            ResultSet rs = this.statement.executeQuery( "SELECT * FROM Tags;" );
            
            while (rs.next()) {

            }

            rs.close();
            closeConnection();
            success = true;
        } catch (Exception e) {
            success = false;
        }
        assertTrue(success);
    }

    
    private void createConnection() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        this.statement = connection.createStatement();
    }
    
    private void closeConnection() throws SQLException {
        this.statement.close();
        this.connection.close();
        
        this.connection = null;
        this.statement = null;
    }
    
}