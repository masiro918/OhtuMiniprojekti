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

import javax.sound.sampled.SourceDataLine;

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
    public void setUp() throws Exception {
        String headline = "Basic";
        String type = "blog";

        recommendation = new ReadingRecommendation(headline, type);
        
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
        recommendations = fakeDb.loadAll();
        
        service = new ReadingRecommendationService(user, fakeDb);
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

//    //FindIndex tests
//    @Test
//    public void findIndexReturnsIndexIfRecommendationIsThere() {
//        recommendations.add(new ReadingRecommendation("Bonus", "book"));
//        recommendations.add(recommendation);
//        int index = service.findIndex("Basic");
//
//        assertEquals(1, index);
//    }
//
//    @Test
//    public void findIndexReturnsNegativeOneIfRecommendationNotFound() {
//        recommendations.add(new ReadingRecommendation("Bonus", "book"));
//        recommendations.add(recommendation);
//        int index = service.findIndex("Not there");
//
//        assertEquals(-1, index);
//    }

    //RemoveRecommendations tests
    @Test
    public void removeRecommendationRemovesRecommendationFromDatabase() {
        BookRecommendation book = new BookRecommendation("Bonus", "book", "Writer");
        int randomId = 4;
        book.setId(randomId);
        recommendations.add(book);
        recommendations.add(recommendation);
        service.removeRecommendation(randomId);
        assertTrue(recommendations.contains(recommendation));
        assertFalse(recommendations.contains(book));
    }

    @Test
    public void removeRecommendationDoesNothingIfNotFound() {
        ReadingRecommendation r = new ReadingRecommendation("Bonus", "book");
        r.setId(2);
        recommendations.add(r);
        recommendations.add(recommendation);
        service.removeRecommendation(1);

        assertEquals(2, recommendations.size());
    }

    @Test
    public void removeRecommendationRemovesCorrectRecommendation() {
        BookRecommendation book = new BookRecommendation("Bonus", "book", "Writer");
        book.setId(1);
        recommendations.add(book);
        recommendations.add(recommendation);
        service.removeRecommendation(1);

        String headline = recommendations.get(0).getHeadline();

        assertEquals("Basic", headline);
    }

    //FindRecommendation tests
    @Test
    public void findRecommendationsReturnsCorrectRecommendation() {
        recommendations.add(recommendation);
        ArrayList<ReadingRecommendationInterface> recom = service.findRecommendations("Basic");

        assertEquals("Basic", recom.get(0).getHeadline());
    }

    @Test
    public void findRecommendationsReturnsEmptyListIfNotFound() {
        recommendations.add(recommendation);

        assertTrue(service.findRecommendations("Not there").isEmpty());
    }

    @Test
    public void findRecommendationsTest() {
        recommendations.add(recommendation);

        ReadingRecommendationInterface rri1 = new ReadingRecommendation("Basic", "podcast");
        ReadingRecommendationInterface rri2 = new ReadingRecommendation("Non-basic", "book");

        recommendations.add(rri1);
        recommendations.add(rri2);
        ArrayList readingrecommendations = service.findRecommendations("Basic");
        //ArrayList readingrecommendations = service.loadRecommendations();

        //System.out.println("###########################");
//        for (Object o : readingrecommendations) {
//            ReadingRecommendationInterface rri = (ReadingRecommendationInterface)o;
//            System.out.println(rri.getHeadline());
//        }
        //System.out.println("###########################");

        int results = readingrecommendations.size();
        assertEquals(results, 2);
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
    
    //FindByWriter tests
    @Test
    public void findIndexesByWriterReturnsCorrectRecommendations() {
        this.recommendations.add(new BookRecommendation("Testbook", "book", "W. R. Iter"));
        this.recommendations.add(new BookRecommendation("Testbook Vol.2", "book", "W. R. Iter"));
        this.recommendations.add(new BookRecommendation("Book about Nothing", "book", "Nobody"));
        
        ArrayList<Integer> indexes = service.findIndexesByWriter("W. R. Iter");
        assertEquals("Testbook", this.recommendations.get(indexes.get(0)).getHeadline());
        assertEquals("Testbook Vol.2", this.recommendations.get(indexes.get(1)).getHeadline());
    }
    
    @Test
    public void findIndexesByWriterIgnoresRecommendationsWithoutWriter() {
        this.recommendations.add(new BookRecommendation("Testbook", "book", "W. R. Iter"));
        this.recommendations.add(new ReadingRecommendation("Plain", "default"));
        this.recommendations.add(new BookRecommendation("Testbook Vol.2", "book", "W. R. Iter"));
        
        ArrayList<Integer> indexes = service.findIndexesByWriter("W. R. Iter");
        assertTrue(indexes.size() == 2);
    }
    
    @Test
    public void findIndexesByWriterReturnsEmptyListIfNoneAreFound() {
        this.recommendations.add(new BookRecommendation("Book about Nothing", "book", "Nobody"));
        assertTrue(service.findIndexesByWriter("Nonexistent Writer").isEmpty());
    }
    
    @Test
    public void findRecommendationsByIDsReturnsCorrectRecommendationsWithFindIndexesByWriter() {
        BookRecommendation book1 = new BookRecommendation("Testbook", "book", "W. R. Iter");
        BookRecommendation book2 = new BookRecommendation("Testbook Vol.2", "book", "W. R. Iter");
        this.recommendations.add(book1);
        this.recommendations.add(book2);
        this.recommendations.add(new BookRecommendation("Book about Nothing", "book", "Nobody"));
        
        ArrayList<Integer> indexes = service.findIndexesByWriter("W. R. Iter");
        ArrayList<ReadingRecommendationInterface> recoms = service.findRecommendationsByIDs(indexes);
        assertEquals("Testbook", recoms.get(0).getHeadline());
        assertEquals("Testbook Vol.2", recoms.get(1).getHeadline());
    }
    //checkBlogInfo tests
    @Test
    public void checkBlogInfoReturnsFalseIfUrlIsEmpty() {
        HashMap<String, String> info = new HashMap<>();
        info.put("type", "blog");
        info.put("headline", "headline");
        info.put("url", " ");
        assertFalse(service.checkBlogInfo(info));
    }
    
    @Test
    public void checkBlogInfoReturnsFalseIfUrlIsNull() {
        HashMap<String, String> info = new HashMap<>();
        info.put("type", "blog");
        info.put("headline", "headline");
        info.put("url", null);
        assertFalse(service.checkBlogInfo(info));
    }
    
    //checkBookInfo tests
    @Test
    public void checkBookInfoReturnsFalseIfHeadlineIsEmpty() {
        HashMap<String, String> info = new HashMap<>();
        info.put("type", "book");
        info.put("headline", " ");
        info.put("writer", "writer");
        assertFalse(service.checkBookInfo(info));
    }
    
    @Test
    public void checkBookInfoReturnsFalseIfWriterIsEmpty() {
        HashMap<String, String> info = new HashMap<>();
        info.put("type", "book");
        info.put("headline", "headline");
        info.put("writer", " ");
        assertFalse(service.checkBookInfo(info));
    }
    
    //checkPodcastInfo tests
    @Test
    public void checkPodcastInfoReturnsFalseIfHeadlineIsEmpty() {
        HashMap<String, String> info = new HashMap<>();
        info.put("type", "podcast");
        info.put("headline", " ");
        info.put("podcastName", "name");
        info.put("description", "description");
        assertFalse(service.checkPodcastInfo(info));
    }
    
    @Test
    public void checkPodcastInfoReturnsFalseIfPodcastNameIsEmpty() {
        HashMap<String, String> info = new HashMap<>();
        info.put("type", "podcast");
        info.put("headline", "headline");
        info.put("podcastName", " ");
        info.put("description", "description");
        assertFalse(service.checkPodcastInfo(info));
    }
    
    @Test
    public void checkPodcastInfoReturnsFalseIfDescriptionIsEmpty() {
        HashMap<String, String> info = new HashMap<>();
        info.put("type", "podcast");
        info.put("headline", "headline");
        info.put("podcastName", "podcastName");
        info.put("description", " ");
        assertFalse(service.checkPodcastInfo(info));
    }
    
    //addComment test
    @Test
    public void addCommentAddsCommentForReadingRecommendation() {
        HashMap<String, String> info = new HashMap<>();
        info.put("headline", "headline");
        info.put("type", "blog");
        info.put("url", "url");
        service.createRecommendation(info);
        int id = service.loadRecommendations().get(0).getId();
        service.addComment("Kommentti", id);
        
        assertEquals("Kommentti", service.loadRecommendations().get(0).getComment());
    }
}
