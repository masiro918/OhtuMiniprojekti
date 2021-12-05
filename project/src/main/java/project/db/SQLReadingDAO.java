package project.db;

import project.domain.BlogRecommendation;
import java.sql.*;
import java.util.ArrayList;
import project.domain.BookRecommendation;
import project.domain.PodcastRecommendation;
import project.domain.ReadingRecommendationInterface;

/**
 * Luokka käsittelee BlogRecommendation-olioden tallennuksen ja poiston
 * tietokannassa.
 */
public class SQLReadingDAO implements ReadingRecommendationDAO {

    private Connection connection = null;
    private Statement statement = null;
    
    private int userId;

    /**
     * Konstruktori.
     */
    public SQLReadingDAO() {
        this.userId = 0;
    }
    
    @Override
    public void setUserId(int id) {
        this.userId = id;
    }

    @Override
    public void add(ReadingRecommendationInterface r) throws Exception {
        if (r.getType().equals("blog")) {
            BlogRecommendation blog = (BlogRecommendation) r;
            addBlog(blog);
        } else if (r.getType().equals("book")) {
            BookRecommendation book = (BookRecommendation) r;
            addBook(book);
        } else if (r.getType().equals("podcast")) {
            PodcastRecommendation podcast = (PodcastRecommendation) r;
            addPodcast(podcast);
        }
    }

    /**
     * Tallentaa blogi-vinkin tietokantaan.
     *
     * @param blogRecommendation lisättävä olio
     * @throws Exception
     */
    public void addBlog(BlogRecommendation blogRecommendation) throws Exception {
        //int commentId = addComment(blogRecommendation.getComment());
        this.createConnection();

        String headline = blogRecommendation.getHeadline();
        String type = blogRecommendation.getType();
        String url = blogRecommendation.getURL();
        String isbn = "empty";
        String writer = blogRecommendation.getWriter();
        String comment = blogRecommendation.getComment();

        ArrayList<String> courses = blogRecommendation.getRelatedCourses();
        ArrayList<String> tags = blogRecommendation.getTags();

        // kesken!!
        String sql = "INSERT INTO ReadingRecommendations (headline, type, url, isbn, writer, comment, user_id) "
                + "values (?, ?, ?, ?, ?, ?, ?);";

        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, headline);
        ps.setString(2, type);
        ps.setString(3, url);
        ps.setString(4, isbn);
        ps.setString(5, writer);
        ps.setString(6, comment);
        ps.setInt(7, this.userId);

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
     * Lisää uuden kirjavinkin.
     * @param book lisättävä kirjavikko
     */
    public void addBook(BookRecommendation book) {
        try {
            this.createConnection();

            String headline = book.getHeadline();
            String type = book.getType();
            String isbn = book.getISBN();
            String writer = book.getWriter();
            String comment = book.getComment();

            ArrayList<String> courses = book.getRelatedCourses();
            ArrayList<String> tags = book.getTags();

            String sql = "INSERT INTO ReadingRecommendations (headline, type, isbn, writer, comment, user_id) "
                    + "values (?, ?, ?, ?, ?, ?);";

            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, headline);
            ps.setString(2, type);
            ps.setString(3, isbn);
            ps.setString(4, writer);
            ps.setString(5, comment);
            ps.setInt(6, this.userId);

            ps.executeUpdate();

            this.closeConnection();

            Integer readingId = getLastIdReading();

            for (String tag : tags) {
                addTag(tag, readingId);
            }

            for (String course : courses) {
                addCourse(course, readingId);
            }
        } catch (Exception e) {

        }

    }
    
    /**
     * Adds a new podcast recommendation.
     * @param podcast added podcast
     */
    public void addPodcast(PodcastRecommendation podcast) {
        try {
            this.createConnection();

            String headline = podcast.getHeadline();
            String type = podcast.getType();
            String writer = podcast.getWriter();
            String podcastName = podcast.getPodcastName();
            String description = podcast.getDescription();
            String comment = podcast.getComment();

            ArrayList<String> courses = podcast.getRelatedCourses();
            ArrayList<String> tags = podcast.getTags();

            String sql = "INSERT INTO ReadingRecommendations (headline, type, writer, podcastName, description, comment, user_id) "
                    + "values (?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, headline);
            ps.setString(2, type);
            ps.setString(3, writer);
            ps.setString(4, podcastName);
            ps.setString(5, description);
            ps.setString(6, comment);
            ps.setInt(7, this.userId);

            ps.executeUpdate();

            this.closeConnection();

            Integer readingId = getLastIdReading();

            for (String tag : tags) {
                addTag(tag, readingId);
            }

            for (String course : courses) {
                addCourse(course, readingId);
            }
            
        } catch (Exception e) {

        }
    }

    /**
     * Hakee blogivinkin id:n perusteella.
     * @param id haettavan blogivinkin id
     * @return haettu blogivinkki
     * @throws Exception
     */
    public BlogRecommendation getBlog(int id) throws Exception {
        this.createConnection();
        String sqlComment = "SELECT * FROM ReadingRecommendations WHERE id=? AND type=?;";
        PreparedStatement ps = this.connection.prepareStatement(sqlComment);
        ps.setInt(1,id);
        ps.setString(2,"blog");
        ResultSet rs = ps.executeQuery();
        BlogRecommendation blog = new BlogRecommendation(rs.getString("headline"),
                                                         rs.getString("type"),
                                                         rs.getString("url"));
        blog.setWriter(rs.getString("writer"));
        blog.setId(id);
        rs.close();
        this.closeConnection();
        return blog;
    }

    /**
     * Poistaa blogi-vinkit tietokannasta.
     *
     * @param blogRecommendation poistettava olio
     * @throws Exception
     */
    private void removeBlog(BlogRecommendation blogRecommendation) throws Exception {
        this.createConnection();
        String sql = "DELETE FROM ReadingRecommendations WHERE url=? writer=? headline=? user_id=?;";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, blogRecommendation.getURL());
        ps.setString(2, blogRecommendation.getWriter());
        ps.setString(3, blogRecommendation.getHeadline());
        ps.setInt(4, this.userId);
        ps.executeUpdate();
        this.closeConnection();
    }


    private void removeBook(BookRecommendation bookRecommendation) throws Exception {
        this.createConnection();
        String sql = "DELETE FROM ReadingRecommendations WHERE ISBN=? writer=? headline=? user_id=?;";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, bookRecommendation.getISBN());
        ps.setString(2, bookRecommendation.getWriter());
        ps.setString(3, bookRecommendation.getHeadline());
        ps.setInt(4, this.userId);
        ps.executeUpdate();
        this.closeConnection();
    }
    
    private void removePodcast(PodcastRecommendation podcastRecommendation) throws Exception {
        this.createConnection();
        String sql = "DELETE FROM ReadingRecommendations WHERE type=? headline=? podcastName=? user_id=?;";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, podcastRecommendation.getType());
        ps.setString(2, podcastRecommendation.getHeadline());
        ps.setString(3, podcastRecommendation.getPodcastName());
        ps.setInt(4, this.userId);
        ps.executeUpdate();
        this.closeConnection();
    }

    @Override
    public void remove(ReadingRecommendationInterface r) throws Exception{
        String type = r.getType();
        if (type == "blog") {
            removeBlog((BlogRecommendation) r);
        } else if (type == "book") {
            removeBook((BookRecommendation) r);
        } if (type == "podcast") {
            removePodcast((PodcastRecommendation) r);
        }
    }

    /**
     * Luo uuden tietokanta-yhetyden.
     *
     * @throws Exception
     */
    private void createConnection() throws Exception {
        this.connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        this.statement = connection.createStatement();
    }

    /**
     * Suolkee tietokanta-yhteyden.
     *
     * @throws Exception
     */
    private void closeConnection() throws Exception {
        this.statement.close();
        this.connection.close();

        this.connection = null;
        this.statement = null;
    }

    /**
     * Lisää kommentin Comments-tauluun, ja palauttaa lisätyn kommentin
     * id-tunnisteen.
     *
     * @param commentStr
     * @throws Exception
     * @return luodun kommentin id
     */
    public int addComment(String commentStr) throws Exception {
        String content = commentStr;

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
     *
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
     *
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

    @Override
    public ArrayList<ReadingRecommendationInterface> loadAll() throws Exception {
        ArrayList<ReadingRecommendationInterface> recommendations = new ArrayList<>();
        this.createConnection();
        
        //TODO: kayta this.userId oikean kayttajan lukuvinkkien hakemiseen!
        
        String sqlComment = "SELECT * FROM ReadingRecommendations;";
        ResultSet rs = this.statement.executeQuery(sqlComment);
        while (rs.next()) {
            String headline = rs.getString("headline");
            String type = rs.getString("type");
            
            if (type.equals("blog")) {
                BlogRecommendation blog = new BlogRecommendation(headline, type, rs.getString("url"));
                blog.setWriter(rs.getString("writer"));
                blog.setId(rs.getInt("id"));
                recommendations.add(blog);
                
            } else if (type.equals("book")) {
                BookRecommendation book = new BookRecommendation(headline, type, rs.getString("isbn"));
                book.setWriter(rs.getString("writer"));
                book.setId(rs.getInt("id"));
                recommendations.add(book);
            }
//            ReadingRecommendation recommendation = new ReadingRecommendation(rs.getString("headline"),
//                    rs.getString("type"));
//            recommendation.setId(rs.getInt("id"));
//            recommendations.add(recommendation);
        }
        rs.close();
        this.closeConnection();
        return recommendations;
    }
}
