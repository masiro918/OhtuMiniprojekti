
package project.domain;

import java.util.ArrayList;
import java.util.HashMap;
import project.domain.Comment;

public interface ReadingRecommendationInterface {
    public void setComment(String comment);
    public Comment getComment();
    public String getHeadline();
    public void addCourse(String course);
    public ArrayList<String> getRelatedCourses();
    public String getType();
    public void addTags(String tag);
    public ArrayList<String> getTags();
    public String getPrint();
    public HashMap<String, String> getInfo();
}
