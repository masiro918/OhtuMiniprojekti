
package project.db;

import java.util.ArrayList;
import java.util.HashMap;
import project.domain.BlogRecommendation;
import project.domain.BookRecommendation;
import project.domain.PodcastRecommendation;
import project.domain.ReadingRecommendationInterface;

public interface ReadingRecommendationDAO {
    public void add(ReadingRecommendationInterface r) throws Exception;
    public void remove(int readingId) throws Exception;
    public ArrayList<ReadingRecommendationInterface> loadAll() throws Exception;
    public ArrayList<ReadingRecommendationInterface> findByApproximateHeadline(String headline) throws Exception;
    public HashMap<String, String> findById(int id) throws Exception;
    public void addComment(String commentStr, int readingRecommendationId) throws Exception;
    public BlogRecommendation getBlog(int id) throws Exception;
    public BookRecommendation getBook(int id) throws Exception;
    public PodcastRecommendation getPodcast(int id) throws Exception;
    public void setUserId(int id);
    public void updateBlog(BlogRecommendation blog) throws Exception;
    public void updateBook(BookRecommendation book) throws Exception;
    public void updatePodcast(PodcastRecommendation podcast) throws Exception;
}
