
package project;

import java.util.ArrayList;
import project.db.ReadingRecommendationDAO;
import project.domain.ReadingRecommendationInterface;

public class FakeReadingRecommendationDAO implements ReadingRecommendationDAO {
    private ArrayList<ReadingRecommendationInterface> fakeRecommendations;
    
    public FakeReadingRecommendationDAO() {
        this.fakeRecommendations = new ArrayList<ReadingRecommendationInterface>();
    }
    
    @Override
    public void add(ReadingRecommendationInterface r) throws Exception {
        this.fakeRecommendations.add(r);
    }

    @Override
    public void remove(ReadingRecommendationInterface r) throws Exception {
        int index = -1;
        for (int i = 0; i < this.fakeRecommendations.size(); i++) {
            if (this.fakeRecommendations.get(i).getHeadline().equals(r.getHeadline())) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            this.fakeRecommendations.remove(index);
        }
    }

    @Override
    public ArrayList<ReadingRecommendationInterface> loadAll() {
        return this.fakeRecommendations;
    }
    
}
