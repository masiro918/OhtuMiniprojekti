package project;

import project.db.*;
import java.sql.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreateTablesTest {

    private Connection connection = null;
    private Statement statement = null;

    @Before
    public void setUp() {
        TableCreator tc = new TableCreator();
    }

    @Test
    public void createUserTableTest() throws Exception {
        /*
        tc.createUser();

        createConnection();
        */
    }

    /*
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
    */
}