package project.domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interface defining the general methods of a reading recommendation.
 */
public interface ReadingRecommendationInterface {
    public void setComment(String comment);
    public String getComment();
    public void setHeadline(String headline);
    public String getHeadline();
    public void setId(int id);
    public int getId();
    public void addCourse(String course);
    public ArrayList<String> getRelatedCourses();
    public void setRelatedCourses(ArrayList<String> courses);
    public String printRelatedCourses();
    public String getType();
    public void addTags(String tag);
    public ArrayList<String> getTags();
    public void setTags(ArrayList<String> tags);
    public String printTags();
    public String getPrint();
    public HashMap<String, String> getInfo();
}
