package project;

import project.logic.ReadingRecommendationService;
import project.domain.ReadingRecommendation;
import project.domain.BlogRecommendation;
import project.domain.BookRecommendation;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import project.domain.User;
import project.domain.ReadingRecommendationInterface;

public class ReadingRecommendationServiceTest {
    
    ReadingRecommendationService service;
    ReadingRecommendationInterface recommendation;
    ArrayList<ReadingRecommendationInterface> recommendations;
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

        FakeReadingRecommendationDAO fakeDb = new FakeReadingRecommendationDAO();
        
        service = new ReadingRecommendationService(user, fakeDb);
        service.setRecommendations(recommendations);
    }

    // CreateRecommendations tests
    @Test
    public void createRecommendationWorksForBlogWithoutWriter() {
        service.createRecommendation(blogInfo);
        ReadingRecommendationInterface addedBlog = recommendations.get(0);
        BlogRecommendation blog = new BlogRecommendation("Blog headline", "blog", "Blog's url");  
        assertEquals(blog.getPrint(), addedBlog.getPrint());
    }

    @Test
    public void createRecommendationWorksForBlogWithWriter() {
        blogInfo.put("writer", "Kirjailija");
        service.createRecommendation(blogInfo);
        ReadingRecommendationInterface addedBlog = recommendations.get(0);
        BlogRecommendation blog = new BlogRecommendation("Blog headline", "blog", "Blog's url");
        blog.setWriter("Kirjailija");  
        assertEquals(blog.getPrint(), addedBlog.getPrint());
    }

    @Test
    public void createRecommendationWorksForBookWithoutIsbn() {
        service.createRecommendation(bookInfo);
        ReadingRecommendationInterface addedBook = recommendations.get(0);
        BookRecommendation book = new BookRecommendation("Book headline", "book", "Kirjoittaja");

        assertEquals(book.getPrint(), addedBook.getPrint());
    }

    @Test
    public void createRecommendationWorksForBookWithIsbn() {
        bookInfo.put("ISBN", "ISBN");
        service.createRecommendation(bookInfo);
        ReadingRecommendationInterface addedBook = recommendations.get(0);
        BookRecommendation book = new BookRecommendation("Book headline", "book", "Kirjoittaja");
        book.setISBN("ISBN");

        assertEquals(book.getPrint(), addedBook.getPrint());
    }

    //FindIndex tests
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

    //RemoveRecommendations tests
    @Test
    public void removeRecommendationReducesSizeIfHeadlineIsFound() {
        recommendations.add(new ReadingRecommendation("Bonus", "book"));
        recommendations.add(recommendation);
        service.removeRecommendation("Bonus");

        assertEquals(1, recommendations.size());
    }

    @Test
    public void removeRecommendationDoesNothingIfNotFound() {
        recommendations.add(new ReadingRecommendation("Bonus", "book"));
        recommendations.add(recommendation);
        service.removeRecommendation("Not here");

        assertEquals(2, recommendations.size());
    }

    @Test
    public void removeRecommendationRemovesCorrectRecommendation() {
        recommendations.add(new ReadingRecommendation("Bonus", "book"));
        recommendations.add(recommendation);
        service.removeRecommendation("Bonus");

        String headline = recommendations.get(0).getHeadline();

        assertEquals("Basic", headline);
    }

    //FindRecommendation tests
    @Test
    public void findRecommendationReturnsCorrectRecommendation() {
        recommendations.add(recommendation);
        ReadingRecommendationInterface recom = service.findRecommendation("Basic");

        assertEquals("Basic", recom.getHeadline());
    }

    @Test
    public void findRecommendationReturnsNullIfNotFound() {
        recommendations.add(recommendation);
        ReadingRecommendationInterface recom = service.findRecommendation("Not there");

        assertNull(recom);
    }

    //ShowRecommendation tests
    @Test
    public void showRecommendationPrintsCorrect() {
        recommendations.add(recommendation);
        String recomPrint = service.showRecommendation(recommendation);

        assertEquals(recommendation.getPrint(), recomPrint);
    }

    //ShowAllRecommendations tests
    @Test
    public void showAllRecommendationsReturnsCorrectIfNoRecommendations() {
        String ans = service.showAllRecommendations();
        
        assertEquals("No recommendations", ans);
    }

}
