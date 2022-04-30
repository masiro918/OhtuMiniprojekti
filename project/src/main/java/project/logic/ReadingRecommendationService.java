package project.logic;

import java.awt.print.Book;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.db.ReadingRecommendationDAO;
import project.domain.*;

/**
 * A class defining all the functions for handling the reading recommendations
 * (create, save, load, remove etc.).
 */
public class ReadingRecommendationService {

    private UserInterface user;
    private ReadingRecommendationDAO recommendationDb;
    private RecommendationCreator creator;

    public ReadingRecommendationService(UserInterface user, ReadingRecommendationDAO recommendationDb) {
        this.user = user;
        this.recommendationDb = recommendationDb;
        this.creator = new RecommendationCreator();
    }

    /**
     * Sets the user that is currently logged in. Service fetches
     * recommendations from database on the basis of userId.
     *
     * @param user user that is currently logged in to app
     */
    public void setUser(UserInterface user) {
        this.user = user;
        this.recommendationDb.setUserId(user.getId());
    }

    /**
     * Loads the user's reading recommendations from the database. Returns empty
     * list if database throws exception.
     *
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
     * Adds a new reading recommendation for the user.
     *
     * @param info a HashMap that contains all provided information of the
     * reading recommendation
     */
    public boolean createRecommendation(HashMap<String, String> info) {
        String type = info.get("type");
        ReadingRecommendation recommendation = null;
        if (type.equals("blog")) {
            if (!checkBlogInfo(info)) {
                return false;
            }
            recommendation = this.creator.createNewBlog(info);
        } else if (type.equals("book")) {
            if (!checkBookInfo(info)) {
                return false;
            }
            recommendation = this.creator.createNewBook(info);
        } else if (type.equals("podcast")) {
            if (!checkPodcastInfo(info)) {
                return false;
            }
            recommendation = this.creator.createNewPodcast(info);
        }

        try {
            this.recommendationDb.add(recommendation);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean deleteRecommendation(int id) {
        try {
            this.recommendationDb.remove(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Check if provided information for new blog is valid.
     *
     * @param info a HashMap that contains all provided information of the blog
     */
    public boolean checkBlogInfo(HashMap<String, String> info) {
        String url = info.get("url");
        if (url == null || url.isBlank()) {
            return false;
        }
        String headline = info.get("headline");
        if (headline == null || headline.isBlank()) {
            return false;
        }
        return true;
    }

    /**
     * Check if provided information for new book is valid.
     *
     * @param info a HashMap that contains all provided information of the book
     */
    public boolean checkBookInfo(HashMap<String, String> info) {
        String headline = info.get("headline");
        if (headline == null || headline.isBlank()) {
            return false;
        }
        String writer = info.get("writer");
        if (writer == null || writer.isBlank()) {
            return false;
        }
        return true;
    }

    /**
     * Check if provided information for new podcast is valid.
     *
     * @param info a HashMap that contains all provided information of the
     * podcast
     */
    public boolean checkPodcastInfo(HashMap<String, String> info) {
        String headline = info.get("headline");
        if (headline == null || headline.isBlank()) {
            return false;
        }
        String podcastName = info.get("podcastName");
        if (podcastName == null || podcastName.isBlank()) {
            return false;
        }
        String description = info.get("description");
        if (description == null || description.isBlank()) {
            return false;
        }
        return true;
    }

    public boolean updateBlog(BlogRecommendation blog, String headline, String url, String writer, String tags, String courses) {
        HashMap<String, String> info = new HashMap<>();
        info.put("url", url);
        info.put("headline", headline);
        if (!checkBlogInfo(info)) {
            return false;
        }
        blog.setUrl(url);
        blog.setWriter(writer);
        blog.setHeadline(headline);
        updateTags(blog, tags);
        updateCourses(blog, courses);
        
        try {
            this.recommendationDb.updateBlog(blog);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
    
    public boolean updateBook(BookRecommendation book, String headline, String isbn, String writer, String tags, String courses) {
        HashMap<String, String> info = new HashMap<>();
        info.put("writer", writer);
        info.put("headline", headline);
        if (!checkBookInfo(info)) {
            return false;
        }
        book.setWriter(writer);
        book.setHeadline(headline);
        book.setISBN(isbn);
        updateTags(book, tags);
        updateCourses(book, courses);
        try {
            this.recommendationDb.updateBook(book);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
    
    public boolean updatePodcast(PodcastRecommendation podcast, String headline, String writer, String podcastName, String description, String tags, String courses) {
        HashMap<String, String> info = new HashMap<>();
        info.put("headline", headline);
        info.put("podcastName", podcastName);
        info.put("description", description);
        if (!checkPodcastInfo(info)) {
            return false;
        }
        podcast.setWriter(writer);
        podcast.setHeadline(headline);
        podcast.setPodcastName(podcastName);
        podcast.setDescription(description);
        updateTags(podcast, tags);
        updateCourses(podcast, courses);
        try {
            this.recommendationDb.updatePodcast(podcast);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
    
    public void updateTags(ReadingRecommendationInterface r, String tags) {
        r.setTags(new ArrayList<String>());
        String[] newTags = tags.split(", ");
        for (String tag : newTags) {
            r.addTags(tag);
        }
        
    }
    
    public void updateCourses(ReadingRecommendationInterface r, String courses) {
        r.setRelatedCourses(new ArrayList<String>());
        String[] newCourses = courses.split(", ");
        for (String course : newCourses) {
            r.addCourse(course);
        }
    }

    /**
     * Removes a reading recommendation from the user.
     *
     * @param recommendationId the id of the recommendation that is to be
     * removed
     * @return true if recommendation was successfully removed, otherwise false
     */
    public boolean removeRecommendation(int recommendationId) {
        try {
            this.recommendationDb.remove(recommendationId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean addComment(String comment, int recommendationId) {
        try {
            this.recommendationDb.addComment(comment, recommendationId);
        } catch (Exception e) {
            return false;
        }
        return true;
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
     * Etsii kaikki, joilla on haettava headline
     *
     * @param headline haettava headline
     * @return reading recommendations if found, null if not found
     */
    public ArrayList<ReadingRecommendationInterface> findRecommendations(String headline) {
        ArrayList<ReadingRecommendationInterface> findedRecommendations = new ArrayList<>();
        try {
            for (ReadingRecommendationInterface r : loadRecommendations()) {
                if (r.getHeadline().equals(headline)) {
                    findedRecommendations.add(r);
                }
            }
        } catch (Exception e) {
            return null;
        }

        return findedRecommendations;
    }

    /**
     * Contains some serious spaghetti.
     *
     * @param writer
     * @return
     */
    public ArrayList<Integer> findIndexesByWriter(String writer) {
        try {
            ArrayList<Integer> indexes = new ArrayList<>();
            ArrayList<ReadingRecommendationInterface> recommendations = loadRecommendations();
            for (int i = 0; i < recommendations.size(); i++) {
                try {
                    Method method = recommendations.get(i).getClass().getMethod("getWriter");
                    String recommendationsWriter = (String) method.invoke(recommendations.get(i));
                    if (recommendationsWriter.equals(writer)) {
                        indexes.add(i);
                    }
                } catch (Exception e) {
                    // recommendation does not have a writer
                }

//                String type = recommendations.get(i).getType();
//                if (type.equals("blog")) {
//                    BlogRecommendation blog = (BlogRecommendation) recommendations.get(i);
//                    if (blog.getWriter().equals(writer)) {
//                        indexes.add(i);
//                    }
//                } else if (type.equals("book")) {
//                    BookRecommendation book = (BookRecommendation) recommendations.get(i);
//                    if (book.getWriter().equals(writer)) {
//                        indexes.add(i);
//                    }
//                } else if (type.equals("podcast")) {
//                    PodcastRecommendation podcast = (PodcastRecommendation) recommendations.get(i);
//                    if (podcast.getWriter().equals(writer)) {
//                        indexes.add(i);
//                    }
//                }
            }
            return indexes;
        } catch (Exception e) {
            return null;
        }
    }
    
    public ArrayList<ReadingRecommendationInterface> findRecommendationsByApproximateHeadline(String headline) {
        ArrayList<ReadingRecommendationInterface> recommendations = new ArrayList<>();
        try {
            recommendations = this.recommendationDb.findByApproximateHeadline(headline);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return recommendations;
    }

    public ArrayList<ReadingRecommendationInterface> findRecommendationsByIDs(ArrayList<Integer> indexes) {
        ArrayList<ReadingRecommendationInterface> recommendations = loadRecommendations();
        ArrayList<ReadingRecommendationInterface> returnList = new ArrayList<>();
        for (Integer i : indexes) {
            returnList.add(recommendations.get(i));
        }
        return returnList;
//        try {
//            // TODO: Change to support all types
//            ArrayList<ReadingRecommendationInterface> recs = new ArrayList<>();
//            for (int index : indexes) {
//                recs.add(this.findBlogId(index));
//            }
//            return recs;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return null;
//        }
    }

    public BlogRecommendation findBlogId(int id) {
        try {
            return this.recommendationDb.getBlog(id);
        } catch (Exception e) {
            return null;
        }
    }

    public BookRecommendation findBookId(int id) {
        try {
            return this.recommendationDb.getBook(id);
        } catch (Exception e) {
            return null;
        }
    }

    public PodcastRecommendation findPodcastId(int id) {
        try {
            return this.recommendationDb.getPodcast(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public HashMap<String, String> findRecommendationById(int id) {
        try {
            return this.recommendationDb.findById(id);
        } catch (Exception e) {
            return null;
        }
    }
}
