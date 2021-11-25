
package project.DAO;

import java.util.ArrayList;

public interface ReadingRecommendationDAO {
    public void setWriter(String writer);
    public void setComment(String comment);
    public String getHeadline();
    public void addCourse(String course);
    public ArrayList getRelatedCourses();
    public String getType();
    public void addTags(String tag);
    public ArrayList getTags();
    public String getPrint();
}
