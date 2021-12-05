package project.domain;

import java.util.HashMap;

/**
 * A class that defines a blog type reading recommendation.
 */
public class BlogRecommendation extends ReadingRecommendation {
    private String url;
    private String writer;
    
    public BlogRecommendation(String headline, String type, String url) {
        super(headline,type);
        this.url = url;
        this.writer = null;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getURL() {
        return this.url;
    }
    
    public void setWriter(String writer) {
        this.writer = writer;
    }
    
    public String getWriter() {
        return this.writer;
    }
    
    /**
     * Returns the information of the blog recommendation (writer, url etc.) as a HashMap.
     * Initially calls the super class method and adds own relevant info to that.
     * 
     * @return a HashMap containing all information stored in the object
     */
    @Override
    public HashMap<String, String> getInfo() {
        HashMap<String, String> info = super.getInfo();
        info.put("url", this.url);
        if (this.writer != null) {
            info.put("writer", this.writer);
        }
        return info;
    }
    
    /**
     * Returns the information of the blog recommendation as a string.
     * 
     * @return printable string
     */
    @Override
    public String getPrint() {
        String print = "Otsikko: " + getHeadline();
        if (this.writer != null) {
            print += "\nKirjoittaja: " + this.writer;
        }
        print += "\nUrl: " + this.url + "\nTyyppi: " + getType();
        if (!getTags().isEmpty()) {
            print += "\nTagit: " + printTags();
        }
        if (!getRelatedCourses().isEmpty()) {
            print += "\nRelated courses: " + printRelatedCourses();
        }
        if (getComment() != null) {
            print += "\nKommentti: " + getComment();
        }
        return print;
    }
    
}
