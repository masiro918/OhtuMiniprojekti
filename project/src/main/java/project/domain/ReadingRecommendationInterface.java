package project.domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interface defining the general methods of a reading recommendation.
 */
public interface ReadingRecommendationInterface {
    public void setComment(String comment);
    public String getComment();
    public String getHeadline();
    public void addCourse(String course);
    public ArrayList<String> getRelatedCourses();
    public String printRelatedCourses();
    public String getType();
    public void addTags(String tag);
    public ArrayList<String> getTags();
    public String printTags();
    public String getPrint();
    public HashMap<String, String> getInfo();
}
