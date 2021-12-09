
package project.db;

import java.util.ArrayList;
import project.domain.BlogRecommendation;
import project.domain.BookRecommendation;
import project.domain.ReadingRecommendationInterface;

public interface ReadingRecommendationDAO {
    public void add(ReadingRecommendationInterface r) throws Exception;
    public void remove(ReadingRecommendationInterface r) throws Exception;
    public ArrayList<ReadingRecommendationInterface> loadAll() throws Exception;
    public BlogRecommendation getBlog(int id) throws Exception;
    public BookRecommendation getBook(int id) throws Exception;
    public void setUserId(int id);
}
