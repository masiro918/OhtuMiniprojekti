package project.db;

import project.domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Luokka käsittelee BlogRecommendation-olioden tallennuksen ja poiston
 * tietokannassa.
 */
public class SQLReadingDAO implements ReadingRecommendationDAO {

    private Connection connection = null;
    private Statement statement = null;

    private String dbUrl = "jdbc:sqlite:database.db";
    private int userId;

    /**
     * Konstruktori.
     */
    public SQLReadingDAO() {
        this.userId = 0;
        ProcessBuilder processBuilder = new ProcessBuilder();
        // Postgres Heroku
        if (processBuilder.environment().get("JDBC_DATABASE_URL") != null) {
            this.dbUrl = System.getenv("JDBC_DATABASE_URL");
        }
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
        String podcastName = "empty";
        String description = "empty";
        String comment = blogRecommendation.getComment();

        ArrayList<String> courses = blogRecommendation.getRelatedCourses();
        ArrayList<String> tags = blogRecommendation.getTags();

        // kesken!!
        String sql = "INSERT INTO ReadingRecommendations (headline, type, url, isbn, writer, podcastName, description, comment, user_id) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, headline);
        ps.setString(2, type);
        ps.setString(3, url);
        ps.setString(4, isbn);
        ps.setString(5, writer);
        ps.setString(6, podcastName);
        ps.setString(7, description);
        ps.setString(8, comment);
        ps.setInt(9, this.userId);

        ps.executeUpdate();
        ps.close();
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
     *
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
            ps.close();
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
     *
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
            ps.close();
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
     *
     * @param id haettavan blogivinkin id
     * @return haettu blogivinkki
     * @throws Exception
     */
    @Override
    public BlogRecommendation getBlog(int id) throws Exception {
        this.createConnection();
        String sqlComment = "SELECT * FROM ReadingRecommendations WHERE id=? AND type=?;";
        PreparedStatement ps = this.connection.prepareStatement(sqlComment);
        ps.setInt(1, id);
        ps.setString(2, "blog");
        ResultSet rs = ps.executeQuery();
        BlogRecommendation blog = new BlogRecommendation(rs.getString("headline"),
                rs.getString("type"),
                rs.getString("url"));
        blog.setWriter(rs.getString("writer"));
        blog.setComment(rs.getString("comment"));
        blog.setId(id);
        rs.close();
        this.closeConnection();
        for (String tag : getAllTags(id)) {
            blog.addTags(tag);
        }
        for (String course : getAllCourses(id)) {
            blog.addCourse(course);
        }
        return blog;
    }

    /**
     *
     * @param id haettavan kirjavinkin id
     * @return haettu kirja
     * @throws Exception
     */
    @Override
    public BookRecommendation getBook(int id) throws Exception {
        this.createConnection();
        String sqlComment = "SELECT * FROM ReadingRecommendations WHERE id=? AND type=?";
        PreparedStatement ps = this.connection.prepareStatement(sqlComment);
        ps.setInt(1, id);
        ps.setString(2, "book");
        ResultSet rs = ps.executeQuery();
        BookRecommendation book = new BookRecommendation(rs.getString("headline"),
                rs.getString("type"),
                rs.getString("writer"));
        book.setISBN(rs.getString("ISBN"));
        book.setComment(rs.getString("comment"));
        book.setId(id);
        rs.close();
        this.closeConnection();
        for (String tag : getAllTags(id)) {
            book.addTags(tag);
        }
        for (String course : getAllCourses(id)) {
            book.addCourse(course);
        }
        return book;
    }
    
    @Override
    public PodcastRecommendation getPodcast(int id) throws Exception {
        this.createConnection();
        String sqlComment = "SELECT * FROM ReadingRecommendations WHERE id=? AND type=?";
        PreparedStatement ps = this.connection.prepareStatement(sqlComment);
        ps.setInt(1, id);
        ps.setString(2, "podcast");
        ResultSet rs = ps.executeQuery();
        PodcastRecommendation podcast = new PodcastRecommendation(rs.getString("headline"),
                rs.getString("type"),
                rs.getString("podcastName"),
                rs.getString("description"));
        podcast.setWriter(rs.getString("writer"));
        podcast.setComment(rs.getString("comment"));
        podcast.setId(id);
        rs.close();
        this.closeConnection();
        for (String tag : getAllTags(id)) {
            podcast.addTags(tag);
        }
        for (String course : getAllCourses(id)) {
            podcast.addCourse(course);
        }
        return podcast;
    }

    /**
     * Hakee kurssin ReadingRecommendations-olion id:n mukaan
     *
     * @param id ReadingRecommendations-olion id
     * @return ReadingRecommendations-olioon liitetty kurssi
     * @throws Exception
     */
    public String getCourse(int id) throws Exception {
        this.createConnection();
        String sql = "SELECT * FROM RelatedCourses WHERE readingRecommendation_id=?;";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        String course = rs.getString("course");
        rs.close();
        ps.close();
        this.closeConnection();
        return course;
    }

    public ArrayList<String> getAllCourses(int id) throws Exception {
        ArrayList<String> courses = new ArrayList<>();
        this.createConnection();
        String sql = "SELECT * FROM RelatedCourses WHERE readingRecommendation_id=?;";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            courses.add(rs.getString("course"));
        }
        rs.close();
        ps.close();
        this.closeConnection();
        return courses;
    }
    
    public void updateTags(ReadingRecommendationInterface r) throws Exception {
        removeTags(r.getId());
        for (String tag : r.getTags()) {
            addTag(tag, r.getId());
        }
    }
    
    public void updateCourses(ReadingRecommendationInterface r) throws Exception {
        removeCourses(r.getId());
        for (String course : r.getRelatedCourses()) {
            addCourse(course, r.getId());
        }
    }
    
    @Override
    public void updateBlog(BlogRecommendation blog) throws Exception {
        this.createConnection();
        String sql = "UPDATE ReadingRecommendations SET headline=?, url=?, writer=? WHERE id=?;";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, blog.getHeadline());
        ps.setString(2, blog.getURL());
        ps.setString(3, blog.getWriter());
        ps.setInt(4, blog.getId());
        ps.executeUpdate();
        ps.close();
        this.closeConnection();
        
        updateTags(blog);
        updateCourses(blog);
    }
    
    @Override
    public void updateBook(BookRecommendation book) throws Exception {
        this.createConnection();
        String sql = "UPDATE ReadingRecommendations SET headline=?, isbn=?, writer=? WHERE id=?;";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, book.getHeadline());
        ps.setString(2, book.getISBN());
        ps.setString(3, book.getWriter());
        ps.setInt(4, book.getId());
        ps.executeUpdate();
        ps.close();
        this.closeConnection();
        
        updateTags(book);
        updateCourses(book);
    }
    
    @Override
    public void updatePodcast(PodcastRecommendation podcast) throws Exception {
        this.createConnection();
        String sql = "UPDATE ReadingRecommendations SET headline=?, writer=?, podcastName=?, description=? WHERE id=?;";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, podcast.getHeadline());
        ps.setString(2, podcast.getWriter());
        ps.setString(3, podcast.getPodcastName());
        ps.setString(4, podcast.getDescription());
        ps.setInt(5, podcast.getId());
        ps.executeUpdate();
        ps.close();
        this.closeConnection();
        
        updateTags(podcast);
        updateCourses(podcast);
    }

    /**
     * Poistaa blogi-vinkit tietokannasta.
     *
     * @param blogRecommendation poistettava olio
     * @throws Exception
     */
    private void removeBlog(BlogRecommendation blogRecommendation) throws Exception {
        this.createConnection();
        String sql = "DELETE FROM ReadingRecommendations WHERE url=? AND writer=? AND headline=? AND user_id=?;";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, blogRecommendation.getURL());
        ps.setString(2, blogRecommendation.getWriter());
        ps.setString(3, blogRecommendation.getHeadline());
        ps.setInt(4, this.userId);
        ps.executeUpdate();
        ps.close();
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
        ps.close();
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
        ps.close();
        this.closeConnection();
    }

    @Override
    public void remove(int recommendationId) throws Exception {
//        String type = r.getType();
//        if (type == "blog") {
//            removeBlog((BlogRecommendation) r);
//        } else if (type == "book") {
//            removeBook((BookRecommendation) r);
//        } else if (type == "podcast") {
//            removePodcast((PodcastRecommendation) r);
//        }
        this.createConnection();
        String sql = "DELETE FROM ReadingRecommendations WHERE id=? AND user_id=?;";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setInt(1, recommendationId);
        ps.setInt(2, this.userId);
        ps.executeUpdate();
        ps.close();
        this.closeConnection();
        
        removeTags(recommendationId);
        removeCourses(recommendationId);
    }
    
    public void removeTags(int recommendationId) {
        try {
            this.createConnection();
            String sql = "DELETE FROM Tags WHERE readingRecommendation_id=?;";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, recommendationId);
            ps.executeUpdate();
            ps.close();
            this.closeConnection();
        } catch (Exception e) {
            
        }
    }
    
    public void removeCourses(int recommendationId) {
        try {
            this.createConnection();
            String sql = "DELETE FROM RelatedCourses WHERE readingRecommendation_id=?;";
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, recommendationId);
            ps.executeUpdate();
            ps.close();
            this.closeConnection();
        } catch (Exception e) {
            
        }
    }

    /**
     * Luo uuden tietokanta-yhetyden.
     *
     * @throws Exception
     */
    private void createConnection() throws Exception {
        this.connection = DriverManager.getConnection(dbUrl);
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
     * Lisää kommentin lukuvinkille
     * id-tunnisteen.
     *
     * @param commentStr
     * @param readingRecommendationId
     * @throws Exception
     */
    @Override
    public void addComment(String commentStr, int readingRecommendationId) throws Exception {
        String content = commentStr;

        this.createConnection();
        String sql = "UPDATE ReadingRecommendations SET comment=? WHERE id=?";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, content);
        ps.setInt(2, readingRecommendationId);
        ps.executeUpdate();
        ps.close();
        this.closeConnection();
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
        rs.close();
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

        String sql = "INSERT INTO Tags (tag, readingRecommendation_id) values (?, ?);";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, tag);
        ps.setInt(2, reading_id);
        ps.executeUpdate();
        ps.close();

        this.closeConnection();
    }
    
    public ArrayList<String> getAllTags(int readingId) throws Exception {
        ArrayList<String> tags = new ArrayList<>();
        this.createConnection();
        String sql = "SELECT * FROM Tags WHERE readingRecommendation_id=?;";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setInt(1, readingId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            tags.add(rs.getString("tag"));
        }
        rs.close();
        ps.close();
        this.closeConnection();
        return tags;
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

    /**
     * Hakee tietokannasta lukuvinkkejä headline mukaan.
     * @param headline haettava headline
     * @return haetut lukuvinkit
     * @throws Exception
     */
    public ArrayList<ReadingRecommendation> findByHeadline(String headline) throws Exception {
        ArrayList readingRecommendations = this.loadAll();
        ArrayList<ReadingRecommendation> findedRecommendations = new ArrayList<>();

        for (Object readingRecommendation : readingRecommendations) {
            ReadingRecommendation rr = (ReadingRecommendation) readingRecommendation;

            if (rr.getHeadline().contains(headline)) {
                findedRecommendations.add(rr);
            }
        }

        return findedRecommendations;
    }

    @Override
    public ArrayList<ReadingRecommendationInterface> loadAll() throws Exception {
        ArrayList<ReadingRecommendationInterface> recommendations = new ArrayList<>();
        this.createConnection();

        String sqlComment = "SELECT * FROM ReadingRecommendations WHERE user_id=" + this.userId;
        ResultSet rs = this.statement.executeQuery(sqlComment);
        while (rs.next()) {
            String headline = rs.getString("headline");
            String type = rs.getString("type");

            if (type.equals("blog")) {
                BlogRecommendation blog = new BlogRecommendation(headline, type, rs.getString("url"));
                blog.setWriter(rs.getString("writer"));
                blog.setId(rs.getInt("id"));
                blog.setComment(rs.getString("comment"));
                recommendations.add(blog);

            } else if (type.equals("book")) {
                BookRecommendation book = new BookRecommendation(headline, type, rs.getString("writer"));
                book.setISBN(rs.getString("isbn"));
                book.setId(rs.getInt("id"));
                book.setComment(rs.getString("comment"));
                recommendations.add(book);

            } else if (type.equals("podcast")) {
                PodcastRecommendation podcast = new PodcastRecommendation(headline, type, rs.getString("podcastName"), rs.getString("description"));
                podcast.setWriter(rs.getString("writer"));
                podcast.setComment(rs.getString("comment"));
                podcast.setId(rs.getInt("id"));
                recommendations.add(podcast);
            }
        }

        rs.close();
        this.closeConnection();
        
        for (ReadingRecommendationInterface r : recommendations) {
            int id = r.getId();
            for (String tag : getAllTags(id)) {
                r.addTags(tag);
            }
            for (String course : getAllCourses(id)) {
                r.addCourse(course);
            }
        }

        return recommendations;
    }

    @Override
    public HashMap<String, String> findById(int id) throws Exception {
        this.createConnection();
        String sqlComment = "SELECT * FROM ReadingRecommendations WHERE id=?";
        PreparedStatement ps = this.connection.prepareStatement(sqlComment);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        HashMap<String, String> recInfo = new HashMap<>();
        recInfo.put("headline", rs.getString("headline"));
        recInfo.put("type", rs.getString("type"));
        recInfo.put("url", rs.getString("url"));
        recInfo.put("isbn", rs.getString("isbn"));
        recInfo.put("writer", rs.getString("writer"));
        recInfo.put("podcastName", rs.getString("podcastName"));
        recInfo.put("description", rs.getString("description"));
        recInfo.put("comment", rs.getString("comment"));
        rs.close();
        this.closeConnection();
        return recInfo;
    }

    @Override
    public ArrayList<ReadingRecommendationInterface> findByApproximateHeadline(String headline) throws Exception {
        this.createConnection();
        ArrayList<ReadingRecommendationInterface> readingRecommendations = new ArrayList<>();
        String sqlComment = "SELECT * FROM ReadingRecommendations WHERE headline LIKE ?";
        PreparedStatement ps = this.connection.prepareStatement(sqlComment);
        ps.setString(1, "%" + headline + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ReadingRecommendationInterface recommendation = createRecommendations(rs);
            readingRecommendations.add(recommendation);
        }
        rs.close();
        this.closeConnection();
        return readingRecommendations;
    }
    
    private ReadingRecommendationInterface createRecommendations(ResultSet rs) throws Exception {
        String headline = rs.getString("headline");
        String type = rs.getString("type");

        if (type.equals("blog")) {
            BlogRecommendation blog = new BlogRecommendation(headline, type, rs.getString("url"));
            blog.setWriter(rs.getString("writer"));
            blog.setId(rs.getInt("id"));
            blog.setComment(rs.getString("comment"));
            return blog;

        } else if (type.equals("book")) {
            BookRecommendation book = new BookRecommendation(headline, type, rs.getString("writer"));
            book.setISBN(rs.getString("isbn"));
            book.setId(rs.getInt("id"));
            book.setComment(rs.getString("comment"));
            return book;

        } else if (type.equals("podcast")) {
            PodcastRecommendation podcast = new PodcastRecommendation(headline, type, rs.getString("podcastName"), rs.getString("description"));
            podcast.setWriter(rs.getString("writer"));
            podcast.setComment(rs.getString("comment"));
            podcast.setId(rs.getInt("id"));
            return podcast;
        }
        
        return null;
    }
}
