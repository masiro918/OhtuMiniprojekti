package project.domain;

import java.util.HashMap;

/**
 * A class that defines a book type reading recommendation
 */
public class BookRecommendation extends ReadingRecommendation {
    private String ISBN;
    private String writer;
    
    public BookRecommendation(String headline, String type, String writer) {
        super(headline,type);
        this.writer = writer;
        this.ISBN = null;
    }
    
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
    
    public String getISBN() {
        return this.ISBN;
    }
    
    public void setWriter(String writer) {
        this.writer = writer;
    }
    
    public String getWriter() {
        return this.writer;
    }
    
    /**
     * Returns the information of the book recommendation (writer, headline etc.) as a HashMap.
     * Initially calls the super class method and adds own relevant info to that.
     * 
     * @return a HashMap containing all information stored in the object
     */
    @Override
    public HashMap<String, String> getInfo() {
        HashMap<String, String> info = super.getInfo();
        info.put("writer", this.writer);
        if (this.ISBN != null) {
            info.put("ISBN", this.ISBN);
        }
        return info;
    }
    
    /**
     * Returns the information of the book recommendation as a string.
     * 
     * @return printable string
     */
    @Override
    public String getPrint() {
        String print = "Kirjoittaja: " + this.writer +
                "\nOtsikko: " + getHeadline() +
                "\nTyyppi: " + getType();
        if (this.ISBN != null) {
            print += "\nISBN: " + this.ISBN;
        }
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
