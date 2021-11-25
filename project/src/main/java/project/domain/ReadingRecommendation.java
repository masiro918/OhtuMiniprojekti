
package project.domain;

import java.util.ArrayList;
import project.DAO.ReadingRecommendationDAO;


public class ReadingRecommendation implements ReadingRecommendationDAO {
    private String headline;
    private String type;
    private ArrayList<String> relatedCourses;
    private ArrayList<String> tags;
    
    public ReadingRecommendation(String o, String t) {
        this.headline = o;
        this.type = t;
        this.relatedCourses = new ArrayList<>();
        this.tags = new ArrayList<>();
    }
    
    public String getHeadline() {
        return this.headline;
    }
    
    public void addCourse(String course) {
        this.relatedCourses.add(course);
    }
    
    public ArrayList getRelatedCourses() {
        return this.relatedCourses;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void addTags(String tag) {
        this.tags.add(tag);
    }
    
    public ArrayList getTags() {
        return this.tags;
    }
    
    public String getPrint() {
        return "Otsikko: " + this.headline +"\nTyyppi: " + this.type + "\nRelated courses: " + this.relatedCourses;
    }
}
