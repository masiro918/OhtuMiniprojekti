
package project.db;

import project.domain.UserInterface;
import project.domain.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * Luokka käsittelee User-olioden tallennuksen
 * ja poiston tietokannassa.
 */
public class SQLUserDAO implements UserDAO {
    private String dbUrl = "jdbc:sqlite:database.db";
    private Connection conn = null;

    public SQLUserDAO() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        // Postgres Heroku
        if (processBuilder.environment().get("JDBC_DATABASE_URL") != null) {
            this.dbUrl = System.getenv("JDBC_DATABASE_URL");
        }
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
    @Override
    public void add(UserInterface user) {
        try {
            this.createConnection();

            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Users (username, password) VALUES (?, ?);");
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());

            //System.out.println(user.getUsername());

            pstmt.executeUpdate();
            pstmt.close();
            this.closeConnection();
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            //System.out.println("Kayttajan tallennus epaonnistui.");
        }
    }

    /**
     * Poistaa käyttäjän tietokannasta.
     * @param user poistettava olio
     * @throws Exception
     */
    @Override
    public void remove(UserInterface user) throws Exception {
        this.createConnection();

        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Users WHERE username=?");
        pstmt.setString(1, user.getUsername());

        pstmt.executeUpdate();
        pstmt.close();
        this.closeConnection();
    }

    /**
     * Hae käyttäjän tiedot tietokannasta.
     * @throws Exception
     */
    @Override
    public boolean login(UserInterface user) throws Exception {
        this.createConnection();

        PreparedStatement pstmt = conn.prepareStatement("SELECT username, password FROM Users WHERE username=? AND password=?");
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getPassword());

        ResultSet rs = pstmt.executeQuery();
        this.closeConnection();

        return rs.next();
    }

    @Override
    public ArrayList<UserInterface> fetchAllUsers() {
        ArrayList<UserInterface> users = new ArrayList<>();
        try {
            this.createConnection();

            PreparedStatement pstmt = conn.prepareStatement("SELECT id, username, password FROM Users");

            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int userId = rs.getInt(1);
                User user = new User(rs.getString(2), rs.getString(3));
                user.setId(userId);
                users.add(user);
            }
            pstmt.close();
            rs.close();
            this.closeConnection();
        } catch (Exception e) {
            return users;
        }
        return users;
    }

    /**
     * Luo uuden tietokanta-yhetyden.
     * @throws Exception
     */
    private void createConnection() throws Exception {
        this.conn = DriverManager.getConnection(dbUrl);
    }

    /**
     * Suolkee tietokanta-yhteyden.
     * @throws Exception
     */
    private void closeConnection() throws Exception {
        this.conn.close();
        this.conn = null;
    }
}
