package project.db;

import project.domain.BlogRecommendation;
import java.sql.*;

/**
 * Luokka käsittelee BlogRecommendation-olioden tallennuksen
 * ja poiston tietokannassa.
 */
public class SQLBlogDAO {
    private Connection connection = null;
    private Statement statement = null;

    /**
     * Konstruktori.
     */
    public SQLBlogDAO() {

    }

    /**
     * Tallentaa blogi-vinkin tietokantaan.
     * @param blogRecommendation lisättävä olio
     *  @throws Exception
     */
    public void add(BlogRecommendation blogRecommendation) throws Exception {
        this.createConnection();

        String headline = blogRecommendation.getHeadline();
        String type = blogRecommendation.getType();
        String url = blogRecommendation.getURL();

        ArrayList<String> courses = blogRecommendation.getRelatedCourses();
        ArrayList<String> tags = blogRecommendation.getTags();
        
        // kesken!!
        String sql = "INSERT INTO BlogRexommendation (Id, Headline, Type, Url, course_id, tag_id) values (9999, 'empty', 'empty' 'empty', 9999, 9999);";

        statement.executeUpdate(sql);
        this.closeConnection();
    }

    /**
     * Poistaa blogi-vinkit tietokannasta.
     * @param blogRecommendation poistettava olio
     * @throws Exception
     */
    public void remove(BlogRecommendation blogRecommendation) throws Exception {
        boolean checking = blogRecommendationExists(blogRecommendation.getURL());
    }

    /**
     * Luo uuden tietokanta-yhetyden.
     * @throws Exception
     */
    private void createConnection() throws Exception {
        this.connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        this.statement = connection.createStatement();
    }

    /**
     * Suolkee tietokanta-yhteyden.
     * @throws Exception
     */
    private void closeConnection() throws Exception {
        this.statement.close();
        this.connection.close();
        
        this.connection = null;
        this.statement = null;
    }

    /**
     * Tarkistaa, että blogimerkintä löytyy tietokannasta.
     * @param url blogin-url osoite (tämä riittää, koska url-osoitteet ovat uniikkeja)
     */
    private void blogRecommendationExists(String url) {

    }
}
