
package project.db;

import project.DAO.UserDAO;
import project.domain.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * Luokka käsittelee User-olioden tallennuksen
 * ja poiston tietokannassa.
 */
public class SQLUserDAO {
    private Connection connection = null;

    public SQLUserDAO() {

    }

    /**
     * Tallentaa käyttäjän tietokantaan.
     * @param user lisättävä olio
     *  @throws Exception
     */
    public void add(UserDAO user) throws Exception {
        this.createConnection();

        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Users (username, password) VALUES (? ?)");
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getPassword());

        pstmt.executeUpdate();
        this.closeConnection();
    }

    /**
     * Poistaa käyttäjän tietokannasta.
     * @param user poistettava olio
     * @throws Exception
     */
    public void remove(UserDAO user) throws Exception {
        this.createConnection();

        PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Users WHERE username=?");
        pstmt.setString(1, user.getUsername());

        pstmt.executeUpdate();
        this.closeConnection();
    }

    /**
     * Hae käyttäjän tiedot tietokannasta.
     * @throws Exception
     */
    public boolean login(UserDAO user) throws Exception {
        this.createConnection();

        PreparedStatement pstmt = connection.prepareStatement("SELECT username, password FROM Users WHERE username=? AND password=?");
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getPassword());

        ResultSet rs = pstmt.executeQuery();
        this.closeConnection();

        return rs.next();
    }

    public ArrayList<UserDAO> fetchAllUsers() throws Exception {
        this.createConnection();

        PreparedStatement pstmt = connection.prepareStatement("SELECT username, password FROM Users");

        ResultSet rs = pstmt.executeQuery();
        this.closeConnection();

        ArrayList<UserDAO> users = new ArrayList<>();
        while (rs.next()) {
            users.add(new User(rs.getString(1), rs.getString(2)));
        }

        return users;
    }

    /**
     * Luo uuden tietokanta-yhetyden.
     * @throws Exception
     */
    private void createConnection() throws Exception {
        this.connection = DriverManager.getConnection("jdbc:sqlite:database.db");
    }

    /**
     * Suolkee tietokanta-yhteyden.
     * @throws Exception
     */
    private void closeConnection() throws Exception {
        this.connection.close();
        this.connection = null;
    }

    /**
     * Tarkistaa, onko ReadingRecommendation-taulu olemassa.
     * @return true, jos on olemassa, muulloin false
     * @throws Exception
     */
    public boolean tableExists() throws Exception {
        return false;
    }
}
