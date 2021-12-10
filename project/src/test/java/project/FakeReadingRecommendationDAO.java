
package project;

import java.util.ArrayList;
import project.db.ReadingRecommendationDAO;
import project.domain.BlogRecommendation;
import project.domain.BookRecommendation;
import project.domain.ReadingRecommendationInterface;

public class FakeReadingRecommendationDAO implements ReadingRecommendationDAO {
    private ArrayList<ReadingRecommendationInterface> fakeRecommendations;
    private int userId;
    
    public FakeReadingRecommendationDAO() {
        this.fakeRecommendations = new ArrayList<ReadingRecommendationInterface>();
        this.userId = 0;
    }
    
    @Override
    public void add(ReadingRecommendationInterface r) throws Exception {
        this.fakeRecommendations.add(r);
    }

    @Override
    public void remove(int recommendationId) throws Exception {
        int index = -1;
        for (int i = 0; i < this.fakeRecommendations.size(); i++) {
            if (this.fakeRecommendations.get(i).getId() == recommendationId) {
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

    @Override
    public BlogRecommendation getBlog(int i) throws Exception {
        // turha, poistetaan myohemmin
        return null;
    }

    @Override
    public void setUserId(int id) {
        this.userId = id;
    }

    @Override
    public BookRecommendation getBook(int id) throws Exception {
        // poistetaan myöhemmin
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
