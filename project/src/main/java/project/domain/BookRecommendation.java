
package project.domain;

public class BookRecommendation extends ReadingRecommendation {
    private String writer;
    
    public BookRecommendation(String headline, String type, String writer) {
        super(headline,type);
        this.writer = writer;
    }
    
    public String getWriter() {
        return this.writer;
    }
    
    @Override
    public String getPrint() {
        String print = "Kirjoittaja: " + this.writer +
                "\nOtsikko: " + this.headline +
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
