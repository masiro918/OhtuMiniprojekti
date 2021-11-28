/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.db;

import java.sql.*;

/**
 * Tarkistaa, onko tarvittavia tietokantatauluja olemassa.
 * @author user
 */
public class TableCreator {
    private Connection connection = null;
    private Statement statement = null;
    
    public TableCreator() {
        
    }

    /**
     * Luo Users-taulu, jos sitä ei ole jo olemassa.
     * @return
     * @throws Exception
     */
    public boolean createUser() throws Exception {
        SQLUserDAO userDb = new SQLUserDAO();
        if (userDb.tableExists()) {
            return true;
        }

        this.createConnection();
        PreparedStatement pstmt = connection.prepareStatement("CREATE TABLE Users (id INTEGER, username STRING, password STRING");
        pstmt.executeQuery();
        this.closeConnection();
        return true;
    }

    /**
     * Luo uuden ReadingRecommendation-taulun, jos sitä ei ole jo oleamassa.
     * @return true jos taulua ei ole olemassa, false, jos taulu on jo olemassa
     * @throws Exception
     */
    public boolean createReadingRecommendation() throws Exception {
        //TODO: check exists
        this.createConnection();
        //(id, headline, type, url, comment_id, course_id, tag_id)
        String sql = "CREATE TABLE ReadingRecommendations (id INTEGER, headline STRING, type STRING,"
                + "url STRING, isbn STRING, writer STRING, comment_id INTEGER, course_id INTEGER,"
                + " tag_id INTEGER);";
        this.statement.execute(sql);
        this.closeConnection();
        return true;
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
