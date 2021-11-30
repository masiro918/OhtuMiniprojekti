package project.db;

import project.domain.BlogRecommendation;
import project.domain.Comment;
import java.sql.*;
import java.util.ArrayList;

/**
 * Luokka käsittelee BlogRecommendation-olioden tallennuksen
 * ja poiston tietokannassa.
 */
public class SQLReadingDAO implements ReadingRecommendationDAO {
    private Connection connection = null;
    private Statement statement = null;

    /**
     * Konstruktori.
     */
    public SQLReadingDAO() {

    }

    /**
     * Tallentaa blogi-vinkin tietokantaan.
     * @param blogRecommendation lisättävä olio
     * @throws Exception
     */
    public void add(BlogRecommendation blogRecommendation) throws Exception {
        Comment comment = blogRecommendation.getComment();
        int commentId = addComment(comment.getContent());
        this.createConnection();

        String headline = blogRecommendation.getHeadline();
        String type = blogRecommendation.getType();
        String url = blogRecommendation.getURL();
        String isbn = "empty";
        String writer = blogRecommendation.getWriter();

        ArrayList<String> courses = blogRecommendation.getRelatedCourses();
        ArrayList<String> tags = blogRecommendation.getTags();
        
        // kesken!!
        String sql = "INSERT INTO ReadingRecommendations (headline, type, url, isbn, writer, comment_id) " +
        "values (?, ?, ?, ?, ?, ?, ?, ?);";

        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, headline);
        ps.setString(2, type);
        ps.setString(3, url);
        ps.setString(4, isbn);
        ps.setString(5, writer);
        ps.setInt(6, commentId);
        

        ps.executeUpdate();

        this.closeConnection();

        // haetaan juuri lisätyn lukuvinkin id
        Integer readingId = getLastIdReading();

        // lisätään tagit ja kurssit
        for (String tag : tags) {
            addTag(tag, readingId);
        }

        for (String course : courses) {
            addCourse(course, readingId);
        }
    }

    /**
     * Poistaa blogi-vinkit tietokannasta.
     * @param blogRecommendation poistettava olio
     * @throws Exception
     */
    public void remove(BlogRecommendation blogRecommendation) throws Exception {
        //boolean checking = blogRecommendationExists(blogRecommendation.getURL());
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
     * Lisää kommentin Comments-tauluun, ja palauttaa lisätyn kommentin id-tunnisteen.
     * @param commentStr
     * @throws Exception
     * @return luodun kommentin id
     */
    public int addComment(String commentStr) throws Exception {
        Comment comment = new Comment(commentStr);

        String content = comment.getContent();
        
        this.createConnection();
        String sql = "INSERT INTO Comments (comment) values (?);";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, content);
        ps.executeUpdate();
        ps.close();
        this.closeConnection();

        // haetaan juuri lisätyn kommentin id
        int id = getLastIdReading();

        return id;
    }

    /**
     * Hakee viimeisimmäksi lisätyn lukuvinkin id:n.
     * @return uusimman lukuvinkin id
     * @throws Exception
     */
    public Integer getLastIdReading() throws Exception {
        this.createConnection();
        String sqlComment = "SELECT * FROM ReadingRecommendations;";
        ResultSet rs = this.statement.executeQuery(sqlComment);

        int id = -1;

        while (rs.next()) {
            id = rs.getInt("id");
        }

        this.closeConnection();

        return id;
    }

    /**
     * Lisää uuden tagin.
     * @param tag lisättävä tagi
     * @param reading_id viite lukuvinkkiin
     * @throws Exception
     */
    public void addTag(String tag, int reading_id) throws Exception {
        this.createConnection();

        String sql = "INSERT INTO Tags (comment, readingRecommendation_id) values (?, ?);";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, tag);
        ps.setInt(2, reading_id);
        ps.executeUpdate();
        ps.close();

        this.closeConnection();
    }

    /**
     * 
     * @param course
     * @param reading_id
     * @throws Exception
     */
    public void addCourse(String course, int reading_id) throws Exception {
        this.createConnection();

        String sql = "INSERT INTO RelatedCourses (course, readingRecommendation_id) values (?, ?);";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, course);
        ps.setInt(2, reading_id);
        ps.executeUpdate();
        ps.close();

        this.closeConnection();
    }
}
