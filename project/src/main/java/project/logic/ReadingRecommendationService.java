package project.logic;

import java.util.ArrayList;
import java.util.HashMap;
import project.db.ReadingRecommendationDAO;
import project.domain.UserInterface;
import project.domain.BlogRecommendation;
import project.domain.BookRecommendation;
import project.domain.ReadingRecommendationInterface;

/**
 * A class defining all the functions for handling the reading recommendations
 * (create, save, load, remove etc.).
 */
public class ReadingRecommendationService {

    //private ArrayList<ReadingRecommendationInterface> recommendations;
    private UserInterface user;
    private ReadingRecommendationDAO recommendationDb;

    public ReadingRecommendationService(UserInterface user, ReadingRecommendationDAO recommendationDb) {
        this.user = user;
        this.recommendationDb = recommendationDb;
    }
    
    /**
     * Sets the user that is currently logged in.
     * Service fetches recommendations from database on the basis of userId.
     * @param user user that is currently logged in to app
     */
    public void setUser(UserInterface user) {
        this.user = user;
        this.recommendationDb.setUserId(user.getId());
    }

    /**
     * Loads the user's reading recommendations from the database.
     * Returns empty list if database throws exception.
     * @return ArrayList of all recommendations
     */
    public ArrayList<ReadingRecommendationInterface> loadRecommendations() {
        try {
            return recommendationDb.loadAll();
        } catch (Exception e) {
            return new ArrayList<ReadingRecommendationInterface>();
        }
    }

    /**
     * Creates a new reading recommendation for the user.
     *
     * @param info a HashMap that contains all provided information of the
     * reading recommendation
     */
    public boolean createRecommendation(HashMap<String, String> info) {
        String type = info.get("type");
        if (type.equals("blog")) {
            return addBlog(info);
        } else if (type.equals("book")) {
            return addBook(info);
        }
        return false;
    }

    public boolean addBlog(HashMap<String, String> info) {
        try {
            String headline = info.get("headline");
            BlogRecommendation r = new BlogRecommendation(headline, "blog", info.get("url"));
            if (info.containsKey("writer")) {
                r.setWriter(info.get("writer"));
            }
            this.recommendationDb.add(r);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean addBook(HashMap<String, String> info) {
        try {
            String headline = info.get("headline");
            BookRecommendation r = new BookRecommendation(headline, "book", info.get("writer"));
            if (info.containsKey("ISBN")) {
                r.setISBN(info.get("ISBN"));
            }
            this.recommendationDb.add(r);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Removes a reading recommendation from the user.
     *
     * @param headline the headline of the recommendation that is to be removed
     */
    public boolean removeRecommendation(String headline) {
        ReadingRecommendationInterface r = findRecommendation(headline);
        try {
            this.recommendationDb.remove(r);
            return true;
        } catch (Exception e) {
            return false;
        }
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
        try {
            if (loadRecommendations().isEmpty()) {
                return "No recommendations";
            }
            String all = "";
            for (ReadingRecommendationInterface r : loadRecommendations()) {
                all += r.getPrint() + "\n\n";
            }
            return all;
        } catch (Exception e) {
            return "Something went wrong";
        }
    }

    /**
     * Finds and returns a reading recommendation based on headline.
     *
     * @param headline the headline of a reading recommendation that is wanted.
     * @return reading recommendation if found, null if not found
     */
    public ReadingRecommendationInterface findRecommendation(String headline) {
        try {
            for (ReadingRecommendationInterface r : loadRecommendations()) {
                if (r.getHeadline().equals(headline)) {
                    return r;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns the index of the wanted reading recommendation in the
     * recommendations list.
     *
     * @param headline headline of the reading recommendation that is searched
     * for.
     * @return index of the reading recommendation as int, negative if not
     * found.
     */
    public int findIndex(String headline) {
        try {
            int index = -1;
            ArrayList<ReadingRecommendationInterface> recommendations = loadRecommendations();
            for (int i = 0; i < recommendations.size(); i++) {
                if (recommendations.get(i).getHeadline().equals(headline)) {
                    index = i;
                    break;
                }
            }
            return index;
        } catch (Exception e) {
            return -1;
        }
    }
    
    public BlogRecommendation findBlogId(int id) {
        try {
            return this.recommendationDb.getBlog(id);
        } catch (Exception e) {
            return null;
        }
    }
}
