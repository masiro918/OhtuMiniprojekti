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

            //System.out.println(br2.getHeadline());
            if (br2.getHeadline().equals("headline-test")) {
                success = true;
            }
        } catch (Exception e) {
            //System.err.println("virhe: " + e.getMessage());
        }
        assertTrue(success);
    }

    @Test
    public void deleteBlogTest() throws Exception {
        boolean success = false;
        this.sqlReadingDAO.setUserId(999);

        BlogRecommendation br = new BlogRecommendation("test", "blog", "http://url-test.url/");
        br.setWriter("test-writer");

        try {
            this.sqlReadingDAO.addBlog(br);
            int lastId = this.sqlReadingDAO.getLastIdReading();
            this.sqlReadingDAO.remove(lastId);
            success = true;
        } catch (Exception e) {
            //System.err.println("writer: " + br.getWriter());
            //System.err.println("virhe: " + e.getMessage());
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
    
    @Test
    public void getAllCoursesReturnsAllCourses() {
        BlogRecommendation blog = new BlogRecommendation("headline", "blog", "url");
        String course1 = "course1";
        String course2 = "course2";
        blog.addCourse(course1);
        blog.addCourse(course2);
        
        try {
            this.sqlReadingDAO.add(blog);
            int id = this.sqlReadingDAO.getLastIdReading();
            ArrayList<String> courses = this.sqlReadingDAO.getAllCourses(id);
            
            assertTrue(courses.contains(course1));
            assertTrue(courses.contains(course2));
            
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void getAllTagsReturnsAllTags() {
        BlogRecommendation blog = new BlogRecommendation("headline", "blog", "url");
        String tag1 = "tag1";
        String tag2 = "tag2";
        blog.addTags(tag1);
        blog.addTags(tag2);
        
        try {
            this.sqlReadingDAO.add(blog);
            int id = this.sqlReadingDAO.getLastIdReading();
            ArrayList<String> tags = this.sqlReadingDAO.getAllTags(id);
            
            assertTrue(tags.contains(tag1));
            assertTrue(tags.contains(tag2));
            
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void correctHeadlineIsReturned() {
        try {
            this.sqlReadingDAO.add(new BlogRecommendation("testi", "blog", ""));
            ArrayList<ReadingRecommendation> hl = this.sqlReadingDAO.findByHeadline("testi");
            assertTrue(hl.size() == 1);
            assertEquals(hl.get(0).getHeadline(), "testi");
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void noHeadlineIsReturnedIfNotFound() {
        try {
            ArrayList<ReadingRecommendation> hl = this.sqlReadingDAO.findByHeadline("Not there");
            assertTrue(hl.size() == 0);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void removeTagsDeletesCorrectTags() {
        String tag1 = "tag1";
        String tag2 = "tag2";
        String tag3 = "tag3";
        
        try {
            this.sqlReadingDAO.addTag(tag1, 0);
            this.sqlReadingDAO.addTag(tag2, 0);
            this.sqlReadingDAO.addTag(tag3, 1);
            
            this.sqlReadingDAO.removeTags(0);
            
            ArrayList<String> tags = this.sqlReadingDAO.getAllTags(0);
            
            assertFalse(tags.contains(tag1));
            assertFalse(tags.contains(tag2));
            
            tags = this.sqlReadingDAO.getAllTags(1);
            
            assertTrue(tags.contains(tag3));
            
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void removeCoursesRemovesCorrectCourses() {
        String course1 = "course1";
        String course2 = "course2";
        String course3 = "course3";
        
        try {
            this.sqlReadingDAO.addCourse(course1, 0);
            this.sqlReadingDAO.addCourse(course2, 0);
            this.sqlReadingDAO.addCourse(course3, 1);
            
            this.sqlReadingDAO.removeCourses(0);
            
            ArrayList<String> courses = this.sqlReadingDAO.getAllCourses(0);
            
            assertFalse(courses.contains(course1));
            assertFalse(courses.contains(course2));
            
            courses = this.sqlReadingDAO.getAllCourses(1);
            
            assertTrue(courses.contains(course3));
            
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void updateBlogUpdatesBlogInformation() {
        BlogRecommendation blog = new BlogRecommendation("OldHeadline", "blog", "OldUrl");
        blog.setWriter("OldWriter");
        
        try {
            this.sqlReadingDAO.addBlog(blog);
            int id = this.sqlReadingDAO.getLastIdReading();
            blog.setId(id);
            
            blog.setHeadline("NewHeadline");
            blog.setUrl("NewUrl");
            blog.setWriter("NewWriter");
            this.sqlReadingDAO.updateBlog(blog);
            
            BlogRecommendation updatedBlog = this.sqlReadingDAO.getBlog(id);
            
            assertEquals("NewHeadline", updatedBlog.getHeadline());
            assertEquals("NewUrl", updatedBlog.getURL());
            assertEquals("NewWriter", updatedBlog.getWriter());
            
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}