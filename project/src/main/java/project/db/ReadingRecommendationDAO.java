
package project.db;

import java.util.ArrayList;
import project.domain.BlogRecommendation;
import project.domain.ReadingRecommendationInterface;

public interface ReadingRecommendationDAO {
    void add(ReadingRecommendationInterface r) throws Exception;
    void remove(ReadingRecommendationInterface r) throws Exception;
    ArrayList<ReadingRecommendationInterface> loadAll() throws Exception;
    BlogRecommendation getBlog(int id) throws Exception;
    
}
