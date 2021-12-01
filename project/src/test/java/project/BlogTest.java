package project;

import project.domain.BlogRecommendation;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlogTest {

    BlogRecommendation blog;

    @Before
    public void setUp() {
        String headline = "Test headline";
        String type = "Blog";
        String url = "Url";

        blog = new BlogRecommendation(headline, type, url);
    }

    @Test
    public void getPrintReturnsCorrectStringForBasicBlog() {
        String correct = "Otsikko: Test headline\nUrl: Url\nTyyppi: Blog";
        assertEquals(correct, blog.getPrint());
    }

    @Test
    public void getPrintReturnsCorrectIfBlogHasWriter() {
        blog.setWriter("Writer");
        String correct = "Otsikko: Test headline\nKirjoittaja: Writer\nUrl: Url\nTyyppi: Blog";
        assertEquals(correct, blog.getPrint());
    }

    @Test
    public void getPrintReturnsCorrectIfBlogHasTags() {
        blog.addTags("Tägi1");
        blog.addTags("Tägi2");
        String correct = "Otsikko: Test headline\nUrl: Url\nTyyppi: Blog\nTagit: Tägi1, Tägi2";

        assertEquals(correct, blog.getPrint());
    }

    @Test
    public void getPrintReturnsCorrectIfBlogHasRelatedCourses() {
        blog.addCourse("Ohte");
        blog.addCourse("Ohtu");
        blog.addCourse("FullStack");
        String correct = "Otsikko: Test headline\nUrl: Url\nTyyppi: Blog\nRelated courses: Ohte, Ohtu, FullStack";

        assertEquals(correct, blog.getPrint());
    }
    
    @Test
    public void getPrintReturnsCorrectIfBlogHasComments() {
        blog.setComment("Hyvä homma");
        String correct = "Otsikko: Test headline\nUrl: Url\nTyyppi: Blog\nKommentti: Hyvä homma";

        assertEquals(correct, blog.getPrint());
    }

    @Test
    public void getPrintReturnsCorrectIfBlogHasAllProperties() {
        blog.setWriter("writer");
        blog.addTags("Tägi");
        blog.addCourse("Ohte");
        blog.addCourse("Ohtu");
        blog.setComment("comment");

        String correct = "Otsikko: Test headline\nKirjoittaja: writer\nUrl: Url\nTyyppi: Blog\nTagit: Tägi\nRelated courses: Ohte, Ohtu\nKommentti: comment";

        assertEquals(correct, blog.getPrint());
    }

}