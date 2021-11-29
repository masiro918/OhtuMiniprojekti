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
        this.createConnection();
        PreparedStatement pstmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Users (id INTEGER, username STRING, password STRING");
        pstmt.executeQuery();
        this.closeConnection();
        return true;
    }

    /**
     * Luo uuden ReadingRecommendations-taulun, jos sitä ei ole jo oleamassa.
     * @throws Exception
     */
    public void createReadingRecommendations() throws Exception {
        this.createConnection();
        String sql = "CREATE TABLE IF NOT EXISTS ReadingRecommendations (id INTEGER, headline STRING, type STRING,"
                + "url STRING, isbn STRING, writer STRING, comment_id INTEGER, course_id INTEGER,"
                + " tag_id INTEGER);";
        this.statement.execute(sql);
        this.closeConnection();
    }

    /**
     * Luo uuden ReadingRecommendation-taulun, jos sitä ei ole jo oleamassa.
     * @throws Exception
     */
    public void createRelatedCourses() throws Exception {
        this.createConnection();
        String sql = "CREATE TABLE IF NOT EXISTS RelatedCourses (id INTEGER, course STRING, readingRecommendation_id INTEGER);";
        this.statement.execute(sql);
        this.closeConnection();
    }

    /**
     * Luo uuden Tags-taulun, jos sitä ei ole jo olemassa.
     * @throws Exception
     */
    public void createTags() throws Exception {
        this.createConnection();
        String sql = "CREATE TABLE IF NOT EXISTS Tags (id INTEGER, tag STRING, readingRecommendation_id INTEGER);";
        this.statement.execute(sql);
        this.closeConnection();
    }

    /**
     * Luo uuden Comments-taulun, jos sitä ei ole jo olemassa.
     * @throws Exception
     */
    public void createCommments() throws Exception {
        this.createConnection();
        String sql = "CREATE TABLE IF NOT EXISTS Comments (id INTEGER, comment STRING, readingRecommendation_id INTEGER);";
        this.statement.execute(sql);
        this.closeConnection();
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
