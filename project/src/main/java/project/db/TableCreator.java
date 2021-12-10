/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.db;

import java.sql.*;
import java.io.*;

/**
 * Tarkistaa, onko tarvittavia tietokantatauluja olemassa.
 * @author user
 */
public class TableCreator {
    private Connection connection = null;
    private Statement statement = null;
    private String url = "jdbc:sqlite:database.db";
    private boolean heroku = false;

    /**
     * Constructor.
     */
    public TableCreator() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        // Postgres Heroku
        if (processBuilder.environment().get("JDBC_DATABASE_URL") != null) {
            this.url = System.getenv("JDBC_DATABASE_URL");
            this.heroku = true;
        }
    }

    /**
     * Luo Users-taulu, jos sitä ei ole jo olemassa.
     * @return
     * @throws Exception
     */
    public boolean createUser() throws Exception {
        this.createConnection();
        //PreparedStatement pstmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Users (id INTEGER PRIMARY KEY AUTOINCREMENT, username STRING, password STRING);");
        //pstmt.executeQuery();
        String sql = "CREATE TABLE IF NOT EXISTS Users (id INTEGER PRIMARY KEY AUTOINCREMENT, username STRING, password STRING);";
        if (heroku) {
            sql = "CREATE TABLE IF NOT EXISTS Users (id SERIAL, username STRING, password STRING);";
        }
        this.statement.execute(sql);
        this.closeConnection();
        return true;
    }

    /**
     * Luo uuden ReadingRecommendations-taulun, jos sitä ei ole jo oleamassa.
     * @throws Exception
     */
    public void createReadingRecommendations() throws Exception {
        this.createConnection();
        String sql = "CREATE TABLE IF NOT EXISTS ReadingRecommendations (id INTEGER PRIMARY KEY AUTOINCREMENT, headline STRING, type STRING,"
                + "url STRING, isbn STRING, writer STRING, podcastName STRING, description STRING, comment STRING, user_id INTEGER);";
        if (heroku) {
            sql = "CREATE TABLE IF NOT EXISTS ReadingRecommendations (id SERIAL, headline STRING, type STRING,"
                    + "url STRING, isbn STRING, writer STRING, podcastName STRING, description STRING, comment STRING, user_id INTEGER);";
        }
        this.statement.execute(sql);
        this.closeConnection();
    }

    /**
     * Luo uuden ReadingRecommendation-taulun, jos sitä ei ole jo oleamassa.
     * @throws Exception
     */
    public void createRelatedCourses() throws Exception {
        this.createConnection();
        String sql = "CREATE TABLE IF NOT EXISTS RelatedCourses (id INTEGER PRIMARY KEY AUTOINCREMENT, course STRING, readingRecommendation_id INTEGER);";
        if (heroku) {
            sql = "CREATE TABLE IF NOT EXISTS RelatedCourses (id SERIAL, course STRING, readingRecommendation_id INTEGER);";
        }
        this.statement.execute(sql);
        this.closeConnection();
    }

    /**
     * Luo uuden Tags-taulun, jos sitä ei ole jo olemassa.
     * @throws Exception
     */
    public void createTags() throws Exception {
        this.createConnection();
        String sql = "CREATE TABLE IF NOT EXISTS Tags (id INTEGER PRIMARY KEY AUTOINCREMENT, tag STRING, readingRecommendation_id INTEGER);";
        if (heroku) {
            sql = "CREATE TABLE IF NOT EXISTS Tags (id SERIAL, tag STRING, readingRecommendation_id INTEGER);";
        }
        this.statement.execute(sql);
        this.closeConnection();
    }

    /**
     * Luo uuden Comments-taulun, jos sitä ei ole jo olemassa.
     * @throws Exception
     */
    public void createCommments() throws Exception {
        this.createConnection();
        String sql = "CREATE TABLE IF NOT EXISTS Comments (id INTEGER PRIMARY KEY AUTOINCREMENT, comment STRING, readingRecommendation_id INTEGER);";
        if (heroku) {
            sql = "CREATE TABLE IF NOT EXISTS Comments (id SERIAL, comment STRING, readingRecommendation_id INTEGER);";
        }
        this.statement.execute(sql);
        this.closeConnection();
    }
    
    private void createConnection() throws SQLException {
        this.connection = DriverManager.getConnection(url);
        this.statement = connection.createStatement();
    }
    
    private void closeConnection() throws SQLException {
        this.statement.close();
        this.connection.close();
        
        this.connection = null;
        this.statement = null;
    }

    /**
     * Tuhoaa tietokannan. Ole huolellinen tämä metodin kanssa. Tällä ei ole tuskin muuta käyttöä
     * kuin testien yhteydessä
     * @throws Exception
     */
    public void destroyDatabase() throws Exception {
        File dbFile = new File("database.db");
        if (dbFile.delete()) {
            return;
        } else {
            throw new Exception("tietokannan poisto ei onnistunut:");
        }
    }
}
