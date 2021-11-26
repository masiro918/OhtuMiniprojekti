
package project.domain;

import java.util.HashMap;

public class BlogRecommendation extends ReadingRecommendation {
    private String url;
    private String writer;
    
    public BlogRecommendation(String headline, String type, String url) {
        super(headline,type);
        this.url = url;
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
    
    @Override
    public HashMap<String, String> getInfo() {
        HashMap<String, String> info = super.getInfo();
        info.put("url", this.url);
        return info;
    }
    
    @Override
    public String getPrint() {
        String print = "Otsikko: " + this.headline;
        if (this.writer != null) {
            print += "\nKirjoittaja: " + this.writer;
        }
        print += "\nUrl: " + this.url + "\nTyyppi: " + this.type;
        if (!this.tags.isEmpty()) {
            print += "\nTagit: " + this.tags;
        }
        if (!this.relatedCourses.isEmpty()) {
            print += "\nRelated courses: " + this.relatedCourses;
        }
        if (this.comment != null) {
            print += "\nKommentti: " + this.comment.getComment();
        }
        return print;
    }
    
}
