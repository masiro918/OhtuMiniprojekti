
package project.DAO;

import java.util.ArrayList;
import java.util.HashMap;

public interface ReadingRecommendationDAO {
    public void setWriter(String writer);
    public void setComment(String comment);
    public String getHeadline();
    public void addCourse(String course);
    public ArrayList<String> getRelatedCourses();
    public String getType();
    public void addTags(String tag);
    public ArrayList<String> getTags();
    public String getPrint();
    public HashMap<String, String> getInfo();
}
