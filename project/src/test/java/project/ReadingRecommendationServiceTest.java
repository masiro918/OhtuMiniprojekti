package project;

import project.logic.ReadingRecommendationService;
import project.DAO.ReadingRecommendationDAO;
import project.domain.ReadingRecommendation;
import project.domain.BlogRecommendation;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import project.domain.User;

public class ReadingRecommendationServiceTest {
    
    ReadingRecommendationService service;
    ReadingRecommendationDAO recommendation;
    ArrayList<ReadingRecommendationDAO> recommendations;
    HashMap<String, String> blogInfo;
    HashMap<String, String> bookInfo;
    User user;

    @Before
    public void setUp() {
        String headline = "Basic";
        String type = "blog";

        recommendation = new ReadingRecommendation(headline, type);
        recommendations = new ArrayList<>();
        
        user = new User("pekka", "salasana1");

        //Basic info for blog
        blogInfo = new HashMap<>();
        blogInfo.put("headline", "Blog headline");
        blogInfo.put("type", "blog");
        blogInfo.put("url", "Blog's url");

        //Basic info for book
        bookInfo = new HashMap<>();
        bookInfo.put("headline", "Book headline");
        bookInfo.put("type", "book");
        bookInfo.put("writer", "Kirjoittaja");


        service = new ReadingRecommendationService(user);
        service.setRecommendations(recommendations);
    }

    @Test
    public void createRecommendationWorksForBlogWithoutWriter() {
        service.createRecommendation(blogInfo);
        ReadingRecommendationDAO addedBlog = recommendations.get(0);
        BlogRecommendation blog = new BlogRecommendation("Blog headline", "blog", "Blog's url");  
        assertEquals(blog.getPrint(), addedBlog.getPrint());
    }

    @Test
    public void createRecommendationWorksForBlogWithWriter() {
        blogInfo.put("writer", "Kirjailija");
        service.createRecommendation(blogInfo);
        ReadingRecommendationDAO addedBlog = recommendations.get(0);
        BlogRecommendation blog = new BlogRecommendation("Blog headline", "blog", "Blog's url");
        blog.setWriter("Kirjailija");  
        assertEquals(blog.getPrint(), addedBlog.getPrint());
    }

    @Test
    public void findIndexReturnsIndexIfRecommendationIsThere() {
        recommendations.add(new ReadingRecommendation("Bonus", "book"));
        recommendations.add(recommendation);
        int index = service.findIndex("Basic");

        assertEquals(1, index);
    }

    @Test
    public void findIndexReturnsNegativeOneIfRecommendationNotFound() {
        recommendations.add(new ReadingRecommendation("Bonus", "book"));
        recommendations.add(recommendation);
        int index = service.findIndex("Not there");

        assertEquals(-1, index);
    }


}
