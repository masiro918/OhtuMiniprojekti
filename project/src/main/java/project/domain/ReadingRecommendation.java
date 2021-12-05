package project.domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A general class for defining a reading recommendation.
 */
public class ReadingRecommendation implements ReadingRecommendationInterface {
    private int id;
    private String headline;
    private String type;
    private String comment;
    private ArrayList<String> relatedCourses;
    private ArrayList<String> tags;
    
    public ReadingRecommendation(String o, String t) {
        this.id = 0;
        this.headline = o;
        this.type = t;
        this.comment = null;
        this.relatedCourses = new ArrayList<>();
        this.tags = new ArrayList<>();
    }
    
    /**
     * Add a comment for the reading recommendation.
     * 
     * @param comment comment that is to be added as string
     */
    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    @Override
    public String getComment() {
        return this.comment;
    }
    
    @Override
    public String getHeadline() {
        return this.headline;
    }
    
    @Override
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    /**
     * Add a course that is related to the reading recommendation.
     * 
     * @param course added course as string
     */
    @Override
    public void addCourse(String course) {
        this.relatedCourses.add(course);
    }
    
    @Override
    public ArrayList<String> getRelatedCourses() {
        return this.relatedCourses;
    }
    
    /**
     * Returns a string representation of related courses.
     * 
     * @return printable string
     */
    @Override
    public String printRelatedCourses() {
        String print = "" + getRelatedCourses();
        return print.substring(1, print.length() - 1);
    }
    
    @Override
    public String getType() {
        return this.type;
    }
    
    /**
     * Adds a tag for the reading recommendation.
     * 
     * @param tag tag that is to be added as string
     */
    @Override
    public void addTags(String tag) {
        this.tags.add(tag);
    }
    
    @Override
    public ArrayList<String> getTags() {
        return this.tags;
    }
    
    /**
     * Returns a string representation of tags.
     * 
     * @return printable string
     */
    @Override
    public String printTags() {
        String print = "" + getTags();
        return print.substring(1, print.length() - 1);
    }
    
    /**
     * Returns the information of the reading recommendation (headline, type and possibly comment) as a HashMap.
     * 
     * @return a HashMap containing all information stored in the object
     */
    @Override
    public HashMap<String, String> getInfo() {
        HashMap<String, String> info = new HashMap<>();
        info.put("headline",this.headline);
        info.put("type", this.type);
        if (this.comment != null) {
            info.put("comment", this.comment);
        }
        if (!this.tags.isEmpty()) {
            String savedTags = "";
            for (String tag : this.tags) {
                savedTags += tag + ";";
            }
            info.put("tags", savedTags);
        }
        if (!this.relatedCourses.isEmpty()) {
            String savedCourses = "";
            for (String course : this.relatedCourses) {
                savedCourses += course + ";";
            }
            info.put("tags", savedCourses);
        }
        return info;
    }
    
    /**
     * Returns the information of the reading recommendation as a string.
     * 
     * @return printable string
     */
    @Override
    public String getPrint() {
        String print = "Otsikko: " + this.headline +
                "\nTyyppi: " + this.type;
        if (!this.tags.isEmpty()) {
            print += "\nTagit: " + printTags();
        }
        if (!this.relatedCourses.isEmpty()) {
            print += "\nRelated courses: " + printRelatedCourses();
        }
        if (this.comment != null) {
            print += "\nKommentti: " + this.comment;
        }
        return print;
    }
}
