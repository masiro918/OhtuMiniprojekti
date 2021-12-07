package project;

import project.db.*;
import project.domain.*;
import project.logic.*;
import org.junit.Before;
import org.junit.Test;

import java.beans.Transient;
import java.rmi.server.RMIClassLoader;
import java.util.ArrayList;
import static org.junit.Assert.*;

import javax.sound.sampled.SourceDataLine;

public class SQLReadingDAOTest {

    private SQLReadingDAO sqlReadingDAO = null;
    private TableCreator tb = new TableCreator();

    @Before
    public void setUp() throws Exception {
        try {
            tb.destroyDatabase();
        } catch (Exception e) {

        }

        this.sqlReadingDAO = new SQLReadingDAO();

        tb.createUser();
        tb.createReadingRecommendations();
        tb.createRelatedCourses();
        tb.createTags();
    }

    @Test
    public void addBlogRecommendationTest() throws Exception {
        BlogRecommendation br = new BlogRecommendation("headline", "blog", "http://url.url/");
        BlogRecommendation br2 = null;

        try {
            this.sqlReadingDAO.addBlog(br);

            br2 = this.sqlReadingDAO.getBlog(1);

        } catch (Exception e) {
            
        }

        assertEquals(br2.getURL(), "http://url.url/");
    }

    @Test
    public void getBlogTest() throws Exception {
        boolean success = false;
        BlogRecommendation br = new BlogRecommendation("headline-test", "blog", "http://url-test.url/");

        try {
            this.sqlReadingDAO.addBlog(br);
            //int id = this.sqlReadingDAO.getLastIdReading();
            BlogRecommendation br2 = this.sqlReadingDAO.getBlog(1);

            System.out.println(br2.getHeadline());
            if (br2.getHeadline().equals("headline-test")) {
                success = true;
            }
        } catch (Exception e) {
            System.err.println("virhe: " + e.getMessage());
        }
        assertTrue(success);
    }
    
    @Test
    public void addCourseTest() throws Exception {
        boolean success = false;
        String course = "testi-kurssi";

        try {
            this.sqlReadingDAO.addCourse(course, 9999);
            String getCourse = this.sqlReadingDAO.getCourse(9999);

            if (getCourse.equals("testi-kurssi")) {
                success = true;
            }
        } catch (Exception e) {

        }
        assertTrue(success);
    }
    
    @Test
    public void loadAllTest() throws Exception {
        BlogRecommendation br3 = null, br4 = null;
        ArrayList recommendations = null;
        try {
            BlogRecommendation br1 = new BlogRecommendation("headline", "blog", "http://url.url/");
            BlogRecommendation br2 = new BlogRecommendation("headline2", "blog2", "http://url2.url/");

            this.sqlReadingDAO.addBlog(br1);
            this.sqlReadingDAO.addBlog(br2);
            
            recommendations = this.sqlReadingDAO.loadAll();
            assertTrue(true);
        }
        catch (Exception e) {
            assertTrue(false);
        }
    }
}