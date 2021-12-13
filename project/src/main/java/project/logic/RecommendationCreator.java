package project.logic;

import java.util.HashMap;
import project.domain.BlogRecommendation;
import project.domain.BookRecommendation;
import project.domain.PodcastRecommendation;

public class RecommendationCreator {

    public BlogRecommendation createNewBlog(HashMap<String, String> info) {
        BlogRecommendation r = new BlogRecommendation(info.get("headline"), "blog", info.get("url"));
        if (info.containsKey("writer")) {
            r.setWriter(info.get("writer"));
        }
        if (info.containsKey("tags")) {
            String[] tags = info.get("tags").split(", "); // Oletuksena, etta tagit tallennettu muodossa 'tag1, tag2, tag3'
            for (String tag : tags) {
                r.addTags(tag);
            }
        }
        if (info.containsKey("courses")) {
            String[] courses = info.get("courses").split(", ");
            for (String course : courses) {
                r.addCourse(course);
            }
        }
        return r;
    }

    public BookRecommendation createNewBook(HashMap<String, String> info) {
        BookRecommendation r = new BookRecommendation(info.get("headline"), "book", info.get("writer"));
        if (info.containsKey("ISBN")) {
            r.setISBN(info.get("ISBN"));
        }
        if (info.containsKey("tags")) {
            String[] tags = info.get("tags").split(", ");
            for (String tag : tags) {
                r.addTags(tag);
            }
        }
        if (info.containsKey("courses")) {
            String[] courses = info.get("courses").split(", ");
            for (String course : courses) {
                r.addCourse(course);
            }
        }
        return r;
    }

    public PodcastRecommendation createNewPodcast(HashMap<String, String> info) {
        PodcastRecommendation r = new PodcastRecommendation(info.get("headline"), "podcast", info.get("podcastName"), info.get("description"));
        if (info.containsKey("writer")) {
            r.setWriter(info.get("writer"));
        }
        if (info.containsKey("tags")) {
            String[] tags = info.get("tags").split(", ");
            for (String tag : tags) {
                r.addTags(tag);
            }
        }
        if (info.containsKey("courses")) {
            String[] courses = info.get("courses").split(", ");
            for (String course : courses) {
                r.addCourse(course);
            }
        }
        return r;
    }
}
