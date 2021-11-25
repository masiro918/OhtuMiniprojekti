
package project.logic;

import java.util.ArrayList;
import java.util.HashMap;
import project.DAO.ReadingRecommendationDAO;
import project.domain.BlogRecommendation;

public class ReadingRecommendationService {
    private ArrayList<ReadingRecommendationDAO> recommendations;
    
    public ReadingRecommendationService(ArrayList<ReadingRecommendationDAO> recommendations) {
        this.recommendations = recommendations;
    }
    
    public void addRecommendation(ReadingRecommendationDAO recommendation) {
        this.recommendations.add(recommendation);
    }
    
    public void createRecommendation(HashMap<String,String> info) {
        String headline = info.get("Headline");
        String type = info.get("Type");
        ReadingRecommendationDAO r = null;
        if (type.equals("Blog")) {
            r = new BlogRecommendation(headline, type, info.get("URL"));
        }
        
        if (info.containsKey("Writer")) {
            r.setWriter(info.get("Writer"));
        }
        
        if (r != null) {
            this.recommendations.add(r);
        }
        
    }
    
    public ReadingRecommendationDAO findRecommendation(String headline) {
        for (ReadingRecommendationDAO r : this.recommendations) {
            if (r.getHeadline().equals(headline)) {
                return r;
            }
        }
        return null;
    }
    
    public ArrayList<ReadingRecommendationDAO> getAllRecommendations() {
        return this.recommendations;
    }
}
