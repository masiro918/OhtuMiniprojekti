// Lukuvinkki-olioiden kayttologiikasta vastaava luokka
package project.logic;

import java.util.ArrayList;
import java.util.HashMap;
import project.domain.UserInterface;
import project.domain.BlogRecommendation;
import project.domain.BookRecommendation;
import project.domain.ReadingRecommendationInterface;

public class ReadingRecommendationService {

    private ArrayList<ReadingRecommendationInterface> recommendations;
    private UserInterface user;
    //oliomuuttujana SQLdatabaseDAO, josta haetaan lukuvinkit kayttajan mukaan

    public ReadingRecommendationService(UserInterface user) {
        this.user = user;
        this.recommendations = loadRecommendations();
    }
    
    public ArrayList<ReadingRecommendationInterface> loadRecommendations() {
        //TODO: kommunikointi databasen kanssa
        //hae kayttajan kaikki lukuvinkit ja palauta listana
        return null;
    }
    
    public void saveRecommendations() {
        //TODO: kommunikointi databasen kanssa
        //tallenna lukuvinkit kayttajan osoittamaan tauluun
    }
    
    //testeja varten, korjataan sitten kun database on maaritelty
    public void setRecommendations(ArrayList<ReadingRecommendationInterface> recommendations) {
        this.recommendations = recommendations;
    }

    public void addRecommendation(ReadingRecommendationInterface recommendation) {
        this.recommendations.add(recommendation);
        saveRecommendations();
    }

    public void createRecommendation(HashMap<String, String> info) {
        String headline = info.get("headline");
        String type = info.get("type");
        if (type.equals("blog")) {
            BlogRecommendation r = new BlogRecommendation(headline, type, info.get("url"));
            if (info.containsKey("writer")) {
                r.setWriter(info.get("writer"));
            }
            this.recommendations.add(r);
            
        } else if (type.equals("book")) {
            BookRecommendation r = new BookRecommendation(headline, type, info.get("writer"));
            if (info.containsKey("ISBN")) {
                r.setISBN(info.get("ISBN"));
            }
            this.recommendations.add(r);
            
        }

    }

    public void removeRecommendation(String headline) {
        int index = findIndex(headline);
        if (index >= 0) {
            this.recommendations.remove(index);
        }
        saveRecommendations();
    }
    
    public String showRecommendation(ReadingRecommendationInterface recommendation) {
        return recommendation.getPrint();
    }
    
    public String showAllRecommendations() {
        if (this.recommendations.isEmpty()) {
            return "No recommendations";
        }
        String all = "";
        for (ReadingRecommendationInterface r : this.recommendations) {
            all += r.getPrint() + "\n\n";
        }
        return all;
    }

    // korvattavissa alla olevalla metodilla findIndex?
    public ReadingRecommendationInterface findRecommendation(String headline) {
        for (ReadingRecommendationInterface r : this.recommendations) {
            if (r.getHeadline().equals(headline)) {
                return r;
            }
        }
        return null;
    }

    // palauttaa halutun lukuvinkki-olion indeksin listassa, negatiivinen jos oliota ei loydy
    public int findIndex(String headline) {
        int index = -1;
        for (int i = 0; i < this.recommendations.size(); i++) {
            if (this.recommendations.get(i).getHeadline().equals(headline)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public ArrayList<ReadingRecommendationInterface> getAllRecommendations() {
        return this.recommendations;
    }
}
