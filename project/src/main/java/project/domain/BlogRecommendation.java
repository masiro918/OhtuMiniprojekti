
package project.domain;

public class BlogRecommendation extends ReadingRecommendation {
    private String writer;
    private String url;
    
    public BlogRecommendation(String headline, String type, String writer, String url) {
        super(headline,type);
        this.writer = writer;
        this.url = url;
    }
    
    public String getWriter() {
        return this.writer;
    }
    
    public String getURL() {
        return this.url;
    }
    
}
