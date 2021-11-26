
package project.domain;

import java.util.HashMap;

public class BookRecommendation extends ReadingRecommendation {
    private String ISBN;
    
    public BookRecommendation(String headline, String type, String writer) {
        super(headline,type);
        this.writer = writer;
    }
    
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
    
    public String getISBN() {
        return this.ISBN;
    }
    
    @Override
    public HashMap<String, String> getInfo() {
        HashMap<String, String> info = super.getInfo();
        if (this.ISBN != null) {
            info.put("ISBN", this.ISBN);
        }
        return info;
    }
    
    @Override
    public String getPrint() {
        String print = "Kirjoittaja: " + this.writer +
                "\nOtsikko: " + this.headline +
                "\nTyyppi: " + this.type;
        if (this.ISBN != null) {
            print += "\nISBN: " + this.ISBN;
        }
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
