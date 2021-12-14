package project;

import java.util.ArrayList;
import java.util.HashMap;
import project.db.ReadingRecommendationDAO;
import project.domain.BlogRecommendation;
import project.domain.BookRecommendation;
import project.domain.PodcastRecommendation;
import project.domain.ReadingRecommendation;
import project.domain.ReadingRecommendationInterface;

public class FakeReadingRecommendationDAO implements ReadingRecommendationDAO {

    private HashMap<Integer, ArrayList> allRecommendationLists;
    private ArrayList<ReadingRecommendationInterface> fakeRecommendations;
    private int userId;

    public FakeReadingRecommendationDAO() {
        this.allRecommendationLists = new HashMap<Integer, ArrayList>();
        this.userId = 0;
        this.allRecommendationLists.put(this.userId, new ArrayList<ReadingRecommendationInterface>());
        this.fakeRecommendations = this.allRecommendationLists.get(this.userId);
    }

    @Override
    public void add(ReadingRecommendationInterface r) throws Exception {
        this.fakeRecommendations.add(r);
        r.setId(this.fakeRecommendations.size());
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
        if (!this.allRecommendationLists.containsKey(this.userId)) {
            this.allRecommendationLists.put(this.userId, new ArrayList<ReadingRecommendationInterface>());
        }
        this.fakeRecommendations = this.allRecommendationLists.get(this.userId);
    }

    @Override
    public BookRecommendation getBook(int id) throws Exception {
        // poistetaan myöhemmin
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HashMap<String, String> findById(int id) throws Exception {
        HashMap<String, String> info = null;
        for (ReadingRecommendationInterface r : this.fakeRecommendations) {
            if (r.getId() == id) {
                info = r.getInfo();
            }
        }
        return info;
    }

    @Override
    public void addComment(String commentStr, int readingRecommendationId) throws Exception {
        for (ReadingRecommendationInterface r : this.fakeRecommendations) {
            if (r.getId() == readingRecommendationId) {
                r.setComment(commentStr);
            }
        }
    }

    public ReadingRecommendationInterface getId(int id) {
        for (ReadingRecommendationInterface r : this.fakeRecommendations) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    @Override
    public PodcastRecommendation getPodcast(int id) throws Exception {
        return (PodcastRecommendation)getId(id);
    }

    @Override
    public void updateBlog(BlogRecommendation br) throws Exception {
        BlogRecommendation updatedBlog = (BlogRecommendation)getId(br.getId());
        updatedBlog.setHeadline(br.getHeadline());
        updatedBlog.setUrl(br.getURL());
        updatedBlog.setWriter(br.getWriter());
        updateTags(br);
        updateCourses(br);
    }

    @Override
    public void updateBook(BookRecommendation br) throws Exception {
        BookRecommendation updatedBook = (BookRecommendation)getId(br.getId());
        updatedBook.setHeadline(br.getHeadline());
        updatedBook.setWriter(br.getWriter());
        updatedBook.setISBN(br.getISBN());
        updateTags(br);
        updateCourses(br);
    }

    @Override
    public void updatePodcast(PodcastRecommendation pr) throws Exception {
        PodcastRecommendation updatedPodcast = (PodcastRecommendation)getId(pr.getId());
        updatedPodcast.setHeadline(pr.getHeadline());
        updatedPodcast.setPodcastName(pr.getPodcastName());
        updatedPodcast.setDescription(pr.getDescription());
        updatedPodcast.setWriter(pr.getWriter());
        updateTags(pr);
        updateCourses(pr);
    }
    
    public void updateTags(ReadingRecommendationInterface r) {
        ReadingRecommendation updatedRecom = (ReadingRecommendation)getId(r.getId());
        for (String tag : r.getTags()) {
            updatedRecom.addTags(tag);
        }
    }
    
    public void updateCourses(ReadingRecommendationInterface r) {
        ReadingRecommendation updatedRecom = (ReadingRecommendation)getId(r.getId());
        for (String course : r.getRelatedCourses()) {
            updatedRecom.addCourse(course);
        }
    }

}
