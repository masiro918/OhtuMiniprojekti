
package project.domain;

import java.util.ArrayList;


public class ReadingRecommendation {
    private String headline;
    private ArrayList<String> relatedCourses;
    private String type;
    
    public ReadingRecommendation(String o, String t) {
        this.headline = o;
        this.type = t;
        this.relatedCourses = new ArrayList<>();
    }
    
    public String getHeadline() {
        return this.headline;
    }
    
    public ArrayList getRelatedCourses() {
        return this.relatedCourses;
    }
    
    public String getType() {
        return this.type;
    }
}
