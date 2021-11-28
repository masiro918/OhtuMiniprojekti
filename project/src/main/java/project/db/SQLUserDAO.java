
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
    private String dbUrl = "jdbc:sqlite:database.db";
    private Connection connection = null;

    public SQLUserDAO() {

    }

    /**
     * Muodostaa yhteyden muuhun, kuin default-tietokantaan.
     * @param dbUrl tietokannan osoite muotoa jdbc:sqlite:database.db
     */
    public SQLUserDAO(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    /**
     * Tallentaa käyttäjän tietokantaan.
     * @param user lisättävä olio
     *  @throws Exception
     */
    public void add(UserDAO user) {
        try {
            this.createConnection();

            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Users (username, password) VALUES (? ?)");
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());

            pstmt.executeUpdate();
            this.closeConnection();
        } catch (Exception e) {

        }
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

    public ArrayList<UserDAO> fetchAllUsers() {
        ArrayList<UserDAO> users = new ArrayList<>();
        try {
            this.createConnection();

            PreparedStatement pstmt = connection.prepareStatement("SELECT username, password FROM Users");

            ResultSet rs = pstmt.executeQuery();
            this.closeConnection();

            while (rs.next()) {
                users.add(new User(rs.getString(1), rs.getString(2)));
            }
        } catch (Exception e) {

        }
        return users;
    }

    /**
     * Luo uuden tietokanta-yhetyden.
     * @throws Exception
     */
    private void createConnection() throws Exception {
        this.connection = DriverManager.getConnection(dbUrl);
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
