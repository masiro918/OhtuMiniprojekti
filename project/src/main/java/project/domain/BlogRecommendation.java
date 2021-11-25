
package project.domain;

public class BlogRecommendation extends ReadingRecommendation {
    private String url;
    
    public BlogRecommendation(String headline, String type, String url) {
        super(headline,type);
        this.url = url;
    }
    
    public String getURL() {
        return this.url;
    }
    
    @Override
    public String getPrint() {
        String print = "Otsikko: " + this.headline +
                "\nUrl: " + this.url +
                "\nTyyppi: " + this.type;
        if (!this.tags.isEmpty()) {
            print += "\nTagit: " + this.tags;
        }
        if (!this.relatedCourses.isEmpty()) {
            print += "\nRelated courses: " + this.relatedCourses;
        }
        if (this.comment != null) {
            print += "\nKommentti: " + this.comment;
        }
        return print;
    }
    
}
