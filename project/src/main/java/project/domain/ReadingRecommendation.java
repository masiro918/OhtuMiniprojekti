
package project.domain;

import java.util.ArrayList;
import project.DAO.ReadingRecommendationDAO;


public class ReadingRecommendation implements ReadingRecommendationDAO {
    public String headline;
    public String type;
    public String writer;
    public Comment comment;
    public ArrayList<String> relatedCourses;
    public ArrayList<String> tags;
    
    public ReadingRecommendation(String o, String t) {
        this.headline = o;
        this.type = t;
        this.relatedCourses = new ArrayList<>();
        this.tags = new ArrayList<>();
    }
    
    @Override
    public void setWriter(String writer) {
        this.writer = writer;
    }
    
    @Override
    public void setComment(String comment) {
        this.comment = new Comment(comment);
    }
    
    @Override
    public String getHeadline() {
        return this.headline;
    }
    
    @Override
    public void addCourse(String course) {
        this.relatedCourses.add(course);
    }
    
    @Override
    public ArrayList getRelatedCourses() {
        return this.relatedCourses;
    }
    
    @Override
    public String getType() {
        return this.type;
    }
    
    @Override
    public void addTags(String tag) {
        this.tags.add(tag);
    }
    
    @Override
    public ArrayList getTags() {
        return this.tags;
    }
    
    @Override
    public String getPrint() {
        String print = "Otsikko: " + this.headline +
                "\nTyyppi: " + this.type;
        if (!this.tags.isEmpty()) {
            print += "\nTagit: " + this.tags;
        }
        if (!this.relatedCourses.isEmpty()) {
            print += "\nRelated courses: " + this.relatedCourses;
        }
        if (this.comment != null) {
            print += "\nKommentti: " + this.comment;
        }
        return print;
    }
}
