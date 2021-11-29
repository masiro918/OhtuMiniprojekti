package project.logic;

import java.util.ArrayList;
import java.util.HashMap;
import project.domain.UserInterface;
import project.domain.BlogRecommendation;
import project.domain.BookRecommendation;
import project.domain.ReadingRecommendationInterface;

/**
 * A class defining all the functions for handling the reading recommendations
 * (create, save,load, remove etc.).
 */
public class ReadingRecommendationService {

    private ArrayList<ReadingRecommendationInterface> recommendations;
    private UserInterface user;
    //oliomuuttujana SQLdatabaseDAO, josta haetaan lukuvinkit kayttajan mukaan

    public ReadingRecommendationService(UserInterface user) {
        this.user = user;
        this.recommendations = loadRecommendations();
    }

    /**
     * Loads the user's reading recommendations from the database.
     */
    public ArrayList<ReadingRecommendationInterface> loadRecommendations() {
        //TODO: kommunikointi databasen kanssa
        //hae kayttajan kaikki lukuvinkit ja palauta listana
        return null;
    }
    
    /**
     * Saves the users reading recommendations to the database.
     */
    public void saveRecommendations() {
        //TODO: kommunikointi databasen kanssa
        //tallenna lukuvinkit kayttajan osoittamaan tauluun
    }

    //testeja varten, korjataan sitten kun database on maaritelty
    public void setRecommendations(ArrayList<ReadingRecommendationInterface> recommendations) {
        this.recommendations = recommendations;
    }
    
    /**
     * Adds a reading recommendation for the user.
     * 
     * @param recommendation reading recommendation that is to be added.
     */
    public void addRecommendation(ReadingRecommendationInterface recommendation) {
        this.recommendations.add(recommendation);
        saveRecommendations();
    }
    
    /**
     * Creates a new reading recommendation for the user.
     * 
     * @param info a HashMap that contains all provided information of the reading recommendation
     */
    public void createRecommendation(HashMap<String, String> info) { // TODO: jaa osiin lukuvinkin tyypin mukaan
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
    
    /**
     * Removes a reading recommendation from the user.
     * 
     * @param headline the headline of the recommendation that is to be removed
     */
    public void removeRecommendation(String headline) {
        int index = findIndex(headline);
        if (index >= 0) {
            this.recommendations.remove(index);
        }
        saveRecommendations();
    }
    
    /**
     * Returns a reading recommendation as a printable string.
     * 
     * @param recommendation the recommendation that is wanted
     */
    public String showRecommendation(ReadingRecommendationInterface recommendation) {
        return recommendation.getPrint();
    }
    
    /**
     * Returns all the user's reading recommendations as a printable string.
     */
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

    /**
     * Finds and returns a reading recommendation based on headline.
     * 
     * @param headline the headline of a reading recommendation that is wanted.
     * @return reading recommendation if found, null if not found
     */
    public ReadingRecommendationInterface findRecommendation(String headline) {
        for (ReadingRecommendationInterface r : this.recommendations) {
            if (r.getHeadline().equals(headline)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Returns the index of the wanted reading recommendation in the recommendations list.
     * 
     * @param headline headline of the reading recommendation that is searched for.
     * @return index of the reading recommendation as int, negative if not found.
     */
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
    
    /**
     * Returns all the user's reading recommendations as an ArrayList.
     * 
     * @return list of all the reading recommendations.
     */
    public ArrayList<ReadingRecommendationInterface> getAllRecommendations() {
        return this.recommendations;
    }
}
