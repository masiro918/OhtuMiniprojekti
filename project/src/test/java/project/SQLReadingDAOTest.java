package project;

import project.db.*;
import project.domain.*;
import project.logic.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.sound.sampled.SourceDataLine;

public class SQLReadingDAOTest {

    private SQLReadingDAO sqlReadingDAO = null;
    private TableCreator tb = new TableCreator();

    @Before
    public void setUp() throws Exception {
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
}