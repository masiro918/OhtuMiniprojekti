
package project.domain;

import java.util.HashMap;

public class PodcastRecommendation extends ReadingRecommendation {
    private String podcastName;
    private String description;
    private String writer;
    
    public PodcastRecommendation(String headline, String type, String podcastName, String description) {
        super(headline, type);
        this.podcastName = podcastName;
        this.description = description;
        this.writer = null;
    }
    
    public void setPodcastName(String newName) {
        this.podcastName = newName;
    }
    
    public String getPodcastName() {
        return this.podcastName;
    }
    
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setWriter(String writer) {
        this.writer = writer;
    }
    
    public String getWriter() {
        return this.writer;
    }
    
    /**
     * Returns the information of the podcast recommendation (writer, podcast name, headline, description etc.) as a HashMap.
     * Initially calls the super class method and adds own relevant info to that.
     * 
     * @return a HashMap containing all information stored in the object
     */
    @Override
    public HashMap<String, String> getInfo() {
        HashMap<String, String> info = super.getInfo();
        info.put("podcastName", this.podcastName);
        info.put("description", this.description);
        if (this.writer != null) {
            info.put("writer", this.writer);
        }
        
        return info;
    }
    
    /**
     * Returns the information of the podcast recommendation as a string.
     * 
     * @return printable string
     */
    @Override
    public String getPrint() {
        String print = "";
        if (this.writer != null) {
            print += "Author: " + this.writer +"\n";
        }
        print += "Podcastin nimi: " + this.podcastName
                + "\nOtsikko: " + getHeadline()
                + "\nKuvaus: " + this.description
                + "\nTyyppi: " + getType();
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
